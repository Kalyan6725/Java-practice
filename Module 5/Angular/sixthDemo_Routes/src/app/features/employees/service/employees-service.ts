import { Injectable, signal, WritableSignal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { EmployeeDTO } from '../../../dto/EmployeeDTO';
import { Observable } from 'rxjs';
import { EmployeeReqDTO } from '../../../dto/EmployeeReqDTO';

@Injectable({
  providedIn: 'root'
})
export class EmployeesService {
    employeesList:WritableSignal<EmployeeDTO[]> = signal([]);
    http: HttpClient = inject(HttpClient);
    getEmployees():Observable<EmployeeDTO[]> {
        return this.http.get<EmployeeDTO[]>('http://localhost:8080/employees/getAll');
    }
    getEmployeeById(id: number):Observable<EmployeeDTO> {
        return this.http.get<EmployeeDTO>(`http://localhost:8080/employees/${id}`);
    }

    addEmployee(employee: EmployeeReqDTO):Observable<EmployeeDTO> {
        return this.http.post<EmployeeDTO>('http://localhost:8080/employees/add', employee);
    }

    updateEmployee(employee: EmployeeDTO):Observable<EmployeeDTO> {
        return this.http.put<EmployeeDTO>(`http://localhost:8080/employees/${employee.id}`, employee);
    }

    deleteEmployee(id: number):Observable<any> {
        return this.http.delete(`http://localhost:8080/employees/deleteById/${id}`, { responseType: 'text' });
    }
}