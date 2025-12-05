package my.app.adapters

import android.content.Intent
import android.net.Uri
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

class AdminProductoAdapter(
    private val productos: MutableList<Producto>,
    private val onVer: (Producto) -> Unit,
    private val onEditar: (Producto) -> Unit,
    private val onEliminar: (Producto) -> Unit
) : RecyclerView.Adapter<AdminProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagen: ImageView = view.findViewById(R.id.imgProductoAdmin)
        val nombre: TextView = view.findViewById(R.id.tvNombreProductoAdmin)
        val precio: TextView = view.findViewById(R.id.tvPrecioProductoAdmin)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminarAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_admin_producto, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = productos[position]

        when {
            !p.imagenUri.isNullOrEmpty() -> {
                Glide.with(holder.itemView.context)
                    .load(p.imagenUri)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.imagen)
            }
            p.imagenResId != null -> holder.imagen.setImageResource(p.imagenResId!!)
            else -> holder.imagen.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.nombre.text = p.nombre
        holder.precio.text = "$${p.precio}"



        holder.btnEliminar.setOnClickListener { onEliminar(p) }
    }

    override fun getItemCount(): Int = productos.size
}
