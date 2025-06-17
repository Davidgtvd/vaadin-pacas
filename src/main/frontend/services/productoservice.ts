const API_BASE = '/api/productos';

export async function listAll() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Error al cargar productos');
  return res.json();
}

export async function buscarPorNombre(nombre: string) {
  const res = await fetch(`${API_BASE}/buscar?nombre=${encodeURIComponent(nombre)}`);
  if (!res.ok) throw new Error('Error al buscar productos');
  return res.json();
}

export async function findByCategoria(categoria: string) {
  const res = await fetch(`${API_BASE}/categoria/${encodeURIComponent(categoria)}`);
  if (!res.ok) throw new Error('Error al filtrar por categoría');
  return res.json();
}

export async function create(producto: any) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto),
  });
  if (!res.ok) throw new Error('Error al crear producto');
}

export async function update(id: number, producto: any) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(producto),
  });
  if (!res.ok) throw new Error('Error al actualizar producto');
}