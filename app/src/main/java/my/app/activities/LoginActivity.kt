package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import my.app.R
import my.app.data.UsuarioRepository
import my.app.data.SesionManager
import my.app.model.Usuario
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegistro = findViewById<Button>(R.id.btnRegistro)

        btnLogin.setOnClickListener {
            val inputEmail = etUsuario.text.toString()
            val inputPassword = etPassword.text.toString()

            if (inputEmail.isEmpty() || inputPassword.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Caso Admin fijo
            if (inputEmail == "admin" && inputPassword == "1234") {
                Toast.makeText(this, "Bienvenido Admin 👑", Toast.LENGTH_SHORT).show()
                SesionManager.esAdmin = true
                SesionManager.usuarioActual = "admin"

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                return@setOnClickListener
            }

            RetrofitClient.instance.getUsuarios()
                .enqueue(object : Callback<List<Usuario>> {
                    override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                        if (response.isSuccessful) {
                            val usuarios = response.body() ?: emptyList()
                            val usuarioEncontrado = usuarios.find { it.email == inputEmail && it.password == inputPassword }

                            if (usuarioEncontrado != null) {
                                UsuarioRepository.usuarioLogueado = usuarioEncontrado
                                SesionManager.esAdmin = false
                                SesionManager.usuarioActual = usuarioEncontrado.nombre

                                Toast.makeText(this@LoginActivity, "Bienvenido ${usuarioEncontrado.nombre} 👋", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos ❌", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Error al consultar usuarios: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        btnRegistro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
