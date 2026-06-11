# 🛸 Rick & Morty — Kotlin Multiplatform App

Aplicación **multiplataforma (Android & iOS)** desarrollada con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform**, que consume la [API pública de Rick & Morty](https://rickandmortyapi.com/). Permite listar personajes con scroll infinito (paginación), consultar el detalle de cada uno y guardar favoritos de forma local con **persistencia offline completa**.

Proyecto diseñado bajo estándares profesionales de **Clean Architecture + MVVM**, orientado a prueba técnica.

---

## 📱 Pantallas Principales

La app consta de **3 pantallas principales**, organizadas en una navegación por pestañas inferior:

### 1. `CharacterScreen` — Lista de Personajes (Datos Remotos)
- Lista paginada de personajes consumida desde la API de Rick & Morty.
- Implementa **Paging 3** para carga infinita optimizada (scroll infinito).
- Durante la carga inicial se muestran **esqueletos de carga** (`CharacterItemSkeleton`) para una experiencia de usuario fluida.
- El usuario puede marcar/desmarcar favoritos directamente desde la lista. El estado visual se actualiza de forma inmediata sin recargar la lista completa, usando un mapa de overrides local en el ViewModel.
- Manejo de estados: `Loading`, `Error` (con botón de reintento) y `Success`.

### 2. `CharacterDetailScreen` — Detalle del Personaje
- Muestra la información completa del personaje seleccionado: imagen a pantalla completa, nombre, estado de vida (con indicador de color dinámico), especie, género y última ubicación conocida.
- Componentes visuales tipo `Card` para cada dato informativo.
- Barra superior con título y botón de navegación hacia atrás.

### 3. `LocalCharacterScreen` — Favoritos (Datos Locales)
- Lista de personajes guardados localmente como favoritos, leída directamente de la base de datos SQLite mediante **SQLDelight**.
- Reactiva en tiempo real: cualquier cambio en favoritos se refleja automáticamente.
- Al quitar un favorito, el ítem se elimina con una animación fluida de salida (`animateItem()` con fade + shrink).
- Muestra un mensaje vacío cuando no hay personajes guardados.

---

## 🏗️ Arquitectura

El proyecto sigue **Clean Architecture** con el patrón de presentación **MVVM**, dividido en 3 capas bien diferenciadas dentro del módulo compartido (`:shared`):

```
┌─────────────────────────────────────────────┐
│           Presentation Layer                │
│  Compose UI · ViewModels · StateFlow        │
├─────────────────────────────────────────────┤
│              Domain Layer                   │
│  Use Cases · Models · Repository Interfaces │
├─────────────────────────────────────────────┤
│               Data Layer                    │
│  Ktor (Remote) · SQLDelight (Local)         │
│  Repository Implementations · Mappers       │
└─────────────────────────────────────────────┘
```

- **Domain Layer**: Modelos de negocio puros (`Character`) e interfaces de repositorio. No depende de ningún framework.
- **Data Layer**: Implementa las interfaces del dominio. Coordina la fuente remota (Ktor) con la caché local (SQLDelight) y expone los datos mediante `Flow`.
- **Presentation Layer**: ViewModels de KMP que exponen `StateFlow` de estado de UI y `SharedFlow` para efectos secundarios (side effects), consumidos desde las pantallas Compose.

---

## 🛠️ Stack Tecnológico

| Librería | Versión | Uso |
|---|---|---|
| **Kotlin Multiplatform** | — | Lógica y UI compartida entre Android e iOS |
| **Compose Multiplatform** | — | Interfaz declarativa multiplataforma |
| **Koin** | Core + Compose + ViewModel | Inyección de dependencias |
| **Ktor Client** | OkHttp (Android) / Darwin (iOS) | Consumo de API REST |
| **SQLDelight** | Android + Native driver | Persistencia local multiplataforma |
| **Paging 3 (KMP)** | — | Scroll infinito paginado |
| **Coil 3** | — | Carga y caché de imágenes |
| **Kotlinx Serialization** | — | Deserialización de respuestas JSON |
| **Androidx Navigation** | — | Navegación entre pantallas |

---

## 📂 Estructura del Proyecto

```
📦 RickAndMortyTestM
 ├── 📁 androidApp/          # Entry point de Android (Activity)
 ├── 📁 iosApp/              # Entry point de iOS (Xcode project)
 └── 📁 shared/              # Módulo compartido KMP
     └── src/
         ├── commonMain/
         │   └── kotlin/
         │       └── org/toch/rickmortytest/
         │           ├── data/           # Repositorios, mappers, fuentes de datos
         │           ├── di/             # Módulos de Koin (DI)
         │           ├── domain/         # Use Cases, Modelos, Interfaces
         │           └── presentation/   # Screens, ViewModels, Componentes
         └── commonMain/sqldelight/      # Esquema SQLDelight (.sq)
```

---

## 🗄️ Base de Datos Local (SQLDelight)

La persistencia offline se gestiona con SQLDelight sobre SQLite, usando la tabla `CharacterEntity`:

```sql
CREATE TABLE CharacterEntity (
    id          INTEGER PRIMARY KEY,
    page        INTEGER NOT NULL,
    name        TEXT NOT NULL,
    status      TEXT NOT NULL,
    species     TEXT NOT NULL,
    gender      TEXT NOT NULL,
    location    TEXT NOT NULL,
    type        TEXT NOT NULL,
    imageUrl    TEXT NOT NULL,
    isFavorite  INTEGER NOT NULL DEFAULT 0
);
```

El driver es multiplataforma: `AndroidSqliteDriver` en Android y `NativeSqliteDriver` en iOS, ambos inyectados mediante Koin.

---

## 🚀 Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener:

- **JDK 17+** (Azul Zulu, Temurin, etc.)
- **Android Studio** Koala / Ladybug o superior, con el plugin **Kotlin Multiplatform** instalado.
- **Xcode 15+** (solo para compilar en iOS, requiere Mac).

---

## ▶️ Cómo Ejecutar

### Android

Ensamblar APK de debug:
```bash
./gradlew :androidApp:assembleDebug
```

Instalar directamente en emulador o dispositivo conectado:
```bash
./gradlew :androidApp:installDebug
```

También puedes usar la configuración de ejecución `androidApp` directamente desde Android Studio.

### iOS

1. Abre el directorio `/iosApp` en **Xcode**.
2. Selecciona el simulador o dispositivo de destino.
3. Presiona **Run** (`Cmd + R`).

> El framework compartido (`Shared.framework`) se genera automáticamente durante la compilación de Xcode mediante el script de Gradle integrado.

---

## 🧪 Pruebas

El proyecto incluye pruebas unitarias de lógica de negocio, integración con Koin y pruebas de interfaz de usuario con captura de pantalla (**Screenshot Testing**) usando **Roborazzi**.

```bash
# Pruebas en Android (Robolectric + Roborazzi)
./gradlew :shared:testAndroidHostTest

# Pruebas en iOS Simulator
./gradlew :shared:iosSimulatorArm64Test

# Pruebas de Desktop (JVM) -- Esto ayuda con las pruebas de UI en multiplatform
./gradlew :shared:desktopTest
```

---

## 🔗 API

Este proyecto consume la [Rick and Morty API](https://rickandmortyapi.com/), una API REST gratuita y pública. No requiere ninguna `API Key` ni configuración adicional de autenticación.

---

*Desarrollado como prueba técnica de Kotlin Multiplatform.*
