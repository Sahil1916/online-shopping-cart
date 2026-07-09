/* ============================================================
   ShopVerse — Core JS (shared across all pages)
   Cart is backend-backed (session-based). Wishlist stays client-side
   (localStorage) since the backend has no wishlist table/endpoint.
   ============================================================ */

/* ---------- Cart (backend-backed) ---------- */
const Cart = {
  _items: [], // [{cartId, productId, productName, price, imageUrl, quantity}]

  async load(){
    try { this._items = await api.getCart() || []; }
    catch { this._items = []; }
    this.updateBadge();
    return this._items;
  },
  async add(productId, qty = 1){
    await api.addToCart(productId, qty);
    await this.load();
    showToast('Added to cart');
  },
  async updateQty(cartId, qty){
    if (qty < 1) return this.remove(cartId);
    await api.updateCartQty(cartId, qty);
    await this.load();
  },
  async remove(cartId){
    await api.removeFromCart(cartId);
    await this.load();
    showToast('Item removed from cart');
  },
  count(){ return this._items.reduce((s,i) => s + i.quantity, 0); },
  total(){ return this._items.reduce((s,i) => s + i.quantity * i.price, 0); },
  updateBadge(){
    document.querySelectorAll('.js-cart-count').forEach(el => el.textContent = this.count());
  }
};

/* ---------- Wishlist (client-only — no backend support) ---------- */
const Wishlist = {
  KEY: 'shopverse_wishlist',
  get(){ return JSON.parse(localStorage.getItem(this.KEY) || '[]'); },
  save(items){ localStorage.setItem(this.KEY, JSON.stringify(items)); this.updateBadge(); },
  has(id){ return this.get().some(i => i.id === id); },
  toggle(product){
    const items = this.get();
    const idx = items.findIndex(i => i.id === product.id);
    if (idx > -1){
      items.splice(idx, 1);
      this.save(items);
      showToast(`${product.name} removed from wishlist`);
      return false;
    } else {
      items.push({ id: product.id, name: product.name, price: product.price, mrp: product.mrp, img: product.img, category: product.category, qty: product.qty });
      this.save(items);
      showToast(`${product.name} added to wishlist`);
      return true;
    }
  },
  remove(id){ this.save(this.get().filter(i => i.id !== id)); },
  clear(){ this.save([]); },
  count(){ return this.get().length; },
  updateBadge(){
    document.querySelectorAll('.js-wishlist-count').forEach(el => el.textContent = this.count());
  }
};

/* ---------- Current user / role helpers ---------- */
const Auth = {
  _user: null,
  async getCurrentUser(){
    if (this._user) return this._user;
    try { this._user = await api.me(); } catch { this._user = null; }
    return this._user;
  },
  // Redirects away if not logged in / wrong role. Call at top of protected pages.
  async requireRole(role, loginPage){
    const user = await this.getCurrentUser();
    if (!user || user.role !== role){
      window.location.href = loginPage;
      return null;
    }
    return user;
  }
};

/* ---------- Toast ---------- */
function showToast(msg){
  let toast = document.querySelector('.toast-shop');
  if (!toast){
    toast = document.createElement('div');
    toast.className = 'toast-shop';
    toast.innerHTML = `<span class="ico"><i class="bi bi-check-lg"></i></span><span class="js-toast-msg"></span>`;
    document.body.appendChild(toast);
  }
  toast.querySelector('.js-toast-msg').textContent = msg;
  toast.classList.add('show');
  clearTimeout(toast._t);
  toast._t = setTimeout(() => toast.classList.remove('show'), 2600);
}

/* ---------- Page Loader ---------- */
window.addEventListener('load', () => {
  const loader = document.querySelector('.page-loader');
  if (loader) setTimeout(() => loader.classList.add('hide'), 350);
});

/* ---------- Navbar scroll effect ---------- */
function initNavScroll(){
  const nav = document.querySelector('.navbar-shop');
  if (!nav) return;
  window.addEventListener('scroll', () => {
    nav.classList.toggle('scrolled', window.scrollY > 30);
  });
}

/* ---------- Back to top ---------- */
function initBackToTop(){
  const btn = document.querySelector('.back-to-top');
  if (!btn) return;
  window.addEventListener('scroll', () => btn.classList.toggle('show', window.scrollY > 400));
  btn.addEventListener('click', () => window.scrollTo({ top: 0, behavior: 'smooth' }));
}

/* ---------- Scroll Reveal ---------- */
function initReveal(){
  const els = document.querySelectorAll('.reveal');
  const obs = new IntersectionObserver((entries) => {
    entries.forEach(e => { if (e.isIntersecting){ e.target.classList.add('in'); obs.unobserve(e.target); } });
  }, { threshold: 0.15 });
  els.forEach((el,i) => { el.style.transitionDelay = (i % 4) * 0.08 + 's'; obs.observe(el); });
}

/* ---------- Mobile menu close-on-click ---------- */
function initNavCollapse(){
  document.querySelectorAll('.navbar-collapse .nav-link').forEach(link => {
    link.addEventListener('click', () => {
      const nav = document.querySelector('.navbar-collapse');
      if (nav?.classList.contains('show')) bootstrap.Collapse.getInstance(nav)?.hide();
    });
  });
}

/* ---------- Product Card Template (matches real backend fields only) ---------- */
function productCardHtml(p){
  const off = p.mrp ? Math.round((1 - p.price / p.mrp) * 100) : 0;
  const inWishlist = Wishlist.has(p.id);
  return `
  <div class="col-6 col-md-4 col-lg-3 reveal">
    <div class="product-card">
      <a href="product-details.html?id=${p.id}" class="product-img-wrap d-block">
        <img src="${p.img}" alt="${p.name}" loading="lazy">
      </a>
      <button class="wishlist-btn ${inWishlist ? 'is-active' : ''}" title="${inWishlist ? 'Remove from wishlist' : 'Add to wishlist'}" onclick='event.preventDefault(); toggleWishlistBtn(this, ${JSON.stringify(p).replace(/'/g,"&apos;")})'><i class="bi ${inWishlist ? 'bi-heart-fill' : 'bi-heart'}"></i></button>
      <div class="p-3">
        <div class="product-cat">${p.category}</div>
        <a href="product-details.html?id=${p.id}"><div class="product-name">${p.name}</div></a>
        <div class="d-flex align-items-center gap-2 flex-wrap mt-1">
          <span class="price-now">₹${p.price.toLocaleString('en-IN')}</span>
          ${p.mrp ? `<span class="price-mrp">₹${p.mrp.toLocaleString('en-IN')}</span><span class="price-off">${off}% off</span>` : ''}
        </div>
        <div class="qty-left mt-1">${p.qty <= 0 ? 'Out of stock' : p.qty < 15 ? `Only ${p.qty} left!` : 'In stock'}</div>
        <button class="btn btn-primary w-100 mt-2 btn-sm-pill" ${p.qty <= 0 ? 'disabled' : ''} onclick='addToCartFromCard(${p.id})'>
          <i class="bi bi-cart-plus me-1"></i> Add to Cart
        </button>
      </div>
    </div>
  </div>`;
}

async function addToCartFromCard(productId){
  try { await Cart.add(productId, 1); } catch { /* toast already shown by apiRequest */ }
}

function toggleWishlistBtn(btn, product){
  const nowIn = Wishlist.toggle(product);
  btn.classList.toggle('is-active', nowIn);
  btn.title = nowIn ? 'Remove from wishlist' : 'Add to wishlist';
  const icon = btn.querySelector('i');
  icon.classList.toggle('bi-heart-fill', nowIn);
  icon.classList.toggle('bi-heart', !nowIn);
}

/* ---------- Init on every page ---------- */
document.addEventListener('DOMContentLoaded', () => {
  Wishlist.updateBadge();
  Cart.load();
  initNavScroll();
  initBackToTop();
  initNavCollapse();
  initReveal();
});
