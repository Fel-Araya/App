package my.app.model

data class Usuario(
    val id: Long? = null,
    val nombre: String,
    val email: String,
    val password: String,
    val esAdmin: Boolean = false
)

