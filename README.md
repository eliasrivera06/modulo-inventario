# Promart - Módulo de Inventarios (Demo UI)

Esta es una interfaz web sencilla, centrada y responsiva para gestionar entidades del módulo de inventarios, alineada con el esquema SQL proporcionado.

## Contenido del proyecto
- `index.html`: Estructura principal, pestañas para entidades y formularios/tabla por sección.
- `styles.css`: Estilos modernos, layout centrado y responsivo.
- `app.js`: Lógica de pestañas, CRUD en memoria, y placeholders para integrar un backend.

## Entidades cubiertas
Mapeo con las tablas del esquema:
- `producto (id_producto, nombre, descripcion, categoria, precio, unidad_medida)`
- `sucursal (id_sucursal, nombre, direccion, tipo)`
- `inventario (id_inventario, id_producto, id_sucursal, cantidad, ubicacion, estado)`
- `proveedor (id_proveedor, nombre, tipo, tiempo_entrega)`
- `despacho (id_despacho, id_producto, id_proveedor, fecha_despacho, medio, estado)`
- `usuario (id_usuario, nombre)`

Los formularios en `index.html` reflejan estos campos. `app.js` realiza operaciones CRUD en memoria con IDs autoincrementales simulados.

## Cómo ejecutar
1. Abre `index.html` en tu navegador.
2. Explora las pestañas y crea/edita/elimina registros. Los datos se mantienen solo en memoria (se pierden al recargar).

## Integración con Backend (API REST)
`app.js` define `API_BASE_URL` (por defecto `null`). Para conectar con tu backend:
1. Cambia en `app.js`:
   ```js
   const API_BASE_URL = 'http://localhost:3000/api';
   ```
2. Implementa endpoints REST por entidad (ejemplo):
   - `GET /producto` → lista de productos
   - `POST /producto` → crea producto
   - `PUT /producto/:id` → actualiza producto
   - `DELETE /producto/:id` → elimina producto

   Repite para `sucursal`, `inventario`, `proveedor`, `despacho`, `usuario`.

3. En las funciones `upsert(...)` y `removeById(...)` puedes integrar `apiFetch(...)` con:
   - Crear: `POST /entidad`
   - Actualizar: `PUT /entidad/:id`
   - Eliminar: `DELETE /entidad/:id`
   - Cargar datos: en `seed()` reemplazar por `GET /entidad` e hidratar `state`.

4. Consideraciones con claves foráneas:
   - `inventario.id_producto` y `inventario.id_sucursal` deben referenciar IDs válidos.
   - `despacho.id_producto` y `despacho.id_proveedor` también.
   - Antes de guardar, el backend debe validar existencia y restricciones (`CHECK`).

## Validaciones y checks del esquema
- `sucursal.tipo ∈ {tienda, CD}`
- `inventario.estado ∈ {exhibido, almacenado, en_transito}`
- `proveedor.tipo ∈ {directo, seller, marketplace}`
- `despacho.medio ∈ {proveedor, seller, CD}`
- `despacho.estado ∈ {pendiente, enviado, entregado}`

Los selects en los formularios ya respetan estas opciones.

## Personalización rápida
- Cambia estilos en `styles.css`.
- Ajusta columnas visibles en tablas desde `index.html`.
- Añade filtros/búsquedas por entidad en `app.js` (por ejemplo, sobre los `state.<entidad>` antes de renderizar).

## Notas
- Esta UI es un punto de partida. No incluye autenticación, paginación ni manejo de errores de red. Puedes ampliarlo según tus necesidades.
