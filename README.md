# AgroLab Sinú — Taller de Patrones de Diseño (Proxy + Facade)

**Asignatura:** Programación III  
**Docente:** Mag. Alberto Paternina León  
**Universidad:** Universidad de Córdoba  
**Programa:** Ingeniería de Sistemas  
**Grupo:** 4  

---

## 👥 Integrantes
* **Fabián Andrés Salgado Mosquera**
* **Efrén de Jesús Polo de la Rosa**
* **Juan Guillermo Villalobos Durango**

---

## 📌 Descripción del Proyecto
Este repositorio contiene la implementación práctica en Java para el taller de patrones de diseño de **Programación III**. Modelamos el dominio propio **AgroLab Sinú** (laboratorio de análisis químico y agrológico de suelos) para demostrar y contrastar el par de patrones de diseño **Proxy** y **Facade** bajo el eje:

> **Intermediarios: control del acceso frente a simplificación del acceso.**

El proyecto está dividido en dos grandes paquetes (`proxy` y `facade`), y cada uno cuenta con una **versión inicial** (que demuestra un defecto real de diseño sin el patrón) y una **versión refactorizada** (que corrige el defecto mediante la aplicación formal del patrón).

---

## 📁 Estructura del Proyecto

```text
src/
├── proxy/
│   ├── inicial/
│   │   └── ProxyInicial.java          # Versión sin patrón: muestra lecturas no autorizadas y consultas repetidas costosas.
│   └── refactorizado/
│       └── ProxyRefactorizado.java    # Versión con Proxy: validación por roles, auditoría, caché y lazy initialization.
└── facade/
    ├── inicial/
    │   └── FacadeInicial.java         # Versión sin patrón: acoplamiento con 5 subsistemas y omisión de facturación en la App Móvil.
    └── refactorizado/
        └── FacadeRefactorizado.java   # Versión con Facade: interfaz unificada mediante AgroLabFacade.
uml/
├── proxy.puml                         # Diagrama de clases PlantUML para el patrón Proxy.
└── facade.puml                        # Diagrama de clases PlantUML para el patrón Facade.