package my.app.activities

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import my.app.R
import my.app.adapters.ProductoAdapter
import my.app.model.Producto
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private val productos = mutableListOf<Producto>()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_producto)

        setupNavbar()

        recyclerView = findViewById(R.id.rvProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productos.clear()
        productos.addAll(my.app.data.ProductoRepository.productos) // copia los locales

        adapter = ProductoAdapter(this, productos)
        recyclerView.adapter = adapter

        cargarProductos()
    }

    private fun cargarProductos() {
        RetrofitClient.instance.getProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val apiList = response.body() ?: emptyList()

                    var anyAdded = false
                    for (p in apiList) {
                        val existsLocally = productos.any { it.id == p.id }
                        if (!existsLocally) {
                            productos.add(p)
                            anyAdded = true
                        } else {
                        }
                    }

                    if (anyAdded) {
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ProductosActivity, "Error al cargar productos: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                Toast.makeText(this@ProductosActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
