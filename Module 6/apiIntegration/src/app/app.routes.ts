import { Routes } from '@angular/router';

export const routes: Routes = [
    { 
    path: '', 
    redirectTo: 'employees', 
    pathMatch: 'full' 
  },
  
  // Dashboard view mapping to GET /employees
  { 
    path: 'employees', 
    loadComponent: () => import('./components/employee-dashboard-component/employee-dashboard-component')
      .then(m => m.EmployeeDashboardComponent),
    title: 'Employee Directory - NorthernArc Portal'
  },
  
  // Create view mapping to POST /employees
  { 
    path: 'employees/new', 
    loadComponent: () => import('./components/employee-form-component/employee-form-component')
      .then(m => m.EmployeeFormComponent),
    title: 'Add New Employee - NorthernArc Portal'
  },
  
  // Edit view mapping to PUT /employees/{id}
  { 
    path: 'employees/edit/:id', 
    loadComponent: () => import('./components/employee-form-component/employee-form-component')
      .then(m => m.EmployeeFormComponent),
    title: 'Edit Employee Profile - NorthernArc Portal'
  },
  
  // Wildcard fallback route to handle 404 navigation gracefully
  { 
    path: '**', 
    redirectTo: 'employees' 
  }
];
