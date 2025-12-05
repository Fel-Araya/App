package my.app.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import my.app.R
import my.app.activities.DetalleActivity
import my.app.model.Producto

class ProductoAdapter(
    private val context: Context,
    private val productos: List<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imgProducto)
        val nombre: TextView = view.findViewById(R.id.tvNombreProducto)
        val precio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val btnVerDetalle: Button = view.findViewById(R.id.btnVerDetalle)
        val btnComprar: Button = view.findViewById(R.id.btnComprar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_itemproducto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]

        when {
            !producto.imagenUri.isNullOrEmpty() -> {
                Glide.with(context)
                    .load(producto.imagenUri)
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder)
                    .into(holder.imagen)
            }

            producto.imagenResId != null -> {
                holder.imagen.setImageResource(producto.imagenResId!!)
            }

            else -> {
                holder.imagen.setImageResource(R.drawable.logo)
            }
        }

        holder.nombre.text = producto.nombre ?: "Sin nombre"
        holder.precio.text = "$${String.format("%.0f", producto.precio ?: 0.0)}"

        val abrirDetalle = View.OnClickListener {
            val intent = Intent(context, DetalleActivity::class.java)
            intent.putExtra("producto", producto)
            context.startActivity(intent)

            if (context is android.app.Activity) {
                context.overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
        }

        holder.btnVerDetalle.setOnClickListener(abrirDetalle)
        holder.btnComprar.setOnClickListener(abrirDetalle)
    }

    override fun getItemCount(): Int = productos.size
}
