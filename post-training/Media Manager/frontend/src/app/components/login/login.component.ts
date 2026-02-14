import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="login-container">
      <h2>Media Manager Login</h2>
      <div *ngIf="errorMessage()" data-testid="error-message" class="error">{{ errorMessage() }}</div>
      <form (submit)="onLogin($event)">
        <div>
          <label for="username">Username:</label>
          <input type="text" id="username" name="username" [(ngModel)]="username" data-testid="username" required>
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" name="password" [(ngModel)]="password" data-testid="password" required>
        </div>
        <button type="submit" data-testid="login-button">Login</button>
      </form>
      <div class="register-link">
        Don't have an account? <a routerLink="/register">Register here</a>
      </div>
    </div>
  `,
  styles: [`
    .login-container { max-width: 400px; margin: 100px auto; padding: 20px; border: 1px solid #ccc; border-radius: 8px; }
    .error { color: red; margin-bottom: 10px; }
    div { margin-bottom: 15px; }
    input { width: 100%; padding: 8px; box-sizing: border-box; }
    button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
    .register-link { margin-top: 15px; text-align: center; font-size: 0.9rem; }
    .register-link a { color: #007bff; text-decoration: none; }
    .register-link a:hover { text-decoration: underline; }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = signal('');

  constructor(private authService: AuthService, private router: Router) { }

  onLogin(event: Event) {
    event.preventDefault();
    this.authService.login(this.username, this.password).subscribe({
      next: (success: boolean) => {
        if (success) {
          this.router.navigate(['/dashboard']);
        } else {
          this.errorMessage.set('Invalid credentials');
        }
      },
      error: () => {
        this.errorMessage.set('Invalid credentials');
      }
    });
  }
}
