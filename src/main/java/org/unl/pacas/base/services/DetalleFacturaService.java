package org.unl.pacas.base.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unl.pacas.base.dao.DetalleFacturaDao;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.models.DetalleFactura;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class DetalleFacturaService {
    private DetalleFacturaDao dao;

    public DetalleFacturaService() {
        dao = new DetalleFacturaDao();
    }

    public void createDetalleFactura(float total, int cantidad, float precioUnitario, int id_producto, int id_compra) throws Exception {
        if (cantidad > 0 && precioUnitario > 0 && total > 0 && id_producto > 0 && id_compra > 0) {
            dao.getObj().setTotal(total);
            dao.getObj().setCantidad(cantidad);
            dao.getObj().setPrecioUnitario(precioUnitario);
            dao.getObj().setId_producto(id_producto);
            dao.getObj().setId_compra(id_compra);
            if (!dao.save())
                throw new Exception("No se pudo guardar el detalle de la factura");
        }
    }

    public void updateDetalleFactura(int id, float total, int cantidad, float precioUnitario, int id_producto, int id_compra) throws Exception {
        if (cantidad > 0 && precioUnitario > 0 && total > 0 && id_producto > 0 && id_compra > 0) {
            dao.setObj(dao.listAll().get(id - 1));
            dao.getObj().setTotal(total);
            dao.getObj().setCantidad(cantidad);
            dao.getObj().setPrecioUnitario(precioUnitario);
            dao.getObj().setId_producto(id_producto);
            dao.getObj().setId_compra(id_compra);
            if (!dao.update(id - 1))
                throw new Exception("No se pudo actualizar el detalle de la factura");
        }
    }

    public void deleteDetalleFactura(int id) throws Exception {
        // Elimina la compra por su id real, no por índice
        if (id > 0) {
            DetalleFactura eliminado = dao.deleteDetalleFactura(id);
            if (eliminado == null) {
                throw new Exception("No se pudo eliminar la compra");
            }
        } else {
            throw new Exception("ID de compra inválido");
        }
    }


    public List<HashMap> listDetalleFactura() {
        List<HashMap> list = new ArrayList<>();
        if (!dao.listAll().isEmpty()) {
            DetalleFactura[] arreglo = dao.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", String.valueOf(arreglo[i].getId()));
                aux.put("cantidad", String.valueOf(arreglo[i].getCantidad()));
                aux.put("precioUnitario", String.valueOf(arreglo[i].getPrecioUnitario()));
                aux.put("total", String.valueOf(arreglo[i].getTotal()));
                aux.put("id_producto", String.valueOf(arreglo[i].getId_producto()));
                aux.put("id_compra", String.valueOf(arreglo[i].getId_compra()));
                list.add(aux);
            }
        }
        return list;
    }
}
