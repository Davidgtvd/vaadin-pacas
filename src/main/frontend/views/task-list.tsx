import {
  Button,
  DatePicker,
  Grid,
  GridColumn,
  TextField,
  Notification,
  HorizontalLayout,
  VerticalLayout,
} from '@vaadin/react-components';
import React, { useState } from 'react';

type Task = {
  id: number;
  description: string;
  dueDate: string; // ISO string
};

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});

function TaskEntryForm({ onTaskCreated }: { onTaskCreated?: (task: Task) => void }) {
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState<string | undefined>(undefined);

  const createTask = () => {
    if (description.trim().length === 0 || !dueDate) {
      Notification.show('No se pudo crear, faltan datos', { duration: 3000, position: 'top-center', theme: 'error' });
      return;
    }
    const newTask: Task = {
      id: Date.now(),
      description: description.trim(),
      dueDate,
    };
    onTaskCreated?.(newTask);
    setDescription('');
    setDueDate(undefined);
    Notification.show('Task added', { duration: 3000, position: 'bottom-end', theme: 'success' });
  };

  return (
    <HorizontalLayout theme="spacing" style={{ alignItems: 'center', flexWrap: 'wrap' }}>
      <TextField
        placeholder="What do you want to do?"
        aria-label="Task description"
        maxlength={255}
        style={{ minWidth: '20em' }}
        value={description}
        onValueChanged={e => setDescription(e.detail.value)}
      />
      <DatePicker
        placeholder="Due date"
        aria-label="Due date"
        value={dueDate}
        onValueChanged={e => setDueDate(e.detail.value)}
      />
      <Button onClick={createTask} theme="primary">
        Create
      </Button>
    </HorizontalLayout>
  );
}

export default function TaskListView() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [filterText, setFilterText] = useState('');

  const addTask = (task: Task) => {
    setTasks(prev => [...prev, task]);
  };

  const filteredTasks = tasks.filter(t =>
    t.description.toLowerCase().includes(filterText.toLowerCase())
  );

  return (
    <VerticalLayout style={{ padding: '1rem', gap: '1rem' }}>
      <h2>Task List</h2>
      <TaskEntryForm onTaskCreated={addTask} />
      <TextField
        placeholder="Filtrar tareas..."
        value={filterText}
        onValueChanged={e => setFilterText(e.detail.value)}
        clearButtonVisible
        style={{ maxWidth: '300px' }}
      />
      <Grid items={filteredTasks} style={{ height: '400px' }}>
        <GridColumn
          header="#"
          width="50px"
          renderer={({ model }) => <span>{model.index + 1}</span>}
        />
        <GridColumn path="description" header="Descripción" />
        <GridColumn
          header="Fecha límite"
          path="dueDate"
          renderer={({ item }) => dateFormatter.format(new Date(item.dueDate))}
          width="150px"
        />
      </Grid>
    </VerticalLayout>
  );
}