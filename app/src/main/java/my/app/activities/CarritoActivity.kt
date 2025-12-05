package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.app.R
import my.app.adapters.CarritoAdapter
import my.app.data.CarritoRepository
import my.app.data.UsuarioRepository
import my.app.model.BoletaRequest
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CarritoActivity : BaseActivity() {

    private lateinit var adapter: CarritoAdapter
    private lateinit var tvTotal: TextView
    private lateinit var btnPagar: Button
    private var enviandoBoleta = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

        setupNavbar()

        val recyclerView = findViewById<RecyclerView>(R.id.rvCarrito)
        tvTotal = findViewById(R.id.tvTotal)
        btnPagar = findViewById(R.id.btnPagar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CarritoAdapter(this, CarritoRepository.productosEnCarrito, null)
        recyclerView.adapter = adapter

        adapter.setOnItemRemoveListener { index ->
            CarritoRepository.productosEnCarrito.removeAt(index)
            adapter.notifyItemRemoved(index)
            actualizarTotal()
        }

        actualizarTotal()

        btnPagar.setOnClickListener {
            if (enviandoBoleta) return@setOnClickListener

            val productosEnCarrito = CarritoRepository.productosEnCarrito.toList()
            val total = productosEnCarrito.sumOf { it.precio }

            if (productosEnCarrito.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (productosEnCarrito.any { it.id == null }) {
                Toast.makeText(this, "Error: algún producto no tiene ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuarioLogueado = UsuarioRepository.usuarioLogueado
            if (usuarioLogueado?.id == null) {
                Toast.makeText(this, "Debe iniciar sesión para pagar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            enviandoBoleta = true
            btnPagar.isEnabled = false

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
            val fechaStr = sdf.format(Date())

            val boletaRequest = BoletaRequest(
                total = total,
                fecha = fechaStr,
                productos = productosEnCarrito.map { it.id!! },
                usuarioId = usuarioLogueado.id!!
            )

            RetrofitClient.instance.enviarBoleta(boletaRequest)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        enviandoBoleta = false
                        btnPagar.isEnabled = true

                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@CarritoActivity,
                                "Boleta enviada correctamente ✅",
                                Toast.LENGTH_SHORT
                            ).show()

                            CarritoRepository.productosEnCarrito.clear()
                            adapter.notifyDataSetChanged()
                            actualizarTotal()

                            startActivity(Intent(this@CarritoActivity, CompraExitosaActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this@CarritoActivity,
                                "Error al enviar boleta: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        enviandoBoleta = false
                        btnPagar.isEnabled = true
                        Toast.makeText(
                            this@CarritoActivity,
                            "Fallo de conexión: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun actualizarTotal() {
        val total = CarritoRepository.productosEnCarrito.sumOf { it.precio }
        tvTotal.text = "Total: $${"%.2f".format(total)}"
    }
}
