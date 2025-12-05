package my.app.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import my.app.R
import my.app.model.Producto

class CarritoAdapter(
    private val context: Context,
    private val productos: MutableList<Producto>,
    private var onItemRemove: ((Int) -> Unit)? = null // Listener opcional
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    fun setOnItemRemoveListener(listener: (Int) -> Unit) {
        onItemRemove = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imgProducto)
        val nombre: TextView = view.findViewById(R.id.tvNombreProducto)
        val precio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]

        when {
            producto.imagenUri != null -> holder.imagen.setImageURI(Uri.parse(producto.imagenUri))
            producto.imagenResId != null -> holder.imagen.setImageResource(producto.imagenResId!!)
            else -> holder.imagen.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.nombre.text = producto.nombre
        holder.precio.text = "$${"%.2f".format(producto.precio)}"

        holder.btnEliminar.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onItemRemove?.invoke(pos)
            }
        }
    }


    override fun getItemCount(): Int = productos.size
}
