package org.unl.pacas.base.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unl.pacas.base.dao.CompraDao;
import org.unl.pacas.base.models.Compra;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class CompraService {
    private CompraDao dao;

    public CompraService() {
        dao = new CompraDao();
    }

    public void createCompra(float subtotal, String nro_factura, float iva, float total, int id_persona) throws Exception {
        if (subtotal > 0 && iva >= 0 && total > 0 && nro_factura.trim().length() > 0 && id_persona > 0) {
            dao.getObj().setSubtotal(subtotal);
            dao.getObj().setNro_factura(nro_factura);
            dao.getObj().setIva(iva);
            dao.getObj().setTotal(total);
            dao.getObj().setId_persona(id_persona);
            if (!dao.save())
                throw new Exception("No se pudo guardar la compra");
        }
    }

    public void updateCompra(int id, float subtotal, String nro_factura, float iva, float total, int id_persona) throws Exception {
        if (subtotal > 0 && iva >= 0 && total > 0 && nro_factura.trim().length() > 0 && id_persona > 0) {
            dao.setObj(dao.listAll().get(id - 1));
            dao.getObj().setSubtotal(subtotal);
            dao.getObj().setNro_factura(nro_factura);
            dao.getObj().setIva(iva);
            dao.getObj().setTotal(total);
            dao.getObj().setId_persona(id_persona);
            if (!dao.update(id - 1))
                throw new Exception("No se pudo actualizar la compra");
        }
    }

    public List<HashMap> listCompra() {
        List<HashMap> list = new ArrayList<>();
        if (!dao.listAll().isEmpty()) {
            Compra[] arreglo = dao.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                HashMap<String, String> aux = new HashMap<>();
                aux.put("id", String.valueOf(arreglo[i].getId()));
                aux.put("subtotal", String.valueOf(arreglo[i].getSubtotal()));
                aux.put("nro_factura", arreglo[i].getNro_factura());
                aux.put("iva", String.valueOf(arreglo[i].getIva()));
                aux.put("total", String.valueOf(arreglo[i].getTotal()));
                aux.put("id_persona", String.valueOf(arreglo[i].getId_persona()));
                list.add(aux);
            }
        }
        return list;
    }
}
