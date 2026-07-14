# Admin vs User Login Flow - Complete Guide

## 🔐 How Role Detection Works

### 1. JWT Token Structure
When a user logs in, the backend returns a JWT token with this payload structure:
```json
{
  "sub": "username",
  "roles": ["ROLE_ADMIN"] // or ["ROLE_USER"]
  "exp": 1234567890
}
```

### 2. Token Decoding Process
**File:** `core/services/token.service.ts`

```typescript
getRolesFromToken(): string[] {
  const token = this.getToken();
  const decoded = this.decodeToken(token);
  return decoded?.roles || decoded?.authorities || [];
}
```

### 3. Role Checking
**File:** `core/services/auth.service.ts`

```typescript
isAdmin(): boolean {
  return this.hasRole('ROLE_ADMIN');
}

isUser(): boolean {
  return this.hasRole('ROLE_USER');
}
```

### 4. Route Protection
**File:** `core/guards/role.guard.ts`

```typescript
export const adminGuard: CanActivateFn = roleGuard('ROLE_ADMIN');
```

The `adminGuard` blocks access to `/admin/*` routes unless user has `ROLE_ADMIN`.

---

## 🗺️ Layout Routing Architecture

### Layout Components
1. **AuthLayoutComponent** - Login/Register page
2. **MainLayoutComponent** - User interface (navbar, footer, content)
3. **AdminLayoutComponent** - Admin interface (sidebar, no navbar/footer)

### Route Configuration
**File:** `app.routes.ts`

```typescript
// User routes → MainLayoutComponent
{
  path: '',
  component: MainLayoutComponent,  // ← Navbar + Footer
  children: [
    { path: '', component: LandingComponent },
    { path: 'products', component: ProductListComponent },
    { path: 'checkout', component: CheckoutComponent },
    // ... other user routes
  ]
}

// Admin routes → AdminLayoutComponent
{
  path: 'admin',
  component: AdminLayoutComponent,  // ← Sidebar only
  canActivate: [adminGuard],
  children: [
    { path: 'dashboard', component: AdminDashboardComponent },
    { path: 'products', component: ManageProductsComponent },
    { path: 'orders', component: ManageOrdersComponent },
    // ... other admin routes
  ]
}
```

---

## ❌ The Problem (Before Fix)

### What Was Happening:
1. Admin logs in successfully
2. Login redirects to `/` (home page)
3. Route `/` uses `MainLayoutComponent`
4. **Admin sees USER interface:**
   - ✗ Regular navbar (not sidebar)
   - ✗ Landing page with categories
   - ✗ Products page (customer view)
   - ✗ "My Orders" (only their orders)

5. Admin had to manually:
   - Click "Admin" dropdown in navbar
   - Navigate to `/admin/dashboard`
   - THEN see proper admin layout

### Root Cause
**File:** `features/auth/pages/login/login.component.ts`

```typescript
// ❌ BEFORE (Wrong)
next: (response) => {
  this.router.navigate([this.returnUrl]); // Always '/' for everyone
}
```

---

## ✅ The Solution (After Fix)

### Updated Login Redirect Logic
**File:** `features/auth/pages/login/login.component.ts`

```typescript
// ✅ AFTER (Correct)
next: (response) => {
  if (this.authService.isAdmin()) {
    // Admin → go to admin dashboard
    this.router.navigate(['/admin/dashboard']);
  } else {
    // User → go to home or returnUrl
    this.router.navigate([this.returnUrl]);
  }
}
```

### Also Fixed: Already Logged In Check
```typescript
ngOnInit(): void {
  // If already logged in, redirect appropriately
  if (this.authService.isAuthenticated()) {
    if (this.authService.isAdmin()) {
      this.router.navigate(['/admin/dashboard']);
    } else {
      this.router.navigate([this.returnUrl]);
    }
  }
}
```

---

## 🎯 Login Flow - Step by Step

### Regular User Login
```
1. User enters credentials
2. Backend validates → returns JWT with ["ROLE_USER"]
3. Frontend saves token in localStorage
4. AuthService.isAdmin() → false
5. Router redirects to → '/' (landing page)
6. MainLayoutComponent loads:
   ┌──────────────────────────────┐
   │  Navbar (Home, Products, Cart)│
   ├──────────────────────────────┤
   │  Landing Page Content         │
   │  - Shop by Category           │
   │  - Featured Products          │
   ├──────────────────────────────┤
   │  Footer                       │
   └──────────────────────────────┘
```

### Admin User Login
```
1. Admin enters credentials
2. Backend validates → returns JWT with ["ROLE_ADMIN"]
3. Frontend saves token in localStorage
4. AuthService.isAdmin() → true
5. Router redirects to → '/admin/dashboard'
6. AdminLayoutComponent loads:
   ┌──────┬──────────────────────┐
   │      │  Top Header          │
   │ Side ├──────────────────────┤
   │ bar  │  Dashboard Content   │
   │      │  - Statistics Cards  │
   │ Nav  │  - Recent Orders     │
   │      │  - Quick Actions     │
   └──────┴──────────────────────┘
```

---

## 🔄 Layout Switching

### How Admin Can Access User View
```
Admin Dashboard → Click "View Store Front" → Navigates to '/'
→ MainLayoutComponent loads (user interface)
→ Admin can browse as customer
→ Still has Admin dropdown in navbar to go back
```

### How Layouts Are Determined
**Router decides layout based on URL path:**

| URL Path | Layout Component | Interface |
|----------|-----------------|-----------|
| `/` | MainLayoutComponent | User (navbar) |
| `/products` | MainLayoutComponent | User (navbar) |
| `/checkout` | MainLayoutComponent | User (navbar) |
| `/admin/dashboard` | AdminLayoutComponent | Admin (sidebar) |
| `/admin/products` | AdminLayoutComponent | Admin (sidebar) |
| `/admin/orders` | AdminLayoutComponent | Admin (sidebar) |

---

## 🛡️ Security Layer

### Protection Mechanism
```
1. Route Guard Check
   ├─ User tries to access /admin/dashboard
   ├─ adminGuard intercepts
   ├─ Checks: authService.hasRole('ROLE_ADMIN')
   │
   ├─ If TRUE → Allow access
   └─ If FALSE → Redirect to '/'

2. Backend API Check
   ├─ Frontend calls admin endpoint
   ├─ JWT token sent in Authorization header
   ├─ Spring Security validates token
   ├─ Checks @PreAuthorize("hasRole('ADMIN')")
   │
   ├─ If TRUE → Return data
   └─ If FALSE → Return 403 Forbidden
```

### Double Security
- **Frontend:** Route guards prevent navigation
- **Backend:** API endpoints validate JWT roles

Even if someone bypasses frontend guards (browser dev tools), backend will reject unauthorized API calls.

---

## 🧪 Testing the Fix

### Test 1: Admin Login
1. Navigate to `/login`
2. Enter admin credentials (e.g., `admin@example.com`)
3. Click Login
4. **Expected:** Redirect to `/admin/dashboard`
5. **Expected:** See admin sidebar (not navbar)
6. **Expected:** See dashboard statistics

### Test 2: User Login
1. Navigate to `/login`
2. Enter user credentials (e.g., `user@example.com`)
3. Click Login
4. **Expected:** Redirect to `/` (landing page)
5. **Expected:** See regular navbar
6. **Expected:** See landing page with categories

### Test 3: Admin Accessing User View
1. Login as admin
2. In sidebar, click "View Store Front"
3. **Expected:** Navigate to `/`
4. **Expected:** See navbar (user interface)
5. **Expected:** Navbar shows "Admin" dropdown
6. Click "Admin" → "Dashboard"
7. **Expected:** Back to admin sidebar view

### Test 4: User Cannot Access Admin
1. Login as regular user
2. Manually navigate to `/admin/dashboard`
3. **Expected:** adminGuard blocks access
4. **Expected:** Redirect to `/` (home)

### Test 5: Guest Cannot Access Admin
1. Logout (or don't login)
2. Navigate to `/admin/dashboard`
3. **Expected:** Redirect to `/login?returnUrl=/admin/dashboard`

---

## 📊 Visual Comparison

### BEFORE Fix (Admin sees user interface)
```
Admin Login → '/' → MainLayoutComponent
┌────────────────────────────────────┐
│ [Navbar] Home Products Cart        │ ← Wrong (navbar)
├────────────────────────────────────┤
│  🏠 SimpleMart                     │
│  Shop by Category                  │ ← Wrong (landing page)
│  [Electronics] [Mobiles]           │
├────────────────────────────────────┤
│ [Footer]                           │ ← Wrong (footer)
└────────────────────────────────────┘
```

### AFTER Fix (Admin sees admin interface)
```
Admin Login → '/admin/dashboard' → AdminLayoutComponent
┌──────┬───────────────────────────┐
│📊    │ [Top Bar] Admin           │
│Dash  ├───────────────────────────┤
│📦    │ Statistics Cards          │ ← Correct (dashboard)
│Prod  │ ┌───┐ ┌───┐ ┌───┐ ┌───┐ │
│➕    │ │ 5 │ │ 3 │ │ 2 │ │ 3 │ │
│Add   │ └───┘ └───┘ └───┘ └───┘ │
│📋    │ Recent Orders             │
│Ord   │ [Table with orders...]    │
│👥    │                           │
│Cust  │ Quick Actions             │
│🏪    │ [Add] [Manage] [View]     │
│Store │                           │
└──────┴───────────────────────────┘
```

---

## 🔧 Implementation Details

### Files Modified
1. ✅ `login.component.ts` - Added role-based redirect after login
2. ✅ `login.component.ts` - Updated ngOnInit for already logged in users

### Files Already Correct (No Changes Needed)
- ✅ `app.routes.ts` - Admin routes use AdminLayoutComponent
- ✅ `auth.service.ts` - Has isAdmin() method
- ✅ `role.guard.ts` - adminGuard protects routes
- ✅ `token.service.ts` - Extracts roles from JWT

---

## 📝 Summary

### The Issue
Admin users were redirected to the customer-facing interface after login, forcing them to manually navigate to admin panel.

### The Fix
Added role-based redirect logic in login component:
- **Admin** → `/admin/dashboard` (sidebar layout)
- **User** → `/` or returnUrl (navbar layout)

### The Result
- ✅ Admin sees admin interface immediately after login
- ✅ User sees customer interface after login
- ✅ Layouts switch correctly based on URL path
- ✅ Guards protect admin routes from unauthorized access
- ✅ Backend validates all API calls

---

**Fixed Date:** 2026-07-12  
**Status:** ✅ Complete and Working
