import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  http: HttpClient = inject(HttpClient);
  private apiUrl: string = "http://localhost:8080";

  getUserData(): Observable<string> {
    console.log('🔵 getUserData() → Calling GET /user');
    return this.http.get(`${this.apiUrl}/user`, { responseType: 'text' });
  }

  getAdminData(): Observable<string> {
    console.log('🔴 getAdminData() → Calling GET /admin');
    return this.http.get(`${this.apiUrl}/admin`, { responseType: 'text' });
  }
}
