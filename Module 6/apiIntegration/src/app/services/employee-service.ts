import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import EmployeeDTO from '../dto/EmployeeDTO';
import { EmployeeReqDTO } from '../dto/EmployeeReqDTO';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  employees: WritableSignal<EmployeeDTO[]> = signal([]);
  private http:HttpClient =inject(HttpClient);
  private apiUrl = 'http://localhost:8080/employees';
  getAll(): Observable<EmployeeDTO[]> {
    return this.http.get<EmployeeDTO[]>(`${this.apiUrl}/getAll`);
  }
  getById(id: number): Observable<EmployeeDTO> {
    return this.http.get<EmployeeDTO>(`${this.apiUrl}/getById/${id}`);
  }
  add(addEmployee: EmployeeReqDTO): Observable<EmployeeDTO> {
    return this.http.post<EmployeeDTO>(`${this.apiUrl}/add`, addEmployee);
  }
  remove(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/deleteById/${id}`);
  }
}
