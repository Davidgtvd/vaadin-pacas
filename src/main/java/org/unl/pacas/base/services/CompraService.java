package org.unl.pacas.base.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unl.pacas.base.dao.CompraDao;
import org.unl.pacas.base.models.Compra;
import org.unl.pacas.base.models.Persona;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompraService {

    @Autowired
    private CompraDao compraDao;

    @Autowired
    private PersonaService personaService;

    // CRUD básicos

    public List<Compra> findAll() {
        return linkedListToList(compraDao.findAll());
    }

    public Optional<Compra> findById(Long id) {
        return compraDao.findById(id);
    }

    public boolean existsById(Long id) {
        return compraDao.existsById(id);
    }

    public Compra save(Compra compra) {
        validarCompra(compra, null);
        return compraDao.save(compra);
    }

    public Compra update(Compra compra) {
        if (compra.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una compra sin ID");
        }
        validarCompra(compra, compra.getId());
        return compraDao.save(compra);
    }

    public void deleteById(Long id) {
        compraDao.deleteById(id);
    }

    public void delete(Compra compra) {
        deleteById(compra.getId());
    }

    // Validaciones

    public void validarCompra(Compra compra, Long idExcluir) {
        if (compra == null) {
            throw new IllegalArgumentException("La compra no puede ser nula");
        }
        if (compra.getSubtotal() <= 0) {
            throw new IllegalArgumentException("El subtotal debe ser mayor a 0");
        }
        if (compra.getIva() < 0) {
            throw new IllegalArgumentException("El IVA no puede ser negativo");
        }
        if (compra.getTotal() <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a 0");
        }
        if (compra.getNroFactura() == null || compra.getNroFactura().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio");
        }
        if (compra.getPersona() == null || compra.getPersona().getId() == null ||
                !personaService.existsById(compra.getPersona().getId())) {
            throw new IllegalArgumentException("Debe seleccionar una persona válida");
        }
    }

    // Búsquedas avanzadas (sin búsqueda por nroFactura)

    public List<Compra> buscarPorPersona(Long personaId) {
        List<Compra> todas = findAll();
        List<Compra> filtradas = new ArrayList<>();
        for (Compra c : todas) {
            if (c.getPersona() != null && c.getPersona().getId() != null && c.getPersona().getId().equals(personaId)) {
                filtradas.add(c);
            }
        }
        return filtradas;
    }

    public List<Compra> buscarPorTexto(String texto) {
        List<Compra> todas = findAll();
        List<Compra> filtradas = new ArrayList<>();
        if (texto == null || texto.trim().isEmpty()) return filtradas;
        String lower = texto.toLowerCase();
        for (Compra c : todas) {
            if (c.getPersona() != null && c.getPersona().getNombreCompleto().toLowerCase().contains(lower)) {
                filtradas.add(c);
            }
        }
        return filtradas;
    }

    public List<Compra> findAllOrdenados(String campo, boolean ascendente) {
        List<Compra> lista = findAll();
        lista.sort((c1, c2) -> {
            int cmp = 0;
            switch (campo.toLowerCase()) {
                case "nrofactura":
                    cmp = c1.getNroFactura().compareToIgnoreCase(c2.getNroFactura());
                    break;
                case "subtotal":
                    cmp = Float.compare(c1.getSubtotal(), c2.getSubtotal());
                    break;
                case "total":
                    cmp = Float.compare(c1.getTotal(), c2.getTotal());
                    break;
                case "persona":
                    cmp = c1.getPersona().getNombreCompleto().compareToIgnoreCase(c2.getPersona().getNombreCompleto());
                    break;
                default:
                    cmp = 0;
            }
            return ascendente ? cmp : -cmp;
        });
        return lista;
    }

    // Estadísticas

    public long contarTotal() {
        return compraDao.contarTotalCompras();
    }

    // Métodos utilitarios para las vistas

    public Compra crearCompra(float subtotal, String nroFactura, float iva, float total, Long personaId) {
        Optional<Persona> personaOpt = personaService.findById(personaId);
        if (personaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la persona con ID: " + personaId);
        }
        Compra compra = new Compra(subtotal, nroFactura, iva, total, personaOpt.get());
        return save(compra);
    }

    public Compra actualizarCompra(Long id, float subtotal, String nroFactura, float iva, float total, Long personaId) {
        Optional<Compra> compraExistente = findById(id);
        if (compraExistente.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la compra con ID: " + id);
        }
        Optional<Persona> personaOpt = personaService.findById(personaId);
        if (personaOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la persona con ID: " + personaId);
        }
        Compra compra = compraExistente.get();
        compra.setSubtotal(subtotal);
        compra.setNroFactura(nroFactura);
        compra.setIva(iva);
        compra.setTotal(total);
        compra.setPersona(personaOpt.get());
        return update(compra);
    }

    public void eliminarCompra(Long id) {
        Optional<Compra> compra = findById(id);
        if (compra.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la compra con ID: " + id);
        }
        deleteById(id);
    }

    // Método auxiliar para convertir LinkedList a List
    private <T> List<T> linkedListToList(LinkedList<T> linkedList) {
        List<T> list = new ArrayList<>();
        if (linkedList != null) {
            linkedList.forEach(list::add);
        }
        return list;
    }
}