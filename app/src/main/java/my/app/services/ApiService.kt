package my.app.services

import my.app.model.Producto
import my.app.model.ProductoRequest
import my.app.model.Usuario
import my.app.model.BoletaRequest
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("productos")
    fun getProductos(): Call<List<Producto>>

    @POST("productos")
    fun crearProducto(@Body producto: ProductoRequest): Call<Producto>

    @DELETE("productos/{id}")
    fun eliminarProducto(@Path("id") id: Long): Call<Void>

    @GET("users")
    fun getUsuarios(): Call<List<Usuario>>

    @POST("users")
    fun crearUsuario(@Body usuario: Usuario): Call<Usuario>

    @DELETE("users/{id}")
    fun eliminarUsuario(@Path("id") id: Long): Call<Void>

    @POST("boletas")
    fun enviarBoleta(@Body boleta: BoletaRequest): Call<Void>
}
