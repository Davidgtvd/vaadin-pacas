const API_BASE = '/api/compras';

export async function findAll() {
  const res = await fetch(API_BASE);
  if (!res.ok) throw new Error('Error al obtener compras');
  return res.json();
}

export async function crearCompra(subtotal: number, nroFactura: string, iva: number, total: number, personaId: string) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ subtotal, nroFactura, iva, total, persona: { id: personaId } }),
  });
  if (!res.ok) throw new Error('Error al crear compra');
  return res.json();
}

export async function actualizarCompra(id: string, subtotal: number, nroFactura: string, iva: number, total: number, personaId: string) {
  const res = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ id, subtotal, nroFactura, iva, total, persona: { id: personaId } }),
  });
  if (!res.ok) throw new Error('Error al actualizar compra');
  return res.json();
}

export async function buscarPorTexto(texto: string) {
  const res = await fetch(`${API_BASE}/buscar?factura=${encodeURIComponent(texto)}`);
  if (!res.ok) throw new Error('Error al buscar compras');
  return res.json();
}