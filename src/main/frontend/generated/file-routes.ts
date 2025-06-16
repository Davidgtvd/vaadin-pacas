import { createRoute as createRoute_1 } from "@vaadin/hilla-file-router/runtime.js";
import type { AgnosticRoute as AgnosticRoute_1 } from "@vaadin/hilla-file-router/types.js";
import * as Page_1 from "../views/cuenta-list.js";
import * as Page_2 from "../views/persona-list.js";
import * as Page_3 from "../views/producto-list.js";
import * as Page_4 from "../views/rol-list.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("cuenta-list", Page_1),
    createRoute_1("persona-list", Page_2),
    createRoute_1("producto-list", Page_3),
    createRoute_1("rol-list", Page_4)
];
export default routes;
