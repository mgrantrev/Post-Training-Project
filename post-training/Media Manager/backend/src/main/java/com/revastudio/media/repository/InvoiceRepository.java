package com.revastudio.media.repository;

import com.revastudio.media.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    @Query("SELECT SUM(i.total) FROM Invoice i")
    BigDecimal getTotalRevenue();

    List<Invoice> findByCustomerIdIn(List<Integer> customerIds);
}
