package my.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.app.model.Producto
import my.app.model.Usuario
import my.app.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import my.app.ui.theme.AppTheme
import my.app.data.UsuarioRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val productosState = mutableStateOf<List<Producto>>(emptyList())
        val usuariosState = mutableStateOf<List<Usuario>>(emptyList())

        // --- Funciones para cargar datos ---
        fun cargarProductos() {
            RetrofitClient.instance.getProductos().enqueue(object : Callback<List<Producto>> {
                override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                    if (response.isSuccessful) {
                        productosState.value = response.body() ?: emptyList()
                    }
                }
                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error productos: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        fun cargarUsuarios() {
            RetrofitClient.instance.getUsuarios().enqueue(object : Callback<List<Usuario>> {
                override fun onResponse(call: Call<List<Usuario>>, response: Response<List<Usuario>>) {
                    if (response.isSuccessful) {
                        val usuarios = response.body() ?: emptyList()
                        usuariosState.value = usuarios
                        UsuarioRepository.usuarios.clear()
                        UsuarioRepository.usuarios.addAll(usuarios)
                    }
                }
                override fun onFailure(call: Call<List<Usuario>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error usuarios: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        fun crearUsuario(nombre: String, email: String, password: String) {
            val nuevoUsuario = Usuario(id = 0L, nombre = nombre, email = email, password = password) // asegurando ID
            RetrofitClient.instance.crearUsuario(nuevoUsuario).enqueue(object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                        cargarUsuarios()
                    } else {
                        Toast.makeText(applicationContext, "Error al crear usuario: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }

        cargarProductos()
        cargarUsuarios()

        // --- Composable ---
        setContent {
            AppTheme {
                var nombre by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {

                    Text("Productos", style = MaterialTheme.typography.titleLarge)
                    ProductListScreen(productos = productosState.value)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Usuarios", style = MaterialTheme.typography.titleLarge)

                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if(nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()){
                                    crearUsuario(nombre, email, password)
                                    nombre = ""
                                    email = ""
                                    password = ""
                                } else {
                                    Toast.makeText(applicationContext, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Crear Usuario")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    UserListScreen(usuarios = usuariosState.value)
                }
            }
        }
    }
}

// --- Composables ---
@Composable
fun ProductListScreen(productos: List<Producto>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(productos) { producto ->
            ProductCard(producto)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ProductCard(producto: Producto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${producto.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Descripción: ${producto.descripcion ?: ""}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun UserListScreen(usuarios: List<Usuario>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(usuarios) { usuario ->
            UserCard(usuario)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun UserCard(usuario: Usuario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${usuario.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Email: ${usuario.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
