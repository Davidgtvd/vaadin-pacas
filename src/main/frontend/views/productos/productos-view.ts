import { LitElement, html, css } from 'lit';
import { css } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import '@vaadin/grid';
import '@vaadin/grid/vaadin-grid-column.js';
import '@vaadin/text-field';
import '@vaadin/number-field';
import '@vaadin/select';
import '@vaadin/dialog';
import '@vaadin/form-layout';
import '@vaadin/notification';
import '@vaadin/text-area';

@customElement('productos-view')
export class ProductosView extends LitElement {
  static styles = css`
    :host {
      display: block;
      padding: var(--lumo-space-m);
    }

    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: var(--lumo-space-l);
    }

    .header h2 {
      margin: 0;
      color: var(--lumo-header-text-color);
    }

    .stats-container {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: var(--lumo-space-m);
      margin-bottom: var(--lumo-space-l);
    }

    .stat-card {
      background: var(--lumo-base-color);
      border: 1px solid var(--lumo-contrast-10pct);
      border-radius: var(--lumo-border-radius-m);
      padding: var(--lumo-space-m);
      text-align: center;
    }

    .stat-value {
      font-size: var(--lumo-font-size-xxl);
      font-weight: bold;
      color: var(--lumo-primary-color);
    }

    .stat-label {
      color: var(--lumo-secondary-text-color);
      font-size: var(--lumo-font-size-s);
    }

    .filters {
      display: flex;
      gap: var(--lumo-space-m);
      margin-bottom: var(--lumo-space-m);
      flex-wrap: wrap;
    }

    .grid-container {
      height: 600px;
      border: 1px solid var(--lumo-contrast-20pct);
      border-radius: var(--lumo-border-radius-m);
    }

    .form-container {
      padding: var(--lumo-space-m);
      max-width: 600px;
    }

    .form-buttons {
      display: flex;
      gap: var(--lumo-space-s);
      justify-content: flex-end;
      margin-top: var(--lumo-space-l);
    }

    .stock-badge {
      padding: var(--lumo-space-xs) var(--lumo-space-s);
      border-radius: var(--lumo-border-radius-s);
      font-size: var(--lumo-font-size-s);
      font-weight: bold;
    }

    .stock-normal { background: #e8f5e8; color: #2e7d32; }
    .stock-bajo { background: #fff3e0; color: #f57c00; }
    .stock-agotado { background: #ffebee; color: #d32f2f; }

    .precio-container {
      display: flex;
      flex-direction: column;
      gap: var(--lumo-space-xs);
    }

    .precio-compra { color: var(--lumo-error-color); }
    .precio-venta { color: var(--lumo-success-color); }
    .margen { color: var(--lumo-secondary-text-color); font-size: var(--lumo-font-size-s); }
  `;

  @state()
  private productos: any[] = [];

  @state()
  private selectedProducto: any = null;

  @state()
  private dialogOpened = false;

  @state()
  private loading = false;

  @state()
  private filtroNombre = '';

  @state()
  private filtroCategoria = '';

  @state()
  private categorias: string[] = [];

  @state()
  private estadisticas: any = {};

  async connectedCallback() {
    super.connectedCallback();
    await this.loadData();
  }

  async loadData() {
    await Promise.all([
      this.loadProductos(),
      this.loadCategorias(),
      this.loadEstadisticas()
    ]);
  }

  async loadProductos() {
    try {
      this.loading = true;
      const response = await fetch('/api/productos');
      this.productos = await response.json();
    } catch (error) {
      console.error('Error loading productos:', error);
      this.showNotification('Error al cargar productos', 'error');
    } finally {
      this.loading = false;
    }
  }

  async loadCategorias() {
    try {
      const response = await fetch('/api/productos/categorias');
      this.categorias = await response.json();
    } catch (error) {
      console.error('Error loading categorias:', error);
    }
  }

  async loadEstadisticas() {
    try {
      const response = await fetch('/api/productos/estadisticas');
      this.estadisticas = await response.json();
    } catch (error) {
      console.error('Error loading estadisticas:', error);
    }
  }

  openDialog(producto?: any) {
    this.selectedProducto = producto || {
      nombre: '',
      descripcion: '',
      categoria: '',
      precioCompra: 0,
      precioVenta: 0,
      stock: 0,
      stockMinimo: 0,
      activo: true
    };
    this.dialogOpened = true;
  }

  closeDialog() {
    this.dialogOpened = false;
    this.selectedProducto = null;
  }

  async saveProducto() {
    if (!this.selectedProducto) return;

    try {
      this.loading = true;
      const method = this.selectedProducto.id ? 'PUT' : 'POST';
      const url = this.selectedProducto.id 
        ? `/api/productos/${this.selectedProducto.id}` 
        : '/api/productos';

      const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(this.selectedProducto)
      });

      if (response.ok) {
        this.showNotification(
          this.selectedProducto.id ? 'Producto actualizado' : 'Producto creado', 
          'success'
        );
        await this.loadData();
        this.closeDialog();
      } else {
        throw new Error('Error al guardar producto');
      }
    } catch (error) {
      console.error('Error saving producto:', error);
      this.showNotification('Error al guardar producto', 'error');
    } finally {
      this.loading = false;
    }
  }

  async deleteProducto(producto: any) {
    if (!confirm(`¿Eliminar producto "${producto.nombre}"?`)) return;

    try {
      this.loading = true;
      const response = await fetch(`/api/productos/${producto.id}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        this.showNotification('Producto eliminado', 'success');
        await this.loadData();
      } else {
        throw new Error('Error al eliminar producto');
      }
    } catch (error) {
      console.error('Error deleting producto:', error);
      this.showNotification('Error al eliminar producto', 'error');
    } finally {
      this.loading = false;
    }
  }

  showNotification(message: string, theme: 'success' | 'error') {
    const notification = document.createElement('vaadin-notification');
    notification.setAttribute('theme', theme);
    notification.setAttribute('duration', '3000');
    notification.setAttribute('position', 'top-center');
    notification.textContent = message;
    document.body.appendChild(notification);
    notification.open();
  }

  updateField(field: string, value: any) {
    if (this.selectedProducto) {
      this.selectedProducto = { ...this.selectedProducto, [field]: value };
    }
  }

  getStockBadge(producto: any) {
    if (producto.stock === 0) {
      return html`<span class="stock-badge stock-agotado">Agotado</span>`;
    } else if (producto.stock <= producto.stockMinimo) {
      return html`<span class="stock-badge stock-bajo">Stock Bajo</span>`;
    } else {
      return html`<span class="stock-badge stock-normal">Normal</span>`;
    }
  }

  getPreciosDisplay(producto: any) {
    const margen = producto.precioCompra > 0 
      ? (((producto.precioVenta - producto.precioCompra) / producto.precioCompra) * 100).toFixed(1)
      : 0;

    return html`
      <div class="precio-container">
        <div class="precio-compra">Compra: $${producto.precioCompra?.toFixed(2)}</div>
        <div class="precio-venta">Venta: $${producto.precioVenta?.toFixed(2)}</div>
        <div class="margen">Margen: ${margen}%</div>
      </div>
    `;
  }

  get productosFiltrados() {
    return this.productos.filter(p => {
      const matchNombre = !this.filtroNombre || 
        p.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase());
      const matchCategoria = !this.filtroCategoria || p.categoria === this.filtroCategoria;
      return matchNombre && matchCategoria;
    });
  }

  render() {
    return html`
      <div class="header">
        <h2>Gestión de Productos</h2>
        <vaadin-button theme="primary" @click="${() => this.openDialog()}">
          Nuevo Producto
        </vaadin-button>
      </div>

      <div class="stats-container">
        <div class="stat-card">
          <div class="stat-value">${this.estadisticas.totalProductos || 0}</div>
          <div class="stat-label">Total Productos</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">$${(this.estadisticas.valorInventarioCompra || 0).toFixed(2)}</div>
          <div class="stat-label">Valor Inventario (Compra)</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">$${(this.estadisticas.valorInventarioVenta || 0).toFixed(2)}</div>
          <div class="stat-label">Valor Inventario (Venta)</div>
        </div>
        <div class="stat-card">
          <div class="stat-value">${this.estadisticas.productosStockBajo || 0}</div>
          <div class="stat-label">Stock Bajo</div>
        </div>
      </div>

      <div class="filters">
        <vaadin-text-field
          placeholder="Buscar por nombre..."
          .value="${this.filtroNombre}"
          @input="${(e: Event) => this.filtroNombre = (e.target as HTMLInputElement).value}"
          clear-button-visible
        ></vaadin-text-field>
        
        <vaadin-select
          placeholder="Filtrar por categoría"
          .value="${this.filtroCategoria}"
          @value-changed="${(e: CustomEvent) => this.filtroCategoria = e.detail.value}"
          .items="${[
            { label: 'Todas las categorías', value: '' },
            ...this.categorias.map(cat => ({ label: cat, value: cat }))
          ]}"
        ></vaadin-select>
      </div>

      <div class="grid-container">
        <vaadin-grid .items="${this.productosFiltrados}" theme="row-stripes">
          <vaadin-grid-column path="nombre" header="Nombre" width="200px"></vaadin-grid-column>
          <vaadin-grid-column path="categoria" header="Categoría" width="150px"></vaadin-grid-column>
          <vaadin-grid-column 
            header="Precios"
            width="180px"
            .renderer="${(root: HTMLElement, column: any, model: any) => {
              root.innerHTML = '';
              root.appendChild(this.getPreciosDisplay(model.item));
            }}"
          ></vaadin-grid-column>
          <vaadin-grid-column 
            path="stock" 
            header="Stock"
            width="100px"
            .renderer="${(root: HTMLElement, column: any, model: any) => {
              root.textContent = model.item.stock.toString();
            }}"
          ></vaadin-grid-column>
          <vaadin-grid-column 
            header="Estado Stock"
            width="120px"
            .renderer="${(root: HTMLElement, column: any, model: any) => {
              root.innerHTML = '';
              root.appendChild(this.getStockBadge(model.item));
            }}"
          ></vaadin-grid-column>
          <vaadin-grid-column 
            header="Acciones"
            width="200px"
            .renderer="${(root: HTMLElement, column: any, model: any) => {
              root.innerHTML = `
                <vaadin-button theme="tertiary small" class="edit-btn">Editar</vaadin-button>
                <vaadin-button theme="tertiary error small" class="delete-btn">Eliminar</vaadin-button>
              `;
              
              const editBtn = root.querySelector('.edit-btn') as HTMLElement;
              const deleteBtn = root.querySelector('.delete-btn') as HTMLElement;
              
              editBtn.onclick = () => this.openDialog(model.item);
              deleteBtn.onclick = () => this.deleteProducto(model.item);
            }}"
          ></vaadin-grid-column>
        </vaadin-grid>
      </div>

      <vaadin-dialog 
        .opened="${this.dialogOpened}" 
        @opened-changed="${(e: CustomEvent) => this.dialogOpened = e.detail.value}"
        header-title="${this.selectedProducto?.id ? 'Editar Producto' : 'Nuevo Producto'}"
      >
        <div class="form-container">
          <vaadin-form-layout>
            <vaadin-text-field
              label="Nombre *"
              .value="${this.selectedProducto?.nombre || ''}"
              @input="${(e: Event) => this.updateField('nombre', (e.target as HTMLInputElement).value)}"
              required
              colspan="2"
            ></vaadin-text-field>
            
            <vaadin-select
              label="Categoría"
              .value="${this.selectedProducto?.categoria || ''}"
              @value-changed="${(e: CustomEvent) => this.updateField('categoria', e.detail.value)}"
              .items="${this.categorias.map(cat => ({ label: cat, value: cat }))}"
              colspan="2"
            ></vaadin-select>
            
            <vaadin-number-field
              label="Precio de Compra *"
              .value="${this.selectedProducto?.precioCompra || 0}"
              @input="${(e: Event) => this.updateField('precioCompra', parseFloat((e.target as HTMLInputElement).value) || 0)}"
              step="0.01"
              min="0"
              required
            ></vaadin-number-field>
            
            <vaadin-number-field
              label="Precio de Venta *"
              .value="${this.selectedProducto?.precioVenta || 0}"
              @input="${(e: Event) => this.updateField('precioVenta', parseFloat((e.target as HTMLInputElement).value) || 0)}"
              step="0.01"
              min="0"
              required
            ></vaadin-number-field>
            
            <vaadin-number-field
              label="Stock Inicial"
              .value="${this.selectedProducto?.stock || 0}"
              @input="${(e: Event) => this.updateField('stock', parseInt((e.target as HTMLInputElement).value) || 0)}"
              min="0"
            ></vaadin-number-field>
            
            <vaadin-number-field
              label="Stock Mínimo"
              .value="${this.selectedProducto?.stockMinimo || 0}"
              @input="${(e: Event) => this.updateField('stockMinimo', parseInt((e.target as HTMLInputElement).value) || 0)}"
              min="0"
            ></vaadin-number-field>
            
            <vaadin-text-area
              label="Descripción"
              .value="${this.selectedProducto?.descripcion || ''}"
              @input="${(e: Event) => this.updateField('descripcion', (e.target as HTMLInputElement).value)}"
              colspan="2"
            ></vaadin-text-area>
          </vaadin-form-layout>
          
          <div class="form-buttons">
            <vaadin-button @click="${this.closeDialog}">Cancelar</vaadin-button>
            <vaadin-button theme="primary" @click="${this.saveProducto}" .disabled="${this.loading}">
              ${this.selectedProducto?.id ? 'Actualizar' : 'Crear'}
            </vaadin-button>
          </div>
        </div>
      </vaadin-dialog>
    `;
  }
}