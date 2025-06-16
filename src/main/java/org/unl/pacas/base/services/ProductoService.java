package org.unl.pacas.base.services;

import org.unl.pacas.base.dao.ProductoDao;
import org.unl.pacas.base.models.Producto;
import org.unl.pacas.base.models.CategoriaProducto;
import org.unl.pacas.base.models.EstadoProducto;
import org.unl.pacas.base.controller.data_struct.list.LinkedList;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class ProductoService {
    
    private ProductoDao productoDao;
    
    public ProductoService() {
        this.productoDao = new ProductoDao();
    }
    
    // Constructor para inyección de dependencias (testing)
    public ProductoService(ProductoDao productoDao) {
        this.productoDao = productoDao;
    }

    // Cerrar recursos
    public void close() {
        if (productoDao != null) {
            productoDao.close();
        }
    }

    // ============= OPERACIONES CRUD =============

    /**
     * Crear un nuevo producto
     */
    public boolean crearProducto(Producto producto) {
        try {
            // Validaciones de negocio
            if (!validarProducto(producto)) {
                return false;
            }
            
            // Verificar que el código no exista
            if (productoDao.existsByCodigo(producto.getCodigo())) {
                System.err.println("Ya existe un producto con el código: " + producto.getCodigo());
                return false;
            }
            
            // Establecer valores por defecto
            if (producto.getFechaCreacion() == null) {
                producto.setFechaCreacion(LocalDateTime.now());
            }
            producto.setFechaActualizacion(LocalDateTime.now());
            
            if (producto.getEstado() == null) {
                producto.setEstado(EstadoProducto.ACTIVO);
            }
            
            return productoDao.save(producto);
        } catch (Exception e) {
            System.err.println("Error en crearProducto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualizar un producto existente
     */
    public boolean actualizarProducto(Producto producto) {
        try {
            if (producto.getId() == null) {
                System.err.println("No se puede actualizar un producto sin ID");
                return false;
            }
            
            if (!validarProducto(producto)) {
                return false;
            }
            
            // Verificar que existe
            Optional<Producto> existente = productoDao.findById(producto.getId());
            if (!existente.isPresent()) {
                System.err.println("No existe el producto con ID: " + producto.getId());
                return false;
            }
            
            // Verificar que el código no esté duplicado (excepto el mismo producto)
            Optional<Producto> productoPorCodigo = productoDao.findByCodigo(producto.getCodigo());
            if (productoPorCodigo.isPresent() && 
                !productoPorCodigo.get().getId().equals(producto.getId())) {
                System.err.println("Ya existe otro producto con el código: " + producto.getCodigo());
                return false;
            }
            
            producto.setFechaActualizacion(LocalDateTime.now());
            return productoDao.save(producto);
        } catch (Exception e) {
            System.err.println("Error en actualizarProducto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Eliminar producto (soft delete)
     */
    public boolean eliminarProducto(Long id) {
        try {
            return productoDao.delete(id);
        } catch (Exception e) {
            System.err.println("Error en eliminarProducto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtener producto por ID
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        try {
            return productoDao.findById(id);
        } catch (Exception e) {
            System.err.println("Error en obtenerProductoPorId: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Obtener producto por código
     */
    public Optional<Producto> obtenerProductoPorCodigo(String codigo) {
        try {
            return productoDao.findByCodigo(codigo);
        } catch (Exception e) {
            System.err.println("Error en obtenerProductoPorCodigo: " + e.getMessage());
            return Optional.empty();
        }
    }

    // ============= CONSULTAS ESPECÍFICAS =============

    /**
     * Obtener todos los productos activos
     */
    public LinkedList<Producto> obtenerProductosActivos() {
        return productoDao.findByEstado(EstadoProducto.ACTIVO);
    }

    /**
     * Obtener productos disponibles para venta
     */
    public LinkedList<Producto> obtenerProductosDisponibles() {
        return productoDao.findDisponibles();
    }

    /**
     * Obtener productos por categoría
     */
    public LinkedList<Producto> obtenerProductosPorCategoria(CategoriaProducto categoria) {
        return productoDao.findByCategoria(categoria);
    }

    /**
     * Obtener productos con stock bajo
     */
    public LinkedList<Producto> obtenerProductosConStockBajo() {
        return productoDao.findConStockBajo();
    }

    /**
     * Buscar productos por nombre
     */
    public LinkedList<Producto> buscarProductosPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new LinkedList<>();
        }
        return productoDao.findByNombreContaining(nombre.trim());
    }

    /**
     * Obtener productos por rango de precios
     */
    public LinkedList<Producto> obtenerProductosPorRangoPrecios(BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin == null || precioMax == null || precioMin.compareTo(precioMax) > 0) {
            return new LinkedList<>();
        }
        return productoDao.findByRangoPrecios(precioMin, precioMax);
    }

    // ============= OPERACIONES DE INVENTARIO =============

    /**
     * Actualizar stock de un producto
     */
    public boolean actualizarStock(Long id, Integer nuevoStock) {
        try {
            if (nuevoStock < 0) {
                System.err.println("El stock no puede ser negativo");
                return false;
            }
            
            Optional<Producto> producto = productoDao.findById(id);
            if (!producto.isPresent()) {
                System.err.println("No existe el producto con ID: " + id);
                return false;
            }
            
            return productoDao.updateStock(id, nuevoStock);
        } catch (Exception e) {
            System.err.println("Error en actualizarStock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reducir stock (para ventas)
     */
    public boolean reducirStock(Long id, Integer cantidad) {
        try {
            Optional<Producto> productoOpt = productoDao.findById(id);
            if (!productoOpt.isPresent()) {
                System.err.println("No existe el producto con ID: " + id);
                return false;
            }
            
            Producto producto = productoOpt.get();
            if (producto.getStock() < cantidad) {
                System.err.println("Stock insuficiente. Disponible: " + producto.getStock() + ", Solicitado: " + cantidad);
                return false;
            }
            
            int nuevoStock = producto.getStock() - cantidad;
            return productoDao.updateStock(id, nuevoStock);
        } catch (Exception e) {
            System.err.println("Error en reducirStock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Aumentar stock (para reposiciones)
     */
    public boolean aumentarStock(Long id, Integer cantidad) {
        try {
            Optional<Producto> productoOpt = productoDao.findById(id);
            if (!productoOpt.isPresent()) {
                System.err.println("No existe el producto con ID: " + id);
                return false;
            }
            
            Producto producto = productoOpt.get();
            int nuevoStock = producto.getStock() + cantidad;
            return productoDao.updateStock(id, nuevoStock);
        } catch (Exception e) {
            System.err.println("Error en aumentarStock: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualizar precio de un producto
     */
    public boolean actualizarPrecio(Long id, BigDecimal nuevoPrecio) {
        try {
            if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("El precio debe ser mayor a cero");
                return false;
            }
            
            return productoDao.updatePrecio(id, nuevoPrecio);
        } catch (Exception e) {
            System.err.println("Error en actualizarPrecio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambiar estado de un producto
     */
    public boolean cambiarEstado(Long id, EstadoProducto nuevoEstado) {
        try {
            return productoDao.updateEstado(id, nuevoEstado);
        } catch (Exception e) {
            System.err.println("Error en cambiarEstado: " + e.getMessage());
            return false;
        }
    }

    // ============= VALIDACIONES Y UTILIDADES =============

    /**
     * Validar datos de un producto
     */
    private boolean validarProducto(Producto producto) {
        if (producto == null) {
            System.err.println("El producto no puede ser null");
            return false;
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            System.err.println("El nombre del producto es obligatorio");
            return false;
        }
        
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            System.err.println("El código del producto es obligatorio");
            return false;
        }
        
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("El precio debe ser mayor a cero");
            return false;
        }
        
        if (producto.getPrecioCosto() == null || producto.getPrecioCosto().compareTo(BigDecimal.ZERO) <= 0) {
            System.err.println("El precio de costo debe ser mayor a cero");
            return false;
        }
        
        if (producto.getStock() == null || producto.getStock() < 0) {
            System.err.println("El stock no puede ser negativo");
            return false;
        }
        
        if (producto.getCategoria() == null) {
            System.err.println("La categoría es obligatoria");
            return false;
        }
        
        return true;
    }

    /**
     * Verificar disponibilidad para venta
     */
    public boolean verificarDisponibilidad(Long id, Integer cantidad) {
        try {
            Optional<Producto> productoOpt = productoDao.findById(id);
            if (!productoOpt.isPresent()) {
                return false;
            }
            
            Producto producto = productoOpt.get();
            return producto.isDisponible() && producto.getStock() >= cantidad;
        } catch (Exception e) {
            System.err.println("Error en verificarDisponibilidad: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtener productos que requieren atención (stock bajo, etc.)
     */
    public LinkedList<Producto> obtenerProductosQueRequierenAtencion() {
        LinkedList<Producto> productosAtencion = new LinkedList<>();
        
        // Productos con stock bajo
        LinkedList<Producto> stockBajo = obtenerProductosConStockBajo();
        for (int i = 0; i < stockBajo.size(); i++) {
            productosAtencion.add(stockBajo.get(i));
        }
        
        // Productos en revisión
        LinkedList<Producto> enRevision = productoDao.findByEstado(EstadoProducto.EN_REVISION);
        for (int i = 0; i < enRevision.size(); i++) {
            productosAtencion.add(enRevision.get(i));
        }
        
        return productosAtencion;
    }

    /**
     * Obtener estadísticas de productos
     */
    public ProductoEstadisticas obtenerEstadisticas() {
        return new ProductoEstadisticas(
            productoDao.count(),
            productoDao.countByEstado(EstadoProducto.ACTIVO),
            productoDao.countByEstado(EstadoProducto.INACTIVO),
            obtenerProductosConStockBajo().size()
        );
    }

    // Clase interna para estadísticas
    public static class ProductoEstadisticas {
        private final long totalProductos;
        private final long productosActivos;
        private final long productosInactivos;
        private final long productosStockBajo;

        public ProductoEstadisticas(long totalProductos, long productosActivos, 
                                  long productosInactivos, long productosStockBajo) {
            this.totalProductos = totalProductos;
            this.productosActivos = productosActivos;
            this.productosInactivos = productosInactivos;
            this.productosStockBajo = productosStockBajo;
        }

        // Getters
        public long getTotalProductos() { return totalProductos; }
        public long getProductosActivos() { return productosActivos; }
        public long getProductosInactivos() { return productosInactivos; }
        public long getProductosStockBajo() { return productosStockBajo; }

        @Override
        public String toString() {
            return String.format(
                "Estadísticas de Productos: Total=%d, Activos=%d, Inactivos=%d, Stock Bajo=%d",
                totalProductos, productosActivos, productosInactivos, productosStockBajo
            );
        }
    }
}