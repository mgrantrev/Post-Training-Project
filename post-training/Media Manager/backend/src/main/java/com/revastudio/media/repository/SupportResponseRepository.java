package com.revastudio.media.repository;

import com.revastudio.media.entity.SupportResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportResponseRepository extends JpaRepository<SupportResponse, Long> {
    List<SupportResponse> findByTicketId(Long ticketId);
}
