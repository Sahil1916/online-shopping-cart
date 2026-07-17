/* ============================================================
   Shop_With_Sahil — Reusable Navbar & Footer partials
   ============================================================ */

function renderNavbar(active = ''){
  const link = (href, label, key) => `<li class="nav-item"><a class="nav-link nav-link-custom ${active===key?'active':''}" href="${href}">${label}</a></li>`;
  return `
  <nav class="navbar navbar-shop navbar-expand-lg">
    <div class="container">
      <a class="brand-logo" href="index.html"><i class="bi bi-bag-check-fill text-violet"></i> Shop_With_Sahil</a>
      <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
        <i class="bi bi-list fs-2"></i>
      </button>
      <div class="collapse navbar-collapse" id="mainNav">
        <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-1">
          ${link('index.html','Home','home')}
          ${link('product-listing.html','Shop','shop')}
          ${link('product-listing.html#categories','Categories','cat')}
          ${link('index.html#testimonials','Reviews','rev')}
        </ul>
        <div class="d-flex align-items-center gap-2 mt-3 mt-lg-0">
          <button class="icon-btn d-none d-md-flex" title="Search" data-bs-toggle="offcanvas" data-bs-target="#searchOffcanvas"><i class="bi bi-search"></i></button>
          <div id="accountSlot"><a href="login.html" class="icon-btn" title="Account"><i class="bi bi-person"></i></a></div>
          <a href="wishlist.html" class="icon-btn" title="Wishlist">
            <i class="bi bi-heart"></i>
            <span class="cart-badge js-wishlist-count">0</span>
          </a>
          <a href="cart.html" class="icon-btn" title="Cart">
            <i class="bi bi-bag"></i>
            <span class="cart-badge js-cart-count">0</span>
          </a>
        </div>
      </div>
    </div>
  </nav>
  <div class="offcanvas offcanvas-top" id="searchOffcanvas">
    <div class="offcanvas-body">
      <div class="container">
        <div class="search-bar-wrap">
          <i class="bi bi-search text-muted"></i>
          <input type="text" placeholder="Search for products, brands and more...">
          <button class="btn btn-primary btn-sm-pill" data-bs-dismiss="offcanvas">Search</button>
        </div>
      </div>
    </div>
  </div>`;
}

function renderFooter(){
  return `
  <footer class="footer-shop">
    <div class="container">
      <div class="row g-4">
        <div class="col-lg-4">
          <a class="brand-logo text-white mb-3 d-inline-flex" href="index.html"><i class="bi bi-bag-check-fill text-coral"></i> Shop_With_Sahil</a>
          <p class="small mb-3">Premium products, honest prices, and delivery you can trust. Shop_With_Sahil brings the best of electronics, fashion and home to your doorstep.</p>
          <div class="footer-social">
            <a href="#"><i class="bi bi-facebook"></i></a>
            <a href="#"><i class="bi bi-instagram"></i></a>
            <a href="#"><i class="bi bi-twitter-x"></i></a>
            <a href="#"><i class="bi bi-youtube"></i></a>
          </div>
        </div>
        <div class="col-lg-2 col-6">
          <h6>Shop</h6>
          <a href="product-listing.html">All Products</a>
          <a href="product-listing.html">Electronics</a>
          <a href="product-listing.html">Fashion</a>
          <a href="product-listing.html">Home</a>
        </div>
        <div class="col-lg-2 col-6">
          <h6>Account</h6>
          <a href="login.html">Login</a>
          <a href="register.html">Register</a>
          <a href="cart.html">My Cart</a>
          <a href="wishlist.html">My Wishlist</a>
          <a href="Orders.html">My Orders</a>
        </div>
        <div class="col-lg-2 col-6">
          <h6>Company</h6>
          <a href="#">About Us</a>
          <a href="#">Careers</a>
          <a href="admin/admin-login.html">Admin Login</a>
          <a href="#">Contact</a>
        </div>
        <div class="col-lg-2 col-6">
          <h6>Support</h6>
          <a href="#">Help Center</a>
          <a href="#">Returns</a>
          <a href="#">Shipping Info</a>
          <a href="#">Track Order</a>
        </div>
      </div>
      <hr class="border-light opacity-10 mt-4">
      <div class="d-flex flex-column flex-md-row justify-content-between align-items-center small pt-2 gap-2">
        <span>© 2026 Shop_With_Sahil. All rights reserved. Built for MCA Advanced Java Project.</span>
        <span class="d-flex gap-3"><i class="bi bi-credit-card-2-front"></i> <i class="bi bi-paypal"></i> <i class="bi bi-wallet2"></i> <i class="bi bi-google-pay"></i></span>
      </div>
    </div>
  </footer>
  <button class="back-to-top"><i class="bi bi-arrow-up"></i></button>`;
}

document.addEventListener('DOMContentLoaded', async () => {
  const navEl = document.getElementById('navbar-placeholder');
  const footEl = document.getElementById('footer-placeholder');
  if (navEl) navEl.outerHTML = renderNavbar(navEl.dataset.active || '');
  if (footEl) footEl.outerHTML = renderFooter();
  Cart.load();
  Wishlist.updateBadge();
  initNavScroll();
  initBackToTop();
  initNavCollapse();

  const slot = document.getElementById('accountSlot');
  if (slot){
    const user = await Auth.getCurrentUser();
    if (user){
      slot.innerHTML = `
        <div class="dropdown">
          <button class="icon-btn" data-bs-toggle="dropdown" title="${user.name}"><i class="bi bi-person-check-fill"></i></button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li><span class="dropdown-item-text small text-muted">Hi, ${user.name}</span></li>
            ${user.role === 'ADMIN' ? '<li><a class="dropdown-item" href="admin/dashboard.html">Admin Dashboard</a></li>' : ''}
            <li><a class="dropdown-item" href="Profile.html"> Profile</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><button class="dropdown-item text-danger" onclick="doLogout()">Logout</button></li>
          </ul>
        </div>`;
    }
  }
});

async function doLogout(){
  try { await api.logout(); } catch {}
  window.location.href = 'login.html';
}
