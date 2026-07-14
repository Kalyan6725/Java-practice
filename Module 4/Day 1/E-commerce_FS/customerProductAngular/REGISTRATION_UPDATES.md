# Frontend Registration & Auth Flow Updates

## Summary of Changes

### ✅ Backend Changes Identified
- **New Entity**: `User` (combines Customer and JpaUser)
  - Fields: `id`, `username`, `password`, `role`, `name`, `orders`
- **New Endpoint**: `POST /auth/register`
  - Request: `{ username, password, name }`
  - Response: `{ id, name, username, role }`
  - Validations: Username (4-50 chars, alphanumeric), Password (8-100 chars)

---

## Frontend Updates Applied

### 1. **API Models** ([api.models.ts](src/app/core/models/api.models.ts))
Added new interfaces:
```typescript
export interface RegisterRequestDTO {
  username: string;
  password: string;
  name: string;
}

export interface RegisterResponseDTO {
  id: number;
  name: string;
  username: string;
  role: string;
}
```

### 2. **Auth Service** ([auth.service.ts](src/app/core/services/auth.service.ts))
Added register method:
```typescript
register(registerData: RegisterRequestDTO): Observable<RegisterResponseDTO>
```
- Endpoint: `POST /auth/register`

### 3. **Register Component** (NEW)
Created complete registration flow:
- **Location**: `src/app/features/auth/pages/register/`
- **Features**:
  - Form validation matching backend requirements
  - Password confirmation with custom validator
  - Success message after registration
  - Auto-redirect to login after 2 seconds
  - Toggle to switch back to login
- **Validations**:
  - Name: Required, max 100 chars
  - Username: Required, 4-50 chars, pattern: `^[a-zA-Z0-9._-]+$`
  - Password: Required, 8-100 chars
  - Confirm Password: Must match password

### 4. **Login Component** ([login.component.ts](src/app/features/auth/pages/login/login.component.ts))
Updated with:
- `@Output() switchToRegister` event emitter
- Click handler to switch to register view

### 5. **Auth Layout** ([auth-layout.component.ts](src/app/layouts/auth-layout/auth-layout.component.ts))
**Major Change**: Now manages toggle between Login/Register
- Removed `<router-outlet>` approach
- Added `showLogin` state flag
- Conditionally renders Login OR Register component
- Handles toggle events from both components

**New Template**:
```html
<app-login *ngIf="showLogin" (switchToRegister)="toggleView()"></app-login>
<app-register *ngIf="!showLogin" (switchToLogin)="toggleView()"></app-register>
```

### 6. **Routes** ([app.routes.ts](src/app/app.routes.ts))
Simplified login route:
```typescript
{
  path: 'login',
  component: AuthLayoutComponent  // No children needed
}
```

---

## User Flow

### **Registration Flow**:
1. User navigates to `/login`
2. Auth layout shows **Login** by default
3. User clicks "Sign up" link
4. Auth layout toggles to **Register** form
5. User fills registration form
6. On success: Shows success message → Auto-switches to **Login** after 2 seconds
7. User logs in → Redirected to **Main Layout** (home page with navbar/footer)

### **Login Flow**:
1. User at `/login` sees Login form
2. User enters credentials
3. On success: Redirected to **Main Layout** (protected routes now accessible)

---

## Architecture Benefits

✅ **Single Entry Point**: `/login` handles both login and register
✅ **No Route Changes**: Toggle happens in-place without navigation
✅ **Smooth UX**: Success message → Auto-switch to login
✅ **Consistent Layout**: Both auth forms share same centered styling
✅ **Type Safety**: Full TypeScript support with DTOs matching backend

---

## Testing Checklist

- [ ] Navigate to `/login` - should show Login form
- [ ] Click "Sign up" - should toggle to Register form
- [ ] Register new user with valid data
- [ ] Verify success message appears
- [ ] Verify auto-switch to login after 2 seconds
- [ ] Login with new credentials
- [ ] Verify redirect to home page with navbar visible
- [ ] Test validation errors for all fields
- [ ] Test password mismatch validation
- [ ] Test duplicate username error from backend

---

## Next Steps (Optional Enhancements)

1. Add email field to registration (if backend supports)
2. Add "Remember Me" functionality
3. Implement password reset flow
4. Add social login integration
5. Add email verification
6. Enhance error messages with backend error details
7. Add loading spinner during registration
8. Add password strength indicator
