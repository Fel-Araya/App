package my.app.data

import my.app.model.Producto

object CarritoRepository {
    val productosEnCarrito = mutableListOf<Producto>()

    fun agregar(producto: Producto) {
        productosEnCarrito.add(producto)
    }

    fun eliminar(producto: Producto) {
        productosEnCarrito.remove(producto)
    }

    fun limpiar() {
        productosEnCarrito.clear()
    }
}
