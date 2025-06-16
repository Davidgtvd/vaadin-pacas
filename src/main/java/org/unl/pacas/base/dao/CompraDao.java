package org.unl.pacas.base.dao;

import org.unl.pacas.base.dao.dao_struct.AdapterDao;
import org.unl.pacas.base.models.Compra;

public class CompraDao extends AdapterDao <Compra> {
    private Compra obj;

    public CompraDao(){
        super(Compra.class);
    }

    public Compra getObj() {
        if (obj == null)
            this.obj = new Compra();
        return this.obj;
    }

    public void setObj(Compra obj) {
        this.obj = obj;
    }

    
    public Boolean save(){
        try{
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Boolean update(Integer pos){
        try {
        this.update(obj, pos);
        return true;
        }catch(Exception e){
            return false;
        }
    }

    public Compra deleteCompra(Integer id) throws Exception {
        Compra compra = get(id);
        remove(id);
        return compra;
    }  
}
