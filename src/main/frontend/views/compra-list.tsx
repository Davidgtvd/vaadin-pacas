import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, Dialog, Grid, GridColumn, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import * as PacaService from 'Frontend/generated/CompraService';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useDataProvider } from '@vaadin/hilla-react-crud';

export const config: ViewConfig = {
  title: 'Compra',
  route: 'pacas',
  menu: {
    icon: 'vaadin:package',
    order: 1,
    title: 'Compra',
  },
};

type Compra = {
  id: string;
  nombre: string;
  descripcion: string;
};

function PacaEntryForm({ onPacaCreated }: { onPacaCreated?: () => void }) {
  const nombre = useSignal('');
  const descripcion = useSignal('');
  const dialogOpened = useSignal(false);

  const createPaca = async () => {
    try {
      if (nombre.value.trim() && descripcion.value.trim()) {
        await PacaService.save(nombre.value, descripcion.value);
        if (onPacaCreated) onPacaCreated();
        nombre.value = '';
        descripcion.value = '';
        dialogOpened.value = false;
        Notification.show('Paca registrada correctamente', { 
          duration: 3000, 
          position: 'bottom-end', 
          theme: 'success' 
        });
      } else {
        Notification.show('Todos los campos son requeridos', { 
          duration: 3000, 
          position: 'top-center', 
          theme: 'error' 
        });
      }
    } catch (error) {
      console.error('Error al guardar paca:', error);
      Notification.show('Error al registrar la paca', { 
        duration: 3000, 
        position: 'top-center', 
        theme: 'error' 
      });
    }
  };

  return (
    <>
      <Dialog
        modeless
        headerTitle="Nueva Paca"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => { dialogOpened.value = detail.value; }}
        footer={
          <>
            <Button onClick={() => (dialogOpened.value = false)}>Cancelar</Button>
            <Button onClick={createPaca} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField 
            label="Nombre" 
            required 
            value={nombre.value} 
            onValueChanged={e => (nombre.value = e.detail.value)} 
          />
          <TextField 
            label="Descripción" 
            required 
            value={descripcion.value} 
            onValueChanged={e => (descripcion.value = e.detail.value)} 
          />
        </VerticalLayout>
      </Dialog>
      <Button 
        onClick={() => (dialogOpened.value = true)}
        theme="primary"
        style={{ marginBottom: '1rem' }}
      >
        Agregar Nueva Paca
      </Button>
    </>
  );
}

export default function PacaListView() {
  const dataProvider = useDataProvider({
    list: async () => {
      try {
        const pacas = await PacaService.listaPacas();
        return pacas || [];
      } catch (error) {
        console.error('Error al cargar pacas:', error);
        Notification.show('Error al cargar las pacas', { 
          duration: 3000, 
          position: 'top-center', 
          theme: 'error' 
        });
        return [];
      }
    }
  });

  return (
    <div className="p-m" style={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <div style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'center',
        marginBottom: '1rem'
      }}>
        <h2 style={{ margin: 0, color: 'var(--lumo-primary-text-color)' }}>
          Gestión de Pacas
        </h2>
        <PacaEntryForm onPacaCreated={dataProvider.refresh} />
      </div>

      <Grid 
        dataProvider={dataProvider.dataProvider}
        theme="row-stripes"
        style={{ flex: 1 }}
      >
        <GridColumn 
          path="id" 
          header="ID" 
          width="80px" 
          frozen 
        />
        <GridColumn 
          path="nombre" 
          header="Nombre"
          autoWidth 
        />
        <GridColumn 
          path="descripcion" 
          header="Descripción"
          autoWidth 
        />
      </Grid>
    </div>
  );
}