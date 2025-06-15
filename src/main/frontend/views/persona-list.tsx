import {
  Button, ComboBox, Dialog, Grid, GridColumn, GridSortColumn,
  HorizontalLayout, Icon, Select, TextField, VerticalLayout, DatePicker
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import PersonaServices from 'Frontend/generated/PersonaServices';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { useEffect, useState } from 'react';

type Persona = {
  id: string;
  nombres: string;
  apellidos: string;
  email: string;
  tipoIdentificacion: string;
  identificacion: string;
  sexo: string;
  telefono?: string;
  direccion?: string;
  fechaNacimiento?: string;
  nombreCompleto?: string;
  edad?: number;
  tieneCuenta?: boolean;
};

// FORMULARIO DE CREAR
type PersonaEntryFormProps = {
  onPersonaCreated?: () => void;
};

function PersonaEntryForm(props: PersonaEntryFormProps) {
  const [dialogOpened, setDialogOpened] = useState(false);
  const [nombres, setNombres] = useState('');
  const [apellidos, setApellidos] = useState('');
  const [email, setEmail] = useState('');
  const [tipoIdentificacion, setTipoIdentificacion] = useState('CEDULA');
  const [identificacion, setIdentificacion] = useState('');
  const [sexo, setSexo] = useState('MASCULINO');
  const [telefono, setTelefono] = useState('');
  const [direccion, setDireccion] = useState('');
  const [fechaNacimiento, setFechaNacimiento] = useState('');

  const tiposIdentificacion = [
    { label: 'Cédula', value: 'CEDULA' },
    { label: 'Pasaporte', value: 'PASAPORTE' },
    { label: 'RUC', value: 'RUC' },
  ];

  const sexoOptions = [
    { label: 'Masculino', value: 'MASCULINO' },
    { label: 'Femenino', value: 'FEMENINO' },
    { label: 'Otro', value: 'OTRO' },
  ];

  const open = () => setDialogOpened(true);
  const close = () => {
    setDialogOpened(false);
    setNombres('');
    setApellidos('');
    setEmail('');
    setTipoIdentificacion('CEDULA');
    setIdentificacion('');
    setSexo('MASCULINO');
    setTelefono('');
    setDireccion('');
    setFechaNacimiento('');
  };

  const createPersona = async () => {
    try {
      if (
        nombres.trim().length > 0 &&
        apellidos.trim().length > 0 &&
        email.trim().length > 0 &&
        identificacion.trim().length > 0
      ) {
        await PersonaServices.create(
          nombres.trim(),
          apellidos.trim(),
          email.trim(),
          tipoIdentificacion,
          identificacion.trim(),
          sexo,
          telefono.trim(),
          direccion.trim(),
          fechaNacimiento || null
        );
        props.onPersonaCreated?.();
        close();
        Notification.show('Persona creada exitosamente', {
          duration: 5000,
          position: 'bottom-end',
          theme: 'success'
        });
      } else {
        Notification.show('Los campos nombres, apellidos, email e identificación son obligatorios', {
          duration: 5000,
          position: 'top-center',
          theme: 'error'
        });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        aria-label="Registrar Persona"
        draggable
        modeless
        opened={dialogOpened}
        onOpenedChanged={(event) => setDialogOpened(event.detail.value)}
        header={
          <h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>
            Registrar Persona
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createPersona}>Registrar</Button>
          </>
        )}
      >
        <VerticalLayout theme="spacing" style={{ width: '450px', maxWidth: '100%', alignItems: 'stretch' }}>
          <HorizontalLayout theme="spacing">
            <TextField label="Nombres" placeholder="Ingrese los nombres" value={nombres} onValueChanged={(evt) => setNombres(evt.detail.value)} required style={{ flex: 1 }} />
            <TextField label="Apellidos" placeholder="Ingrese los apellidos" value={apellidos} onValueChanged={(evt) => setApellidos(evt.detail.value)} required style={{ flex: 1 }} />
          </HorizontalLayout>
          <TextField label="Email" placeholder="ejemplo@correo.com" value={email} onValueChanged={(evt) => setEmail(evt.detail.value)} required />
          <HorizontalLayout theme="spacing">
            <ComboBox label="Tipo de Identificación" items={tiposIdentificacion} value={tipoIdentificacion} onValueChanged={(evt) => setTipoIdentificacion(evt.detail.value)} required style={{ flex: 1 }} />
            <TextField label="Identificación" placeholder="Número de identificación" value={identificacion} onValueChanged={(evt) => setIdentificacion(evt.detail.value)} required style={{ flex: 1 }} />
          </HorizontalLayout>
          <HorizontalLayout theme="spacing">
            <ComboBox label="Sexo" items={sexoOptions} value={sexo} onValueChanged={(evt) => setSexo(evt.detail.value)} required style={{ flex: 1 }} />
            <DatePicker label="Fecha de Nacimiento" value={fechaNacimiento} onValueChanged={(evt) => setFechaNacimiento(evt.detail.value)} style={{ flex: 1 }} />
          </HorizontalLayout>
          <TextField label="Teléfono" placeholder="Número de teléfono" value={telefono} onValueChanged={(evt) => setTelefono(evt.detail.value)} />
          <TextField label="Dirección" placeholder="Dirección completa" value={direccion} onValueChanged={(evt) => setDireccion(evt.detail.value)} />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">Registrar Persona</Button>
    </>
  );
}

// Puedes dejar tu PersonaEditForm igual que en tu código, solo cambia useSignal por useState si no usas Hilla signals.

export default function PersonaListView() {
  const [items, setItems] = useState<Persona[]>([]);
  const [allItems, setAllItems] = useState<Persona[]>([]);
  const [criterio, setCriterio] = useState('');
  const [texto, setTexto] = useState('');

  useEffect(() => { loadData(); }, []);

  const loadData = async () => {
    try {
      const data = await PersonaServices.listAll();
      const filteredData = (data ?? []).filter(Boolean);
      setItems(filteredData);
      setAllItems(filteredData);
    } catch (error) {
      handleError(error);
    }
  };

  const itemSelect = [
    { label: 'Nombres', value: 'nombres' },
    { label: 'Apellidos', value: 'apellidos' },
    { label: 'Email', value: 'email' },
    { label: 'Identificación', value: 'identificacion' },
    { label: 'Teléfono', value: 'telefono' },
  ];

  const search = () => {
    if (!criterio || !texto.trim()) {
      Notification.show('Seleccione un criterio e ingrese texto a buscar', {
        duration: 3000,
        position: 'top-center',
        theme: 'error'
      });
      return;
    }
    const searchText = texto.trim().toLowerCase();
    const filteredResults = allItems.filter(item => {
      let fieldValue = '';
      switch (criterio) {
        case 'nombres': fieldValue = item.nombres?.toLowerCase() || ''; break;
        case 'apellidos': fieldValue = item.apellidos?.toLowerCase() || ''; break;
        case 'email': fieldValue = item.email?.toLowerCase() || ''; break;
        case 'identificacion': fieldValue = item.identificacion?.toLowerCase() || ''; break;
        case 'telefono': fieldValue = item.telefono?.toLowerCase() || ''; break;
        default: return false;
      }
      return fieldValue.startsWith(searchText);
    });
    setItems(filteredResults);
    if (filteredResults.length === 0) {
      Notification.show(`No se encontraron personas que empiecen con "${texto}" en ${criterio}`, {
        duration: 5000,
        position: 'top-center',
        theme: 'contrast'
      });
    } else {
      Notification.show(`Se encontraron ${filteredResults.length} resultado(s)`, {
        duration: 3000,
        position: 'bottom-end',
        theme: 'success'
      });
    }
  };

  const onKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') search();
  };

  const showAll = () => {
    setItems(allItems);
    setCriterio('');
    setTexto('');
    Notification.show(`Mostrando todas las personas (${allItems.length})`, {
      duration: 2000,
      position: 'bottom-end',
      theme: 'success'
    });
  };

  function renderIndex({ model }: { model: any }) {
    return <span>{model.index + 1}</span>;
  }

  function renderNombreCompleto({ item }: { item: Persona }) {
    return (
      <div>
        <div style={{ fontWeight: 'bold' }}>{item.nombres} {item.apellidos}</div>
        <div style={{ fontSize: '0.8em', color: 'var(--lumo-secondary-text-color)' }}>
          {item.email}
        </div>
      </div>
    );
  }

  function renderIdentificacion({ item }: { item: Persona }) {
    return (
      <div>
        <div style={{ fontWeight: 'bold' }}>{item.identificacion}</div>
        <div style={{ fontSize: '0.8em', color: 'var(--lumo-secondary-text-color)' }}>
          {item.tipoIdentificacion}
        </div>
      </div>
    );
  }

  function renderSexo({ item }: { item: Persona }) {
    const color = item.sexo === 'FEMENINO' ? '#ff6b9d' : item.sexo === 'MASCULINO' ? '#4dabf7' : '#69db7c';
    return (
      <span
        style={{
          padding: '4px 8px',
          borderRadius: '12px',
          backgroundColor: color + '20',
          color: color,
          fontSize: '0.8em',
          fontWeight: 'bold'
        }}
      >
        {item.sexo}
      </span>
    );
  }

  function renderEdad({ item }: { item: Persona }) {
    if (!item.fechaNacimiento) return <span>-</span>;
    const today = new Date();
    const birthDate = new Date(item.fechaNacimiento);
    const age = today.getFullYear() - birthDate.getFullYear();
    return <span>{age} años</span>;
  }

  function renderEstadoCuenta({ item }: { item: Persona }) {
    return (
      <span
        style={{
          padding: '4px 8px',
          borderRadius: '12px',
          backgroundColor: item.tieneCuenta ? '#51cf66' : '#ffd43b',
          color: item.tieneCuenta ? '#2b8a3e' : '#fab005',
          fontSize: '0.8em',
          fontWeight: 'bold'
        }}
      >
        {item.tieneCuenta ? 'Con cuenta' : 'Sin cuenta'}
      </span>
    );
  }

  // Puedes agregar tu PersonaEditForm aquí igual que en tu código original

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m" style={{ background: '#f8f8fa' }}>
      <ViewToolbar title="Gestión de Personas">
        <Group>
          <PersonaEntryForm onPersonaCreated={loadData} />
        </Group>
      </ViewToolbar>
      <HorizontalLayout theme="spacing">
        <Select items={itemSelect} value={criterio} onValueChanged={(evt) => setCriterio(evt.detail.value)} placeholder="Seleccione un criterio" />
        <TextField placeholder="Buscar por palabra..." style={{ width: '50%' }} value={texto} onValueChanged={(evt) => setTexto(evt.detail.value)} onKeyDown={onKeyDown}>
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">BUSCAR</Button>
        <Button onClick={showAll} theme="secondary">MOSTRAR TODOS</Button>
      </HorizontalLayout>
      <Grid items={items} style={{ background: 'white', borderRadius: 12, boxShadow: '0 2px 8px #0001' }}>
        <GridColumn header="#" renderer={renderIndex} width="60px" />
        <GridSortColumn onDirectionChanged={(e) => {}} path="nombres" header="Persona" renderer={renderNombreCompleto} />
        <GridColumn header="Identificación" renderer={renderIdentificacion} width="150px" />
        <GridColumn header="Sexo" renderer={renderSexo} width="100px" />
        <GridColumn header="Edad" renderer={renderEdad} width="80px" />
        <GridColumn path="telefono" header="Teléfono" width="120px" />
        <GridColumn header="Estado" renderer={renderEstadoCuenta} width="110px" />
        {/* Agrega aquí tu columna de acciones si tienes PersonaEditForm */}
      </Grid>
      <div style={{ marginTop: '1rem', fontSize: '0.9rem', color: 'var(--lumo-secondary-text-color)' }}>
        Total de personas: {items.length}
        {items.length !== allItems.length && (
          <span style={{ marginLeft: '10px', fontStyle: 'italic' }}>
            (de {allItems.length} totales)
          </span>
        )}
      </div>
    </main>
  );
}