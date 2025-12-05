package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.app.R
import my.app.adapters.AdminProductoAdapter
import my.app.model.Producto
import my.app.model.ProductoRequest
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminViewActivity : BaseActivity() {

    private lateinit var adapter: AdminProductoAdapter
    private lateinit var rv: RecyclerView
    private val productos = mutableListOf<Producto>()
    private val imagenLocal = R.drawable.ic_launcher_foreground

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        setupNavbar()

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val etPrecio = findViewById<EditText>(R.id.etPrecio)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        rv = findViewById(R.id.rvProductosAdmin)
        rv.layoutManager = LinearLayoutManager(this)

        adapter = AdminProductoAdapter(
            productos = productos,
            onVer = { p ->
                val i = Intent(this, DetalleActivity::class.java)
                i.putExtra("producto", p)
                startActivity(i)
            },
            onEditar = { p ->
                val i = Intent(this, EditarProductoActivity::class.java)
                i.putExtra("productoId", p.id)
                startActivity(i)
            },
            onEliminar = { p ->
                RetrofitClient.instance.eliminarProducto(p.id)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                val idx = productos.indexOfFirst { it.id == p.id }
                                if (idx >= 0) {
                                    productos.removeAt(idx)
                                    adapter.notifyItemRemoved(idx)
                                    Toast.makeText(this@AdminViewActivity, "Producto eliminado ✅", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@AdminViewActivity, "Error al eliminar: ${response.code()}", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(this@AdminViewActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        )

        rv.adapter = adapter

        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val desc = etDescripcion.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()

            if (nombre.isEmpty() || desc.isEmpty() || precio == null) {
                Toast.makeText(this, "Completa nombre, descripción y precio.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevoProductoRequest = ProductoRequest(
                nombre = nombre,
                descripcion = desc,
                precio = precio,
                imagenUrl = null
            )

            RetrofitClient.instance.crearProducto(nuevoProductoRequest)
                .enqueue(object : Callback<Producto> {
                    override fun onResponse(call: Call<Producto>, response: Response<Producto>) {
                        if (response.isSuccessful) {
                            response.body()?.let { creado ->
                                val productoLocal = Producto(
                                    id = creado.id ?: 0L,  // <- aquí el cambio
                                    nombre = creado.nombre,
                                    descripcion = creado.descripcion,
                                    precio = creado.precio,
                                    imagenResId = imagenLocal,
                                    imagenUri = creado.imagenUri
                                )
                                productos.add(0, productoLocal)
                                adapter.notifyItemInserted(0)
                                rv.scrollToPosition(0)

                                etNombre.text.clear()
                                etDescripcion.text.clear()
                                etPrecio.text.clear()

                                Toast.makeText(this@AdminViewActivity, "Producto agregado ✅", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@AdminViewActivity, "Error al crear: ${response.code()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Producto>, t: Throwable) {
                        Toast.makeText(this@AdminViewActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
        }

        cargarProductos()
    }

    private fun cargarProductos() {
        RetrofitClient.instance.getProductos()
            .enqueue(object : Callback<List<Producto>> {
                override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                    if (response.isSuccessful) {
                        productos.clear()
                        response.body()?.forEach { creado ->
                            productos.add(
                                Producto(
                                    id = creado.id ?: 0L,  // <- cambio aquí también
                                    nombre = creado.nombre,
                                    descripcion = creado.descripcion,
                                    precio = creado.precio,
                                    imagenResId = imagenLocal,
                                    imagenUri = creado.imagenUri
                                )
                            )
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(this@AdminViewActivity, "Error al cargar productos: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
