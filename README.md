# 🏃‍♂️ FitApp – Actividad Física y Sensores

Aplicación Android desarrollada en Kotlin que permite registrar sesiones de actividad física y utilizar sensores del dispositivo como el acelerómetro y el GPS.

---

## 📱 Funcionalidades

- Registro manual de actividades (Caminar, Correr, Bicicleta).
- Visualización de sesiones registradas mediante un `RecyclerView`.
- Registro de actividad en tiempo real:
    - Detección de movimiento usando el **acelerómetro**.
    - Geolocalización con **GPS**.
    - **Cronómetro** en tiempo real durante la sesión.
- Finalizar sesión guarda automáticamente la actividad con duración y hora.
- Diseño adaptado a dispositivos móviles con interfaz sencilla y clara.

---

## 📸 Preview

<div style="display: flex; gap: 10px;">
  <img src="/Screenshots/Screenshot1.png" alt="Screenshot 1" width="180"/>
  <img src="/Screenshots/Screenshot2.png" alt="Screenshot 2" width="180"/>
    <img src="/Screenshots/Screenshot3.png" alt="Screenshot 3" width="180"/>
    <img src="/Screenshots/Screenshot4.png" alt="Screenshot 4" width="180"/>
    <img src="/Screenshots/Screenshot5.png" alt="Screenshot 5" width="180"/>
</div>

## 🛠️ Tecnologías usadas

- Kotlin
- Android SDK
- RecyclerView + ViewHolder
- Sensores: Acelerómetro, GPS
- Intents y navegación entre pantallas
- Permisos en tiempo de ejecución
- UI con XML

---


## 🚀 Cómo ejecutar

1. Clona el repositorio:

```bash
git clone https://github.com/serxa92/FitApp.git
