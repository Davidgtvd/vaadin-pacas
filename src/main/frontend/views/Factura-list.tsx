import {
  Dialog,
  Button,
  VerticalLayout,
  TextField,
  ComboBox,
  NumberField,
  HorizontalLayout,
  Notification,
  Grid,
  GridColumn,
} from '@vaadin/react-components';
import { useState } from 'react';

type Persona = { id: number; nombreCompleto: string };
type Producto = { id: number; nombre: string };
type DetalleFactura = {
  id?: number;
  cantidad: number;
  precioUnitario: number;
  total: number;
  producto: Producto;
};
type Factura = {
  id: number;
  nroFactura: string;
  persona: Persona;
  detalles: DetalleFactura[];
  total: number;
};

// Datos simulados
const personasMock: Persona[] = [
  { id: 1, nombreCompleto: 'Juan Perez' },
  { id: 2, nombreCompleto: 'Maria Gomez' },
];

const productosMock: Producto[] = [
  { id: 1, nombre: 'Camisa' },
  { id: 2, nombre: 'Pantalón' },
  { id: 3, nombre: 'Chaqueta' },
];

const facturasMock: Factura[] = [
  {
    id: 1,
    nroFactura: 'FAC001',
    persona: personasMock[0],
    detalles: [
      { cantidad: 2, precioUnitario: 20, total: 40, producto: productosMock[0] },
      { cantidad: 1, precioUnitario: 50, total: 50, producto: productosMock[1] },
    ],
    total: 90,
  },
];

function FacturaEntryForm({ onFacturaCreated }: { onFacturaCreated?: (factura: Factura) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [notificationOpened, setNotificationOpened] = useState(false);
  const [nroFactura, setNroFactura] = useState('');
  const [personaId, setPersonaId] = useState<number | undefined>(undefined);
  const [personas] = useState(personasMock);
  const [detalles, setDetalles] = useState<DetalleFactura[]>([]);
  const [productos] = useState(productosMock);
  const [productoId, setProductoId] = useState<number | undefined>(undefined);
  const [cantidad, setCantidad] = useState<number | undefined>(undefined);
  const [precioUnitario, setPrecioUnitario] = useState<number | undefined>(undefined);

  const addDetalle = () => {
    if (!productoId || !cantidad || !precioUnitario) {
      alert('Complete los datos del detalle');
      return;
    }
    const producto = productos.find(p => p.id === productoId);
    if (!producto) return;
    setDetalles([
      ...detalles,
      {
        cantidad,
        precioUnitario,
        total: cantidad * precioUnitario,
        producto,
      },
    ]);
    setProductoId(undefined);
    setCantidad(undefined);
    setPrecioUnitario(undefined);
  };

  const createFactura = () => {
    if (!nroFactura.trim() || !personaId || detalles.length === 0) {
      alert('Complete todos los campos y agregue al menos un detalle');
      return;
    }
    const nuevaFactura: Factura = {
      id: Date.now(),
      nroFactura: nroFactura.trim(),
      persona: personas.find(p => p.id === personaId)!,
      detalles,
      total: detalles.reduce((acc, d) => acc + d.total, 0),
    };
    onFacturaCreated?.(nuevaFactura);
    setDialogOpened(false);
    setNroFactura('');
    setPersonaId(undefined);
    setDetalles([]);
    setNotificationOpened(true);
  };

  return (
    <>
      <Dialog
        opened={dialogOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setDialogOpened(e.detail.value)}
        header={<h2>Registrar Factura</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={() => setDialogOpened(false)}>Cancelar</Button>
            <Button theme="primary" onClick={createFactura}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: 400 }}>
          <TextField
            label="Nro. Factura"
            value={nroFactura}
            onValueChanged={(e: CustomEvent<{ value: string }>) => setNroFactura(e.detail.value)}
            required
          />
          <ComboBox
            label="Persona"
            items={personas}
            itemLabelPath="nombreCompleto"
            itemValuePath="id"
            value={personaId !== undefined ? String(personaId) : undefined}
            onValueChanged={(e: CustomEvent<{ value: string | undefined }>) =>
              setPersonaId(e.detail.value ? Number(e.detail.value) : undefined)
            }
            required
          />
          <h4>Detalles</h4>
          <HorizontalLayout>
            <ComboBox
              label="Producto"
              items={productos}
              itemLabelPath="nombre"
              itemValuePath="id"
              value={productoId !== undefined ? String(productoId) : undefined}
              onValueChanged={(e: CustomEvent<{ value: string | undefined }>) =>
                setProductoId(e.detail.value ? Number(e.detail.value) : undefined)
              }
              style={{ width: 120 }}
            />
            <NumberField
              label="Cantidad"
              value={cantidad !== undefined ? String(cantidad) : ''}
              onValueChanged={(e: CustomEvent<{ value: string }>) => setCantidad(Number(e.detail.value))}
              min={1}
              style={{ width: 80 }}
            />
            <NumberField
              label="Precio Unitario"
              value={precioUnitario !== undefined ? String(precioUnitario) : ''}
              onValueChanged={(e: CustomEvent<{ value: string }>) => setPrecioUnitario(Number(e.detail.value))}
              min={0.01}
              style={{ width: 100 }}
            />
            <Button onClick={addDetalle}>Agregar</Button>
          </HorizontalLayout>
          <Grid style={{ height: 120 }} items={detalles}>
            <GridColumn path="producto.nombre" header="Producto" />
            <GridColumn path="cantidad" header="Cantidad" />
            <GridColumn path="precioUnitario" header="Precio Unitario" />
            <GridColumn path="total" header="Total" />
          </Grid>
        </VerticalLayout>
      </Dialog>

      <Button theme="primary" onClick={() => setDialogOpened(true)}>
        Registrar Factura
      </Button>

      <Notification
        opened={notificationOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setNotificationOpened(e.detail.value)}
        theme="success"
      >
        Factura registrada exitosamente
      </Notification>
    </>
  );
}

export default function FacturaListView() {
  const [facturas, setFacturas] = useState<Factura[]>(facturasMock);

  const onFacturaCreated = (nuevaFactura: Factura) => {
    setFacturas([...facturas, nuevaFactura]);
  };

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      <FacturaEntryForm onFacturaCreated={onFacturaCreated} />
      <Grid style={{ height: 300 }} items={facturas}>
        <GridColumn path="nroFactura" header="Nro. Factura" />
        <GridColumn path="persona.nombreCompleto" header="Persona" />
        <GridColumn path="total" header="Total" />
      </Grid>
    </VerticalLayout>
  );
}