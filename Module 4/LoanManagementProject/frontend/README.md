# Northern Arc Loan Management System - Frontend

## 📋 Overview

This is a **complete static frontend prototype** for the Northern Arc Loan Management System built with HTML5, CSS3, Bootstrap 5, and vanilla JavaScript. The frontend matches the backend entities and endpoints for seamless future integration.

## 🏗️ Project Structure

```
frontend/
│
├── index.html                      # Landing page with loan products
├── login.html                      # Login page for all users
├── register.html                   # Customer registration page
├── contact.html                    # Contact us page
│
├── customer-dashboard.html         # Customer dashboard
├── loan-details.html              # Detailed loan account view
├── emi-payment.html               # EMI payment form
├── payment-success.html           # Payment confirmation page
│
├── admin-dashboard.html           # Admin/Manager dashboard
├── customers.html                 # Customer management
├── loan-accounts.html             # Loan account management
├── loan-products.html             # Loan product management
├── create-loan-account.html       # Create new loan account
│
├── css/
│   └── style.css                  # Custom styles
│
├── js/
│   └── script.js                  # Main JavaScript functions
│
└── images/                        # Image assets (if any)
```

## 🎨 Technology Stack

- **HTML5** - Semantic markup
- **CSS3** - Custom styling
- **Bootstrap 5.3.2** - Responsive UI framework
- **Bootstrap Icons 1.11.1** - Icon library
- **Vanilla JavaScript** - Client-side logic (no jQuery/React/Vue)

## 🚀 Features

### Customer Module
- ✅ Landing page with loan products
- ✅ User registration and login
- ✅ Dashboard with loan accounts overview
- ✅ Detailed loan account view
- ✅ EMI payment functionality
- ✅ Payment history
- ✅ Search and filter loans

### Admin/Manager Module
- ✅ Dashboard with statistics (matches DashboardDTO)
- ✅ Customer management (CRUD operations UI)
- ✅ Loan account management
- ✅ Loan product management
- ✅ Create loan account form
- ✅ Search, filter, and sort functionality
- ✅ Pagination UI

## 🔐 Demo Credentials

### Customer Account
- **Email:** john.smith@email.com
- **Password:** password123
- **Role:** USER

### Manager Account
- **Email:** manager@northernarc.com
- **Password:** manager123
- **Role:** MANAGER

### Admin Account
- **Email:** admin@northernarc.com
- **Password:** admin123
- **Role:** ADMIN

## 💾 Static Data Structure

The frontend uses hardcoded data that mirrors your backend entities:

### Customer Entity
- customerId, customerName, email, branch, role

### LoanProduct Entity
- loanCode, loanName, loanType (PERSONAL, HOME, VEHICLE, EDUCATION, BUSINESS)
- dailyPenaltyRate

### LoanAccount Entity
- loanAccountId, loanAmount, emiAmount, loanStartDate, emiDueDate
- status (ACTIVE, CLOSED, OVERDUE)
- customer, loanProduct

### EmiPayment Entity
- paymentId, amountPaid, penaltyPaid, paymentType (CASH, CARD, ONLINE, UPI)
- paymentDate

## 🎯 How to Use

### 1. Open the Application
Simply open `index.html` in a modern web browser (Chrome, Firefox, Edge, Safari)

### 2. Navigation Flow

**Customer Journey:**
```
Landing Page → Register/Login → Customer Dashboard → View Loans → Loan Details → Pay EMI → Success
```

**Admin Journey:**
```
Landing Page → Login → Admin Dashboard → Manage Customers/Loans/Products → Create Loan Account
```

### 3. Testing Features

#### Customer Actions:
1. Login with customer credentials
2. View all loan accounts
3. Click "View" to see detailed loan information
4. Click "Pay EMI" to process payment (simulated)
5. View payment history

#### Admin Actions:
1. Login with admin/manager credentials
2. View dashboard statistics
3. Navigate to "Customers" to manage users
4. Navigate to "Loan Accounts" to view all loans
5. Navigate to "Loan Products" to manage products
6. Click "Create Loan Account" to add new loan

## 🔧 Customization

### Modifying Static Data

Edit the HTML files directly to update:
- Customer names and details
- Loan account information
- EMI payment history
- Dashboard statistics

### Styling Changes

All custom styles are in `css/style.css`. Key variables:
```css
:root {
    --primary-color: #0d6efd;
    --success-color: #198754;
    --danger-color: #dc3545;
}
```

### Adding New Pages

1. Create new HTML file following the existing structure
2. Include navigation bar and footer
3. Link Bootstrap CSS/JS and custom files
4. Add authentication check if needed

## 📱 Responsive Design

The application is fully responsive and works on:
- ✅ Desktop (1920px+)
- ✅ Laptop (1366px - 1920px)
- ✅ Tablet (768px - 1024px)
- ✅ Mobile (320px - 767px)

## 🔄 Future Backend Integration

This frontend is designed for easy integration:

1. **Authentication**: Replace sessionStorage with JWT tokens from backend
2. **API Calls**: Use fetch/axios to call backend endpoints
3. **Dynamic Data**: Replace hardcoded data with API responses
4. **Form Submissions**: POST to backend endpoints instead of static alerts

### Example Integration Points:

```javascript
// Current (Static)
const user = { email: 'john@email.com', password: 'password123' };

// Future (Dynamic)
fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
})
.then(res => res.json())
.then(data => {
    sessionStorage.setItem('token', data.token);
});
```

## 📊 Backend Alignment

### Matches these Backend Endpoints:

**Authentication:**
- POST `/api/auth/login`
- POST `/api/auth/register`

**Customer:**
- GET `/api/loan/customers`
- GET `/api/loan/customer/{customerId}`

**Loan Accounts:**
- GET `/api/loan/accounts`
- GET `/api/loan/account/{loanAccountId}`
- POST `/api/loan/account/create`

**Loan Products:**
- GET `/api/loan/products`
- GET `/api/loan/product/{loanCode}`

**EMI Payments:**
- GET `/api/loan/payments/account/{loanAccountId}`
- POST `/api/loan/payment/record`

**Dashboard:**
- GET `/api/loan/dashboard`

## 🎨 UI/UX Highlights

- **Professional Banking Theme** - Clean blue and white color scheme
- **Card-Based Layout** - Modern and organized
- **Responsive Tables** - Fully functional on mobile
- **Status Badges** - Color-coded (Active: Green, Overdue: Yellow, Closed: Grey)
- **Icon Integration** - Bootstrap Icons for visual clarity
- **Smooth Animations** - CSS transitions on hover/click
- **Form Validation** - HTML5 validation with clear error messages
- **Accessibility** - ARIA labels and semantic HTML

## 🐛 Known Limitations (Static Prototype)

- ❌ No actual authentication (session-based simulation)
- ❌ No data persistence (refresh loses session)
- ❌ No real payment processing
- ❌ Search/filter works only on visible table rows
- ❌ Pagination is UI-only (no actual pagination)
- ❌ No file uploads (document upload fields are placeholders)

## ✅ Browser Compatibility

- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

## 📝 Notes

- All data is **static** and stored in HTML/JavaScript
- Session management uses `sessionStorage` (cleared on browser close)
- No external dependencies beyond Bootstrap CDN
- All forms use `onsubmit` event handlers for simulation
- CORS not an issue since no backend calls are made

## 🎓 Learning Outcomes

This frontend demonstrates:
- Enterprise UI/UX design patterns
- Role-based access control (UI level)
- Banking application workflows
- Form validation and error handling
- Responsive design principles
- Clean code organization

## 🔮 Next Steps (Post Backend Integration)

1. Replace static data with API calls
2. Implement JWT-based authentication
3. Add real-time validation with backend
4. Implement actual file uploads
5. Add WebSocket for real-time notifications
6. Implement actual pagination with backend
7. Add comprehensive error handling
8. Implement loading states and spinners

## 📞 Support

For questions or issues related to the frontend:
- Review the code comments in HTML/CSS/JS files
- Check browser console for JavaScript errors
- Ensure you're using a modern browser
- Verify all files are in correct directory structure

---

**Built with ❤️ for Northern Arc Loan Management System**

*This is a static prototype designed to match the Java Spring Boot backend perfectly.*
