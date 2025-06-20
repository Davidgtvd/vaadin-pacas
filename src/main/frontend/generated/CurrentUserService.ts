import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type UserInfo_1 from "./org/unl/pacas/security/hilla/CurrentUserService/UserInfo.js";
async function getUserInfo_1(init?: EndpointRequestInit_1): Promise<UserInfo_1> { return client_1.call("CurrentUserService", "getUserInfo", {}, init); }
export { getUserInfo_1 as getUserInfo };
