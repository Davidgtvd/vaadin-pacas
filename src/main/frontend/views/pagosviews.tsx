// src/views/pago-list.tsx
import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  HorizontalLayout,
  Notification,
  TextField,
  VerticalLayout,
} from '@vaadin/react-components';
import PagoServices from 'Frontend/generated/PagoServices';
import CompraServices from 'Frontend/generated/CompraServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { useEffect, useState } from 'react';

type Pago = {
  id: string;
  codigoSeguridad: string;
  metodoPago: 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO';
  estado: boolean;
  fechaPago: string;
  compra: {
    id: string;
    numFactura: string;
    total: number;
  };
};

type Compra = {
  id: string;
  numFactura: string;
  total: number;
};

// --- FORMULARIO DE CREAR PAGO ---
type PagoEntryFormProps = {
  onPagoCreated?: () => void;
};

function PagoEntryForm({ onPagoCreated }: PagoEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [codigoSeguridad, setCodigoSeguridad] = useState('');
  const [metodoPago, setMetodoPago] = useState<'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO'>('TARJETA_DE_CREDITO');
  const [compraId, setCompraId] = useState('');
  const [compras, setCompras] = useState<Compra[]>([]);

  useEffect(() => {
    if (dialogOpened) {
      loadCompras();
    }
  }, [dialogOpened]);

  const loadCompras = async () => {
    try {
      const comprasData = await CompraServices.getComprasSinPago();
      setCompras(comprasData ?? []);
    } catch (error) {
      handleError(error);
    }
  };

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setCodigoSeguridad('');
    setMetodoPago('TARJETA_DE_CREDITO');
    setCompraId('');
  };

  const createPago = async () => {
    try {
      if (!codigoSeguridad || !compraId) {
        Notification.show('Todos los campos son obligatorios', { 
          duration: 4000, 
          position: 'top-center', 
          theme: 'error' 
        });
        return;
      }

      if (codigoSeguridad.length < 3 || codigoSeguridad.length > 4) {
        Notification.show('El código de seguridad debe tener 3 o 4 dígitos', { 
          duration: 4000, 
          position: 'top-center', 
          theme: 'error' 
        });
        return;
      }

      await PagoServices.create(
        codigoSeguridad,
        metodoPago,
        parseInt(compraId)
      );
      
      onPagoCreated?.();
      close();
      Notification.show('Pago registrado exitosamente', { 
        duration: 4000, 
        position: 'bottom-end', 
        theme: 'success' 
      });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Pago"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Registrar Pago</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createPago}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '450px', maxWidth: '100%' }}>
          <ComboBox
            label="Método de Pago"
            items={[
              { label: 'Tarjeta de Crédito', value: 'TARJETA_DE_CREDITO' },
              { label: 'Tarjeta de Débito', value: 'TARJETA_DE_DEBITO' }
            ]}
            value={metodoPago}
            onValueChanged={e => setMetodoPago(e.detail.value as 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO')}
            required
          />

          <TextField 
            label="Código de Seguridad" 
            placeholder="3 o 4 dígitos"
            value={codigoSeguridad}
            onValueChanged={e => setCodigoSeguridad(e.detail.value)}
            maxLength={4}
            required
          />

          <ComboBox
            label="Compra"
            items={compras.map(c => ({ 
              label: `Factura #${c.numFactura} - $${c.total.toFixed(2)}`, 
              value: c.id 
            })}
            value={compraId}
            onValueChanged={e => setCompraId(e.detail.value)}
            placeholder="Seleccione una compra"
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={open}>Registrar Pago</Button>
    </>
  );
}

// --- FORMULARIO DE EDICIÓN ---
type PagoEditFormProps = {
  pago: Pago;
  onPagoUpdated?: () => void;
};

function PagoEditForm({ pago, onPagoUpdated }: PagoEditFormProps) {
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

  const updatePago = async () => {
    try {
      if (!codigoSeguridad) {
        Notification.show('El código de seguridad es obligatorio', { 
          duration: 4000, 
          position: 'top-center', 
          theme: 'error' 
        });
        return;
      }

      await PagoServices.update(
        pago.id,
        codigoSeguridad,
        metodoPago,
        estado
      );
      
      onPagoUpdated?.();
      close();
      Notification.show('Pago actualizado exitosamente', { 
        duration: 4000, 
        position: 'bottom-end', 
        theme: 'success' 
      });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Pago"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Editar Pago</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updatePago}>Guardar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '400px', maxWidth: '100%' }}>
          <ComboBox
            label="Método de Pago"
            items={[
              { label: 'Tarjeta de Crédito', value: 'TARJETA_DE_CREDITO' },
              { label: 'Tarjeta de Débito', value: 'TARJETA_DE_DEBITO' }
            ]}
            value={metodoPago}
            onValueChanged={e => setMetodoPago(e.detail.value as 'TARJETA_DE_CREDITO' | 'TARJETA_DE_DEBITO')}
            required
          />

          <TextField 
            label="Código de Seguridad" 
            value={codigoSeguridad}
            onValueChanged={e => setCodigoSeguridad(e.detail.value)}
            maxLength={4}
            required
          />

          <ComboBox
            label="Estado"
            items={[
              { label: 'Activo', value: true },
              { label: 'Inactivo', value: false }
            ]}
            value={estado}
            onValueChanged={e => setEstado(e.detail.value)}
            required
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

// --- VISTA PRINCIPAL ---
export default function PagoListView() {
  const [pagos, setPagos] = useState<Pago[]>([]);
  const [loading, setLoading] = useState(false);
  const [filterFactura, setFilterFactura] = useState('');

  const loadPagos = async () => {
    setLoading(true);
    try {
      const data = filterFactura.trim()
        ? await PagoServices.buscarPorFactura(filterFactura.trim())
        : await PagoServices.listAll();
      setPagos(data ?? []);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadPagos();
  }, []);

  const onPagoCreated = () => loadPagos();
  const onPagoUpdated = () => loadPagos();

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
        <TextField
          label="Buscar por factura"
          value={filterFactura}
          onValueChanged={e => setFilterFactura(e.detail.value)}
          clearButtonVisible
        />
        <Button theme="primary" onClick={loadPagos} disabled={loading}>
          Buscar
        </Button>
        <PagoEntryForm onPagoCreated={onPagoCreated} />
      </HorizontalLayout>

      <Grid items={pagos} style={{ height: '600px' }} loading={loading}>
        <Grid.Column path="compra.numFactura" header="Factura" />
        <Grid.Column
          path="metodoPago"
          header="Método"
          renderer={({ item }) => item.metodoPago === 'TARJETA_DE_CREDITO' ? 'Tarjeta Crédito' : 'Tarjeta Débito'}
        />
        <Grid.Column
          path="codigoSeguridad"
          header="Código Seguridad"
          renderer={({ item }) => '••••'}
        />
        <Grid.Column
          path="estado"
          header="Estado"
          renderer={({ item }) => item.estado ? 'Activo' : 'Inactivo'}
        />
        <Grid.Column path="fechaPago" header="Fecha Pago" />
        <Grid.Column
          path="compra.total"
          header="Monto"
          renderer={({ item }) => `$${item.compra.total.toFixed(2)}`}
        />
        <Grid.Column
          header="Acciones"
          width="150px"
          flexGrow={0}
          renderer={({ item }) => <PagoEditForm pago={item} onPagoUpdated={onPagoUpdated} />}
        />
      </Grid>
    </VerticalLayout>
  );
  
}