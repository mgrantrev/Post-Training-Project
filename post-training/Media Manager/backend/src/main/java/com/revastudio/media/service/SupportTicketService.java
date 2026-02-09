package com.revastudio.media.service;

import com.revastudio.media.entity.SupportResponse;
import com.revastudio.media.entity.SupportTicket;
import com.revastudio.media.repository.SupportResponseRepository;
import com.revastudio.media.repository.SupportTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository repo;
    private final SupportResponseRepository responseRepo;

    public SupportTicket createTicket(SupportTicket t) {
        return repo.save(t);
    }

    public List<SupportTicket> findAll() {
        return repo.findAll();
    }

    public List<SupportTicket> findByCustomer(Integer customerId) {
        return repo.findByCustomerId(customerId);
    }

    public List<SupportTicket> findByEmployee(Integer employeeId) {
        return repo.findByEmployeeId(employeeId);
    }

    public SupportResponse addResponse(Long ticketId, SupportResponse response) {
        return repo.findById(ticketId).map(t -> {
            response.setTicket(t);
            return responseRepo.save(response);
        }).orElse(null);
    }

    public SupportTicket closeTicket(Long id) {
        return repo.findById(id).map(t -> {
            t.setStatus("CLOSED");
            return repo.save(t);
        }).orElse(null);
    }
}
