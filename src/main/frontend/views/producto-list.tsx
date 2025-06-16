// producto-list.tsx
import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  HorizontalLayout,
  Notification,
  NumberField,
  TextArea,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import ProductoServices from 'Frontend/generated/ProductoServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { useEffect, useState } from 'react';

type Producto = {
  id: string;
  nombre: string;
  descripcion?: string;
  codigo: string;
  precio: number;
  precioCosto: number;
  stock: number;
  stockMinimo?: number;
  categoria: CategoriaProducto;
  estado: EstadoProducto;
  unidadMedida?: string;
  marca?: string;
  modelo?: string;
  fechaCreacion: string;
  fechaActualizacion: string;
  ubicacion?: string;
  proveedor?: string;
  // Métodos calculados del backend
  nombreCompleto?: string;
  displayName?: string;
  margenGanancia?: number;
  porcentajeMargen?: number;
  valorTotalStock?: number;
  stockBajo?: boolean;
  disponible?: boolean;
  estadoStock?: string;
  informacionCompleta?: string;
};

enum CategoriaProducto {
  MIXTA = 'MIXTA',
  ADULTO = 'ADULTO',
  VERANO_INVIERNO = 'VERANO_INVIERNO',
  NINO = 'NINO',
  DEPORTIVA = 'DEPORTIVA'
}

enum EstadoProducto {
  ACTIVO = 'ACTIVO',
  INACTIVO = 'INACTIVO',
  DESCONTINUADO = 'DESCONTINUADO',
  AGOTADO = 'AGOTADO',
  EN_REVISION = 'EN_REVISION'
}

const CATEGORIAS_OPTIONS = [
  { label: 'Mixta', value: CategoriaProducto.MIXTA },
  { label: 'Adulto', value: CategoriaProducto.ADULTO },
  { label: 'Verano/Invierno', value: CategoriaProducto.VERANO_INVIERNO },
  { label: 'Niño', value: CategoriaProducto.NINO },
  { label: 'Deportiva', value: CategoriaProducto.DEPORTIVA },
];

const ESTADOS_OPTIONS = [
  { label: 'Activo', value: EstadoProducto.ACTIVO },
  { label: 'Inactivo', value: EstadoProducto.INACTIVO },
  { label: 'Descontinuado', value: EstadoProducto.DESCONTINUADO },
  { label: 'Agotado', value: EstadoProducto.AGOTADO },
  { label: 'En Revisión', value: EstadoProducto.EN_REVISION },
];

// --- FORMULARIO DE CREAR PRODUCTO ---
type ProductoEntryFormProps = {
  onProductoCreated?: () => void;
};

function ProductoEntryForm({ onProductoCreated }: ProductoEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [codigo, setCodigo] = useState('');
  const [precio, setPrecio] = useState<number | undefined>(undefined);
  const [precioCosto, setPrecioCosto] = useState<number | undefined>(undefined);
  const [stock, setStock] = useState<number | undefined>(0);
  const [stockMinimo, setStockMinimo] = useState<number | undefined>(0);
  const [categoria, setCategoria] = useState<CategoriaProducto | undefined>(undefined);
  const [estado, setEstado] = useState<EstadoProducto>(EstadoProducto.ACTIVO);
  const [unidadMedida, setUnidadMedida] = useState('');
  const [marca, setMarca] = useState('');
  const [modelo, setModelo] = useState('');
  const [ubicacion, setUbicacion] = useState('');
  const [proveedor, setProveedor] = useState('');

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    // Reset form
    setNombre('');
    setDescripcion('');
    setCodigo('');
    setPrecio(undefined);
    setPrecioCosto(undefined);
    setStock(0);
    setStockMinimo(0);
    setCategoria(undefined);
    setEstado(EstadoProducto.ACTIVO);
    setUnidadMedida('');
    setMarca('');
    setModelo('');
    setUbicacion('');
    setProveedor('');
  };

  const createProducto = async () => {
    try {
      // Validaciones básicas
      if (!nombre.trim() || !codigo.trim() || !precio || !precioCosto || !categoria) {
        Notification.show('Los campos nombre, código, precio, precio de costo y categoría son obligatorios', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      if (precio <= 0 || precioCosto <= 0) {
        Notification.show('Los precios deben ser mayores a 0', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      if (stock !== undefined && stock < 0) {
        Notification.show('El stock no puede ser negativo', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      const nuevoProducto = {
        nombre: nombre.trim(),
        descripcion: descripcion.trim() || undefined,
        codigo: codigo.trim(),
        precio,
        precioCosto,
        stock: stock ?? 0,
        stockMinimo: stockMinimo ?? 0,
        categoria,
        estado,
        unidadMedida: unidadMedida.trim() || undefined,
        marca: marca.trim() || undefined,
        modelo: modelo.trim() || undefined,
        ubicacion: ubicacion.trim() || undefined,
        proveedor: proveedor.trim() || undefined,
      };

      await ProductoServices.save(nuevoProducto);
      onProductoCreated?.();
      close();
      Notification.show('Producto creado exitosamente', 
        { duration: 4000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Crear Producto"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Crear Producto</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createProducto}>Crear</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '600px', maxWidth: '100%' }}>
          <HorizontalLayout theme="spacing">
            <TextField 
              label="Nombre" 
              placeholder="Nombre del producto" 
              value={nombre} 
              onValueChanged={e => setNombre(e.detail.value)} 
              required 
              style={{ flex: 2 }}
            />
            <TextField 
              label="Código" 
              placeholder="Código único" 
              value={codigo} 
              onValueChanged={e => setCodigo(e.detail.value)} 
              required 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <TextArea 
            label="Descripción" 
            placeholder="Descripción del producto (opcional)" 
            value={descripcion} 
            onValueChanged={e => setDescripcion(e.detail.value)} 
          />

          <HorizontalLayout theme="spacing">
            <NumberField 
              label="Precio" 
              placeholder="0.00" 
              value={precio?.toString() ?? ''} 
              onValueChanged={e => setPrecio(e.detail.value ? parseFloat(e.detail.value) : undefined)} 
              step={0.01} 
              min={0.01}
              required 
              style={{ flex: 1 }}
            />
            <NumberField 
              label="Precio de Costo" 
              placeholder="0.00" 
              value={precioCosto?.toString() ?? ''} 
              onValueChanged={e => setPrecioCosto(e.detail.value ? parseFloat(e.detail.value) : undefined)} 
              step={0.01} 
              min={0.01}
              required 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <NumberField 
              label="Stock" 
              placeholder="0" 
              value={stock?.toString() ?? ''} 
              onValueChanged={e => setStock(e.detail.value ? parseInt(e.detail.value) : undefined)} 
              min={0}
              style={{ flex: 1 }}
            />
            <NumberField 
              label="Stock Mínimo" 
              placeholder="0" 
              value={stockMinimo?.toString() ?? ''} 
              onValueChanged={e => setStockMinimo(e.detail.value ? parseInt(e.detail.value) : undefined)} 
              min={0}
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <ComboBox
              label="Categoría"
              items={CATEGORIAS_OPTIONS}
              value={categoria}
              onValueChanged={e => setCategoria(e.detail.value as CategoriaProducto)}
              placeholder="Seleccione una categoría"
              required
              style={{ flex: 1 }}
            />
            <ComboBox
              label="Estado"
              items={ESTADOS_OPTIONS}
              value={estado}
              onValueChanged={e => setEstado(e.detail.value as EstadoProducto)}
              required
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <TextField 
              label="Unidad de Medida" 
              placeholder="ej: piezas, kg, metros" 
              value={unidadMedida} 
              onValueChanged={e => setUnidadMedida(e.detail.value)} 
              style={{ flex: 1 }}
            />
            <TextField 
              label="Marca" 
              placeholder="Marca del producto" 
              value={marca} 
              onValueChanged={e => setMarca(e.detail.value)} 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <TextField 
              label="Modelo" 
              placeholder="Modelo del producto" 
              value={modelo} 
              onValueChanged={e => setModelo(e.detail.value)} 
              style={{ flex: 1 }}
            />
            <TextField 
              label="Ubicación" 
              placeholder="Ubicación en almacén" 
              value={ubicacion} 
              onValueChanged={e => setUbicacion(e.detail.value)} 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <TextField 
            label="Proveedor" 
            placeholder="Nombre del proveedor" 
            value={proveedor} 
            onValueChanged={e => setProveedor(e.detail.value)} 
          />
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={open}>Crear Producto</Button>
    </>
  );
}

// --- FORMULARIO DE EDICIÓN ---
type ProductoEditFormProps = {
  producto: Producto;
  onProductoUpdated?: () => void;
};

function ProductoEditForm({ producto, onProductoUpdated }: ProductoEditFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombre, setNombre] = useState(producto.nombre);
  const [descripcion, setDescripcion] = useState(producto.descripcion ?? '');
  const [codigo, setCodigo] = useState(producto.codigo);
  const [precio, setPrecio] = useState<number>(producto.precio);
  const [precioCosto, setPrecioCosto] = useState<number>(producto.precioCosto);
  const [stock, setStock] = useState<number>(producto.stock);
  const [stockMinimo, setStockMinimo] = useState<number>(producto.stockMinimo ?? 0);
  const [categoria, setCategoria] = useState<CategoriaProducto>(producto.categoria);
  const [estado, setEstado] = useState<EstadoProducto>(producto.estado);
  const [unidadMedida, setUnidadMedida] = useState(producto.unidadMedida ?? '');
  const [marca, setMarca] = useState(producto.marca ?? '');
  const [modelo, setModelo] = useState(producto.modelo ?? '');
  const [ubicacion, setUbicacion] = useState(producto.ubicacion ?? '');
  const [proveedor, setProveedor] = useState(producto.proveedor ?? '');

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    // Reset to original values
    setNombre(producto.nombre);
    setDescripcion(producto.descripcion ?? '');
    setCodigo(producto.codigo);
    setPrecio(producto.precio);
    setPrecioCosto(producto.precioCosto);
    setStock(producto.stock);
    setStockMinimo(producto.stockMinimo ?? 0);
    setCategoria(producto.categoria);
    setEstado(producto.estado);
    setUnidadMedida(producto.unidadMedida ?? '');
    setMarca(producto.marca ?? '');
    setModelo(producto.modelo ?? '');
    setUbicacion(producto.ubicacion ?? '');
    setProveedor(producto.proveedor ?? '');
  };

  const updateProducto = async () => {
    try {
      // Validaciones básicas
      if (!nombre.trim() || !codigo.trim() || !precio || !precioCosto) {
        Notification.show('Los campos nombre, código, precio y precio de costo son obligatorios', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      if (precio <= 0 || precioCosto <= 0) {
        Notification.show('Los precios deben ser mayores a 0', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      if (stock < 0) {
        Notification.show('El stock no puede ser negativo', 
          { duration: 4000, position: 'top-center', theme: 'error' });
        return;
      }

      const productoActualizado = {
        ...producto,
        nombre: nombre.trim(),
        descripcion: descripcion.trim() || undefined,
        codigo: codigo.trim(),
        precio,
        precioCosto,
        stock,
        stockMinimo,
        categoria,
        estado,
        unidadMedida: unidadMedida.trim() || undefined,
        marca: marca.trim() || undefined,
        modelo: modelo.trim() || undefined,
        ubicacion: ubicacion.trim() || undefined,
        proveedor: proveedor.trim() || undefined,
      };

      await ProductoServices.save(productoActualizado);
      onProductoUpdated?.();
      close();
      Notification.show('Producto actualizado exitosamente', 
        { duration: 4000, position: 'bottom-end', theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Producto"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Editar Producto</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateProducto}>Guardar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '600px', maxWidth: '100%' }}>
          <HorizontalLayout theme="spacing">
            <TextField 
              label="Nombre" 
              value={nombre} 
              onValueChanged={e => setNombre(e.detail.value)} 
              required 
              style={{ flex: 2 }}
            />
            <TextField 
              label="Código" 
              value={codigo} 
              onValueChanged={e => setCodigo(e.detail.value)} 
              required 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <TextArea 
            label="Descripción" 
            value={descripcion} 
            onValueChanged={e => setDescripcion(e.detail.value)} 
          />

          <HorizontalLayout theme="spacing">
            <NumberField 
              label="Precio" 
              value={precio.toString()} 
              onValueChanged={e => setPrecio(parseFloat(e.detail.value) || 0)} 
              step={0.01} 
              min={0.01}
              required 
              style={{ flex: 1 }}
            />
            <NumberField 
              label="Precio de Costo" 
              value={precioCosto.toString()} 
              onValueChanged={e => setPrecioCosto(parseFloat(e.detail.value) || 0)} 
              step={0.01} 
              min={0.01}
              required 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <NumberField 
              label="Stock" 
              value={stock.toString()} 
              onValueChanged={e => setStock(parseInt(e.detail.value) || 0)} 
              min={0}
              style={{ flex: 1 }}
            />
            <NumberField 
              label="Stock Mínimo" 
              value={stockMinimo.toString()} 
              onValueChanged={e => setStockMinimo(parseInt(e.detail.value) || 0)} 
              min={0}
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <ComboBox
              label="Categoría"
              items={CATEGORIAS_OPTIONS}
              value={categoria}
              onValueChanged={e => setCategoria(e.detail.value as CategoriaProducto)}
              required
              style={{ flex: 1 }}
            />
            <ComboBox
              label="Estado"
              items={ESTADOS_OPTIONS}
              value={estado}
              onValueChanged={e => setEstado(e.detail.value as EstadoProducto)}
              required
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <TextField 
              label="Unidad de Medida" 
              value={unidadMedida} 
              onValueChanged={e => setUnidadMedida(e.detail.value)} 
              style={{ flex: 1 }}
            />
            <TextField 
              label="Marca" 
              value={marca} 
              onValueChanged={e => setMarca(e.detail.value)} 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <HorizontalLayout theme="spacing">
            <TextField 
              label="Modelo" 
              value={modelo} 
              onValueChanged={e => setModelo(e.detail.value)} 
              style={{ flex: 1 }}
            />
            <TextField 
              label="Ubicación" 
              value={ubicacion} 
              onValueChanged={e => setUbicacion(e.detail.value)} 
              style={{ flex: 1 }}
            />
          </HorizontalLayout>

          <TextField 
            label="Proveedor" 
            value={proveedor} 
            onValueChanged={e => setProveedor(e.detail.value)} 
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="tertiary">Editar</Button>
    </>
  );
}

// --- VISTA PRINCIPAL ---
export default function ProductoListView() {
  const [productos, setProductos] = useState<Producto[]>([]);
  const [loading, setLoading] = useState(false);
  const [filterTexto, setFilterTexto] = useState('');
  const [filterCategoria, setFilterCategoria] = useState<CategoriaProducto | undefined>(undefined);
  const [filterEstado, setFilterEstado] = useState<EstadoProducto | undefined>(undefined);

  const loadProductos = async () => {
    setLoading(true);
    try {
      let data: Producto[] = [];
      
      if (filterTexto.trim()) {
        data = await ProductoServices.findByNombreContaining(filterTexto.trim()) ?? [];
      } else if (filterCategoria) {
        data = await ProductoServices.findByCategoria(filterCategoria) ?? [];
      } else if (filterEstado) {
        data = await ProductoServices.findByEstado(filterEstado) ?? [];
      } else {
        data = await ProductoServices.findAll() ?? [];
      }
      
      setProductos(data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadProductos();
  }, []);

  const onProductoCreated = () => loadProductos();
  const onProductoUpdated = () => loadProductos();

  const clearFilters = () => {
    setFilterTexto('');
    setFilterCategoria(undefined);
    setFilterEstado(undefined);
  };

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('es-EC', {
      style: 'currency',
      currency: 'USD'
    }).format(value);
  };

  const getEstadoTheme = (estado: EstadoProducto) => {
    switch (estado) {
      case EstadoProducto.ACTIVO: return 'success';
      case EstadoProducto.AGOTADO: return 'error';
      case EstadoProducto.EN_REVISION: return 'contrast';
      case EstadoProducto.DESCONTINUADO: return 'error';
      case EstadoProducto.INACTIVO: return 'primary';
      default: return 'primary';
    }
  };

  const getStockStatus = (producto: Producto) => {
    if (producto.stock === 0) return { text: 'Sin Stock', theme: 'error' };
    if (producto.stockMinimo && producto.stock <= producto.stockMinimo) return { text: 'Stock Bajo', theme: 'contrast' };
    return { text: 'Normal', theme: 'success' };
  };

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      {/* Filtros y acciones */}
      <VerticalLayout theme="spacing">
        <HorizontalLayout theme="spacing" style={{ alignItems: 'end', flexWrap: 'wrap' }}>
          <TextField
            label="Buscar por nombre"
            value={filterTexto}
            onValueChanged={e => setFilterTexto(e.detail.value)}
            clearButtonVisible
            style={{ minWidth: '200px' }}
          />
          <ComboBox
            label="Filtrar por categoría"
            items={[{ label: 'Todos', value: undefined }, ...CATEGORIAS_OPTIONS]}
            value={filterCategoria}
            onValueChanged={e => setFilterCategoria(e.detail.value as CategoriaProducto)}
            clearButtonVisible
            style={{ minWidth: '150px' }}
          />
          <ComboBox
            label="Filtrar por estado"
            items={[{ label: 'Todos', value: undefined }, ...ESTADOS_OPTIONS]}
            value={filterEstado}
            onValueChanged={e => setFilterEstado(e.detail.value as EstadoProducto)}
            clearButtonVisible
            style={{ minWidth: '150px' }}
          />
          <Button theme="primary" onClick={loadProductos} disabled={loading}>
            Buscar
          </Button>
          <Button theme="tertiary" onClick={clearFilters}>
            Limpiar Filtros
          </Button>
        </HorizontalLayout>
        
        <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
          <ProductoEntryForm onProductoCreated={onProductoCreated} />
          <span style={{ color: 'var(--lumo-secondary-text-color)' }}>
            Total: {productos.length} producto{productos.length !== 1 ? 's' : ''}
          </span>
        </HorizontalLayout>
      </VerticalLayout>

      {/* Grid de productos */}
      <Grid items={productos} style={{ height: '600px' }} loading={loading}>
        <Grid.Column 
          path="codigo" 
          header="Código" 
          width="100px" 
          flexGrow={0}
        />
        <Grid.Column 
          path="nombre" 
          header="Nombre" 
          renderer={({ item }) => (
            <div>
              <strong>{item.nombre}</strong>
              {item.marca && <div style={{ fontSize: '0.8em', color: 'var(--lumo-secondary-text-color)' }}>
                {item.marca} {item.modelo}
              </div>}
            </div>
          )}
        />
        <Grid.Column 
          path="categoria" 
          header="Categoría"
          width="130px"
          flexGrow={0}
          renderer={({ item }) => CATEGORIAS_OPTIONS.find(c => c.value === item.categoria)?.label ?? item.categoria}
        />
        <Grid.Column 
          path="precio" 
          header="Precio"
          width="100px"
          flexGrow={0}
          renderer={({ item }) => formatCurrency(item.precio)}
        />
        <Grid.Column 
          path="stock" 
          header="Stock"
          width="80px"
          flexGrow={0}
          renderer={({ item }) => {
            const status = getStockStatus(item);
            return (
              <div style={{ 
                color: status.theme === 'error' ? 'var(--lumo-error-text-color)' :
                      status.theme === 'contrast' ? 'var(--lumo-contrast-color)' :
                      'var(--lumo-success-text-color)'
              }}>
                {item.stock}
              </div>
            );
          }}
        />
        <Grid.Column 
          path="estado" 
          header="Estado"
          width="120px"
          flexGrow={0}
          renderer={({ item }) => (
            <span style={{
              padding: '2px 8px',
              borderRadius: '12px',
              fontSize: '0.8em',
              backgroundColor: getEstadoTheme(item.estado) === 'success' ? 'var(--lumo-success-color-10pct)' :
                              getEstadoTheme(item.estado) === 'error' ? 'var(--lumo-error-color-10pct)' :
                              'var(--lumo-contrast-color-10pct)',
              color: getEstadoTheme(item.estado) === 'success' ? 'var(--lumo-success-text-color)' :
                    getEstadoTheme(item.estado) === 'error' ? 'var(--lumo-error-text-color)' :
                    'var(--lumo-contrast-color)'
            }}>
              {ESTADOS_OPTIONS.find(e => e.value === item.estado)?.label ?? item.estado}
            </span>
          )}
        />
        <Grid.Column 
          path="ubicacion" 
          header="Ubicación"
          width="120px"
          flexGrow={0}
          renderer={({ item }) => item.ubicacion ?? '-'}
        />
        <Grid.Column
          header="Acciones"
          width="100px"
          flexGrow={0}
          renderer={({ item }) => (
            <HorizontalLayout theme="spacing">
              <ProductoEditForm producto={item} onProductoUpdated={onProductoUpdated} />
            </HorizontalLayout>
          )}
        />
      </Grid>
    </VerticalLayout>
  );
}