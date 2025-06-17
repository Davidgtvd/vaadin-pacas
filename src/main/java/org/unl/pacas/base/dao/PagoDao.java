package org.unl.pacas.base.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.unl.pacas.base.models.MetodoPago;
import org.unl.pacas.base.models.Pago;

import java.util.List;

@Repository
public class PagoDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Pago save(Pago pago) {
        if (pago.getId() == null) {
            entityManager.persist(pago);
            return pago;
        } else {
            return entityManager.merge(pago);
        }
    }

    public Pago findById(Long id) {
        return entityManager.find(Pago.class, id);
    }

    public List<Pago> findAll() {
        TypedQuery<Pago> query = entityManager.createQuery("SELECT p FROM Pago p", Pago.class);
        return query.getResultList();
    }

    public void deleteById(Long id) {
        Pago pago = findById(id);
        if (pago != null) {
            entityManager.remove(pago);
        }
    }

    public List<Pago> findByMetodoPago(MetodoPago metodoPago) {
        TypedQuery<Pago> query = entityManager.createQuery(
                "SELECT p FROM Pago p WHERE p.metodoPago = :metodoPago", Pago.class);
        query.setParameter("metodoPago", metodoPago);
        return query.getResultList();
    }

    public boolean existsById(Long id) {
        String jpql = "SELECT COUNT(p) FROM Pago p WHERE p.id = :id";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count != null && count > 0;
    }
}