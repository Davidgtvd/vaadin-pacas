// cuenta-list.tsx
import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  HorizontalLayout,
  Notification,
  PasswordField,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import CuentaServices from 'Frontend/generated/CuentaServices';
import PersonaServices from 'Frontend/generated/PersonaServices';
import RolServices from 'Frontend/generated/RolServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { useEffect, useState } from 'react';

type Cuenta = {
  id: string;
  usuario: string;
  activo: boolean;
  fechaCreacion: string;
  ultimoAcceso?: string;
  intentosFallidos: number;
  fechaBloqueo?: string;
  persona: {
    id: string;
    nombres: string;
    apellidos: string;
    email: string;
    nombreCompleto?: string;
  };
  rol: {
    id: string;
    nombre: string;
    descripcion: string;
  };
};

type Persona = {
  id: string;
  nombres: string;
  apellidos: string;
  email: string;
  nombreCompleto: string;
};

type Rol = {
  id: string;
  nombre: string;
  descripcion: string;
};

// --- FORMULARIO DE CREAR CUENTA ---
type CuentaEntryFormProps = {
  onCuentaCreated?: () => void;
};

function CuentaEntryForm({ onCuentaCreated }: CuentaEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [usuario, setUsuario] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [confirmarContrasena, setConfirmarContrasena] = useState('');
  const [rolId, setRolId] = useState('');
  const [personaId, setPersonaId] = useState('');
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [roles, setRoles] = useState<Rol[]>([]);

  useEffect(() => {
    if (dialogOpened) {
      loadPersonasYRoles();
    }
  }, [dialogOpened]);

  const loadPersonasYRoles = async () => {
    try {
      const [personasData, rolesData] = await Promise.all([
        PersonaServices.getPersonasSinCuenta(),
        RolServices.listAll(),
      ]);
      setPersonas(personasData ?? []);
      setRoles(rolesData ?? []);
    } catch (error) {
      handleError(error);
    }
  };

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setUsuario('');
    setContrasena('');
    setConfirmarContrasena('');
    setRolId('');
    setPersonaId('');
  };

  const createCuenta = async () => {
    try {
      if (!usuario.trim() || !contrasena || !confirmarContrasena || !rolId || !personaId) {
        Notification.show('Todos los campos son obligatorios', { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }
      if (contrasena !== confirmarContrasena) {
        Notification.show('Las contraseñas no coinciden', { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }
      if (contrasena.length < 6) {
        Notification.show('La contraseña debe tener al menos 6 caracteres', { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }
      await CuentaServices.create(usuario.trim(), contrasena, parseInt(rolId), parseInt(personaId));
      onCuentaCreated?.();
      close();
      Notification.show('Cuenta creada exitosamente', { duration: 4000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Cuenta"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Registrar Cuenta</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createCuenta}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '450px', maxWidth: '100%' }}>
          <TextField label="Usuario" placeholder="Nombre de usuario único" value={usuario} onValueChanged={e => setUsuario(e.detail.value)} required />
          <HorizontalLayout theme="spacing">
            <PasswordField label="Contraseña" placeholder="Mínimo 6 caracteres" value={contrasena} onValueChanged={e => setContrasena(e.detail.value)} required style={{ flex: 1 }} />
            <PasswordField label="Confirmar Contraseña" placeholder="Repetir contraseña" value={confirmarContrasena} onValueChanged={e => setConfirmarContrasena(e.detail.value)} required style={{ flex: 1 }} />
          </HorizontalLayout>
          <ComboBox
            label="Persona"
            items={personas.map(p => ({ label: `${p.nombreCompleto} (${p.email})`, value: p.id }))}
            value={personaId}
            onValueChanged={e => setPersonaId(e.detail.value)}
            placeholder="Seleccione una persona"
            required
          />
          <ComboBox
            label="Rol"
            items={roles.map(r => ({ label: `${r.nombre} - ${r.descripcion}`, value: r.id }))}
            value={rolId}
            onValueChanged={e => setRolId(e.detail.value)}
            placeholder="Seleccione un rol"
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={open}>Registrar Cuenta</Button>
    </>
  );
}

// --- FORMULARIO DE EDICIÓN ---
type CuentaEditFormProps = {
  cuenta: Cuenta;
  onCuentaUpdated?: () => void;
};

function CuentaEditForm({ cuenta, onCuentaUpdated }: CuentaEditFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [usuario, setUsuario] = useState(cuenta.usuario);
  const [rolId, setRolId] = useState(cuenta.rol.id);
  const [activo, setActivo] = useState(cuenta.activo);
  const [roles, setRoles] = useState<Rol[]>([]);

  useEffect(() => {
    if (dialogOpened) {
      loadRoles();
    }
  }, [dialogOpened]);

  const loadRoles = async () => {
    try {
      const rolesData = await RolServices.listAll();
      setRoles(rolesData ?? []);
    } catch (error) {
      handleError(error);
    }
  };

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setUsuario(cuenta.usuario);
    setRolId(cuenta.rol.id);
    setActivo(cuenta.activo);
  };

  const updateCuenta = async () => {
    try {
      if (!usuario.trim() || !rolId) {
        Notification.show('Usuario y rol son obligatorios', { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }
      await CuentaServices.update(cuenta.id, usuario.trim(), parseInt(rolId), activo);
      onCuentaUpdated?.();
      close();
      Notification.show('Cuenta actualizada exitosamente', { duration: 4000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Cuenta"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Editar Cuenta</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateCuenta}>Guardar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '400px', maxWidth: '100%' }}>
          <TextField label="Usuario" value={usuario} onValueChanged={e => setUsuario(e.detail.value)} required />
          <ComboBox
            label="Rol"
            items={roles.map(r => ({ label: `${r.nombre} - ${r.descripcion}`, value: r.id }))}
            value={rolId}
            onValueChanged={e => setRolId(e.detail.value)}
            required
          />
          <ComboBox
            label="Estado"
            items={[
              { label: 'Activo', value: true },
              { label: 'Inactivo', value: false },
            ]}
            value={activo}
            onValueChanged={e => setActivo(e.detail.value)}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

// --- VISTA PRINCIPAL ---
export default function CuentaListView() {
  const [cuentas, setCuentas] = useState<Cuenta[]>([]);
  const [loading, setLoading] = useState(false);
  const [filterUsuario, setFilterUsuario] = useState('');

  const loadCuentas = async () => {
    setLoading(true);
    try {
      const data = filterUsuario.trim()
        ? await CuentaServices.buscarPorTexto(filterUsuario.trim())
        : await CuentaServices.listAll();
      setCuentas(data ?? []);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCuentas();
  }, []);

  const onCuentaCreated = () => loadCuentas();
  const onCuentaUpdated = () => loadCuentas();

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
        <TextField
          label="Buscar usuario"
          value={filterUsuario}
          onValueChanged={e => setFilterUsuario(e.detail.value)}
          clearButtonVisible
        />
        <Button theme="primary" onClick={loadCuentas} disabled={loading}>
          Buscar
        </Button>
        <CuentaEntryForm onCuentaCreated={onCuentaCreated} />
      </HorizontalLayout>

      <Grid items={cuentas} style={{ height: '600px' }} loading={loading}>
        <Grid.Column path="usuario" header="Usuario" />
        <Grid.Column
          path="persona.nombreCompleto"
          header="Persona"
          renderer={({ item }) => `${item.persona.nombres} ${item.persona.apellidos}`}
        />
        <Grid.Column path="rol.nombre" header="Rol" />
        <Grid.Column
          path="activo"
          header="Estado"
          renderer={({ item }) => (item.activo ? 'Activo' : 'Inactivo')}
        />
        <Grid.Column path="fechaCreacion" header="Fecha Creación" />
        <Grid.Column path="ultimoAcceso" header="Último Acceso" />
        <Grid.Column path="intentosFallidos" header="Intentos Fallidos" />
        <Grid.Column
          path="fechaBloqueo"
          header="Fecha Bloqueo"
          renderer={({ item }) => item.fechaBloqueo ?? '-'}
        />
        <Grid.Column
          header="Acciones"
          width="150px"
          flexGrow={0}
          renderer={({ item }) => <CuentaEditForm cuenta={item} onCuentaUpdated={onCuentaUpdated} />}
        />
      </Grid>
    </VerticalLayout>
  );
}