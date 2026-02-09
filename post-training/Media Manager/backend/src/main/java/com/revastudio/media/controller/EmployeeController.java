package com.revastudio.media.controller;

import com.revastudio.media.entity.Invoice;
import com.revastudio.media.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@CrossOrigin
public class EmployeeController {

    private final MetricsService metricsService;

    @GetMapping("/{employeeId}/metrics")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Map<String, Object>> getMetrics(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(metricsService.getEmployeeMetrics(employeeId));
    }

    @GetMapping("/{employeeId}/sales")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<Invoice>> getSalesDetails(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(metricsService.getCustomerSalesDetails(employeeId));
    }
}
