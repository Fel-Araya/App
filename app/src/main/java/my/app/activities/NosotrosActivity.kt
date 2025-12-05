package my.app.activities

import android.os.Bundle
import my.app.R

class NosotrosActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nosotros)
        setupNavbar()
    }
}