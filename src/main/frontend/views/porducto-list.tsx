import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  GridColumn,
  GridSortColumn,
  HorizontalLayout,
  Notification,
  NumberField,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import { useState } from 'react';

type Producto = {
  id: number;
  nombre: string;
  descripcion?: string;
  codigo: string;
  precio: number;
  precioCosto: number;
  stock: number;
  stockMinimo?: number;
  categoria: string;
  fechaCreacion: string;
  fechaActualizacion: string;
  unidadMedida?: string;
  marca?: string;
  modelo?: string;
  ubicacion?: string;
  proveedor?: string;
};

// Opciones de categoría
const CATEGORIAS_OPTIONS = [
  { label: 'Mixta', value: 'MIXTA' },
  { label: 'Adulto', value: 'ADULTO' },
  { label: 'Verano/Invierno', value: 'VERANO_INVIERNO' },
  { label: 'Niño', value: 'NINO' },
  { label: 'Deportiva', value: 'DEPORTIVA' },
];

// Datos simulados iniciales
const productosMock: Producto[] = [
  {
    id: 1,
    nombre: 'Camisa Azul',
    codigo: 'CAM-AZ-001',
    precio: 25.0,
    precioCosto: 15.0,
    stock: 100,
    categoria: 'ADULTO',
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
    ubicacion: 'Estante 1',
  },
  {
    id: 2,
    nombre: 'Pantalón Negro',
    codigo: 'PAN-NE-002',
    precio: 40.0,
    precioCosto: 25.0,
    stock: 50,
    categoria: 'ADULTO',
    fechaCreacion: new Date().toISOString(),
    fechaActualizacion: new Date().toISOString(),
    ubicacion: 'Estante 2',
  },
];

function ProductoEntryForm({ onProductoCreated }: { onProductoCreated?: (producto: Producto) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [form, setForm] = useState<Partial<Producto>>({
    nombre: '',
    codigo: '',
    precio: 0,
    precioCosto: 0,
    stock: 0,
    categoria: '',
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (field: keyof Producto, value: any) => {
    setForm(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = () => {
    if (!form.nombre || !form.codigo || !form.categoria || form.precio === undefined || form.stock === undefined) {
      Notification.show('Complete los campos obligatorios', { duration: 3000, position: 'top-center', theme: 'error' });
      return;
    }
    setLoading(true);
    const nuevoProducto: Producto = {
      id: Date.now(),
      nombre: form.nombre,
      codigo: form.codigo,
      precio: form.precio,
      precioCosto: form.precioCosto ?? 0,
      stock: form.stock,
      categoria: form.categoria,
      fechaCreacion: new Date().toISOString(),
      fechaActualizacion: new Date().toISOString(),
      ubicacion: form.ubicacion,
      descripcion: form.descripcion,
      unidadMedida: form.unidadMedida,
      marca: form.marca,
      modelo: form.modelo,
      proveedor: form.proveedor,
      stockMinimo: form.stockMinimo,
    };
    onProductoCreated?.(nuevoProducto);
    setLoading(false);
    setDialogOpened(false);
    setForm({
      nombre: '',
      codigo: '',
      precio: 0,
      precioCosto: 0,
      stock: 0,
      categoria: '',
    });
    Notification.show('Producto creado exitosamente', { duration: 3000, position: 'top-center', theme: 'success' });
  };

  return (
    <>
      <Button theme="primary success" onClick={() => setDialogOpened(true)}>
        Nuevo Producto
      </Button>
      <Dialog opened={dialogOpened} onOpenedChanged={e => setDialogOpened(e.detail.value)}>
        <VerticalLayout style={{ width: 350, gap: 'var(--lumo-space-m)' }}>
          <TextField
            label="Nombre"
            value={form.nombre || ''}
            onValueChanged={e => handleChange('nombre', e.detail.value)}
            required
          />
          <TextField
            label="Código"
            value={form.codigo || ''}
            onValueChanged={e => handleChange('codigo', e.detail.value)}
            required
          />
          <ComboBox
            label="Categoría"
            items={CATEGORIAS_OPTIONS}
            value={form.categoria || ''}
            onValueChanged={e => handleChange('categoria', e.detail.value)}
            required
          />
          <NumberField
            label="Precio"
            value={(form.precio ?? 0).toString()}
            onValueChanged={e => handleChange('precio', Number(e.detail.value))}
            required
          />
          <NumberField
            label="Precio Costo"
            value={(form.precioCosto ?? 0).toString()}
            onValueChanged={e => handleChange('precioCosto', Number(e.detail.value))}
          />
          <NumberField
            label="Stock"
            value={(form.stock ?? 0).toString()}
            onValueChanged={e => handleChange('stock', Number(e.detail.value))}
            required
          />
          <HorizontalLayout theme="spacing">
            <Button theme="primary" onClick={handleSubmit} disabled={loading}>
              Guardar
            </Button>
            <Button theme="tertiary" onClick={() => setDialogOpened(false)}>
              Cancelar
            </Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </>
  );
}

function ProductoEditForm({ producto, onProductoUpdated }: { producto: Producto; onProductoUpdated?: (producto: Producto) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [form, setForm] = useState<Partial<Producto>>({ ...producto });
  const [loading, setLoading] = useState(false);

  const handleChange = (field: keyof Producto, value: any) => {
    setForm(prev => ({ ...prev, [field]: value }));
  };

  const handleSubmit = () => {
    if (!form.nombre || !form.codigo || !form.categoria || form.precio === undefined || form.stock === undefined) {
      Notification.show('Complete los campos obligatorios', { duration: 3000, position: 'top-center', theme: 'error' });
      return;
    }
    setLoading(true);
    const productoActualizado: Producto = {
      id: producto.id,
      nombre: form.nombre,
      codigo: form.codigo,
      precio: form.precio,
      precioCosto: form.precioCosto ?? 0,
      stock: form.stock,
      categoria: form.categoria,
      fechaCreacion: producto.fechaCreacion,
      fechaActualizacion: new Date().toISOString(),
      ubicacion: form.ubicacion,
      descripcion: form.descripcion,
      unidadMedida: form.unidadMedida,
      marca: form.marca,
      modelo: form.modelo,
      proveedor: form.proveedor,
      stockMinimo: form.stockMinimo,
    };
    onProductoUpdated?.(productoActualizado);
    setLoading(false);
    setDialogOpened(false);
    Notification.show('Producto actualizado exitosamente', { duration: 3000, position: 'top-center', theme: 'success' });
  };

  return (
    <>
      <Button theme="tertiary" onClick={() => setDialogOpened(true)} style={{ fontSize: 12 }}>
        Editar
      </Button>
      <Dialog opened={dialogOpened} onOpenedChanged={e => setDialogOpened(e.detail.value)}>
        <VerticalLayout style={{ width: 350, gap: 'var(--lumo-space-m)' }}>
          <TextField
            label="Nombre"
            value={form.nombre || ''}
            onValueChanged={e => handleChange('nombre', e.detail.value)}
            required
          />
          <TextField
            label="Código"
            value={form.codigo || ''}
            onValueChanged={e => handleChange('codigo', e.detail.value)}
            required
          />
          <ComboBox
            label="Categoría"
            items={CATEGORIAS_OPTIONS}
            value={form.categoria || ''}
            onValueChanged={e => handleChange('categoria', e.detail.value)}
            required
          />
          <NumberField
            label="Precio"
            value={(form.precio ?? 0).toString()}
            onValueChanged={e => handleChange('precio', Number(e.detail.value))}
            required
          />
          <NumberField
            label="Precio Costo"
            value={(form.precioCosto ?? 0).toString()}
            onValueChanged={e => handleChange('precioCosto', Number(e.detail.value))}
          />
          <NumberField
            label="Stock"
            value={(form.stock ?? 0).toString()}
            onValueChanged={e => handleChange('stock', Number(e.detail.value))}
            required
          />
          <HorizontalLayout theme="spacing">
            <Button theme="primary" onClick={handleSubmit} disabled={loading}>
              Guardar
            </Button>
            <Button theme="tertiary" onClick={() => setDialogOpened(false)}>
              Cancelar
            </Button>
          </HorizontalLayout>
        </VerticalLayout>
      </Dialog>
    </>
  );
}

export default function ProductoListView() {
  const [productos, setProductos] = useState<Producto[]>(productosMock);
  const [filterTexto, setFilterTexto] = useState('');
  const [filterCategoria, setFilterCategoria] = useState<string | undefined>(undefined);

  const filteredProductos = productos.filter(p => {
    const matchesTexto = filterTexto.trim() === '' || p.nombre.toLowerCase().includes(filterTexto.toLowerCase());
    const matchesCategoria = !filterCategoria || p.categoria === filterCategoria;
    return matchesTexto && matchesCategoria;
  });

  const addProducto = (producto: Producto) => {
    setProductos(prev => [...prev, producto]);
  };

  const updateProducto = (productoActualizado: Producto) => {
    setProductos(prev => prev.map(p => (p.id === productoActualizado.id ? productoActualizado : p)));
  };

  const clearFilters = () => {
    setFilterTexto('');
    setFilterCategoria(undefined);
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m" style={{ background: '#f8f8fa' }}>
      <HorizontalLayout theme="spacing" style={{ alignItems: 'end', flexWrap: 'wrap' }}>
        <TextField
          label="Buscar por nombre"
          value={filterTexto}
          onValueChanged={e => setFilterTexto(e.detail.value)}
          clearButtonVisible
          style={{ minWidth: '200px' }}
          onKeyDown={e => e.key === 'Enter' && null /* no async search needed */}
        />
        <ComboBox
          label="Filtrar por categoría"
          items={[{ label: 'Todos', value: undefined }, ...CATEGORIAS_OPTIONS]}
          value={filterCategoria}
          onValueChanged={e => setFilterCategoria(e.detail.value)}
          clearButtonVisible
          style={{ minWidth: '150px' }}
        />
        <Button theme="primary" onClick={clearFilters}>
          Limpiar Filtros
        </Button>
        <ProductoEntryForm onProductoCreated={addProducto} />
      </HorizontalLayout>

      <Grid
        items={filteredProductos}
        style={{ background: 'white', borderRadius: 12, boxShadow: '0 2px 8px #0001', height: '600px' }}
      >
        <GridColumn header="Código" path="codigo" width="100px" />
        <GridSortColumn header="Nombre" path="nombre" />
        <GridColumn header="Categoría" path="categoria" width="130px" />
        <GridColumn header="Precio" path="precio" width="100px" />
        <GridColumn header="Stock" path="stock" width="80px" />
        <GridColumn header="Ubicación" path="ubicacion" width="120px" />
        <GridColumn
          header="Acciones"
          width="100px"
          renderer={({ item }) => <ProductoEditForm producto={item} onProductoUpdated={updateProducto} />}
        />
      </Grid>
    </main>
  );
}