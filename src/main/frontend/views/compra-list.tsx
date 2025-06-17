import {
  Button,
  ComboBox,
  Dialog,
  Grid,
  GridColumn,
  HorizontalLayout,
  NumberField,
  TextField,
  VerticalLayout,
  Notification,
} from '@vaadin/react-components';
import { useState } from 'react';

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

// Datos simulados
const personasMock: Persona[] = [
  { id: '1', nombreCompleto: 'Juan Perez' },
  { id: '2', nombreCompleto: 'Maria Gomez' },
];

const comprasMock: Compra[] = [
  {
    id: '1',
    subtotal: 100,
    nroFactura: 'F001',
    iva: 12,
    total: 112,
    persona: personasMock[0],
  },
  {
    id: '2',
    subtotal: 200,
    nroFactura: 'F002',
    iva: 24,
    total: 224,
    persona: personasMock[1],
  },
];

function CompraEntryForm({ onCompraCreated }: { onCompraCreated?: (compra: Compra) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [subtotal, setSubtotal] = useState<number | undefined>(undefined);
  const [nroFactura, setNroFactura] = useState('');
  const [iva, setIva] = useState<number | undefined>(undefined);
  const [total, setTotal] = useState<number | undefined>(undefined);
  const [personaId, setPersonaId] = useState<string | undefined>(undefined);
  const [personas] = useState(personasMock);

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setSubtotal(undefined);
    setNroFactura('');
    setIva(undefined);
    setTotal(undefined);
    setPersonaId(undefined);
  };

  const createCompra = () => {
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

    const nuevaCompra: Compra = {
      id: String(Date.now()),
      subtotal,
      nroFactura: nroFactura.trim(),
      iva,
      total,
      persona: personas.find(p => p.id === personaId)!,
    };

    onCompraCreated?.(nuevaCompra);
    close();
    Notification.show('Compra registrada exitosamente', {
      duration: 4000,
      position: 'bottom-end',
      theme: 'success',
    });
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Compra"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setDialogOpened(e.detail.value)}
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
            onValueChanged={(e) => setSubtotal(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0.01}
            required
          />
          <TextField
            label="Nro. Factura"
            value={nroFactura}
            onValueChanged={(e) => setNroFactura(e.detail.value)}
            required
          />
          <NumberField
            label="IVA"
            value={iva?.toString() ?? ''}
            onValueChanged={(e) => setIva(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0}
            required
          />
          <NumberField
            label="Total"
            value={total?.toString() ?? ''}
            onValueChanged={(e) => setTotal(e.detail.value ? parseFloat(e.detail.value) : undefined)}
            min={0.01}
            required
          />
          <ComboBox
            label="Persona"
            items={personas}
            itemLabelPath="nombreCompleto"
            itemValuePath="id"
            value={personaId}
            onValueChanged={(e) => setPersonaId(e.detail.value)}
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

function CompraEditForm({ compra, onCompraUpdated }: { compra: Compra; onCompraUpdated?: (compra: Compra) => void }) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [subtotal, setSubtotal] = useState<number>(compra.subtotal);
  const [nroFactura, setNroFactura] = useState(compra.nroFactura);
  const [iva, setIva] = useState<number>(compra.iva);
  const [total, setTotal] = useState<number>(compra.total);
  const [personaId, setPersonaId] = useState<string>(compra.persona.id);
  const [personas] = useState(personasMock);

  const open = () => setDialogOpened(true);

  const close = () => {
    setDialogOpened(false);
    setSubtotal(compra.subtotal);
    setNroFactura(compra.nroFactura);
    setIva(compra.iva);
    setTotal(compra.total);
    setPersonaId(compra.persona.id);
  };

  const updateCompra = () => {
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

    const compraActualizada: Compra = {
      id: compra.id,
      subtotal,
      nroFactura: nroFactura.trim(),
      iva,
      total,
      persona: personas.find(p => p.id === personaId)!,
    };

    onCompraUpdated?.(compraActualizada);
    close();
    Notification.show('Compra actualizada exitosamente', {
      duration: 4000,
      position: 'bottom-end',
      theme: 'success',
    });
  };

  return (
    <>
      <Dialog
        aria-label="Editar Compra"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(e: CustomEvent<{ value: boolean }>) => setDialogOpened(e.detail.value)}
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
            onValueChanged={(e) => setSubtotal(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0.01}
            required
          />
          <TextField
            label="Nro. Factura"
            value={nroFactura}
            onValueChanged={(e) => setNroFactura(e.detail.value)}
            required
          />
          <NumberField
            label="IVA"
            value={iva?.toString() ?? ''}
            onValueChanged={(e) => setIva(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0}
            required
          />
          <NumberField
            label="Total"
            value={total?.toString() ?? ''}
            onValueChanged={(e) => setTotal(e.detail.value ? parseFloat(e.detail.value) : 0)}
            min={0.01}
            required
          />
          <ComboBox
            label="Persona"
            items={personas}
            itemLabelPath="nombreCompleto"
            itemValuePath="id"
            value={personaId}
            onValueChanged={(e) => setPersonaId(e.detail.value)}
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
  const [compras, setCompras] = useState<Compra[]>(comprasMock);
  const [filterTexto, setFilterTexto] = useState('');

  const filteredCompras = compras.filter(c =>
    c.persona.nombreCompleto.toLowerCase().includes(filterTexto.toLowerCase())
  );

  const onCompraCreated = (nuevaCompra: Compra) => {
    setCompras([...compras, nuevaCompra]);
  };

  const onCompraUpdated = (compraActualizada: Compra) => {
    setCompras(compras.map(c => (c.id === compraActualizada.id ? compraActualizada : c)));
  };

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
            onValueChanged={(e) => setFilterTexto(e.detail.value)}
            clearButtonVisible
            style={{ minWidth: '200px' }}
          />
          <Button theme="primary" disabled>
            Buscar
          </Button>
          <Button theme="tertiary" onClick={clearFilters}>
            Limpiar Filtros
          </Button>
        </HorizontalLayout>
        <HorizontalLayout theme="spacing" style={{ alignItems: 'center' }}>
          <CompraEntryForm onCompraCreated={onCompraCreated} />
          <span style={{ color: 'var(--lumo-secondary-text-color)' }}>
            Total: {filteredCompras.length} compra{filteredCompras.length !== 1 ? 's' : ''}
          </span>
        </HorizontalLayout>
      </VerticalLayout>
      {/* Grid de compras */}
      <Grid items={filteredCompras} style={{ height: 400 }}>
        <GridColumn path="nroFactura" header="Nro. Factura" />
        <GridColumn path="subtotal" header="Subtotal" />
        <GridColumn path="iva" header="IVA" />
        <GridColumn path="total" header="Total" />
        <GridColumn path="persona.nombreCompleto" header="Persona" />
        <GridColumn
          header="Acciones"
          renderer={({ item }) => (
            <CompraEditForm compra={item as Compra} onCompraUpdated={onCompraUpdated} />
          )}
        />
      </Grid>
    </VerticalLayout>
  );
}