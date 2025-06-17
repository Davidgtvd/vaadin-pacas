const API_BASE = '/api/pagos';

export async function listAll() {
  const response = await fetch(API_BASE);
  if (!response.ok) throw new Error('Error al obtener pagos');
  return response.json();
}

export async function create(pago: any) {
  const response = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(pago),
  });
  if (!response.ok) throw new Error('Error al crear pago');
  return response.json();
}

export async function update(id: string, pago: any) {
  const response = await fetch(`${API_BASE}/${id}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(pago),
  });
  if (!response.ok) throw new Error('Error al actualizar pago');
  return response.json();
}

export async function buscarPorFactura(factura: string) {
  const response = await fetch(`${API_BASE}?factura=${encodeURIComponent(factura)}`);
  if (!response.ok) throw new Error('Error al buscar pagos por factura');
  return response.json();
}