package my.app.data

import my.app.R
import my.app.model.Producto

object ProductoRepository {

    val productos = mutableListOf(
        Producto(
            id = 1,
            nombre = "Pro Deck",
            descripcion = "Tabla profesional de 8 capas",
            precio = 89000.0,
            imagenResId = R.drawable.deck
        ),
        Producto(
            id = 2,
            nombre = "Speed Wheels",
            descripcion = "Ruedas de alta velocidad 52mm",
            precio = 45000.0,
            imagenResId = R.drawable.speedwheels
        ),
        Producto(
            id = 3,
            nombre = "Metal Trucks",
            descripcion = "Ejes de aluminio reforzado",
            precio = 65000.0,
            imagenResId = R.drawable.metal_trucks
        )
    )

    fun byId(id: Int): Producto? = productos.find { it.id == id.toLong() }


    fun agregarProducto(producto: Producto) {
        // Evita duplicados por ID
        if (productos.none { it.id == producto.id }) {
            productos.add(0, producto) // agregamos al inicio
        }
    }
}
