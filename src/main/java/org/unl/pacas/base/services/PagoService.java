package org.unl.pacas.base.services;
import org.unl.pacas.base.models.Pago;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class PagoService {
    private Pago[] pagos = new Pago[100]; // Capacidad inicial
    private int size = 0;
    private long nextId = 1;
    
    public Pago[] findAll() {
        Pago[] result = new Pago[size];
        System.arraycopy(pagos, 0, result, 0, size);
        return result;
    }
    
    public Pago save(Pago pago) {
        if (pago.getId() == null) {
            // Nuevo pago
            pago.setId(nextId++);
            pago.setFechaPago(LocalDateTime.now());
            
            if (size >= pagos.length) {
                resizeArray();
            }
            pagos[size++] = pago;
        } else {
            // Actualizar pago existente
            for (int i = 0; i < size; i++) {
                if (pagos[i].getId().equals(pago.getId())) {
                    pagos[i] = pago;
                    break;
                }
            }
        }
        return pago;
    }
    
    public void delete(Pago pago) {
        for (int i = 0; i < size; i++) {
            if (pagos[i].getId().equals(pago.getId())) {
                // Desplazar elementos
                System.arraycopy(pagos, i + 1, pagos, i, size - i - 1);
                pagos[--size] = null;
                break;
            }
        }
    }
    
    public Pago findById(Long id) {
        for (int i = 0; i < size; i++) {
            if (pagos[i].getId().equals(id)) {
                return pagos[i];
            }
        }
        return null;
    }
    
    private void resizeArray() {
        Pago[] newArray = new Pago[pagos.length * 2];
        System.arraycopy(pagos, 0, newArray, 0, size);
        pagos = newArray;
    }
}