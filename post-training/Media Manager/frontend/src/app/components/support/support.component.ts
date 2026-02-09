import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="support-container">
      <header>
        <h2>Support Case Management</h2>
        <div class="role-badge">{{ role() }} View</div>
      </header>

      <!-- Customer View -->
      <div *ngIf="role() === 'Customer'">
        <button data-testid="new-ticket-button" (click)="showForm.set(true)" *ngIf="!showForm()">New Support Ticket</button>
        
        <div *ngIf="showForm()" class="ticket-form">
          <input [(ngModel)]="newTicket.subject" data-testid="ticket-subject" placeholder="Subject">
          <textarea [(ngModel)]="newTicket.description" data-testid="ticket-description" placeholder="Description"></textarea>
          <button data-testid="submit-ticket-button" (click)="onSubmit()">Submit Ticket</button>
          <button (click)="showForm.set(false)">Cancel</button>
        </div>

        <div class="ticket-list">
          <h3>My Tickets</h3>
          <div *ngFor="let ticket of tickets()" (click)="selectedTicket.set(ticket)" class="ticket-item" data-testid="ticket-item">
            <span class="subject">{{ ticket.subject }}</span> - <span class="status">{{ ticket.status }}</span>
          </div>
        </div>
      </div>

      <!-- Employee View -->
      <div *ngIf="role() === 'Employee'">
        <div class="ticket-list" data-testid="employee-ticket-list">
          <h3>All Customer Tickets</h3>
          <div *ngFor="let ticket of tickets()" (click)="selectedTicket.set(ticket)" class="ticket-item" data-testid="employee-ticket-item">
            <span class="subject">{{ ticket.subject }}</span> - 
            <span class="status" data-testid="ticket-status">{{ ticket.status }}</span>
          </div>
        </div>
      </div>

      <!-- Ticket Detail View (Shared) -->
      <div *ngIf="selectedTicket()" class="ticket-detail" data-testid="ticket-detail-view">
        <hr>
        <h4>{{ selectedTicket().subject }}</h4>
        <p><strong>Description:</strong> {{ selectedTicket().description }}</p>
        
        <div class="responses">
          <h5>Responses</h5>
          <div *ngFor="let resp of selectedTicket().responses" class="response" data-testid="ticket-response">
            <p>{{ resp.message }} - <i>{{ resp.employeeName }}</i></p>
          </div>
          <p *ngIf="!selectedTicket().responses?.length">No responses yet.</p>
        </div>

        <!-- Employee Actions -->
        <div *ngIf="role() === 'Employee'" class="employee-actions">
          <textarea [(ngModel)]="employeeResponse" data-testid="ticket-response-input" placeholder="Type your response..."></textarea>
          <button data-testid="submit-response-button" (click)="onRespond()">Send Response</button>
          <button data-testid="close-ticket-button" (click)="onClose()">Close Ticket</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .support-container { padding: 20px; font-family: sans-serif; }
    header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
    .role-badge { background: #eee; padding: 5px 10px; border-radius: 4px; font-weight: bold; }
    .ticket-form { margin-bottom: 20px; border: 1px solid #ddd; padding: 15px; border-radius: 8px; }
    .ticket-form input, .ticket-form textarea { width: 100%; margin-bottom: 10px; padding: 8px; box-sizing: border-box; }
    .ticket-item { cursor: pointer; padding: 12px; border: 1px solid #eee; margin-bottom: 8px; border-radius: 4px; }
    .ticket-item:hover { background-color: #f5f5f5; }
    .ticket-item .subject { font-weight: bold; }
    .responses { margin-top: 15px; background: #fafafa; padding: 10px; border-radius: 4px; }
    .response { border-bottom: 1px solid #eee; padding: 5px 0; }
    .employee-actions { margin-top: 20px; padding-top: 15px; border-top: 2px solid #eee; }
    .employee-actions textarea { width: 100%; height: 80px; margin-bottom: 10px; padding: 8px; box-sizing: border-box; }
    button { padding: 8px 16px; margin-right: 10px; cursor: pointer; }
  `]
})
export class SupportComponent implements OnInit {
  role = signal<string | null>('');
  tickets = signal<any[]>([]);
  selectedTicket = signal<any>(null);
  showForm = signal(false);
  newTicket = { subject: '', description: '' };
  employeeResponse = '';

  constructor(private customerService: CustomerService) { }

  ngOnInit() {
    this.role.set(localStorage.getItem('user_role'));
    this.loadTickets();
  }

  loadTickets() {
    if (this.role() === 'Customer') {
      this.customerService.getTickets(1).subscribe(tickets => this.tickets.set(tickets));
    } else if (this.role() === 'Employee') {
      this.customerService.getAllTickets().subscribe(tickets => this.tickets.set(tickets));
    }
  }

  onSubmit() {
    this.customerService.createTicket(1, this.newTicket).subscribe(ticket => {
      this.tickets.set([...this.tickets(), ticket]);
      this.showForm.set(false);
      this.newTicket = { subject: '', description: '' };
    });
  }

  onRespond() {
    const ticket = this.selectedTicket();
    if (!ticket || !this.employeeResponse) return;
    this.customerService.addResponse(ticket.id, { message: this.employeeResponse }).subscribe(response => {
      if (!ticket.responses) ticket.responses = [];
      ticket.responses.push(response);
      this.selectedTicket.set({ ...ticket });
      this.employeeResponse = '';
    });
  }

  onClose() {
    const ticket = this.selectedTicket();
    if (!ticket) return;
    this.customerService.closeTicket(ticket.id).subscribe(() => {
      ticket.status = 'Closed';
      this.selectedTicket.set({ ...ticket });
      this.tickets.set(this.tickets().map(t => t.id === ticket.id ? { ...ticket } : t));
    });
  }
}
