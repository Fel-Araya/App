package my.app.model

data class BoletaRequest(
    val total: Double,
    val fecha: String,
    val productos: List<Long>,
    val usuarioId: Long
)
