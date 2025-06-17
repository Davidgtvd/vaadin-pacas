import {
  Button,
  Dialog,
  Grid,
  GridColumn,
  GridSortColumn,
  HorizontalLayout,
  Icon,
  Select,
  TextField,
  VerticalLayout,
  Notification,
} from '@vaadin/react-components';
import { useEffect, useState } from 'react';

type Rol = {
  id: number;
  nombre: string;
  descripcion: string;
  imagen?: string; // URL o base64 para mostrar ícono/imagen
};

// FORMULARIO DE CREAR
function RolEntryForm({ onRolCreated }: { onRolCreated?: (rol: Rol) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [imagen, setImagen] = useState('');

  const open = () => setDialogOpened(true);
  const close = () => {
    setDialogOpened(false);
    setNombre('');
    setDescripcion('');
    setImagen('');
  };

  const createRol = () => {
    if (nombre.trim().length === 0 || descripcion.trim().length === 0) {
      Notification.show('Todos los campos son obligatorios', { duration: 4000, theme: 'error' });
      return;
    }
    const nuevoRol: Rol = {
      id: Date.now(),
      nombre: nombre.trim(),
      descripcion: descripcion.trim(),
      imagen: imagen.trim() || undefined,
    };
    onRolCreated?.(nuevoRol);
    close();
    Notification.show('Rol creado exitosamente', { duration: 4000, theme: 'success' });
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Rol"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={<h2 style={{ fontWeight: 'bold' }}>Registrar Rol</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createRol}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: 350 }}>
          <TextField
            label="Nombre del rol"
            value={nombre}
            onValueChanged={e => setNombre(e.detail.value)}
            required
          />
          <TextField
            label="Descripción"
            value={descripcion}
            onValueChanged={e => setDescripcion(e.detail.value)}
            required
          />
          <TextField
            label="URL de imagen/ícono (opcional)"
            value={imagen}
            onValueChanged={e => setImagen(e.detail.value)}
            placeholder="https://..."
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">Registrar Rol</Button>
    </>
  );
}

// FORMULARIO DE EDICIÓN
function RolEditForm({ rol, onRolUpdated }: { rol: Rol; onRolUpdated?: (rol?: Rol) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState(rol.nombre);
  const [descripcion, setDescripcion] = useState(rol.descripcion);
  const [imagen, setImagen] = useState(rol.imagen ?? '');

  useEffect(() => {
    setNombre(rol.nombre);
    setDescripcion(rol.descripcion);
    setImagen(rol.imagen ?? '');
  }, [rol]);

  const open = () => setDialogOpened(true);
  const close = () => setDialogOpened(false);

  const updateRol = () => {
    if (nombre.trim().length === 0 || descripcion.trim().length === 0) {
      Notification.show('Todos los campos son obligatorios', { duration: 4000, theme: 'error' });
      return;
    }
    const rolActualizado: Rol = {
      ...rol,
      nombre: nombre.trim(),
      descripcion: descripcion.trim(),
      imagen: imagen.trim() || undefined,
    };
    onRolUpdated?.(rolActualizado);
    close();
    Notification.show('Rol actualizado exitosamente', { duration: 4000, theme: 'success' });
  };

  const deleteRol = () => {
    if (confirm('¿Está seguro de eliminar este rol?')) {
      onRolUpdated?.(undefined); // Indica eliminación
      close();
      Notification.show('Rol eliminado exitosamente', { duration: 4000, theme: 'success' });
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Rol"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={<h2 style={{ fontWeight: 'bold' }}>Editar Rol</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="error" onClick={deleteRol}>Eliminar</Button>
            <Button theme="primary" onClick={updateRol}>Actualizar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: 350 }}>
          <TextField
            label="Nombre del rol"
            value={nombre}
            onValueChanged={e => setNombre(e.detail.value)}
            required
          />
          <TextField
            label="Descripción"
            value={descripcion}
            onValueChanged={e => setDescripcion(e.detail.value)}
            required
          />
          <TextField
            label="URL de imagen/ícono (opcional)"
            value={imagen}
            onValueChanged={e => setImagen(e.detail.value)}
            placeholder="https://..."
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">Editar</Button>
    </>
  );
}

export default function RolListView() {
  const [items, setItems] = useState<Rol[]>([
    // Datos iniciales de ejemplo
    { id: 1, nombre: 'Administrador', descripcion: 'Acceso total', imagen: '' },
    { id: 2, nombre: 'Usuario', descripcion: 'Acceso limitado', imagen: '' },
  ]);
  const [allItems, setAllItems] = useState<Rol[]>(items);
  const [criterio, setCriterio] = useState('');
  const [texto, setTexto] = useState('');

  useEffect(() => {
    setAllItems(items);
  }, [items]);

  const itemSelect = [
    { label: 'Nombre', value: 'nombre' },
    { label: 'Descripción', value: 'descripcion' },
  ];

  const search = () => {
    if (!criterio || !texto.trim()) {
      Notification.show('Seleccione un criterio e ingrese texto a buscar', { duration: 3000, theme: 'error' });
      return;
    }
    const searchText = texto.trim().toLowerCase();
    const filtered = allItems.filter(item => {
      let fieldValue = '';
      switch (criterio) {
        case 'nombre':
          fieldValue = item.nombre?.toLowerCase() || '';
          break;
        case 'descripcion':
          fieldValue = item.descripcion?.toLowerCase() || '';
          break;
        default:
          return false;
      }
      return fieldValue.startsWith(searchText);
    });
    setItems(filtered);
    Notification.show(
      filtered.length === 0
        ? `No se encontraron roles que empiecen con "${texto}" en ${criterio}`
        : `Se encontraron ${filtered.length} resultado(s)`,
      { duration: 4000, theme: filtered.length === 0 ? 'contrast' : 'success' }
    );
  };

  const showAll = () => {
    setItems(allItems);
    setCriterio('');
    setTexto('');
    Notification.show(`Mostrando todos los roles (${allItems.length})`, { duration: 2000, theme: 'success' });
  };

  const onRolCreated = (nuevoRol: Rol) => {
    setItems(prev => [...prev, nuevoRol]);
    setAllItems(prev => [...prev, nuevoRol]);
  };

  const onRolUpdated = (rolActualizado?: Rol) => {
    if (!rolActualizado) {
      // Eliminación: eliminar rol con id igual al que se pasó
      // Para esto, necesitamos que el RolEditForm pase el id al llamar onRolUpdated con undefined
      // Aquí simplificamos: si rolActualizado es undefined, no hacemos nada
      return;
    }
    setItems(prev => prev.map(r => (r.id === rolActualizado.id ? rolActualizado : r)));
    setAllItems(prev => prev.map(r => (r.id === rolActualizado.id ? rolActualizado : r)));
  };

  const onRolDeleted = (rolId: number) => {
    setItems(prev => prev.filter(r => r.id !== rolId));
    setAllItems(prev => prev.filter(r => r.id !== rolId));
  };

  // Modificamos RolEditForm para llamar onRolDeleted en eliminación
  // Para eso, pasamos onRolDeleted a RolEditForm y llamamos allí

  // Actualizamos renderActions para pasar onRolDeleted

  function renderIndex({ model }: { model: any }) {
    return <span>{model.index + 1}</span>;
  }

  function renderImagen({ item }: { item: Rol }) {
    return (
      <img
        src={item.imagen || 'https://placehold.co/48x48?text=Rol'}
        alt={item.nombre}
        style={{ width: 48, height: 48, borderRadius: 8, objectFit: 'cover', border: '1px solid #eee' }}
      />
    );
  }

  function renderActions({ item }: { item: Rol }) {
    return (
      <RolEditForm
        rol={item}
        onRolUpdated={updatedRol => {
          if (updatedRol) {
            onRolUpdated(updatedRol);
          } else {
            onRolDeleted(item.id);
          }
        }}
      />
    );
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m" style={{ background: '#f8f8fa' }}>
      <div style={{ paddingBottom: '1rem' }}>
        <RolEntryForm onRolCreated={onRolCreated} />
      </div>

      <HorizontalLayout theme="spacing" style={{ marginBottom: '1rem' }}>
        <Select
          items={itemSelect}
          value={criterio}
          onValueChanged={e => setCriterio(e.detail.value)}
          placeholder="Seleccione un criterio"
        />
        <TextField
          placeholder="Buscar por palabra..."
          style={{ width: '50%' }}
          value={texto}
          onValueChanged={e => setTexto(e.detail.value)}
          onKeyDown={e => {
            if (e.key === 'Enter') search();
          }}
        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">
          BUSCAR
        </Button>
        <Button onClick={showAll} theme="secondary">
          MOSTRAR TODOS
        </Button>
      </HorizontalLayout>

      <Grid items={items} style={{ background: 'white', borderRadius: 12, boxShadow: '0 2px 8px #0001' }}>
        <GridColumn header="#" renderer={renderIndex} width="60px" />
        <GridColumn header="Imagen" renderer={renderImagen} width="70px" />
        <GridSortColumn path="nombre" header="Rol" />
        <GridColumn path="descripcion" header="Descripción" />
        <GridColumn header="Acciones" renderer={renderActions} width="110px" />
      </Grid>

      <div
        style={{
          marginTop: '1rem',
          fontSize: '0.9rem',
          color: 'var(--lumo-secondary-text-color)',
        }}
      >
        Total de roles: {items.length}
        {items.length !== allItems.length && (
          <span style={{ marginLeft: '10px', fontStyle: 'italic' }}>(de {allItems.length} totales)</span>
        )}
      </div>
    </main>
  );
}