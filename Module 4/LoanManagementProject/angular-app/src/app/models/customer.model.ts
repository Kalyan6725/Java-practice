export interface Customer {
  id: string;
  name: string;
  email: string;
  phone: string;
  address?: string;
  city?: string;
  state?: string;
  pincode?: string;
  dateOfBirth?: string;
  panCard?: string;
  aadharCard?: string;
  registeredDate: string;
  status: 'ACTIVE' | 'INACTIVE' | 'BLOCKED';
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: 'CUSTOMER' | 'ADMIN' | 'MANAGER' | 'UNDERWRITER';
  phone?: string;
}
