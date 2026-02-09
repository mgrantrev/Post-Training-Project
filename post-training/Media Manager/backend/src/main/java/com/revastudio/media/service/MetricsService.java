package com.revastudio.media.service;

import com.revastudio.media.entity.Invoice;
import com.revastudio.media.entity.User;
import com.revastudio.media.repository.InvoiceRepository;
import com.revastudio.media.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final InvoiceRepository invoiceRepo;
    private final UserRepository userRepo;

    public Map<String, Object> getEmployeeMetrics(Integer employeeId) {
        BigDecimal totalRevenue = invoiceRepo.getTotalRevenue();
        if (totalRevenue == null)
            totalRevenue = BigDecimal.ZERO;

        List<User> assistedCustomers = userRepo.findBySupportRepId(employeeId);

        return Map.of(
                "totalRevenue", totalRevenue,
                "assistedCustomersCount", assistedCustomers.size(),
                "assistedCustomers", assistedCustomers);
    }

    public List<Invoice> getCustomerSalesDetails(Integer employeeId) {
        List<Integer> customerIds = userRepo.findBySupportRepId(employeeId)
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());

        return invoiceRepo.findByCustomerIdIn(customerIds);
    }
}
