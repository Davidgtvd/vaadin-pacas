import { LitElement, html, css } from 'lit';
import { customElement } from 'lit/decorators.js';

@customElement('dashboard-view')
export class DashboardView extends LitElement {
  static styles = css`
    :host {
      display: block;
      padding: 25px;
    }
  `;

  render() {
    return html`
      <h1>Dashboard</h1>
      <p>Bienvenido al dashboard de tu aplicación de pacas de ropa.</p>
    `;
  }
}
