import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type Rol_1 from "./org/unl/pacas/base/models/Rol.js";
async function create_1(nombre: string | undefined, descripcion: string | undefined, init?: EndpointRequestInit_1): Promise<Rol_1 | undefined> { return client_1.call("RolServices", "create", { nombre, descripcion }, init); }
async function delete_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("RolServices", "delete", { id }, init); }
async function listAll_1(init?: EndpointRequestInit_1): Promise<Array<Rol_1 | undefined> | undefined> { return client_1.call("RolServices", "listAll", {}, init); }
async function update_1(id: number | undefined, nombre: string | undefined, descripcion: string | undefined, init?: EndpointRequestInit_1): Promise<Rol_1 | undefined> { return client_1.call("RolServices", "update", { id, nombre, descripcion }, init); }
export { create_1 as create, delete_1 as delete, listAll_1 as listAll, update_1 as update };
