/*
  Promart - Módulo de Inventarios (demo UI)
  - Tabs de navegación
  - Mock data en memoria (CRUD básico)
  - Placeholders para integrar API REST (API_BASE_URL)
*/

const API_BASE_URL = null; // Ej: 'http://localhost:3000/api' (cámbialo al conectar backend)

const state = {
  producto: [],
  sucursal: [],
  inventario: [],
  proveedor: [],
  despacho: [],
  usuario: [],
};

// Mock IDs autoincrementales
const idCounters = {
  producto: 1,
  sucursal: 1,
  inventario: 1,
  proveedor: 1,
  despacho: 1,
  usuario: 1,
};

// Helpers ----------------------------------------------------
const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

function setYear() {
  const y = new Date().getFullYear();
  const el = document.getElementById('year');
  if (el) el.textContent = y;
}

function switchTab(id) {
  $$('.tab').forEach(btn => btn.classList.toggle('active', btn.dataset.tab === id));
  $$('.panel').forEach(p => p.classList.toggle('active', p.id === id));
}

function serializeForm(form) {
  const data = new FormData(form);
  return Object.fromEntries(data.entries());
}

function clearForm(form) {
  form.reset();
  // Asegurar que hidden ids queden vacíos
  $$('input[type="hidden"]', form).forEach(i => (i.value = ''));
}

// Rendering tablas ------------------------------------------
function renderProducto() {
  const tbody = $('#table-producto tbody');
  tbody.innerHTML = state.producto
    .map(
      p => `<tr>
        <td>${p.id_producto}</td>
        <td>${p.nombre ?? ''}</td>
        <td>${p.categoria ?? ''}</td>
        <td>${p.precio ?? ''}</td>
        <td>${p.unidad_medida ?? ''}</td>
        <td>
          <button class="ghost" data-entity="producto" data-id="${p.id_producto}" data-action="edit">Editar</button>
          <button class="danger" data-entity="producto" data-id="${p.id_producto}" data-action="delete">Eliminar</button>
        </td>
      </tr>`
    )
    .join('');
}

function renderSucursal() {
  const tbody = $('#table-sucursal tbody');
  tbody.innerHTML = state.sucursal
    .map(
      s => `<tr>
        <td>${s.id_sucursal}</td>
        <td>${s.nombre ?? ''}</td>
        <td>${s.direccion ?? ''}</td>
        <td>${s.tipo ?? ''}</td>
        <td>
          <button class="ghost" data-entity="sucursal" data-id="${s.id_sucursal}" data-action="edit">Editar</button>
          <button class="danger" data-entity="sucursal" data-id="${s.id_sucursal}" data-action="delete">Eliminar</button>
        </td>
      </tr>`
    )
    .join('');
}

function renderInventario() {
  // Sincronizar selects dependientes
  const selProd = $('#form-inventario select[name="id_producto"]');
  const selSuc = $('#form-inventario select[name="id_sucursal"]');
  selProd.innerHTML = '<option value="">Seleccione...</option>' +
    state.producto.map(p => `<option value="${p.id_producto}">${p.nombre}</option>`).join('');
  selSuc.innerHTML = '<option value="">Seleccione...</option>' +
    state.sucursal.map(s => `<option value="${s.id_sucursal}">${s.nombre}</option>`).join('');

  const tbody = $('#table-inventario tbody');
  tbody.innerHTML = state.inventario
    .map(inv => {
      const prod = state.producto.find(p => p.id_producto == inv.id_producto);
      const suc = state.sucursal.find(s => s.id_sucursal == inv.id_sucursal);
      return `<tr>
        <td>${inv.id_inventario}</td>
        <td>${prod?.nombre ?? inv.id_producto}</td>
        <td>${suc?.nombre ?? inv.id_sucursal}</td>
        <td>${inv.cantidad ?? ''}</td>
        <td>${inv.ubicacion ?? ''}</td>
        <td>${inv.estado ?? ''}</td>
        <td>
          <button class="ghost" data-entity="inventario" data-id="${inv.id_inventario}" data-action="edit">Editar</button>
          <button class="danger" data-entity="inventario" data-id="${inv.id_inventario}" data-action="delete">Eliminar</button>
        </td>
      </tr>`;
    })
    .join('');
}

function renderProveedor() {
  const tbody = $('#table-proveedor tbody');
  tbody.innerHTML = state.proveedor
    .map(
      pr => `<tr>
        <td>${pr.id_proveedor}</td>
        <td>${pr.nombre ?? ''}</td>
        <td>${pr.tipo ?? ''}</td>
        <td>${pr.tiempo_entrega ?? ''}</td>
        <td>
          <button class="ghost" data-entity="proveedor" data-id="${pr.id_proveedor}" data-action="edit">Editar</button>
          <button class="danger" data-entity="proveedor" data-id="${pr.id_proveedor}" data-action="delete">Eliminar</button>
        </td>
      </tr>`
    )
    .join('');
}

function renderDespacho() {
  // selects dependientes
  const selProd = $('#form-despacho select[name="id_producto"]');
  const selProv = $('#form-despacho select[name="id_proveedor"]');
  selProd.innerHTML = '<option value="">Seleccione...</option>' +
    state.producto.map(p => `<option value="${p.id_producto}">${p.nombre}</option>`).join('');
  selProv.innerHTML = '<option value="">Seleccione...</option>' +
    state.proveedor.map(pr => `<option value="${pr.id_proveedor}">${pr.nombre}</option>`).join('');

  const tbody = $('#table-despacho tbody');
  tbody.innerHTML = state.despacho
    .map(d => {
      const prod = state.producto.find(p => p.id_producto == d.id_producto);
      const prov = state.proveedor.find(p => p.id_proveedor == d.id_proveedor);
      return `<tr>
        <td>${d.id_despacho}</td>
        <td>${prod?.nombre ?? d.id_producto}</td>
        <td>${prov?.nombre ?? d.id_proveedor}</td>
        <td>${d.fecha_despacho ?? ''}</td>
        <td>${d.medio ?? ''}</td>
        <td>${d.estado ?? ''}</td>
        <td>
          <button class="ghost" data-entity="despacho" data-id="${d.id_despacho}" data-action="edit">Editar</button>
          <button class="danger" data-entity="despacho" data-id="${d.id_despacho}" data-action="delete">Eliminar</button>
        </td>
      </tr>`;
    })
    .join('');
}

function renderUsuario() {
  const tbody = $('#table-usuario tbody');
  tbody.innerHTML = state.usuario
    .map(
      u => `<tr>
        <td>${u.id_usuario}</td>
        <td>${u.nombre ?? ''}</td>
        <td>
          <button class="ghost" data-entity="usuario" data-id="${u.id_usuario}" data-action="edit">Editar</button>
          <button class="danger" data-entity="usuario" data-id="${u.id_usuario}" data-action="delete">Eliminar</button>
        </td>
      </tr>`
    )
    .join('');
}

// CRUD genérico en memoria ----------------------------------
function upsert(entity, payload, idField) {
  const list = state[entity];
  if (!payload[idField]) {
    // create
    const newId = idCounters[entity]++;
    payload[idField] = newId;
    list.push(payload);
  } else {
    // update
    const idx = list.findIndex(x => x[idField] == payload[idField]);
    if (idx !== -1) list[idx] = { ...list[idx], ...payload };
  }
}

function removeById(entity, id, idField) {
  const list = state[entity];
  const idx = list.findIndex(x => x[idField] == id);
  if (idx !== -1) list.splice(idx, 1);
}

// Cargar data inicial de ejemplo -----------------------------
function seed() {
  // Productos
  upsert('producto', { nombre: 'Taladro Percutor', descripcion: '800W, 13mm', categoria: 'Herramientas', precio: 249.90, unidad_medida: 'unidad' }, 'id_producto');
  upsert('producto', { nombre: 'Pintura Látex 1G', descripcion: 'Blanco mate', categoria: 'Pinturas', precio: 59.90, unidad_medida: 'galón' }, 'id_producto');
  // Sucursales
  upsert('sucursal', { nombre: 'Promart San Isidro', direccion: 'Av. Ejemplo 123', tipo: 'tienda' }, 'id_sucursal');
  upsert('sucursal', { nombre: 'CD Villa El Salvador', direccion: 'Parque Industrial', tipo: 'CD' }, 'id_sucursal');
  // Proveedores
  upsert('proveedor', { nombre: 'Makita Perú', tipo: 'directo', tiempo_entrega: 7 }, 'id_proveedor');
  upsert('proveedor', { nombre: 'Pinturas Andinas', tipo: 'seller', tiempo_entrega: 5 }, 'id_proveedor');
  // Usuarios
  upsert('usuario', { nombre: 'Operador Inventarios' }, 'id_usuario');
  // Inventario
  upsert('inventario', { id_producto: 1, id_sucursal: 1, cantidad: 25, ubicacion: 'Pasillo H1', estado: 'exhibido' }, 'id_inventario');
  upsert('inventario', { id_producto: 2, id_sucursal: 2, cantidad: 120, ubicacion: 'Rack A-12', estado: 'almacenado' }, 'id_inventario');
  // Despacho
  upsert('despacho', { id_producto: 1, id_proveedor: 1, fecha_despacho: '2025-09-15', medio: 'proveedor', estado: 'enviado' }, 'id_despacho');
}

// Bindings de formularios -----------------------------------
function bindForms() {
  // Producto
  $('#form-producto').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    if (data.precio) data.precio = Number(data.precio);
    upsert('producto', data, 'id_producto');
    renderProducto();
    renderInventario(); // refrescar selects dependientes
    clearForm(e.target);
  });

  // Sucursal
  $('#form-sucursal').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    upsert('sucursal', data, 'id_sucursal');
    renderSucursal();
    renderInventario();
    clearForm(e.target);
  });

  // Inventario
  $('#form-inventario').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    if (data.cantidad) data.cantidad = Number(data.cantidad);
    upsert('inventario', data, 'id_inventario');
    renderInventario();
    clearForm(e.target);
  });

  // Proveedor
  $('#form-proveedor').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    if (data.tiempo_entrega) data.tiempo_entrega = Number(data.tiempo_entrega);
    upsert('proveedor', data, 'id_proveedor');
    renderProveedor();
    renderDespacho();
    clearForm(e.target);
  });

  // Despacho
  $('#form-despacho').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    upsert('despacho', data, 'id_despacho');
    renderDespacho();
    clearForm(e.target);
  });

  // Usuario
  $('#form-usuario').addEventListener('submit', e => {
    e.preventDefault();
    const data = serializeForm(e.target);
    upsert('usuario', data, 'id_usuario');
    renderUsuario();
    clearForm(e.target);
  });

  // Botones limpiar explícitos
  $$('button[data-action="clear"]').forEach(btn => {
    btn.addEventListener('click', e => {
      const form = e.target.closest('form');
      if (form) clearForm(form);
    });
  });
}

// Delegación para editar / eliminar --------------------------
function bindTables() {
  document.body.addEventListener('click', e => {
    const btn = e.target.closest('button[data-action]');
    if (!btn) return;
    const entity = btn.getAttribute('data-entity');
    const id = btn.getAttribute('data-id');
    const action = btn.getAttribute('data-action');

    if (!entity || !id || !action) return;

    const idField = {
      producto: 'id_producto',
      sucursal: 'id_sucursal',
      inventario: 'id_inventario',
      proveedor: 'id_proveedor',
      despacho: 'id_despacho',
      usuario: 'id_usuario',
    }[entity];

    if (action === 'delete') {
      if (confirm('¿Eliminar registro?')) {
        removeById(entity, id, idField);
        rerender(entity);
      }
      return;
    }

    if (action === 'edit') {
      const list = state[entity];
      const row = list.find(x => x[idField] == id);
      if (!row) return;
      const form = document.getElementById(`form-${entity}`);
      if (!form) return;
      // Poner valores en el form
      Object.entries(row).forEach(([k, v]) => {
        const input = form.querySelector(`[name="${k}"]`);
        if (input) input.value = v;
      });
      // Cambiar a la pestaña correspondiente
      switchTab(entity);
    }
  });
}

function rerender(entity) {
  switch (entity) {
    case 'producto':
      renderProducto();
      renderInventario();
      renderDespacho();
      break;
    case 'sucursal':
      renderSucursal();
      renderInventario();
      break;
    case 'inventario':
      renderInventario();
      break;
    case 'proveedor':
      renderProveedor();
      renderDespacho();
      break;
    case 'despacho':
      renderDespacho();
      break;
    case 'usuario':
      renderUsuario();
      break;
  }
}

// Navegación de tabs ----------------------------------------
function bindTabs() {
  $$('.tab').forEach(btn => {
    btn.addEventListener('click', () => switchTab(btn.dataset.tab));
  });
}

// Placeholders para backend ---------------------------------
async function apiFetch(path, options = {}) {
  if (!API_BASE_URL) return null; // sin backend, no hacer nada
  const res = await fetch(`${API_BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) throw new Error('Error API');
  return await res.json();
}

// Init -------------------------------------------------------
document.addEventListener('DOMContentLoaded', () => {
  setYear();
  bindTabs();
  bindForms();
  bindTables();
  seed();
  renderProducto();
  renderSucursal();
  renderInventario();
  renderProveedor();
  renderDespacho();
  renderUsuario();
});
