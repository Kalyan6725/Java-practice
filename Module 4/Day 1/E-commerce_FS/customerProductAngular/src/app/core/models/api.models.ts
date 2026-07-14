// ==================== AUTH MODELS ====================

export interface JwtRequestDTO {
  username: string;
  password: string;
}

export interface JwtResponseDTO {
  token: string;
}

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

// ==================== PRODUCT MODELS ====================

export interface ProductRequestDTO {
  name: string;
  brand: string;
  category: string;
  price: number;
  stock: number;
}

export interface ProductResponseDTO {
  id: number;
  name: string;
  brand: string;
  category: string;
  price: number;
  stock: number;
  // Optional frontend properties (not from backend)
  image?: string;
  description?: string;
}

// ==================== CUSTOMER MODELS ====================

export interface CustomerRequestDTO {
  name: string;
  orders?: any[];
}

export interface CustomerResponseDTO {
  id: number;
  name: string;
  orders?: OrderSummaryDTO[];
}

// ==================== ORDER MODELS ====================

export interface OrderItemCreateRequestDTO {
  quantity: number;
  productId: number;
}

export interface OrderRequestDTO {
  orderDate: string; // ISO date string
  customerId: number;
  orderItems: OrderItemCreateRequestDTO[];
}

export interface OrderItemResponseDTO {
  id: number;
  quantity: number;
  orderId: number;
  productId: number;
}

export interface OrderResponseDTO {
  id: number;
  orderDate: string;
  customerId: number;
  orderItems: OrderItemResponseDTO[];
}

export interface OrderSummaryDTO {
  id: number;
  orderDate: string;
  orderItems: OrderItemResponseDTO[];
}

// ==================== ORDER ITEM MODELS ====================

export interface OrderItemRequestDTO {
  quantity: number;
  orderId: number;
  productId: number;
}

// ==================== API RESPONSE WRAPPER ====================

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  error?: string;
}

// ==================== ERROR RESPONSE ====================

export interface ErrorResponse {
  message: string;
  status: number;
  timestamp: string;
  path?: string;
}
