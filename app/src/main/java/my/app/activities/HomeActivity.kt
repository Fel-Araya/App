package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import my.app.R

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupNavbar()

        val btnCatalogo = findViewById<Button>(R.id.btnCatalogo)

        btnCatalogo.setOnClickListener {
            val i = Intent(this, ProductosActivity::class.java)
            startActivity(i)
        }
    }
}
