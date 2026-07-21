export type UserRole = 'USER' | 'UNDERWRITER' | 'MANAGER' | 'ADMIN';

/** POST /api/auth/login */
export interface LoginRequest {
  email: string;
  password: string;
}

/** data payload of a successful login */
export interface LoginResponse {
  token: string;
  message: string;
}

/** POST /api/auth/register (role is forced to USER by the backend) */
export interface RegisterRequest {
  customerName: string;
  email: string;
  password: string;
  phone: string;
  address: string;
  branch: string;
}

/** Decoded/derived identity of the signed-in user, kept in memory + storage. */
export interface AuthUser {
  email: string;
  role: UserRole;
  customerId?: number;
  customerName?: string;
}
