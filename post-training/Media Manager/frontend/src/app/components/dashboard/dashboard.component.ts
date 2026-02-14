import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="dashboard-container">
      <header>
        <h1>Media Manager Dashboard</h1>
        <div class="user-info">
            <div class="role-switcher" *ngIf="availableRoles().length > 1">
              <label>View as: </label>
              <select [ngModel]="selectedRole()" (ngModelChange)="onRoleChange($event)">
                <option *ngFor="let r of availableRoles()" [value]="r">{{ r }}</option>
              </select>
            </div>
            <span *ngIf="availableRoles().length === 1" data-testid="user-role">{{ selectedRole() }}</span>
            <button data-testid="logout-button" (click)="onLogout()">Logout</button>
        </div>
      </header>
      
      <div *ngIf="selectedRole() === 'CUSTOMER'">
        <nav class="sub-nav">
          <button data-testid="get-support-button" (click)="router.navigate(['/support'])">Get Support</button>
        </nav>
        <h2>My Purchased Tracks</h2>
        <div class="search-box">
          <input type="text" placeholder="Search tracks..." [ngModel]="searchText()" (ngModelChange)="searchText.set($event)" data-testid="search-input">
        </div>
        <div class="track-list">
          <div *ngFor="let track of filteredTracks()" class="track-item" data-testid="track-item">
            <span class="track-name" data-testid="track-name">{{ track.name }}</span> - 
            <span class="track-album" data-testid="track-album">{{ track.albumTitle }}</span> by 
            <span class="track-artist" data-testid="track-artist">{{ track.artistName }}</span>
          </div>
        </div>
      </div>
      
      <div *ngIf="selectedRole() === 'EMPLOYEE'" data-testid="sales-metrics-view">
        <nav class="sub-nav">
          <button data-testid="manage-tickets-button" (click)="router.navigate(['/support'])">Manage Support Tickets</button>
        </nav>
        
        <h2>Sales & Performance Metrics</h2>
        <div class="metrics-grid">
           <div class="metric-card" data-testid="sales-item">
             <h3>Total Revenue</h3>
             <p class="value">$12,450.00</p>
           </div>
           <div class="metric-card">
             <h3>Assisted Customers</h3>
             <p class="value">45</p>
           </div>
        </div>

        <div class="details-section">
            <h3>Customers Assisted & Recent Sales</h3>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Customer Name</th>
                        <th>Tracks Purchased</th>
                        <th>Billing Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <tr *ngFor="let sale of mockSales">
                        <td>{{ sale.customer }}</td>
                        <td>{{ sale.trackCount }}</td>
                        <td>{{ sale.amount | currency }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard-container { padding: 20px; font-family: sans-serif; }
    header { display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #eee; margin-bottom: 20px; padding-bottom: 10px; }
    .user-info { display: flex; align-items: center; gap: 15px; }
    .user-info span { font-weight: bold; color: #444; }
    .role-switcher { display: flex; align-items: center; gap: 8px; }
    .role-switcher select { padding: 4px; border-radius: 4px; border: 1px solid #ccc; }
    .sub-nav { margin-bottom: 20px; }
    .search-box { margin-bottom: 20px; }
    .track-item { padding: 12px; border-bottom: 1px solid #eee; }
    .track-name { font-weight: bold; }
    .metrics-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }
    .metric-card { padding: 20px; border: 1px solid #ddd; border-radius: 8px; background: #fff; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
    .metric-card h3 { margin: 0 0 10px 0; font-size: 1rem; color: #666; }
    .metric-card .value { font-size: 1.5rem; font-weight: bold; margin: 0; color: #2c3e50; }
    .data-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
    .data-table th, .data-table td { text-align: left; padding: 12px; border-bottom: 1px solid #eee; }
    .data-table th { background-color: #f8f9fa; color: #333; }
    button { padding: 8px 16px; cursor: pointer; }
    .logout-button { background: #fee; border: 1px solid #fcc; color: #c00; }
  `]
})
export class DashboardComponent implements OnInit {
  availableRoles = signal<string[]>([]);
  selectedRole = signal<string | null>(null);
  tracks = signal<any[]>([]);
  searchText = signal('');
  mockSales = [
    { customer: 'John Doe', trackCount: 5, amount: 45.00 },
    { customer: 'Jane Smith', trackCount: 3, amount: 27.00 },
    { customer: 'Robert Johnson', trackCount: 8, amount: 72.00 }
  ];

  filteredTracks = computed(() => {
    const search = this.searchText().toLowerCase();
    return this.tracks().filter(t =>
      t.name.toLowerCase().includes(search) ||
      t.albumTitle.toLowerCase().includes(search) ||
      t.artistName.toLowerCase().includes(search)
    );
  });

  constructor(public router: Router, private customerService: CustomerService, private authService: AuthService) { }

  ngOnInit() {
    const roles = this.authService.getRoles();
    this.availableRoles.set(roles);

    if (roles.length === 0) {
      this.router.navigate(['/login']);
      return;
    }

    this.selectedRole.set(roles[0]);
    this.loadDataForRole(roles[0]);
  }

  onRoleChange(newRole: string) {
    this.selectedRole.set(newRole);
    this.loadDataForRole(newRole);
  }

  loadDataForRole(role: string) {
    if (role === 'CUSTOMER') {
      this.customerService.getTracks(1).subscribe({
        next: tracks => this.tracks.set(tracks),
        error: err => console.error('Failed to load tracks:', err)
      });
    }
  }

  onLogout() {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user_roles');
    this.router.navigate(['/login']);
  }
}
