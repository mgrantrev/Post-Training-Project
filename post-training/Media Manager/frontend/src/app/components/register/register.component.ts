import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-register',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    template: `
    <div class="register-container">
      <h2>Create an Account</h2>
      <div *ngIf="errorMessage()" class="error">{{ errorMessage() }}</div>
      <div *ngIf="successMessage()" class="success">{{ successMessage() }}</div>
      
      <form (submit)="onRegister($event)" *ngIf="!successMessage()">
        <div>
          <label for="username">Username:</label>
          <input type="text" id="username" name="username" [(ngModel)]="regData.username" required>
        </div>
        <div>
          <label for="password">Password:</label>
          <input type="password" id="password" name="password" [(ngModel)]="regData.password" required>
        </div>
        <div>
          <label for="firstName">First Name:</label>
          <input type="text" id="firstName" name="firstName" [(ngModel)]="regData.firstName" required>
        </div>
        <div>
          <label for="lastName">Last Name:</label>
          <input type="text" id="lastName" name="lastName" [(ngModel)]="regData.lastName" required>
        </div>
        <div>
          <label for="email">Email:</label>
          <input type="email" id="email" name="email" [(ngModel)]="regData.email" required>
        </div>
        <button type="submit">Register</button>
      </form>
      
      <div class="login-link">
        Already have an account? <a routerLink="/login">Login here</a>
      </div>
    </div>
  `,
    styles: [`
    .register-container { max-width: 500px; margin: 50px auto; padding: 30px; border: 1px solid #ccc; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .error { color: #dc3545; margin-bottom: 20px; padding: 10px; background: #f8d7da; border-radius: 4px; }
    .success { color: #28a745; margin-bottom: 20px; padding: 10px; background: #d4edda; border-radius: 4px; }
    div { margin-bottom: 15px; }
    label { display: block; margin-bottom: 5px; font-weight: bold; }
    input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
    button { width: 100%; padding: 12px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 1rem; }
    button:hover { background-color: #218838; }
    .login-link { margin-top: 20px; text-align: center; }
    .login-link a { color: #007bff; text-decoration: none; }
  `]
})
export class RegisterComponent {
    regData = {
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        email: ''
    };
    errorMessage = signal('');
    successMessage = signal('');

    constructor(private authService: AuthService, private router: Router) { }

    onRegister(event: Event) {
        event.preventDefault();
        this.authService.register(this.regData).subscribe({
            next: () => {
                this.successMessage.set('Registration successful! You can now login.');
                setTimeout(() => this.router.navigate(['/login']), 2000);
            },
            error: (err) => {
                this.errorMessage.set(err.error || 'Registration failed. Please try again.');
            }
        });
    }
}
