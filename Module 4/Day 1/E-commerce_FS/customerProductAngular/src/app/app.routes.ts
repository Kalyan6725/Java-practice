import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layouts/main-layout/main-layout.component';
import { AuthLayoutComponent } from './layouts/auth-layout/auth-layout.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { LandingComponent } from './features/home/pages/landing/landing.component';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { ProductListComponent } from './features/products/pages/product-list/product-list.component';
import { ProductDetailComponent } from './features/products/pages/product-detail/product-detail.component';
import { CheckoutComponent } from './features/orders/pages/checkout/checkout.component';
import { OrderSuccessComponent } from './features/orders/pages/order-success/order-success.component';
import { MyOrdersComponent } from './features/orders/pages/my-orders/my-orders.component';
import { OrderDetailComponent } from './features/orders/pages/order-detail/order-detail.component';
import { AdminDashboardComponent } from './features/admin/pages/dashboard/admin-dashboard.component';
import { ManageProductsComponent } from './features/admin/pages/manage-products/manage-products.component';
import { ManageOrdersComponent } from './features/admin/pages/manage-orders/manage-orders.component';
import { AddProductComponent } from './features/admin/pages/add-product/add-product.component';
import { ManageCustomersComponent } from './features/admin/pages/manage-customers/manage-customers.component';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/role.guard';

export const routes: Routes = [
  // Auth routes with auth layout (Login/Register toggle)
  {
    path: 'login',
    component: AuthLayoutComponent
  },
  
  // Main routes with main layout
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      { path: '', component: LandingComponent },
      { path: 'products', component: ProductListComponent },
      { path: 'products/:id', component: ProductDetailComponent },
      { path: 'checkout', component: CheckoutComponent, canActivate: [authGuard] },
      { path: 'order-success', component: OrderSuccessComponent, canActivate: [authGuard] },
      { path: 'my-orders', component: MyOrdersComponent, canActivate: [authGuard] },
      { path: 'orders/:id', component: OrderDetailComponent, canActivate: [authGuard] }
    ]
  },
  
  // Admin routes with dedicated admin layout - protected by adminGuard
  {
    path: 'admin',
    component: AdminLayoutComponent,
    canActivate: [adminGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'products', component: ManageProductsComponent },
      { path: 'products/add', component: AddProductComponent },
      { path: 'orders', component: ManageOrdersComponent },
      { path: 'customers', component: ManageCustomersComponent }
    ]
  },
  
  // Redirect unknown routes to home
  { path: '**', redirectTo: '' }
];
