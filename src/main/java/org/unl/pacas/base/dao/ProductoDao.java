package org.unl.pacas.base.dao;

import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.CategoriaProducto;
import org.unl.pacas.base.models.EstadoProducto;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class ProductoDao {
    
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public ProductoDao() {
        try {
            this.entityManagerFactory = Persistence.createEntityManagerFactory("pacas-persistence-unit");
            this.entityManager = entityManagerFactory.createEntityManager();
        } catch (Exception e) {
            System.err.println("Error al inicializar ProductoDao: " + e.getMessage());
        }
    }

    // Método para cerrar recursos
    public void close() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    // CREATE - Guardar producto
    public boolean save(Producto producto) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            if (producto.getId() == null) {
                entityManager.persist(producto);
            } else {
                entityManager.merge(producto);
            }
            
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al guardar producto: " + e.getMessage());
            return false;
        }
    }

    // READ - Buscar por ID
    public Optional<Producto> findById(Long id) {
        try {
            Producto producto = entityManager.find(Producto.class, id);
            return Optional.ofNullable(producto);
        } catch (Exception e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    // READ - Buscar por código
    public Optional<Producto> findByCodigo(String codigo) {
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.codigo = :codigo", Producto.class);
            query.setParameter("codigo", codigo);
            
            Producto producto = query.getSingleResult();
            return Optional.ofNullable(producto);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error al buscar producto por código: " + e.getMessage());
            return Optional.empty();
        }
    }

    // READ - Obtener todos los productos
    public LinkedList<Producto> findAll() {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p ORDER BY p.nombre ASC", Producto.class);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar por categoría
    public LinkedList<Producto> findByCategoria(CategoriaProducto categoria) {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.categoria = :categoria ORDER BY p.nombre ASC", 
                Producto.class);
            query.setParameter("categoria", categoria);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar por estado
    public LinkedList<Producto> findByEstado(EstadoProducto estado) {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.estado = :estado ORDER BY p.nombre ASC", 
                Producto.class);
            query.setParameter("estado", estado);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos por estado: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar productos disponibles
    public LinkedList<Producto> findDisponibles() {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.estado = :estado AND p.stock > 0 ORDER BY p.nombre ASC", 
                Producto.class);
            query.setParameter("estado", EstadoProducto.ACTIVO);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos disponibles: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar productos con stock bajo
    public LinkedList<Producto> findConStockBajo() {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.stock <= p.stockMinimo AND p.estado = :estado ORDER BY p.stock ASC", 
                Producto.class);
            query.setParameter("estado", EstadoProducto.ACTIVO);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos con stock bajo: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar por nombre (coincidencia parcial)
    public LinkedList<Producto> findByNombreContaining(String nombre) {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(:nombre) ORDER BY p.nombre ASC", 
                Producto.class);
            query.setParameter("nombre", "%" + nombre + "%");
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return productos;
    }

    // READ - Buscar por rango de precios
    public LinkedList<Producto> findByRangoPrecios(BigDecimal precioMin, BigDecimal precioMax) {
        LinkedList<Producto> productos = new LinkedList<>();
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMin AND :precioMax ORDER BY p.precio ASC", 
                Producto.class);
            query.setParameter("precioMin", precioMin);
            query.setParameter("precioMax", precioMax);
            
            java.util.List<Producto> resultList = query.getResultList();
            for (Producto producto : resultList) {
                productos.add(producto);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar productos por rango de precios: " + e.getMessage());
        }
        return productos;
    }

    // UPDATE - Actualizar stock
    public boolean updateStock(Long id, Integer nuevoStock) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Query query = entityManager.createQuery(
                "UPDATE Producto p SET p.stock = :nuevoStock, p.fechaActualizacion = :fecha WHERE p.id = :id");
            query.setParameter("nuevoStock", nuevoStock);
            query.setParameter("fecha", LocalDateTime.now());
            query.setParameter("id", id);
            
            int updated = query.executeUpdate();
            transaction.commit();
            
            return updated > 0;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }

    // UPDATE - Actualizar precio
    public boolean updatePrecio(Long id, BigDecimal nuevoPrecio) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Query query = entityManager.createQuery(
                "UPDATE Producto p SET p.precio = :nuevoPrecio, p.fechaActualizacion = :fecha WHERE p.id = :id");
            query.setParameter("nuevoPrecio", nuevoPrecio);
            query.setParameter("fecha", LocalDateTime.now());
            query.setParameter("id", id);
            
            int updated = query.executeUpdate();
            transaction.commit();
            
            return updated > 0;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar precio: " + e.getMessage());
            return false;
        }
    }

    // UPDATE - Cambiar estado
    public boolean updateEstado(Long id, EstadoProducto nuevoEstado) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Query query = entityManager.createQuery(
                "UPDATE Producto p SET p.estado = :nuevoEstado, p.fechaActualizacion = :fecha WHERE p.id = :id");
            query.setParameter("nuevoEstado", nuevoEstado);
            query.setParameter("fecha", LocalDateTime.now());
            query.setParameter("id", id);
            
            int updated = query.executeUpdate();
            transaction.commit();
            
            return updated > 0;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    // DELETE - Eliminar producto (soft delete)
    public boolean delete(Long id) {
        return updateEstado(id, EstadoProducto.INACTIVO);
    }

    // DELETE - Eliminar permanentemente
    public boolean deletePermanently(Long id) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            
            Producto producto = entityManager.find(Producto.class, id);
            if (producto != null) {
                entityManager.remove(producto);
                transaction.commit();
                return true;
            }
            transaction.rollback();
            return false;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error al eliminar producto permanentemente: " + e.getMessage());
            return false;
        }
    }

    // UTILITY - Contar productos
    public long count() {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Producto p", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al contar productos: " + e.getMessage());
            return 0;
        }
    }

    // UTILITY - Contar productos por estado
    public long countByEstado(EstadoProducto estado) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Producto p WHERE p.estado = :estado", Long.class);
            query.setParameter("estado", estado);
            return query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error al contar productos por estado: " + e.getMessage());
            return 0;
        }
    }

    // UTILITY - Verificar si existe código
    public boolean existsByCodigo(String codigo) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Producto p WHERE p.codigo = :codigo", Long.class);
            query.setParameter("codigo", codigo);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            System.err.println("Error al verificar existencia de código: " + e.getMessage());
            return false;
        }
    }
}