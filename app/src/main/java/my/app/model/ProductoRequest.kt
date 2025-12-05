package my.app.model

data class ProductoRequest(
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagenUrl: String? = null
)