# Admin Panel Implementation Guide

## Overview
The SimpleMart e-commerce application now includes a comprehensive admin panel that adapts the UI based on user role. When an admin logs in, they see additional admin-specific navigation links and pages.

## Architecture Decision
**Approach Used:** Same Frontend, Adaptive UI (Option 2)

### Why This Approach?
- ✅ **Single Codebase**: Easier maintenance and deployment
- ✅ **Shared Components**: Reuse existing UI components (navbar, footer, layouts)
- ✅ **Role-Based Access Control**: Leverage existing JWT authentication
- ✅ **Better UX**: Seamless experience for users with multiple roles
- ✅ **Easier Updates**: Changes to shared components benefit both user and admin interfaces

## Implementation Details

### 1. Admin Pages Created

#### a. Admin Dashboard (`/admin/dashboard`)
**Location:** `src/app/features/admin/pages/dashboard/`

**Features:**
- Overview statistics (Total Products, Total Orders, Low Stock Items)
- Recent orders table with quick view
- Low stock alerts
- Quick action buttons

**Key Methods:**
```typescript
loadDashboardData() - Fetches products and orders
calculateLowStockProducts() - Identifies products with stock < 10
```

#### b. Manage Products Page (`/admin/products`)
**Location:** `src/app/features/admin/pages/manage-products/`

**Features:**
- Complete product list with search and filtering
- Filter by category
- Search by product name or brand
- Stock status indicators (In Stock, Low Stock, Out of Stock)
- Edit, view, and delete actions for each product
- Product statistics summary

**Key Methods:**
```typescript
loadProducts() - Fetches all products from backend
filterProducts() - Applies search and category filters
deleteProduct(id, name) - Deletes a product with confirmation
getStockClass(stock) - Returns CSS class based on stock level
```

#### c. Manage Orders Page (`/admin/orders`)
**Location:** `src/app/features/admin/pages/manage-orders/`

**Features:**
- All orders list across all customers
- Search by Order ID or Customer ID
- Filter by order status
- View order details
- Update order status (button available, backend integration pending)
- Order statistics summary

**Key Methods:**
```typescript
loadOrders() - Fetches all orders (admin sees all orders)
filterOrders() - Applies search filters
getOrderStatus() - Returns current order status
```

### 2. Routing Configuration

**Admin Routes Added to `app.routes.ts`:**
```typescript
{
  path: 'admin',
  canActivate: [adminGuard],
  children: [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: AdminDashboardComponent },
    { path: 'products', component: ManageProductsComponent },
    { path: 'orders', component: ManageOrdersComponent }
  ]
}
```

**Protection:**
- All admin routes are protected by `adminGuard`
- Non-admin users attempting to access these routes will be redirected
- adminGuard checks for `ROLE_ADMIN` in JWT token

### 3. Navbar Updates

**Changes Made:**
- Added `isAdmin` property to track admin status
- Admin dropdown menu in main navigation (desktop)
- Conditional rendering:
  - Regular users see "Orders" link
  - Admins see "Admin" dropdown with Dashboard, Manage Products, Manage Orders
- User dropdown menu shows:
  - "Administrator" badge for admins
  - Admin quick links at the top of dropdown
  - Standard user links below

**Admin Navigation Links:**
```html
<!-- Main Navigation -->
<li class="nav-item dropdown" *ngIf="isAuthenticated && isAdmin">
  <a class="nav-link dropdown-toggle">Admin</a>
  <!-- Dropdown with admin links -->
</li>

<!-- User Dropdown -->
<ng-container *ngIf="isAdmin">
  <!-- Admin-specific links -->
</ng-container>
```

### 4. Security Implementation

**Guards:**
- `authGuard`: Checks if user is authenticated
- `adminGuard`: Checks if user has ROLE_ADMIN
- `roleGuard`: Generic factory for role-based access

**Backend Integration:**
- JWT tokens contain user role information
- AuthService provides `isAdmin()` method
- Backend validates roles on API calls
- Admin endpoints require ROLE_ADMIN in Spring Security

## How It Works

### For Regular Users:
1. Login with user credentials
2. See standard navigation (Home, Products, Orders, Cart)
3. Can view their own orders only
4. Cannot access `/admin/*` routes

### For Admin Users:
1. Login with admin credentials
2. See enhanced navigation with Admin dropdown
3. Access admin dashboard at `/admin/dashboard`
4. Manage all products at `/admin/products`
5. View all orders at `/admin/orders`
6. Can also use regular user features (browse products, place orders)

## API Endpoints Used

### Admin Dashboard:
- `GET /products/all` - Fetch all products for statistics
- `GET /orders/all` - Fetch all orders (admin-only endpoint)

### Manage Products:
- `GET /products/all` - Fetch all products
- `DELETE /products/{id}` - Delete a product (admin-only)
- `GET /products/categories` - Get unique categories
- `GET /products/brands` - Get unique brands

### Manage Orders:
- `GET /orders/all` - Fetch all orders across all customers
- `GET /orders/{id}` - Fetch single order details
- `PUT /orders/{id}/status` - Update order status (to be implemented)

## Testing the Admin Panel

### Prerequisites:
1. Backend Spring Boot application running on `http://localhost:8080`
2. Database with admin user account
3. Angular dev server running on `http://localhost:4200`

### Test Steps:

#### 1. Test Admin Login:
```
Email: admin@example.com (or your admin account)
Password: admin123 (or your admin password)
```

#### 2. Verify Admin Navigation:
- Check navbar shows "Admin" dropdown
- User dropdown should show "Administrator" badge
- Admin links should be visible

#### 3. Test Admin Dashboard:
- Navigate to `/admin/dashboard`
- Verify statistics cards display correct counts
- Check recent orders table loads
- Verify low stock alerts appear

#### 4. Test Manage Products:
- Navigate to `/admin/products`
- Search for products
- Filter by category
- Try deleting a product (with confirmation)
- Verify stock indicators (In Stock, Low Stock, Out of Stock)

#### 5. Test Manage Orders:
- Navigate to `/admin/orders`
- Search by Order ID
- View order details
- Verify order statistics

#### 6. Test Non-Admin Access:
- Logout
- Login with regular user account
- Try accessing `/admin/dashboard` manually
- Should be redirected to home page

## Future Enhancements

### Planned Features:
1. **Product Management:**
   - Add new product form
   - Edit existing products
   - Bulk product import/export
   - Product image upload

2. **Order Management:**
   - Update order status (Processing → Shipped → Delivered)
   - Order cancellation
   - Print invoice/packing slip
   - Order analytics and reporting

3. **Customer Management:**
   - View all customers
   - Customer details page
   - Customer order history
   - Block/unblock customers

4. **Analytics Dashboard:**
   - Sales charts and graphs
   - Revenue tracking
   - Top selling products
   - Customer analytics

5. **Inventory Management:**
   - Stock alerts and notifications
   - Bulk stock updates
   - Product variants (size, color)
   - Supplier management

6. **Settings:**
   - Site configuration
   - Payment gateway settings
   - Shipping methods
   - Tax configuration

## File Structure
```
customerProductAngular/src/app/
├── features/
│   └── admin/
│       └── pages/
│           ├── dashboard/
│           │   ├── admin-dashboard.component.ts
│           │   ├── admin-dashboard.component.html
│           │   └── admin-dashboard.component.css
│           ├── manage-products/
│           │   ├── manage-products.component.ts
│           │   ├── manage-products.component.html
│           │   └── manage-products.component.css
│           └── manage-orders/
│               ├── manage-orders.component.ts
│               ├── manage-orders.component.html
│               └── manage-orders.component.css
├── core/
│   └── guards/
│       └── role.guard.ts (adminGuard exported)
├── app.routes.ts (admin routes added)
└── shared/
    └── components/
        └── navbar/ (updated with admin links)
```

## Notes
- All admin pages use Bootstrap 5 for consistent UI
- Components use standalone architecture (no NgModules)
- ChangeDetectorRef used for proper loading state management
- All API calls include proper error handling
- Admin panel is fully responsive for mobile devices

## Troubleshooting

### Admin Links Not Showing:
- Verify JWT token contains ROLE_ADMIN
- Check AuthService.isAdmin() returns true
- Check localStorage for token and user info

### Cannot Access Admin Routes:
- Verify adminGuard is imported in app.routes.ts
- Check backend returns 200 for admin endpoints
- Verify user has ROLE_ADMIN in database

### Orders Not Loading:
- Check backend `/orders/all` endpoint exists
- Verify admin user has permission
- Check network tab for 403 errors

---

**Implementation Date:** 2024
**Version:** 1.0
**Status:** ✅ Minimum Viable Admin Panel Complete
