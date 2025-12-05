// my/app/activities/EditarProductoActivity.kt
package my.app.activities

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import my.app.R
import my.app.data.ProductoRepository
import my.app.model.Producto

class EditarProductoActivity : BaseActivity() {

    private var producto: Producto? = null
    private var nuevaImagenUri: String? = null

    private lateinit var imgPreview: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etCategoria: EditText

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            nuevaImagenUri = it.toString()
            imgPreview.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)
        setupNavbar()

        val id = intent.getIntExtra("productoId", -1)
        producto = ProductoRepository.byId(id)

        imgPreview = findViewById(R.id.imgPreviewEdit)
        etNombre = findViewById(R.id.etNombreEdit)
        etDescripcion = findViewById(R.id.etDescripcionEdit)
        etPrecio = findViewById(R.id.etPrecioEdit)
        val btnCambiarImagen = findViewById<Button>(R.id.btnCambiarImagenEdit)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarEdit)

        if (producto == null) {
            Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etNombre.setText(producto!!.nombre)
        etDescripcion.setText(producto!!.descripcion)
        etPrecio.setText(producto!!.precio.toString())

        val uri = producto!!.imagenUri
        if (uri.isNullOrBlank()) {
            imgPreview.setImageResource(android.R.color.transparent) // sin imagen
        } else {
            imgPreview.setImageURI(Uri.parse(uri))
        }

        btnCambiarImagen.setOnClickListener {
            pickImage.launch("image/*")
        }

        btnGuardar.setOnClickListener {
            val p = producto ?: return@setOnClickListener

            val nuevoPrecio = etPrecio.text.toString().toDoubleOrNull()
            if (etNombre.text.isNullOrBlank() || etDescripcion.text.isNullOrBlank() || nuevoPrecio == null) {
                Toast.makeText(this, "Completa nombre, descripción y precio.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            p.nombre = etNombre.text.toString()
            p.descripcion = etDescripcion.text.toString()
            p.precio = nuevoPrecio
            if (nuevaImagenUri != null) p.imagenUri = nuevaImagenUri

            Toast.makeText(this, "Cambios guardados ✅", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
