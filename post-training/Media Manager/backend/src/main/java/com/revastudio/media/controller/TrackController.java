package com.revastudio.media.controller;

import com.revastudio.media.dto.TrackDto;
import com.revastudio.media.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin
public class TrackController {

    private final TrackService trackService;

    @GetMapping("/{customerId}/tracks")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<TrackDto>> getTracks(@PathVariable Integer customerId) {
        return ResponseEntity.ok(trackService.getTracksForCustomer(customerId));
    }
}
