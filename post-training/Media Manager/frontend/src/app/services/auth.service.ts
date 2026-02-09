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
          return true;
        }
        return false;
      }),
      catchError(() => of(false))
    );
  }

  getToken() { return this.token || localStorage.getItem('jwt'); }
}
