package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import my.app.R
import my.app.model.Usuario
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        setupNavbar()

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val tvExito = findViewById<TextView>(R.id.tvRegistroExitoso)
        tvExito.visibility = View.INVISIBLE

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty()) {

                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    email = correo,
                    password = password
                )

                RetrofitClient.instance.crearUsuario(nuevoUsuario)
                    .enqueue(object : Callback<Usuario> {
                        override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                            if (response.isSuccessful) {

                                val fadeIn = AlphaAnimation(0f, 1f)
                                fadeIn.duration = 500
                                fadeIn.fillAfter = true

                                tvExito.text = "Registro exitoso ✅"
                                tvExito.visibility = View.VISIBLE
                                tvExito.startAnimation(fadeIn)

                                Handler(Looper.getMainLooper()).postDelayed({
                                    val fadeOut = AlphaAnimation(1f, 0f)
                                    fadeOut.duration = 500
                                    fadeOut.fillAfter = true
                                    fadeOut.setAnimationListener(object : Animation.AnimationListener {
                                        override fun onAnimationStart(animation: Animation?) {}
                                        override fun onAnimationEnd(animation: Animation?) {
                                            startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                                            finish()
                                        }
                                        override fun onAnimationRepeat(animation: Animation?) {}
                                    })
                                    tvExito.startAnimation(fadeOut)
                                }, 1500)

                            } else {
                                Toast.makeText(
                                    this@RegistroActivity,
                                    "Error al registrar: ${response.code()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Usuario>, t: Throwable) {
                            Toast.makeText(
                                this@RegistroActivity,
                                "Error de conexión: ${t.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })

            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
