import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map, of, catchError } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private token: string | null = null;

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<boolean> {
    return this.http.post<any>('/api/auth/login', { username, password }).pipe(
      map((data: any) => {
        if (data.token) {
          this.token = data.token;
          localStorage.setItem('jwt', data.token);
          const roles = this.getRolesFromToken(data.token);
          localStorage.setItem('user_roles', JSON.stringify(roles));
          return true;
        }
        return false;
      }),
      catchError(() => of(false))
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post<any>('/api/auth/register', userData);
  }

  private getRolesFromToken(token: string): string[] {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.roles || [];
    } catch (e) {
      return [];
    }
  }

  getRoles(): string[] {
    const roles = localStorage.getItem('user_roles');
    return roles ? JSON.parse(roles) : [];
  }

  getToken() { return this.token || localStorage.getItem('jwt'); }
}
