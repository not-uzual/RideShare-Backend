package com.ujjwal.rideshare.controller;

import com.ujjwal.rideshare.dto.RideResponse;
import com.ujjwal.rideshare.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverController {

    private final RideService rideService;

    @GetMapping("/rides/requests")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<List<RideResponse>> getPendingRides() {
        return ResponseEntity.ok(rideService.getPendingRides());
    }

    @PostMapping("/rides/{id}/accept")
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    public ResponseEntity<RideResponse> acceptRide(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(rideService.acceptRide(id, username));
    }
}
