package my.app.model


import java.util.Date


data class Boleta(
    val fecha: Date = Date(),
    val total: Double,
    val productos: List<Producto>,
    val usuarioId: Long
)