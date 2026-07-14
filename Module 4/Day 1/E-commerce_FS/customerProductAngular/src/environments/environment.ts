export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  endpoints: {
    auth: {
      login: '/auth/login'
    },
    products: {
      base: '/api/products',
      byId: '/api/products/:id',
      byCategory: '/api/products/category/:category',
      byBrand: '/api/products/brand/:brand',
      byPriceRange: '/api/products/price-range',
      sortByPriceAsc: '/api/products/sort/price-asc',
      sortByPriceDesc: '/api/products/sort/price-desc',
      sortByName: '/api/products/sort/name',
      sortByCategory: '/api/products/sort/category'
    },
    orders: {
      base: '/api/orders',
      byId: '/api/orders/:id'
    },
    orderItems: {
      base: '/api/order-items',
      byId: '/api/order-items/:id'
    },
    customers: {
      base: '/api/customers',
      byId: '/api/customers/:id',
      updateName: '/api/customers/update/:id/:name'
    },
    admin: {
      userCustomerMapping: '/api/admin/user-customer/:username/:customerId'
    }
  }
};
