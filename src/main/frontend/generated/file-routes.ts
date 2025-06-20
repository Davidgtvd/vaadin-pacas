import { createRoute as createRoute_1 } from "@vaadin/hilla-file-router/runtime.js";
import type { AgnosticRoute as AgnosticRoute_1, RouteModule as RouteModule_1 } from "@vaadin/hilla-file-router/types.js";
import { lazy as lazy_1 } from "react";
import * as Page_1 from "../views/@index.js";
import * as Layout_1 from "../views/@layout.js";
const routes: readonly AgnosticRoute_1[] = [
    createRoute_1("", Layout_1.default, (Layout_1 as RouteModule_1).config, [
        createRoute_1("", Page_1.default, (Page_1 as RouteModule_1).config),
        createRoute_1("compra-list", lazy_1(() => import("../views/compra-list.js")), { "route": "pacas", "title": "Compra", "menu": { "icon": "vaadin:package", "order": 1, "title": "Compra" }, "flowLayout": false }),
        createRoute_1("task-list", lazy_1(() => import("../views/task-list.js")), { "route": "task-list", "title": "Task List", "menu": { "icon": "vaadin:clipboard-check", "order": 1, "title": "Task List" }, "flowLayout": false })
    ])
];
export default routes;
