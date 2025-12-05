📱 App Android – Proyecto Mobile

Este repositorio contiene una aplicación móvil desarrollada en Android Studio, creada con Kotlin y siguiendo una estructura moderna basada en actividades, adapters, repositories y consumo de API mediante Retrofit.

La app permite visualizar productos, ver detalles, cargar imágenes desde GitHub/URL y consumir una API externa desplegada en la nube.

🚀 Tecnologías utilizadas

Kotlin

Android Studio

MVVM / Repositorios

RecyclerView

Glide (carga de imágenes)

Retrofit (consumo de API REST)

Material Design Components

📂 Estructura del proyecto
.
├── app/                        # Código principal de la aplicación
│   ├── src/main/java           # Activities, adapters, viewmodels, repos
│   ├── src/main/res            # Layouts, drawables, icons
│   └── AndroidManifest.xml
├── gradle/                     # Configuración del wrapper de Gradle
├── .idea/                      # Configuración del IDE
├── build.gradle.kts            # Dependencias del proyecto
├── settings.gradle.kts         # Configuración de módulos
└── .gitignore                  # Archivos ignorados por Git

🌐 Conexión con API externa

La app consume una API propia desarrollada en Java + Spring Boot, desplegada en Railway y conectada a Neon Tech.

Esto permite:

Obtener productos desde la nube

Cargar imágenes desde URLs

Mezclar productos locales + productos remotos

Si deseas, puedo agregar aquí mismo los endpoints exactos.

🖼️ Manejo de imágenes

La app maneja imágenes de dos formas:

Productos locales → Imágenes dentro de drawable/

Productos nuevos → URL alojada en GitHub RAW u otro servidor

Todo esto se realiza mediante Glide.
