import { createRoute as createRoute_1 } from "@vaadin/hilla-file-router/runtime.js";
import type { AgnosticRoute as AgnosticRoute_1 } from "@vaadin/hilla-file-router/types.js";
<<<<<<< HEAD
import * as Page_1 from "../views/cuenta-list.js";
import * as Page_2 from "../views/persona-list.js";
import * as Page_3 from "../views/producto-list.js";
import * as Page_4 from "../views/rol-list.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("cuenta-list", Page_1),
    createRoute_1("persona-list", Page_2),
    createRoute_1("producto-list", Page_3),
    createRoute_1("rol-list", Page_4)
=======
import * as Page_1 from "../views/@index.js";
import * as Layout_1 from "../views/@layout.js";
import * as Page_2 from "../views/App.js";
import * as Page_3 from "../views/cuenta-list.js";
import * as Page_4 from "../views/Login.js";
import * as Page_5 from "../views/PaginaPrincipal.js";
import * as Page_6 from "../views/persona-list.js";
import * as Page_7 from "../views/registro-list.js";
import * as Page_8 from "../views/Registro.js";
import * as Page_9 from "../views/rol-list.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("", Layout_1, [
        createRoute_1("", Page_1),
        createRoute_1("App", Page_2),
        createRoute_1("cuenta-list", Page_3),
        createRoute_1("Login", Page_4),
        createRoute_1("PaginaPrincipal", Page_5),
        createRoute_1("persona-list", Page_6),
        createRoute_1("Registro", Page_8),
        createRoute_1("registro-list", Page_7),
        createRoute_1("rol-list", Page_9)
    ])
>>>>>>> 6884a960 (Cambios locales de David antes de sincronizar)
];
export default routes;
