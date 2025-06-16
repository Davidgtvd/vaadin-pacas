import {
  Button, Dialog, Grid, HorizontalLayout, NumberField, TextField, VerticalLayout, ComboBox, Notification
} from '@vaadin/react-components';
import FacturaServices from 'Frontend/generated/FacturaServices';
import DetalleFacturaServices from 'Frontend/generated/DetalleFacturaServices';
import ProductoServices from 'Frontend/generated/ProductoServices';
import PersonaServices from 'Frontend/generated/PersonaServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { useEffect, useState } from 'react';

type Persona = { id: string; nombreCompleto: string; };
type Producto = { id: string; nombre: string; };
type DetalleFactura = {
  id?: string;
  cantidad: number;
  precioUnitario: number;
  total: number;
  producto: Producto;
};
type Factura = {
  id: string;
  nroFactura: string;
  persona: Persona;
  detalles: DetalleFactura[];
  total: number;
};

function FacturaEntryForm({ onFacturaCreated }: { onFacturaCreated?: () => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nroFactura, setNroFactura] = useState('');
  const [personaId, setPersonaId] = useState<string | undefined>();
  const [personas, setPersonas] = useState<Persona[]>([]);
  const [detalles, setDetalles] = useState<DetalleFactura[]>([]);
  const [productos, setProductos] = useState<Producto[]>([]);
  const [productoId, setProductoId] = useState<string | undefined>();
  const [cantidad, setCantidad] = useState<number | undefined>();
  const [precioUnitario, setPrecioUnitario] = useState<number | undefined>();

  useEffect(() => {
    PersonaServices.listAll().then((data: any) => setPersonas((data ?? []).map((p: any) => ({
      id: String(p.id), nombreCompleto: p.nombreCompleto || `${p.nombres} ${p.apellidos}`,
    }))));
    ProductoServices.findAll().then((data: any) => setProductos((data ?? []).map((p: any) => ({
      id: String(p.id), nombre: p.nombre,
    }))));
  }, []);

  const addDetalle = () => {
    if (!productoId || !cantidad || !precioUnitario) {
      Notification.show('Complete los datos del detalle', { theme: 'error' });
      return;
    }
    const producto = productos.find(p => p.id === productoId);
    if (!producto) return;
    setDetalles([...detalles, {
      cantidad,
      precioUnitario,
      total: cantidad * precioUnitario,
      producto,
    }]);
    setProductoId(undefined);
    setCantidad(undefined);
    setPrecioUnitario(undefined);
  };

  const createFactura = async () => {
    try {
      if (!nroFactura.trim() || !personaId || detalles.length === 0) {
        Notification.show('Complete todos los campos y agregue al menos un detalle', { theme: 'error' });
        return;
      }
      await FacturaServices.crearFactura(nroFactura, personaId, detalles);
      onFacturaCreated?.();
      setDialogOpened(false);
      setNroFactura('');
      setPersonaId(undefined);
      setDetalles([]);
      Notification.show('Factura registrada exitosamente', { theme: 'success' });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog opened={dialogOpened} onOpenedChanged={e => setDialogOpened(e.detail.value)}
        header={<h2>Registrar Factura</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={() => setDialogOpened(false)}>Cancelar</Button>
            <Button theme="primary" onClick={createFactura}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: 400 }}>
          <TextField label="Nro. Factura" value={nroFactura} onValueChanged={e => setNroFactura(e.detail.value)} required />
          <ComboBox label="Persona" items={personas} itemLabelPath="nombreCompleto" itemValuePath="id"
            value={personaId} onValueChanged={e => setPersonaId(e.detail.value)} required />
          <h4>Detalles</h4>
          <HorizontalLayout>
            <ComboBox label="Producto" items={productos} itemLabelPath="nombre" itemValuePath="id"
              value={productoId} onValueChanged={e => setProductoId(e.detail.value)} style={{ width: 120 }} />
            <NumberField label="Cantidad" value={cantidad?.toString() ?? ''} onValueChanged={e => setCantidad(Number(e.detail.value))} min={1} style={{ width: 80 }} />
            <NumberField label="Precio Unitario" value={precioUnitario?.toString() ?? ''} onValueChanged={e => setPrecioUnitario(Number(e.detail.value))} min={0.01} style={{ width: 100 }} />
            <Button onClick={addDetalle}>Agregar</Button>
          </HorizontalLayout>
          <Grid items={detalles} style={{ height: 120 }}>
            <Grid.Column path="producto.nombre" header="Producto" />
            <Grid.Column path="cantidad" header="Cantidad" />
            <Grid.Column path="precioUnitario" header="Precio Unitario" />
            <Grid.Column path="total" header="Total" />
          </Grid>
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={() => setDialogOpened(true)}>Registrar Factura</Button>
    </>
  );
}

export default function FacturaListView() {
  const [facturas, setFacturas] = useState<Factura[]>([]);
  const [loading, setLoading] = useState(false);

  const loadFacturas = async () => {
    setLoading(true);
    try {
      const data = await FacturaServices.findAll() ?? [];
      setFacturas(data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadFacturas(); }, []);

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      <FacturaEntryForm onFacturaCreated={loadFacturas} />
      <Grid items={facturas} style={{ height: '600px' }} loading={loading}>
        <Grid.Column path="nroFactura" header="Nro. Factura" />
        <Grid.Column path="persona.nombreCompleto" header="Persona" />
        <Grid.Column path="total" header="Total" />
        <Grid.Column
          header="Detalles"
          renderer={({ item }) => (
            <ul>
              {item.detalles?.map((d: DetalleFactura, idx: number) => (
                <li key={idx}>
                  {d.producto.nombre} - {d.cantidad} x {d.precioUnitario} = {d.total}
                </li>
              ))}
            </ul>
          )}
        />
      </Grid>
    </VerticalLayout>
  );
  
}