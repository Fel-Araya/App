package my.app.activities

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import my.app.R
import my.app.data.CarritoRepository
import my.app.model.Producto
import java.text.NumberFormat
import java.util.*

class DetalleActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        setupNavbar()

        val tvNombre = findViewById<TextView>(R.id.tvNombreDetalle)
        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcionDetalle)
        val tvPrecio = findViewById<TextView>(R.id.tvPrecioDetalle)
        val ivProducto = findViewById<ImageView>(R.id.imgDetalle)
        val btnAgregarCarrito = findViewById<Button>(R.id.btnAgregarCarrito)

        val producto = intent.getParcelableExtra<Producto>("producto")

        if (producto != null) {
            tvNombre.text = producto.nombre ?: "Sin nombre"
            tvDescripcion.text = producto.descripcion ?: "Sin descripción"
            val formattedPrice = NumberFormat.getCurrencyInstance(Locale.getDefault())
                .format(producto.precio ?: 0.0)
            tvPrecio.text = formattedPrice

            when {
                producto.imagenResId != null -> ivProducto.setImageResource(producto.imagenResId!!)
                !producto.imagenUri.isNullOrEmpty() -> Glide.with(this)
                    .load(producto.imagenUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivProducto)
                else -> ivProducto.setImageResource(R.drawable.ic_launcher_foreground)
            }

            btnAgregarCarrito.setOnClickListener {
                CarritoRepository.agregar(producto)
                showToast("${producto.nombre} agregado al carrito ✅")
            }

        } else {
            finish()
        }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
