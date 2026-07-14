# Backend Integration - Services & API Documentation

## ✅ COMPLETED INFRASTRUCTURE

### 📁 File Structure Created

```
src/
├── environments/
│   ├── environment.ts          ✅ Development config
│   └── environment.prod.ts     ✅ Production config
│
├── app/
│   └── core/
│       ├── models/
│       │   └── api.models.ts   ✅ All DTOs & interfaces
│       │
│       ├── services/
│       │   ├── auth.service.ts       ✅ Authentication
│       │   ├── token.service.ts      ✅ JWT token management
│       │   ├── product.service.ts    ✅ Product operations
│       │   ├── order.service.ts      ✅ Order operations
│       │   ├── order-item.service.ts ✅ Order item operations
│       │   ├── customer.service.ts   ✅ Customer operations
│       │   ├── cart.service.ts       ✅ Shopping cart management
│       │   └── loading.service.ts    ✅ Loading state
│       │
│       ├── interceptors/
│       │   ├── auth.interceptor.ts    ✅ JWT token injection
│       │   ├── error.interceptor.ts   ✅ Error handling
│       │   └── loading.interceptor.ts ✅ Loading indicator
│       │
│       └── guards/
│           ├── auth.guard.ts  ✅ Authentication guard
│           └── role.guard.ts  ✅ Role-based access control
```

---

## 🔧 SERVICES OVERVIEW

### 1️⃣ AuthService

**Purpose**: Handle user authentication and authorization

**Backend Endpoint**: `POST /auth/login`

**Methods**:

```typescript
// Login user
login(credentials: JwtRequestDTO): Observable<JwtResponseDTO>

// Logout user
logout(): void

// Check if user is authenticated
isAuthenticated(): boolean

// Get current username
getCurrentUsername(): string | null

// Get user roles
getCurrentUserRoles(): string[]

// Check if user has specific role
hasRole(role: string): boolean

// Check if user is admin
isAdmin(): boolean

// Check if user is regular user
isUser(): boolean
```

**Usage Example**:

```typescript
constructor(private authService: AuthService) {}

login() {
  const credentials = {
    username: 'johndoe',
    password: 'password123'
  };

  this.authService.login(credentials).subscribe({
    next: (response) => {
      console.log('Login successful:', response.token);
      this.router.navigate(['/']);
    },
    error: (error) => {
      console.error('Login failed:', error.message);
    }
  });
}

logout() {
  this.authService.logout();
}

checkAuth() {
  if (this.authService.isAuthenticated()) {
    console.log('User is logged in');
    console.log('Username:', this.authService.getCurrentUsername());
    console.log('Is Admin:', this.authService.isAdmin());
  }
}
```

---

### 2️⃣ TokenService

**Purpose**: Manage JWT tokens

**Methods**:

```typescript
// Save token to localStorage
saveToken(token: string): void

// Get token from localStorage
getToken(): string | null

// Remove token from localStorage
removeToken(): void

// Get username from token
getUsernameFromToken(): string | null

// Get roles from token
getRolesFromToken(): string[]

// Check if token is expired
isTokenExpired(): boolean

// Get token expiration date
getTokenExpirationDate(): Date | null
```

---

### 3️⃣ ProductService

**Purpose**: Handle all product-related operations

**Backend Endpoints**:
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create product (ADMIN)
- `PUT /api/products/{id}` - Update product (ADMIN)
- `DELETE /api/products/{id}` - Delete product (ADMIN)
- `GET /api/products/category/{category}` - Filter by category
- `GET /api/products/brand/{brand}` - Filter by brand
- `GET /api/products/price-range?minPrice=X&maxPrice=Y` - Filter by price
- `GET /api/products/sort/price-asc` - Sort by price ascending
- `GET /api/products/sort/price-desc` - Sort by price descending
- `GET /api/products/sort/name` - Sort by name
- `GET /api/products/sort/category` - Sort by category

**Methods**:

```typescript
getAllProducts(): Observable<ProductResponseDTO[]>
getProductById(id: number): Observable<ProductResponseDTO>
createProduct(product: ProductRequestDTO): Observable<ProductResponseDTO>
updateProduct(id: number, product: ProductRequestDTO): Observable<ProductResponseDTO>
deleteProduct(id: number): Observable<string>
getProductsByCategory(category: string): Observable<ProductResponseDTO[]>
getProductsByBrand(brand: string): Observable<ProductResponseDTO[]>
getProductsByPriceRange(minPrice: number, maxPrice: number): Observable<ProductResponseDTO[]>
sortProductsByPriceAsc(): Observable<ProductResponseDTO[]>
sortProductsByPriceDesc(): Observable<ProductResponseDTO[]>
sortProductsByName(): Observable<ProductResponseDTO[]>
sortProductsByCategory(): Observable<ProductResponseDTO[]>
```

**Usage Example**:

```typescript
constructor(private productService: ProductService) {}

ngOnInit() {
  // Get all products
  this.productService.getAllProducts().subscribe({
    next: (products) => {
      this.products = products;
    },
    error: (error) => {
      console.error('Error loading products:', error.message);
    }
  });
}

filterByCategory(category: string) {
  this.productService.getProductsByCategory(category).subscribe({
    next: (products) => {
      this.products = products;
    }
  });
}

sortProducts(sortType: string) {
  switch(sortType) {
    case 'price-asc':
      this.productService.sortProductsByPriceAsc().subscribe(...);
      break;
    case 'price-desc':
      this.productService.sortProductsByPriceDesc().subscribe(...);
      break;
    case 'name':
      this.productService.sortProductsByName().subscribe(...);
      break;
  }
}
```

---

### 4️⃣ OrderService

**Purpose**: Handle order operations

**Backend Endpoints**:
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create order
- `PUT /api/orders/{id}` - Update order (ADMIN)
- `DELETE /api/orders/{id}` - Delete order (ADMIN)

**Methods**:

```typescript
createOrder(order: OrderRequestDTO): Observable<OrderResponseDTO>
getOrderById(id: number): Observable<OrderResponseDTO>
getAllOrders(): Observable<OrderResponseDTO[]>
updateOrder(id: number, order: OrderRequestDTO): Observable<OrderResponseDTO>
deleteOrder(id: number): Observable<string>
```

**Usage Example**:

```typescript
constructor(
  private orderService: OrderService,
  private cartService: CartService
) {}

placeOrder() {
  const cartItems = this.cartService.getCartItems();
  
  const orderRequest: OrderRequestDTO = {
    orderDate: new Date().toISOString(),
    customerId: this.currentCustomerId,
    orderItems: cartItems.map(item => ({
      productId: item.product.id,
      quantity: item.quantity
    }))
  };

  this.orderService.createOrder(orderRequest).subscribe({
    next: (order) => {
      console.log('Order placed successfully:', order.id);
      this.cartService.clearCart();
      this.router.navigate(['/order-success']);
    },
    error: (error) => {
      console.error('Order failed:', error.message);
    }
  });
}

loadMyOrders() {
  this.orderService.getAllOrders().subscribe({
    next: (orders) => {
      this.orders = orders;
    }
  });
}

viewOrderDetails(orderId: number) {
  this.orderService.getOrderById(orderId).subscribe({
    next: (order) => {
      this.selectedOrder = order;
    }
  });
}
```

---

### 5️⃣ CartService

**Purpose**: Manage shopping cart (client-side)

**Methods**:

```typescript
// Cart operations
addToCart(product: ProductResponseDTO, quantity: number): void
removeFromCart(productId: number): void
updateQuantity(productId: number, quantity: number): void
clearCart(): void

// Cart queries
getCartItems(): CartItem[]
getCartItemCount(): number
isInCart(productId: number): boolean
getProductQuantity(productId: number): number

// Price calculations
getSubtotal(): number
getTax(): number
getShipping(): number
getTotal(): number

// Observables
cartItems$: Observable<CartItem[]>
```

**Usage Example**:

```typescript
constructor(private cartService: CartService) {
  // Subscribe to cart changes
  this.cartService.cartItems$.subscribe(items => {
    this.cartItems = items;
    this.cartCount = this.cartService.getCartItemCount();
  });
}

addProductToCart(product: ProductResponseDTO, quantity: number = 1) {
  this.cartService.addToCart(product, quantity);
  console.log('Added to cart!');
}

updateItemQuantity(productId: number, quantity: number) {
  this.cartService.updateQuantity(productId, quantity);
}

removeItem(productId: number) {
  this.cartService.removeFromCart(productId);
}

checkout() {
  const subtotal = this.cartService.getSubtotal();
  const tax = this.cartService.getTax();
  const shipping = this.cartService.getShipping();
  const total = this.cartService.getTotal();
  
  console.log('Order Summary:', { subtotal, tax, shipping, total });
}
```

---

### 6️⃣ CustomerService

**Purpose**: Handle customer operations (ADMIN only)

**Backend Endpoints**:
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `POST /api/customers` - Create customer
- `DELETE /api/customers/{id}` - Delete customer
- `PUT /api/customers/update/{id}/{name}` - Update customer name
- `PUT /api/admin/user-customer/{username}/{customerId}` - Map user to customer

**Methods**:

```typescript
getAllCustomers(): Observable<CustomerResponseDTO[]>
getCustomerById(id: number): Observable<CustomerResponseDTO>
createCustomer(customer: CustomerRequestDTO): Observable<CustomerResponseDTO>
deleteCustomer(id: number): Observable<{ message: string }>
updateCustomerName(id: number, name: string): Observable<number>
mapUserToCustomer(username: string, customerId: number): Observable<{ message: string }>
```

---

## 🛡️ INTERCEPTORS

### 1. Auth Interceptor
- **Purpose**: Automatically adds JWT token to all HTTP requests
- **Header**: `Authorization: Bearer {token}`
- **Skips**: `/auth/login` endpoint

### 2. Error Interceptor
- **Purpose**: Handles HTTP errors globally
- **Features**:
  - 401: Redirects to login
  - 403: Shows forbidden message
  - 404: Shows not found message
  - 500: Shows server error message
  - Logs all errors to console

### 3. Loading Interceptor
- **Purpose**: Shows/hides loading indicator
- **Features**:
  - Tracks active requests
  - Shows spinner when requests are pending
  - Hides spinner when all requests complete

---

## 🔒 GUARDS

### Auth Guard

**Usage**: Protect routes that require authentication

```typescript
// In app.routes.ts
{
  path: 'products',
  component: ProductListComponent,
  canActivate: [authGuard]
}
```

### Role Guards

**Usage**: Protect routes by user role

```typescript
// In app.routes.ts
{
  path: 'admin',
  component: AdminDashboardComponent,
  canActivate: [adminGuard]
}

{
  path: 'my-orders',
  component: MyOrdersComponent,
  canActivate: [userGuard]
}
```

---

## 🌐 ENVIRONMENT CONFIGURATION

### Development (environment.ts)
- API URL: `http://localhost:8080`
- All endpoints configured

### Production (environment.prod.ts)
- API URL: `https://your-production-api.com`
- Update with your production URL

---

## 📝 DATA MODELS

All DTOs are defined in `core/models/api.models.ts`:

- `JwtRequestDTO` - Login credentials
- `JwtResponseDTO` - JWT token response
- `ProductRequestDTO` - Create/update product
- `ProductResponseDTO` - Product data
- `OrderRequestDTO` - Create order
- `OrderResponseDTO` - Order data
- `OrderItemRequestDTO` - Order item data
- `CustomerRequestDTO` - Create/update customer
- `CustomerResponseDTO` - Customer data

---

## 🚀 NEXT STEPS

1. **Update Components to Use Services**
   - Replace static data with API calls
   - Add loading states
   - Handle errors

2. **Add Route Guards**
   - Protect authenticated routes
   - Add role-based restrictions

3. **Implement Reactive Forms**
   - Login form validation
   - Checkout form validation
   - Product forms

4. **Add Error Handling UI**
   - Toast notifications
   - Error messages
   - Retry buttons

5. **Testing**
   - Test all API endpoints
   - Handle edge cases
   - Error scenarios

---

## 📌 IMPORTANT NOTES

⚠️ **CORS Configuration Required**
Your Spring Boot backend must allow requests from `http://localhost:4200`

Add to Spring Boot:
```java
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
            }
        };
    }
}
```

⚠️ **JWT Token Format**
The token service expects JWT with these claims:
- `sub` or `username` - for username
- `roles` or `authorities` - for user roles
- `exp` - for expiration time

---

## ✅ STATUS: BACKEND INTEGRATION INFRASTRUCTURE COMPLETE!

All services, interceptors, guards, and configurations are ready.
Next phase: Integrate services into components and replace static data.
