import { Button, Dialog, Grid, GridColumn, TextField, VerticalLayout, Checkbox } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import React, { useEffect } from 'react';

type Registro = {
  id?: number;
  nombre: string;
  edad: string;
  correo: string;
  clave: string;
  estado: boolean;
};

function RegistroEntryForm({ onRegistroCreated }: { onRegistroCreated?: () => void }) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal('');
  const edad = useSignal('');
  const correo = useSignal('');
  const clave = useSignal('');
  const estado = useSignal(false);
  const edadError = useSignal('');

  const open = () => (dialogOpened.value = true);
  const close = () => (dialogOpened.value = false);

  const limpiar = () => {
    nombre.value = '';
    edad.value = '';
    correo.value = '';
    clave.value = '';
    estado.value = false;
    edadError.value = '';
  };

  const createRegistro = async () => {
    if (
      !nombre.value.trim() ||
      !edad.value.trim() ||
      !correo.value.trim() ||
      !clave.value.trim()
    ) {
      Notification.show('Faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    if (!/^\d+$/.test(edad.value)) {
      edadError.value = 'Solo se permiten números';
      Notification.show('Edad solo permite números', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    try {
      await fetch('/api/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          nombre: nombre.value,
          edad: Number(edad.value),
          correo: correo.value,
          clave: clave.value,
          estado: estado.value,
        }),
      });
      if (onRegistroCreated) onRegistroCreated();
      limpiar();
      dialogOpened.value = false;
      Notification.show('Registro creado exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      Notification.show('Error al crear registro', { duration: 5000, position: 'top-center', theme: 'error' });
    }
  };

  const handleEdadChange = (value: string) => {
    edad.value = value;
    if (value === '' || /^\d+$/.test(value)) {
      edadError.value = '';
    } else {
      edadError.value = 'Solo se permiten números';
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Usuario"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={e => (dialogOpened.value = e.detail.value)}
        header={
          <h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>
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
          <TextField label="Nombre" value={nombre.value} onValueChanged={e => (nombre.value = e.detail.value)} />
          <TextField
            label="Edad"
            value={edad.value}
            onValueChanged={e => handleEdadChange(e.detail.value)}
            errorMessage={edadError.value}
            invalid={!!edadError.value}
          />
          <TextField label="Correo" value={correo.value} onValueChanged={e => (correo.value = e.detail.value)} />
          <TextField label="Clave" value={clave.value} onValueChanged={e => (clave.value = e.detail.value)} />
          <Checkbox label="Activo" checked={estado.value} onCheckedChanged={e => (estado.value = e.detail.value)} />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Registrar</Button>
    </>
  );
}

function RegistroEntryFormUpdate({ arguments: item, onRegistroUpdated }: { arguments: Registro; onRegistroUpdated?: () => void }) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal(item.nombre);
  const edad = useSignal(item.edad);
  const correo = useSignal(item.correo);
  const clave = useSignal(item.clave);
  const estado = useSignal(item.estado);
  const edadError = useSignal('');

  const open = () => (dialogOpened.value = true);
  const close = () => (dialogOpened.value = false);

  const updateRegistro = async () => {
    if (
      !nombre.value.trim() ||
      !edad.value.trim() ||
      !correo.value.trim() ||
      !clave.value.trim()
    ) {
      Notification.show('Faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    if (!/^\d+$/.test(edad.value)) {
      edadError.value = 'Solo se permiten números';
      Notification.show('Edad solo permite números', { duration: 5000, position: 'top-center', theme: 'error' });
      return;
    }
    try {
      await fetch(`/api/registro/${item.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          id: item.id,
          nombre: nombre.value,
          edad: Number(edad.value),
          correo: correo.value,
          clave: clave.value,
          estado: estado.value,
        }),
      });
      if (onRegistroUpdated) onRegistroUpdated();
      dialogOpened.value = false;
      Notification.show('Registro actualizado', { duration: 5000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      Notification.show('Error al actualizar registro', { duration: 5000, position: 'top-center', theme: 'error' });
    }
  };

  const handleEdadChange = (value: string) => {
    edad.value = value;
    if (value === '' || /^\d+$/.test(value)) {
      edadError.value = '';
    } else {
      edadError.value = 'Solo se permiten números';
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Usuario"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={e => (dialogOpened.value = e.detail.value)}
        header={
          <h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>
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
          <TextField label="Nombre" value={nombre.value} onValueChanged={e => (nombre.value = e.detail.value)} />
          <TextField
            label="Edad"
            value={edad.value}
            onValueChanged={e => handleEdadChange(e.detail.value)}
            errorMessage={edadError.value}
            invalid={!!edadError.value}
          />
          <TextField label="Correo" value={correo.value} onValueChanged={e => (correo.value = e.detail.value)} disabled />
          <TextField label="Clave" value={clave.value} onValueChanged={e => (clave.value = e.detail.value)} />
          <Checkbox label="Activo" checked={estado.value} onCheckedChanged={e => (estado.value = e.detail.value)} />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

export default function RegistroListView() {
  const registros = useSignal<Registro[]>([]);

  const cargarRegistros = () => {
    fetch('/api/registro')
      .then(res => res.json())
      .then(data => (registros.value = Array.isArray(data) ? data : []));
  };

  useEffect(() => {
    cargarRegistros();
  }, []);

  function index({ model }: { model: { index: number } }) {
    return <span>{model.index + 1}</span>;
  }

  function link({ item }: { item: Registro }) {
    return <RegistroEntryFormUpdate arguments={item} onRegistroUpdated={cargarRegistros} />;
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Registros">
        <Group>
          <RegistroEntryForm onRegistroCreated={cargarRegistros} />
        </Group>
      </ViewToolbar>
      <Grid items={registros.value}>
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