import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  GridColumn,
  HorizontalLayout,
  Notification,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import { useState } from 'react';

type Pago = {
  id: string;
  codigoSeguridad: string;
  metodoPago: 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO';
  estado: boolean;
  fechaPago: string;
  compra?: {
    id: string;
    numFactura?: string;
    total?: number;
  };
};

// Datos simulados iniciales
const pagosMock: Pago[] = [
  {
    id: '1',
    codigoSeguridad: '123',
    metodoPago: 'TARJETA_DE_CREDITO',
    estado: true,
    fechaPago: new Date().toISOString(),
    compra: { id: '1', numFactura: 'F001', total: 150.0 },
  },
  {
    id: '2',
    codigoSeguridad: '456',
    metodoPago: 'TARJETA_DE_DEBITO',
    estado: false,
    fechaPago: new Date().toISOString(),
    compra: { id: '2', numFactura: 'F002', total: 200.0 },
  },
];

function PagoEntryForm({ onPagoCreated }: { onPagoCreated?: (pago: Pago) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [codigoSeguridad, setCodigoSeguridad] = useState('');
  const [metodoPago, setMetodoPago] = useState<'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO'>('TARJETA_DE_CREDITO');
  const [compraId, setCompraId] = useState('');

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setCodigoSeguridad('');
    setMetodoPago('TARJETA_DE_CREDITO');
    setCompraId('');
  };

  const createPago = () => {
    if (!codigoSeguridad || !compraId) {
      Notification.show('Todos los campos son obligatorios', {
        duration: 4000,
        position: 'top-center',
        theme: 'error',
      });
      return;
    }

    if (codigoSeguridad.length < 3 || codigoSeguridad.length > 4) {
      Notification.show('El código de seguridad debe tener 3 o 4 dígitos', {
        duration: 4000,
        position: 'top-center',
        theme: 'error',
      });
      return;
    }

    const nuevoPago: Pago = {
      id: String(Date.now()),
      codigoSeguridad,
      metodoPago,
      estado: true,
      fechaPago: new Date().toISOString(),
      compra: { id: compraId, numFactura: compraId, total: undefined },
    };

    onPagoCreated?.(nuevoPago);
    close();
    Notification.show('Pago registrado exitosamente', {
      duration: 4000,
      position: 'bottom-end',
      theme: 'success',
    });
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Pago"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Registrar Pago</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createPago}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '450px', maxWidth: '100%' }}>
          <ComboBox
            label="Método de Pago"
            items={[
              { label: 'Tarjeta de Crédito', value: 'TARJETA_DE_CREDITO' },
              { label: 'Tarjeta de Débito', value: 'TARJETA_DE_DEBITO' },
            ]}
            value={metodoPago}
            onValueChanged={(e) => setMetodoPago(e.detail.value as 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO')}
            required
          />

          <TextField
            label="Código de Seguridad"
            placeholder="3 o 4 dígitos"
            value={codigoSeguridad}
            onValueChanged={(e) => setCodigoSeguridad(e.detail.value)}
            maxlength={4}
            required
          />

          <TextField
            label="ID de Compra"
            placeholder="Ingrese el ID de la compra"
            value={compraId}
            onValueChanged={(e) => setCompraId(e.detail.value)}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={open}>
        Registrar Pago
      </Button>
    </>
  );
}

function PagoEditForm({ pago, onPagoUpdated }: { pago: Pago; onPagoUpdated?: (pago: Pago) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [codigoSeguridad, setCodigoSeguridad] = useState(pago.codigoSeguridad);
  const [metodoPago, setMetodoPago] = useState(pago.metodoPago);
  const [estado, setEstado] = useState(pago.estado);

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setCodigoSeguridad(pago.codigoSeguridad);
    setMetodoPago(pago.metodoPago);
    setEstado(pago.estado);
  };

  const updatePago = () => {
    if (!codigoSeguridad) {
      Notification.show('El código de seguridad es obligatorio', {
        duration: 4000,
        position: 'top-center',
        theme: 'error',
      });
      return;
    }

    const pagoActualizado: Pago = {
      ...pago,
      codigoSeguridad,
      metodoPago,
      estado,
    };

    onPagoUpdated?.(pagoActualizado);
    close();
    Notification.show('Pago actualizado exitosamente', {
      duration: 4000,
      position: 'bottom-end',
      theme: 'success',
    });
  };

  return (
    <>
      <Dialog
        aria-label="Editar Pago"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Editar Pago</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updatePago}>
              Guardar
            </Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '400px', maxWidth: '100%' }}>
          <ComboBox
            label="Método de Pago"
            items={[
              { label: 'Tarjeta de Crédito', value: 'TARJETA_DE_CREDITO' },
              { label: 'Tarjeta de Débito', value: 'TARJETA_DE_DEBITO' },
            ]}
            value={metodoPago}
            onValueChanged={(e) => setMetodoPago(e.detail.value as 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO')}
            required
          />

          <TextField
            label="Código Seguridad"
            value={codigoSeguridad}
            onValueChanged={(e) => setCodigoSeguridad(e.detail.value)}
            maxlength={4}
            required
          />
          <ComboBox
            label="Estado"
            items={[
              { label: 'Activo', value: 'true' },
              { label: 'Inactivo', value: 'false' },
            ]}
            value={estado ? 'true' : 'false'}
            onValueChanged={(e) => setEstado(e.detail.value === 'true')}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

export default function PagoListView() {
  const [pagos, setPagos] = useState<Pago[]>(pagosMock);
  const [filterFactura, setFilterFactura] = useState('');

  const filteredPagos = pagos.filter(p =>
    p.compra?.numFactura?.toLowerCase().includes(filterFactura.toLowerCase())
  );

  const onPagoCreated = (nuevoPago: Pago) => {
    setPagos([...pagos, nuevoPago]);
  };

  const onPagoUpdated = (pagoActualizado: Pago) => {
    setPagos(pagos.map(p => (p.id === pagoActualizado.id ? pagoActualizado : p)));
  };

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
        <TextField
          label="Buscar por factura"
          value={filterFactura}
          onValueChanged={(e) => setFilterFactura(e.detail.value)}
          clearButtonVisible
        />
        <Button theme="primary" disabled>
          Buscar
        </Button>
        <PagoEntryForm onPagoCreated={onPagoCreated} />
      </HorizontalLayout>

      <Grid items={filteredPagos} style={{ height: '600px' }}>
        <GridColumn path="compra.numFactura" header="Factura" />
        <GridColumn
          path="metodoPago"
          header="Método"
          renderer={({ item }) =>
            item.metodoPago === 'TARJETA_DE_CREDITO' ? 'Tarjeta Crédito' : 'Tarjeta Débito'
          }
        />
        <GridColumn
          path="codigoSeguridad"
          header="Código Seguridad"
          renderer={() => '••••'}
        />
        <GridColumn
          path="estado"
          header="Estado"
          renderer={({ item }) => (item.estado ? 'Activo' : 'Inactivo')}
        />
        <GridColumn path="fechaPago" header="Fecha Pago" />
        <GridColumn
          path="compra.total"
          header="Monto"
          renderer={({ item }) => `$${item.compra?.total?.toFixed(2) ?? '0.00'}`}
        />
        <GridColumn
          header="Acciones"
          width="150px"
          flexGrow={0}
          renderer={({ item }) => <PagoEditForm pago={item} onPagoUpdated={onPagoUpdated} />}
        />
      </Grid>
    </VerticalLayout>
  );
}