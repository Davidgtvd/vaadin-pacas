import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  HorizontalLayout,
  NumberField,
  TextField,
  VerticalLayout,
  Notification,
} from '@vaadin/react-components';
import CompraServices from 'Frontend/generated/CompraServices';
import PersonaServices from 'Frontend/generated/PersonaServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { useEffect, useState } from 'react';

type Persona = {
  id: string;
  nombreCompleto: string;
};

type Compra = {
  id: string;
  subtotal: number;
  nroFactura: string;
  iva: number;
  total: number;
  persona: Persona;
};

type CompraEntryFormProps = {
  onCompraCreated?: () => void;
};

function CompraEntryForm({ onCompraCreated }: CompraEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [subtotal, setSubtotal] = useState<number | undefined>(undefined);
  const [nroFactura, setNroFactura] = useState('');
  const [iva, setIva] = useState<number | undefined>(undefined);
  const [total, setTotal] = useState<number | undefined>(undefined);
  const [personaId, setPersonaId] = useState<string | undefined>(undefined);
  const [personas, setPersonas] = useState<Persona[]>([]);

  useEffect(() => {
    PersonaServices.listAll().then((data: any) => {
      setPersonas(
        (data ?? []).map((p: any) => ({
          id: String(p.id),
          nombreCompleto: p.nombreCompleto || `${p.nombres} ${p.apellidos}`,
        }))
      );
    });
  }, []);

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setSubtotal(undefined);
    setNroFactura('');
    setIva(undefined);
    setTotal(undefined);
    setPersonaId(undefined);
  };

  const createCompra = async () => {
    try {
      if (
        subtotal === undefined ||
        !nroFactura.trim() ||
        iva === undefined ||
        total === undefined ||
        !personaId
      ) {
        Notification.show('Todos los campos son obligatorios', {
          duration: 4000,
          position: 'top-center',
          theme: 'error',
        });
        return;
      }
      await CompraServices.crearCompra(
        subtotal,
        nroFactura.trim(),
        iva,
        total,
        personaId
      );
      onCompraCreated?.();
      close();
      Notification.show('Compra registrada exitosamente', {
        duration: 4000,
        position: 'bottom-end',
        theme: 'success',
      });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Compra"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Registrar Compra</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createCompra}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '400px', maxWidth: '100%' }}>
          <NumberField
            label="Subtotal"
            value={subtotal?.toString() ?? ''}
            onValueChanged={e => setSubtotal(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0.01}
            required
          />
          <TextField
            label="Nro. Factura"
            value={nroFactura}
            onValueChanged={e => setNroFactura(e.detail.value)}
            required
          />
          <NumberField
            label="IVA"
            value={iva?.toString() ?? ''}
            onValueChanged={e => setIva(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0}
            required
          />
          <NumberField
            label="Total"
            value={total?.toString() ?? ''}
            onValueChanged={e => setTotal(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0.01}
            required
          />
          <ComboBox
            label="Persona"
            items={personas}
            itemLabelPath="nombreCompleto"
            itemValuePath="id"
            value={personaId}
            onValueChanged={e => setPersonaId(e.detail.value)}
            required
            placeholder="Seleccione una persona"
          />
        </VerticalLayout>
      </Dialog>
      <Button theme="primary" onClick={open}>
        Registrar Compra
      </Button>
    </>
  );
}

type CompraEditFormProps = {
  compra: Compra;
  onCompraUpdated?: () => void;
};

function CompraEditForm({ compra, onCompraUpdated }: CompraEditFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [subtotal, setSubtotal] = useState<number>(compra.subtotal);
  const [nroFactura, setNroFactura] = useState(compra.nroFactura);
  const [iva, setIva] = useState<number>(compra.iva);
  const [total, setTotal] = useState<number>(compra.total);
  const [personaId, setPersonaId] = useState<string>(compra.persona.id);
  const [personas, setPersonas] = useState<Persona[]>([]);

  useEffect(() => {
    PersonaServices.listAll().then((data: any) => {
      setPersonas(
        (data ?? []).map((p: any) => ({
          id: String(p.id),
          nombreCompleto: p.nombreCompleto || `${p.nombres} ${p.apellidos}`,
        }))
      );
    });
  }, []);

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setSubtotal(compra.subtotal);
    setNroFactura(compra.nroFactura);
    setIva(compra.iva);
    setTotal(compra.total);
    setPersonaId(compra.persona.id);
  };

  const updateCompra = async () => {
    try {
      if (
        subtotal === undefined ||
        !nroFactura.trim() ||
        iva === undefined ||
        total === undefined ||
        !personaId
      ) {
        Notification.show('Todos los campos son obligatorios', {
          duration: 4000,
          position: 'top-center',
          theme: 'error',
        });
        return;
      }
      await CompraServices.actualizarCompra(
        compra.id,
        subtotal,
        nroFactura.trim(),
        iva,
        total,
        personaId
      );
      onCompraUpdated?.();
      close();
      Notification.show('Compra actualizada exitosamente', {
        duration: 4000,
        position: 'bottom-end',
        theme: 'success',
      });
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Editar Compra"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
        header={<h2 style={{ margin: 0 }}>Editar Compra</h2>}
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateCompra}>
              Guardar
            </Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '400px', maxWidth: '100%' }}>
          <NumberField
            label="Subtotal"
            value={subtotal?.toString() ?? ''}
            onValueChanged={e => setSubtotal(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0.01}
            required
          />
          <TextField
            label="Nro. Factura"
            value={nroFactura}
            onValueChanged={e => setNroFactura(e.detail.value)}
            required
          />
          <NumberField
            label="IVA"
            value={iva?.toString() ?? ''}
            onValueChanged={e => setIva(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0}
            required
          />
          <NumberField
            label="Total"
            value={total?.toString() ?? ''}
            onValueChanged={e => setTotal(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0.01}
            required
          />
          <ComboBox
            label="Persona"
            items={personas}
            itemLabelPath="nombreCompleto"
            itemValuePath="id"
            value={personaId}
            onValueChanged={e => setPersonaId(e.detail.value)}
            required
            placeholder="Seleccione una persona"
          />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="tertiary">
        Editar
      </Button>
    </>
  );
}

export default function CompraListView() {
  const [compras, setCompras] = useState<Compra[]>([]);
  const [loading, setLoading] = useState(false);
  const [filterTexto, setFilterTexto] = useState('');

  const loadCompras = async () => {
    setLoading(true);
    try {
      let data: Compra[] = [];
      if (filterTexto.trim()) {
        data = await CompraServices.buscarPorTexto(filterTexto.trim()) ?? [];
      } else {
        data = await CompraServices.findAll() ?? [];
      }
      setCompras(data);
    } catch (error) {
      handleError(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCompras();
  }, []);

  const onCompraCreated = () => loadCompras();
  const onCompraUpdated = () => loadCompras();

  const clearFilters = () => {
    setFilterTexto('');
  };

  return (
    <VerticalLayout theme="spacing" style={{ width: '100%', height: '100%' }}>
      {/* Filtros y acciones */}
      <VerticalLayout theme="spacing">
        <HorizontalLayout theme="spacing" style={{ alignItems: 'end', flexWrap: 'wrap' }}>
          <TextField
            label="Buscar por persona"
            value={filterTexto}
            onValueChanged={e => setFilterTexto(e.detail.value)}
            clearButtonVisible
            style={{ minWidth: '200px' }}
          />
          <Button theme="primary" onClick={loadCompras} disabled={loading}>
            Buscar
          </Button>
          <Button theme="tertiary" onClick={clearFilters}>
            Limpiar Filtros
          </Button>
        </HorizontalLayout>
        <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
          <CompraEntryForm onCompraCreated={onCompraCreated} />
          <span style={{ color: 'var(--lumo-secondary-text-color)' }}>
            Total: {compras.length} compra{compras.length !== 1 ? 's' : ''}
          </span>
        </HorizontalLayout>
      </VerticalLayout>

      {/* Grid de compras */}
      <Grid items={compras} style={{ height: '600px' }} loading={loading}>
        <Grid.Column path="nroFactura" header="Nro. Factura" width="120px" flexGrow={0} />
        <Grid.Column path="subtotal" header="Subtotal" width="100px" flexGrow={0} />
        <Grid.Column path="iva" header="IVA" width="80px" flexGrow={0} />
        <Grid.Column path="total" header="Total" width="100px" flexGrow={0} />
        <Grid.Column
          path="persona"
          header="Persona"
          renderer={({ item }) => item.persona?.nombreCompleto ?? '-'}
          width="200px"
          flexGrow={0}
        />
        <Grid.Column
          header="Acciones"
          width="100px"
          flexGrow={0}
          renderer={({ item }) => (
            <HorizontalLayout theme="spacing">
              <CompraEditForm compra={item} onCompraUpdated={onCompraUpdated} />
            </HorizontalLayout>
          )}
        />
      </Grid>
    </VerticalLayout>
  );
}