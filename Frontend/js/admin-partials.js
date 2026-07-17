/* ============================================================
   Shop_With_Sahil Admin — Sidebar & Topbar partials
   ============================================================ */

function renderAdminSidebar(active = ''){
  const link = (href, icon, label, key) => `<a href="${href}" class="${active===key?'active':''}"><i class="bi ${icon}"></i> ${label}</a>`;
  return `
  <aside class="admin-sidebar" id="adminSidebar">
    <a href="../index.html" class="brand-logo text-white mb-4 d-flex"><i class="bi bi-bag-check-fill text-coral"></i> Shop_With_Sahil</a>
    <p class="text-uppercase small opacity-50 mb-2" style="font-size:.72rem; letter-spacing:.08em;">Main</p>
    ${link('dashboard.html','bi-grid-1x2-fill','Dashboard','dash')}
    ${link('product-management.html','bi-box-seam-fill','Products','products')}
    ${link('user-management.html','bi-people-fill','Users','users')}
    ${link('order-management.html','bi-receipt-cutoff','Orders','orders')}
    <p class="text-uppercase small opacity-50 mb-2 mt-4" style="font-size:.72rem; letter-spacing:.08em;">Account</p>
    <a href="#" onclick="event.preventDefault(); adminLogout();"><i class="bi bi-box-arrow-left"></i> Logout</a>
  </aside>`;
}

async function adminLogout(){
  try { await api.logout(); } catch {}
  window.location.href = 'admin-login.html';
}

function renderAdminTopbar(title, subtitle = ''){
  return `
  <div class="admin-topbar flex-wrap gap-3">
    <div class="d-flex align-items-center gap-3">
      <button class="btn btn-light-soft d-lg-none" onclick="document.getElementById('adminSidebar').classList.toggle('show')"><i class="bi bi-list"></i></button>
      <div>
        <h3 class="mb-0">${title}</h3>
        ${subtitle ? `<p class="text-muted small mb-0">${subtitle}</p>` : ''}
      </div>
    </div>
    <div class="d-flex align-items-center gap-3">
      <button class="icon-btn"><i class="bi bi-bell"></i></button>
      <div class="d-flex align-items-center gap-2">
       
        <div class="d-none d-md-block">
          <div class="fw-semibold small" id="adminNameSlot">Admin</div>
          <div class="text-muted small" style="font-size:.75rem;">Administrator</div>
        </div>
      </div>
    </div>
  </div>`;
}

document.addEventListener('DOMContentLoaded', async () => {
  const user = await Auth.requireRole('ADMIN', 'admin-login.html');
  if (!user) return; // redirected already

  const sideEl = document.getElementById('admin-sidebar-placeholder');
  const topEl = document.getElementById('admin-topbar-placeholder');
  if (sideEl) sideEl.outerHTML = renderAdminSidebar(sideEl.dataset.active || '');
  if (topEl) topEl.outerHTML = renderAdminTopbar(topEl.dataset.title || '', topEl.dataset.subtitle || '');

  const nameSlot = document.getElementById('adminNameSlot');
  if (nameSlot) nameSlot.textContent = user.name;
});
