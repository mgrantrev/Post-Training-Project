package com.revastudio.media.repository;

import com.revastudio.media.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByCustomerId(Integer customerId);

    List<SupportTicket> findByEmployeeId(Integer employeeId);
}
