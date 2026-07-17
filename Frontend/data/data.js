/* ============================================================
   Shop_With_Sahil — API Client (matches online-shopping-cart backend exactly)
   Base URL points at Eclipse/Tomcat; frontend is served separately
   via VS Code Live Server, so every call needs credentials:'include'.
   ============================================================ */

const API_BASE = 'http://localhost:8080/online-shopping-cart/api';

async function apiRequest(path, options = {}) {
  try {
    const res = await fetch(`${API_BASE}${path}`, {
      credentials: 'include',
      headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
      ...options
    });
    const isJson = (res.headers.get('content-type') || '').includes('application/json');
    const body = isJson ? await res.json().catch(() => null) : null;

    if (!res.ok) {
      const msg = typeof body === 'string' ? body : (body?.message || `Request failed (${res.status})`);
      throw new Error(msg);
    }
    return body;
  } catch (e) {
    showToast(e.message || 'Something went wrong. Please try again.');
    throw e;
  }
}

/* ---------- Typed wrappers matching exact servlet endpoints ---------- */
const api = {
  // Users
  register: (data) => apiRequest('/users/register', { method: 'POST', body: JSON.stringify(data) }),
  login: (data) => apiRequest('/users/login', { method: 'POST', body: JSON.stringify(data) }),
  logout: () => apiRequest('/users/logout', { method: 'POST' }),
  me: () => apiRequest('/users/me'),

  // Products
  getProducts: () => apiRequest('/products'),
  getProduct: (id) => apiRequest(`/products?id=${id}`),
  addProduct: (p) => apiRequest('/products', { method: 'POST', body: JSON.stringify(p) }),
  updateProduct: (p) => apiRequest('/products', { method: 'PUT', body: JSON.stringify(p) }),
  deleteProduct: (id) => apiRequest(`/products?id=${id}`, { method: 'DELETE' }),

  // Cart (cartId is the row id; productId is the product)
  getCart: () => apiRequest('/cart'),
  addToCart: (productId, quantity) => apiRequest('/cart', { method: 'POST', body: JSON.stringify({ productId, quantity }) }),
  updateCartQty: (cartId, quantity) => apiRequest('/cart', { method: 'PUT', body: JSON.stringify({ id: cartId, quantity }) }),
  removeFromCart: (cartId) => apiRequest(`/cart?id=${cartId}`, { method: 'DELETE' }),

  // Orders
  placeOrder: (shippingAddress, paymentMethod) => apiRequest('/orders', { method: 'POST', body: JSON.stringify({ shippingAddress, paymentMethod }) }),
  getMyOrders: () => apiRequest('/orders'),
  getOrderDetails: (id) => apiRequest(`/orders/${id}`),

  

  // Admin
  getAllOrdersAdmin: () => apiRequest('/admin/orders'),
  updateOrderStatus: (id, status) => apiRequest(`/admin/orders/${id}`, { method: 'PUT', body: JSON.stringify({ status }) }),
  getAllUsersAdmin: () => apiRequest('/admin/users'),
  updateUserStatus: (id, status) => apiRequest(`/admin/users/${id}`, { method: 'PUT', body: JSON.stringify({ status }) }),
};

/* ---------- Categories (derived client-side from products, no backend table) ---------- */
function deriveCategories(products) {
  const counts = {};
  products.forEach(p => { counts[p.category] = (counts[p.category] || 0) + 1; });
  const icons = { Electronics: 'bi-cpu', Fashion: 'bi-bag-heart', Home: 'bi-house-heart' };
  return Object.keys(counts).map(name => ({ name, count: counts[name], icon: icons[name] || 'bi-grid' }));
}

/* ---------- Helpers to normalize backend Product into card-friendly shape ---------- */
function normalizeProduct(p) {
    return {
        id: p.id,
        name: p.name,
        description: p.description,
        category: p.category,
        price: Number(p.price),
        mrp: p.mrp,
        qty: p.quantity,
        img: p.imageUrl
            ? p.imageUrl
            : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&q=80"
    };
}
