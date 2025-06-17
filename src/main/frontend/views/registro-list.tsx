import {
  Button,
  Checkbox,
  Dialog,
  Grid,
  GridColumn,
  TextField,
  VerticalLayout,
  Notification,
} from '@vaadin/react-components';
import React, { useState } from 'react';

type Registro = {
  id: number;
  nombre: string;
  edad: string;
  correo: string;
  clave: string;
  estado: boolean;
};

function RegistroEntryForm({ onRegistroCreated }: { onRegistroCreated?: (registro: Registro) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState('');
  const [edad, setEdad] = useState('');
  const [correo, setCorreo] = useState('');
  const [clave, setClave] = useState('');
  const [estado, setEstado] = useState(false);
  const [edadError, setEdadError] = useState('');

  const open = () => setDialogOpened(true);
  const close = () => setDialogOpened(false);

  const limpiar = () => {
    setNombre('');
    setEdad('');
    setCorreo('');
    setClave('');
    setEstado(false);
    setEdadError('');
  };

  const createRegistro = () => {
    if (!nombre.trim() || !edad.trim() || !correo.trim() || !clave.trim()) {
      Notification.show('Faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    if (!/^\d+$/.test(edad)) {
      setEdadError('Solo se permiten números');
      Notification.show('Edad solo permite números', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    const nuevoRegistro: Registro = {
      id: Date.now(),
      nombre: nombre.trim(),
      edad: edad.trim(),
      correo: correo.trim(),
      clave: clave.trim(),
      estado,
    };
    onRegistroCreated?.(nuevoRegistro);
    limpiar();
    close();
    Notification.show('Registro creado exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
  };

  const handleEdadChange = (value: string) => {
    setEdad(value);
    if (value === '' || /^\d+$/.test(value)) {
      setEdadError('');
    } else {
      setEdadError('Solo se permiten números');
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Usuario"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={
          <h2 style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>
            Registrar Usuario
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createRegistro}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <TextField label="Nombre" value={nombre} onValueChanged={e => setNombre(e.detail.value)} />
          <TextField
            label="Edad"
            value={edad}
            onValueChanged={e => handleEdadChange(e.detail.value)}
            errorMessage={edadError}
            invalid={!!edadError}
          />
          <TextField label="Correo" value={correo} onValueChanged={e => setCorreo(e.detail.value)} />
          <TextField label="Clave" value={clave} onValueChanged={e => setClave(e.detail.value)} />
          <Checkbox label="Activo" checked={estado} onCheckedChanged={e => setEstado(e.detail.value)} />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Registrar</Button>
    </>
  );
}

function RegistroEntryFormUpdate({ registro, onRegistroUpdated }: { registro: Registro; onRegistroUpdated?: (registro: Registro) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState(registro.nombre);
  const [edad, setEdad] = useState(registro.edad);
  const [correo] = useState(registro.correo);
  const [clave, setClave] = useState(registro.clave);
  const [estado, setEstado] = useState(registro.estado);
  const [edadError, setEdadError] = useState('');

  const open = () => setDialogOpened(true);
  const close = () => setDialogOpened(false);

  const updateRegistro = () => {
    if (!nombre.trim() || !edad.trim() || !correo.trim() || !clave.trim()) {
      Notification.show('Faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    if (!/^\d+$/.test(edad)) {
      setEdadError('Solo se permiten números');
      Notification.show('Edad solo permite números', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    const registroActualizado: Registro = {
      id: registro.id,
      nombre: nombre.trim(),
      edad: edad.trim(),
      correo,
      clave: clave.trim(),
      estado,
    };
    onRegistroUpdated?.(registroActualizado);
    close();
    Notification.show('Registro actualizado', { duration: 5000, position: 'bottom-end', theme: 'success' });
  };

  const handleEdadChange = (value: string) => {
    setEdad(value);
    if (value === '' || /^\d+$/.test(value)) {
      setEdadError('');
    } else {
      setEdadError('Solo se permiten números');
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Usuario"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={
          <h2 style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>
            Editar Usuario
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateRegistro}>Actualizar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <TextField label="Nombre" value={nombre} onValueChanged={e => setNombre(e.detail.value)} />
          <TextField
            label="Edad"
            value={edad}
            onValueChanged={e => handleEdadChange(e.detail.value)}
            errorMessage={edadError}
            invalid={!!edadError}
          />
          <TextField label="Correo" value={correo} disabled />
          <TextField label="Clave" value={clave} onValueChanged={e => setClave(e.detail.value)} />
          <Checkbox label="Activo" checked={estado} onCheckedChanged={e => setEstado(e.detail.value)} />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

export default function RegistroListView() {
  const [registros, setRegistros] = useState<Registro[]>([]);

  const cargarRegistros = () => {
    // Aquí podrías cargar datos iniciales o dejar vacío
  };

  React.useEffect(() => {
    cargarRegistros();
  }, []);

  const agregarRegistro = (nuevo: Registro) => {
    setRegistros(prev => [...prev, nuevo]);
  };

  const actualizarRegistro = (actualizado: Registro) => {
    setRegistros(prev => prev.map(r => (r.id === actualizado.id ? actualizado : r)));
  };

  function index({ model }: { model: { index: number } }) {
    return <span>{model.index + 1}</span>;
  }

  function link({ item }: { item: Registro }) {
    return <RegistroEntryFormUpdate registro={item} onRegistroUpdated={actualizarRegistro} />;
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <div style={{ paddingBottom: '1rem' }}>
        <RegistroEntryForm onRegistroCreated={agregarRegistro} />
      </div>
      <Grid items={registros}>
        <GridColumn header="Nro" renderer={index} />
        <GridColumn path="nombre" header="Nombre" />
        <GridColumn path="edad" header="Edad" />
        <GridColumn path="correo" header="Correo" />
        <GridColumn path="estado" header="Activo" />
        <GridColumn header="Acciones" renderer={link} />
      </Grid>
    </main>
  );
}