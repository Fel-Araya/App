🛹 App Skate Shop – Proyecto Mobile (Actualizado)
Este repositorio contiene una aplicación móvil profesional para una tienda de skate, desarrollada en Android Studio con Kotlin. La arquitectura ha sido evolucionada para garantizar que la API sea la única fuente de verdad, permitiendo una gestión dinámica de inventario.

🚀 Nuevas Funcionalidades Implementadas
Sincronización API Real-Time: La app ya no depende de listas estáticas locales; se sincroniza automáticamente con el servidor cada vez que la vista de productos se vuelve visible (onResume).

Panel de Administración (Editor de Producto): Interfaz completa para crear y editar productos con validaciones de campos obligatorios.

Selector de Categorías (Spinner): Implementación de un sistema de categorías fijas (Tablas, Ruedas, Ejes, Poleras) para mantener la integridad de los datos en la base de datos.

Interfaz de Usuario Limpia: Eliminación de indicadores visuales innecesarios (dots del carrusel) y optimización de iconos (Skate & Menú Hamburguesa).

Gestión de Ofertas: Sistema de filtrado dinámico para destacar productos con precios rebajados.

🛠️ Stack Tecnológico
Lenguaje: Kotlin.

Network: Retrofit 2 + GSON para consumo de API REST.

Imágenes: Glide para renderizado de URLs externas y recursos locales.

UI/UX: Material Design 3, ViewPager2 (Carrusel), RecyclerView (Grilla 2x2) y TabLayout.

Procesamiento: KAPT (Kotlin Annotation Processing Tool).

📂 Estructura del Proyecto Actualizada
Plaintext

├── app/
│   ├── src/main/java/my/app/
│   │   ├── activities/     # Home, Productos, EditarProducto (Admin), Ofertas
│   │   ├── adapters/       # ImageCarousel, ProductoAdapter, CategoryAdapter
│   │   ├── data/           # ProductoRepository (Caché sincronizado con API)
│   │   ├── model/          # Data Classes: Producto, ProductoRequest, Category
│   │   └── services/       # RetrofitClient e interfaces de API
│   ├── src/main/res/
│   │   ├── layout/         # activity_home, activity_producto_editor, etc.
│   │   └── drawable/       # Iconos vectoriales personalizados (Skate, Menu)
│   └── AndroidManifest.xml # Permisos de Internet y declaración de Actividades
🌐 Conexión con la API
La aplicación está configurada para trabajar exclusivamente con un entorno de nube (Railway + Neon Tech).

GET /productos: Recupera el catálogo completo (16 productos base + nuevos ingresos).

POST /productos: Permite al administrador subir nuevos productos sin necesidad de asignar IDs manualmente.

PUT /productos/{id}: Actualización de información de productos existentes.

🔧 Notas de Compilación y Solución de Errores
Para generar la APK correctamente tras las últimas actualizaciones de dependencias:

Limpieza: Ejecutar Build > Clean Project.

Caché: File > Invalidate Caches / Restart si persisten errores de NonExistentClass.

OneDrive: Se recomienda trabajar el proyecto fuera de carpetas sincronizadas para evitar bloqueos de archivos por parte de Gradle.
