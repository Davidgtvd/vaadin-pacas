// cuenta-list.tsx
import {
  Button, ComboBox, Dialog, Grid, GridColumn, GridSortColumn,
  HorizontalLayout, Icon, Select, TextField, VerticalLayout, PasswordField
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import CuentaServices from 'Frontend/generated/CuentaServices';
import PersonaServices from 'Frontend/generated/PersonaServices';
import RolServices from 'Frontend/generated/RolServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useEffect, useState } from 'react';

// Si usas Hilla File Router, puedes dejar esto, si no, elimínalo
// export const config: ViewConfig = { ... }

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
  estaBloqueada?: boolean;
  diasSinAcceso?: number;
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

function CuentaEntryForm(props: CuentaEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [usuario, setUsuario] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [confirmarContrasena, setConfirmarContrasena] = useState('');
  const [rolId, setRolId] = useState('');
  const [personaId, setPersonaId] = useState('');
  const [activo, setActivo] = useState(true);

  const [personas, setPersonas] = useState<Persona[]>([]);
  const [roles, setRoles] = useState<Rol[]>([]);

  useEffect(() => {
    loadPersonasYRoles();
  }, []);

  const loadPersonasYRoles = async () => {
    try {
      const [personasData, rolesData] = await Promise.all([
        PersonaServices.getPersonasSinCuenta(),
        RolServices.listAll()
      ]);
      setPersonas(personasData ?? []);
      setRoles(rolesData ?? []);
    } catch (error) {
      handleError(error);
    }
  };

  const open = () => { 
    setDialogOpened(true);
    loadPersonasYRoles();
  };
  
  const close = () => {
    setDialogOpened(false);
    setUsuario('');
    setContrasena('');
    setConfirmarContrasena('');
    setRolId('');
    setPersonaId('');
    setActivo(true);
  };

  const createCuenta = async () => {
    try {
      if (
        usuario.trim().length > 0 &&
        contrasena.length > 0 &&
        rolId &&
        personaId
      ) {
        if (contrasena !== confirmarContrasena) {
          Notification.show('Las contraseñas no coinciden', { duration: 4000, position: 'top-center', theme: 'error' });
          return;
        }
        if (contrasena.length < 6) {
          Notification.show('La contraseña debe tener al menos 6 caracteres', { duration: 4000, position: 'top-center', theme: 'error' });
          return;
        }
        await CuentaServices.create(
          usuario.trim(),
          contrasena,
          parseInt(rolId),
          parseInt(personaId)
        );
        props.onCuentaCreated?.();
        close();
        Notification.show('Cuenta creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('Todos los campos son obligatorios', { duration: 5000, position: 'top-center', theme: 'error' });
      }
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
        onOpenedChanged={(event) => setDialogOpened(event.detail.value)}
        header={<h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>Registrar Cuenta</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createCuenta}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '450px', maxWidth: '100%', alignItems: 'stretch' }}>
          <TextField label="Usuario" placeholder="Nombre de usuario único" value={usuario} onValueChanged={(evt) => setUsuario(evt.detail.value)} required />
          <HorizontalLayout theme="spacing">
            <PasswordField label="Contraseña" placeholder="Mínimo 6 caracteres" value={contrasena} onValueChanged={(evt) => setContrasena(evt.detail.value)} required style={{ flex: 1 }} />
            <PasswordField label="Confirmar Contraseña" placeholder="Repetir contraseña" value={confirmarContrasena} onValueChanged={(evt) => setConfirmarContrasena(evt.detail.value)} required style={{ flex: 1 }} />
          </HorizontalLayout>
          <ComboBox label="Persona" items={personas.map(p => ({ label: `${p.nombreCompleto} (${p.email})`, value: p.id }))} value={personaId} onValueChanged={(evt) => setPersonaId(evt.detail.value)} placeholder="Seleccione una persona" required />
          <ComboBox label="Rol" items={roles.map(r => ({ label: `${r.nombre} - ${r.descripcion}`, value: r.id }))} value={rolId} onValueChanged={(evt) => setRolId(evt.detail.value)} placeholder="Seleccione un rol" required />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">Registrar Cuenta</Button>
    </>
  );
}

// --- FORMULARIO DE EDICIÓN ---
type CuentaEditFormProps = {
  cuenta: Cuenta;
  onCuentaUpdated?: () => void;
};

function CuentaEditForm(props: CuentaEditFormProps) {
  // ... (igual que tu código, sin cambios importantes)
  // Puedes copiar la función de tu versión, ya que está bien estructurada.
  // Por espacio, no la repito aquí, pero no requiere cambios mayores.
  // Si necesitas que la vuelva a escribir, dímelo.

  // ... (resto del código igual)
  return (
    // ... (igual que tu código)
    <></>
  );
}

// --- VISTA PRINCIPAL ---
export default function CuentaListView() {
  // ... (igual que tu código, puedes dejarlo tal cual)
  // Solo asegúrate de NO registrar esta vista como ruta pública en React/Hilla.
  // Úsala como componente embebido o en rutas privadas si usas Hilla.

  return (
    // ... (igual que tu código)
    <></>
  );
}