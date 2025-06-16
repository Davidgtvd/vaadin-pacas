import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type Cuenta_1 from "./org/unl/pacas/base/models/Cuenta.js";
async function buscarPorTexto_1(texto: string | undefined, init?: EndpointRequestInit_1): Promise<Array<Cuenta_1 | undefined> | undefined> { return client_1.call("CuentaServices", "buscarPorTexto", { texto }, init); }
async function create_1(usuario: string | undefined, contrasena: string | undefined, rolId: number | undefined, personaId: number | undefined, init?: EndpointRequestInit_1): Promise<Cuenta_1 | undefined> { return client_1.call("CuentaServices", "create", { usuario, contrasena, rolId, personaId }, init); }
async function delete_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CuentaServices", "delete", { id }, init); }
async function findById_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<Cuenta_1 | undefined> { return client_1.call("CuentaServices", "findById", { id }, init); }
async function listAll_1(init?: EndpointRequestInit_1): Promise<Array<Cuenta_1 | undefined> | undefined> { return client_1.call("CuentaServices", "listAll", {}, init); }
async function update_1(id: number | undefined, usuario: string | undefined, rolId: number | undefined, activo: boolean | undefined, init?: EndpointRequestInit_1): Promise<Cuenta_1 | undefined> { return client_1.call("CuentaServices", "update", { id, usuario, rolId, activo }, init); }
export { buscarPorTexto_1 as buscarPorTexto, create_1 as create, delete_1 as delete, findById_1 as findById, listAll_1 as listAll, update_1 as update };
