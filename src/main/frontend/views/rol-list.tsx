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
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import * as RolServices from 'Frontend/generated/RolServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useEffect, useState } from 'react';

type Rol = {
  id: number;
  nombre: string;
  descripcion: string;
  imagen?: string; // URL o base64 para mostrar ícono/imagen
};

// FORMULARIO DE CREAR
type RolEntryFormProps = {
  onRolCreated?: () => void;
};

function RolEntryForm({ onRolCreated }: RolEntryFormProps) {
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

  const createRol = async () => {
    try {
      if (nombre.trim().length > 0 && descripcion.trim().length > 0) {
        await RolServices.create(nombre.trim(), descripcion.trim());
        onRolCreated?.();
        close();
        Notification.show('Rol creado exitosamente', { duration: 4000, theme: 'success' });
      } else {
        Notification.show('Todos los campos son obligatorios', { duration: 4000, theme: 'error' });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Rol"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={<h2 className="draggable" style={{ fontWeight: 'bold' }}>Registrar Rol</h2>}
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
type RolEditFormProps = {
  rol: Rol;
  onRolUpdated?: () => void;
};

function RolEditForm({ rol, onRolUpdated }: RolEditFormProps) {
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

  const updateRol = async () => {
    try {
      if (nombre.trim().length > 0 && descripcion.trim().length > 0) {
        await RolServices.update(Number(rol.id), nombre.trim(), descripcion.trim());
        onRolUpdated?.();
        close();
        Notification.show('Rol actualizado exitosamente', { duration: 4000, theme: 'success' });
      } else {
        Notification.show('Todos los campos son obligatorios', { duration: 4000, theme: 'error' });
      }
    } catch (error) {
      handleError(error);
    }
  };

  const deleteRol = async () => {
    try {
      if (confirm('¿Está seguro de eliminar este rol?')) {
        await RolServices.delete(Number(rol.id));
        onRolUpdated?.();
        close();
        Notification.show('Rol eliminado exitosamente', { duration: 4000, theme: 'success' });
      }
    } catch (error) {
      handleError(error);
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
        header={<h2 className="draggable" style={{ fontWeight: 'bold' }}>Editar Rol</h2>}
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
  const [items, setItems] = useState<Rol[]>([]);
  const [allItems, setAllItems] = useState<Rol[]>([]);
  const [criterio, setCriterio] = useState('');
  const [texto, setTexto] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const data = await RolServices.listAll();
      const filteredData = (data ?? []).filter((item): item is Rol => item !== undefined);
      setItems(filteredData);
      setAllItems(filteredData);
    } catch (error) {
      handleError(error);
    }
  };

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
    return <RolEditForm rol={item} onRolUpdated={loadData} />;
  }

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m" style={{ background: '#f8f8fa' }}>
      <ViewToolbar title="Gestión de Roles">
        <Group>
          <RolEntryForm onRolCreated={loadData} />
        </Group>
      </ViewToolbar>

      {/* Panel de búsqueda */}
      <HorizontalLayout theme="spacing">
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

      {/* Grid con datos */}
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