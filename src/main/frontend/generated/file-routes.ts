import { createRoute as createRoute_1 } from "@vaadin/hilla-file-router/runtime.js";
import type { AgnosticRoute as AgnosticRoute_1 } from "@vaadin/hilla-file-router/types.js";
import * as Page_1 from "../views/@index.js";
import * as Layout_1 from "../views/@layout.js";
import * as Page_2 from "../views/App.js";
import * as Page_3 from "../views/compra-list.js";
import * as Page_4 from "../views/cuenta-list.js";
import * as Page_5 from "../views/Factura-list.js";
import * as Page_6 from "../views/Login.js";
import * as Page_7 from "../views/PaginaPrincipal.js";
import * as Page_8 from "../views/pagosviews.js";
import * as Page_9 from "../views/persona-list.js";
import * as Page_10 from "../views/porducto-list.js";
import * as Page_11 from "../views/registro-list.js";
import * as Page_12 from "../views/Registro.js";
import * as Page_13 from "../views/rol-list.js";
import * as Page_14 from "../views/task-list.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("", Layout_1, [
        createRoute_1("", Page_1),
        createRoute_1("App", Page_2),
        createRoute_1("compra-list", Page_3),
        createRoute_1("cuenta-list", Page_4),
        createRoute_1("Factura-list", Page_5),
        createRoute_1("Login", Page_6),
        createRoute_1("PaginaPrincipal", Page_7),
        createRoute_1("pagosviews", Page_8),
        createRoute_1("persona-list", Page_9),
        createRoute_1("porducto-list", Page_10),
        createRoute_1("Registro", Page_12),
        createRoute_1("registro-list", Page_11),
        createRoute_1("rol-list", Page_13),
        createRoute_1("task-list", Page_14)
    ])
];
export default routes;
