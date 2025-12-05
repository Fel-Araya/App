package my.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import my.app.R

class CompraExitosaActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compra_exitosa)
        setupNavbar()

        val imgCheck = findViewById<ImageView>(R.id.imgCheck)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val btnVolver = findViewById<Button>(R.id.btnVolverInicio)

        val anim = AnimationUtils.loadAnimation(this, R.anim.success_pop)
        imgCheck.startAnimation(anim)
        tvMensaje.startAnimation(anim)

        btnVolver.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
