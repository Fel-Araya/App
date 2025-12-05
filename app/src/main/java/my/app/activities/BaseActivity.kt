package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import my.app.R
import my.app.data.SesionManager

open class BaseActivity : androidx.appcompat.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupNavbar() {

        val logo = findViewById<ImageView?>(R.id.logoImage)
        val btnProductos = findViewById<Button?>(R.id.btnNavProductos)
        val btnNosotros = findViewById<Button?>(R.id.btnNavNosotros)
        val btnAdmin = findViewById<Button?>(R.id.btnNavAdmin)

        val carritoNormal = findViewById<ImageView?>(R.id.btnNavCarritoNormal)
        val carritoAdmin = findViewById<ImageView?>(R.id.btnNavCarritoAdmin)

        val btnLogout = findViewById<Button?>(R.id.btnNavLogout)

        val esAdmin = SesionManager.esAdmin

        if (esAdmin) {
            btnAdmin?.visibility = View.VISIBLE
            carritoNormal?.visibility = View.GONE
            carritoAdmin?.visibility = View.VISIBLE
        } else {
            btnAdmin?.visibility = View.GONE
            carritoAdmin?.visibility = View.GONE
            carritoNormal?.visibility = View.VISIBLE
        }

        btnLogout?.visibility = View.VISIBLE

        // Navegación
        logo?.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        btnProductos?.setOnClickListener {
            startActivity(Intent(this, ProductosActivity::class.java))
        }

        btnNosotros?.setOnClickListener {
            startActivity(Intent(this, NosotrosActivity::class.java))
        }

        btnAdmin?.setOnClickListener {
            startActivity(Intent(this, AdminViewActivity::class.java))
        }

        val abrirCarrito = View.OnClickListener {
            startActivity(Intent(this, CarritoActivity::class.java))
        }

        carritoNormal?.setOnClickListener(abrirCarrito)
        carritoAdmin?.setOnClickListener(abrirCarrito)

        btnLogout?.setOnClickListener {
            SesionManager.esAdmin = false
            SesionManager.usuarioActual = null

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
