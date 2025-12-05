package my.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Producto(
    var id: Long,
    var nombre: String,
    var descripcion: String? = "",
    var precio: Double,
    var imagenResId: Int? = null,
    var imagenUri: String? = null
) : Parcelable
