const API_BASE = '/api/hilla/facturas';

export async function listAll() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Error al cargar facturas');
  return res.json();
}

export async function findById(id: number) {
  const res = await fetch(`${API_BASE}/${id}`);
  if (!res.ok) throw new Error(`Error al buscar factura con ID ${id}`);
  return res.json();
}

export async function create(factura: any) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(factura),
  });
  if (!res.ok) throw new Error('Error al crear factura');
  return res.json();
}

export async function update(id: number, factura: any) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(factura),
  });
  if (!res.ok) throw new Error('Error al actualizar factura');
  return res.json();
}

export async function deleteById(id: number) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'DELETE',
  });
  if (!res.ok) throw new Error('Error al eliminar factura');
}

export async function buscarPorProducto(productoId: number) {
  const res = await fetch(`${API_BASE}/producto/${productoId}`);
  if (!res.ok) throw new Error('Error al buscar facturas por producto');
  return res.json();
}

export async function buscarPorCompra(compraId: number) {
  const res = await fetch(`${API_BASE}/compra/${compraId}`);
  if (!res.ok) throw new Error('Error al buscar facturas por compra');
  return res.json();
}

export async function findAllOrdenados(campo: string, ascendente: boolean) {
  const res = await fetch(`${API_BASE}/ordenados?campo=${encodeURIComponent(campo)}&ascendente=${ascendente}`);
  if (!res.ok) throw new Error('Error al obtener facturas ordenadas');
  return res.json();
}

export async function contarTotal() {
  const res = await fetch(`${API_BASE}/contar`);
  if (!res.ok) throw new Error('Error al contar facturas');
  return res.json();
}