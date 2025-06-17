const API_BASE = '/api/hilla/personas';

export async function listAll() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Error al cargar personas');
  return res.json();
}

export async function findById(id: number) {
  const res = await fetch(`${API_BASE}/${id}`);
  if (!res.ok) throw new Error(`Error al buscar persona con ID ${id}`);
  return res.json();
}

export async function create(persona: any) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(persona),
  });
  if (!res.ok) throw new Error('Error al crear persona');
  return res.json();
}

export async function update(id: number, persona: any) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(persona),
  });
  if (!res.ok) throw new Error('Error al actualizar persona');
  return res.json();
}

export async function deleteById(id: number) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Error al eliminar persona');
}