# Arte Dulce y Masas 🥐🍰

Aplicación de comercio electrónico profesional para una repostería artesanal. Este proyecto gestiona un flujo completo de venta, desde el catálogo dinámico hasta el cierre del pedido vía WhatsApp.

## 🚀 Funcionalidades Principales (Valor de Negocio)
* **Carrito de Compras Inteligente:** Gestión de sesiones (`HttpSession`) con recálculo automático de totales y opción de vaciado.
* **Lógica Multimoneda:** Conversión automática de USD a Bolívares (VES) integrando tasas de cambio en tiempo real.
* **Checkout con WhatsApp:** Generación de mensajes estructurados automáticos para enviar el pedido al vendedor con un solo clic.
* **Panel Administrativo:** Gestión completa (CRUD) de productos, categorías y precios con acceso protegido.
* **Filtros de Catálogo:** Navegación fluida por categorías para mejorar la experiencia del usuario (UX).

## 🛠️ Stack Tecnológico
* **Backend:** Java con **Spring Boot** (Spring Web, Data JPA, Security).
* **Base de Datos:** **MySQL** con un modelo relacional optimizado para ventas.
* **Frontend:** **Thymeleaf**, HTML5, y CSS3 (Diseño responsivo).
* **Productividad:** **Lombok** y Spring Boot DevTools.

## 📊 Estructura de Datos
La aplicación maneja una base de datos relacional con:
* `productos`: Catálogo, precios e imágenes.
* `ventas`: Histórico detallado de transacciones, métodos de pago y totales.

---
*Desarrollado como parte de mi portafolio profesional para soluciones de E-commerce.*
