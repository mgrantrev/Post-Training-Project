package com.revastudio.media.controller;

import com.revastudio.media.entity.SupportTicket;
import com.revastudio.media.service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class SupportTicketController {

    private final SupportTicketService service;

    @PostMapping("/customers/{customerId}/tickets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<SupportTicket> createTicket(@PathVariable Integer customerId,
            @RequestBody SupportTicket ticket) {
        ticket.setCustomerId(customerId);
        return ResponseEntity.ok(service.createTicket(ticket));
    }

    @GetMapping("/customers/{customerId}/tickets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<SupportTicket>> getCustomerTickets(@PathVariable Integer customerId) {
        return ResponseEntity.ok(service.findByCustomer(customerId));
    }

    @GetMapping("/employees/{employeeId}/tickets")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<SupportTicket>> getEmployeeTicketsById(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(service.findByEmployee(employeeId));
    }

    @GetMapping("/employees/tickets")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/employees/tickets/{ticketId}/responses")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<com.revastudio.media.entity.SupportResponse> addResponse(@PathVariable Long ticketId,
            @RequestBody com.revastudio.media.entity.SupportResponse response) {
        return ResponseEntity.ok(service.addResponse(ticketId, response));
    }

    @PutMapping("/employees/tickets/{id}/close")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<SupportTicket> closeTicket(@PathVariable Long id) {
        SupportTicket closed = service.closeTicket(id);
        if (closed == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(closed);
    }
}
