package my.app.data

import my.app.model.Usuario

object UsuarioRepository {
    val usuarios = mutableListOf<Usuario>()

    var usuarioLogueado: Usuario? = null
}
