/* ============================================================
   Northern Arc - Loan Management System (static mockups)
   Shared script: injects navbar + footer, small interactions.
   NOTE: This is a static mockup. Data is sample data only.
   Each page comments the real backend endpoint it maps to.
   ============================================================ */

(function () {
  // ---- Brand logo markup (reused in navbar + footer) ----
  function brandHtml(href) {
    return (
      '<a class="brand" href="' + (href || 'index.html') + '">' +
      '  <span class="mark"><span class="tri"></span><span class="star">\u2726</span></span>' +
      '  <span class="name"><b>N<span class="dot">O</span>RTHERN</b><span>ARC</span></span>' +
      '</a>'
    );
  }

  // ---- Nav definitions per area ----
  var NAVS = {
    public: [
      { href: 'index.html', label: 'Home' },
      { href: 'loan-products.html', label: 'Loan Products' },
      { href: 'contact.html', label: 'Contact' }
    ],
    customer: [
      { href: 'customer-dashboard.html', label: 'Dashboard' },
      { href: 'loan-products.html', label: 'Products' },
      { href: 'apply-loan.html', label: 'Apply' },
      { href: 'my-applications.html', label: 'Applications' },
      { href: 'loan-accounts.html', label: 'My Loans' },
      { href: 'profile.html', label: 'Profile' }
    ],
    staff: [
      { href: 'admin-dashboard.html', label: 'Dashboard' },
      { href: 'customers.html', label: 'Customers' },
      { href: 'loan-products-admin.html', label: 'Products' },
      { href: 'applications-review.html', label: 'Applications' },
      { href: 'loan-accounts-admin.html', label: 'Accounts' },
      { href: 'emi-payments-admin.html', label: 'Payments' }
    ]
  };

  var AREA_META = {
    public: { pill: null, right: [{ href: 'login.html', label: 'Login', cls: 'btn btn-outline btn-sm' }, { href: 'register.html', label: 'Register', cls: 'btn btn-primary btn-sm' }] },
    customer: { pill: 'CUSTOMER', right: [{ href: 'index.html', label: 'Logout', cls: 'btn btn-outline btn-sm' }] },
    staff: { pill: 'STAFF', right: [{ href: 'index.html', label: 'Logout', cls: 'btn btn-outline btn-sm' }] }
  };

  function buildNavbar() {
    var body = document.body;
    var area = body.getAttribute('data-area');
    if (!area || !NAVS[area]) return;
    var active = body.getAttribute('data-active') || '';
    var home = area === 'staff' ? 'admin-dashboard.html' : area === 'customer' ? 'customer-dashboard.html' : 'index.html';

    var links = NAVS[area].map(function (l) {
      var cls = l.href === active ? 'active' : '';
      return '<a class="' + cls + '" href="' + l.href + '">' + l.label + '</a>';
    }).join('');

    var meta = AREA_META[area];
    var right = meta.right.map(function (r) {
      return '<a class="' + r.cls + '" href="' + r.href + '">' + r.label + '</a>';
    }).join(' ');
    var pill = meta.pill ? '<span class="pill">' + meta.pill + '</span>' : '';

    var nav = document.createElement('nav');
    nav.className = 'navbar';
    nav.innerHTML =
      '<div class="inner">' +
      brandHtml(home) + pill +
      '<div class="links">' + links + '<span style="width:8px"></span>' + right + '</div>' +
      '</div>';
    body.insertBefore(nav, body.firstChild);
  }

  function buildFooter() {
    if (document.body.getAttribute('data-footer') === 'off') return;
    var footer = document.createElement('footer');
    footer.className = 'footer';
    footer.innerHTML =
      '<div class="container inner">' +
      '<div>' + brandHtml('index.html') + '</div>' +
      '<div class="muted" style="font-size:.85rem">&copy; 2026 Northern Arc Loan Management. Sample UI mockup.</div>' +
      '<div class="actions"><a href="contact.html">Contact</a><a href="#">Privacy</a><a href="#">Terms</a></div>' +
      '</div>';
    document.body.appendChild(footer);
  }

  // ---- Helpers exposed for pages ----
  window.LMS = {
    inr: function (n) {
      if (n == null || isNaN(n)) return '\u20B90';
      return '\u20B9' + Number(n).toLocaleString('en-IN', { maximumFractionDigits: 2 });
    },
    // Fake "submit" for mockup forms: prevent real POST, redirect to a target.
    mockSubmit: function (formId, redirectTo, message) {
      var form = document.getElementById(formId);
      if (!form) return;
      form.addEventListener('submit', function (e) {
        e.preventDefault();
        if (message) { try { sessionStorage.setItem('lms_flash', message); } catch (err) {} }
        if (redirectTo) window.location.href = redirectTo;
      });
    },
    // Show a flash message set by a previous page, into #flash element.
    showFlash: function () {
      var el = document.getElementById('flash');
      if (!el) return;
      var msg = null;
      try { msg = sessionStorage.getItem('lms_flash'); sessionStorage.removeItem('lms_flash'); } catch (e) {}
      if (msg) { el.textContent = msg; el.style.display = 'block'; }
      else { el.style.display = 'none'; }
    }
  };

  document.addEventListener('DOMContentLoaded', function () {
    buildNavbar();
    buildFooter();
    if (document.getElementById('flash')) window.LMS.showFlash();
  });
})();
