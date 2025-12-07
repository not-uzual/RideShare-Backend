package com.ujjwal.rideshare.controller;

import com.ujjwal.rideshare.dto.CreateRideRequest;
import com.ujjwal.rideshare.dto.RideResponse;
import com.ujjwal.rideshare.service.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<RideResponse> createRide(@Valid @RequestBody CreateRideRequest request, Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(rideService.createRide(request, username));
    }

    @GetMapping("/user/rides")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<RideResponse>> getUserRides(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(rideService.getUserRides(username));
    }

    @PostMapping("/rides/{id}/complete")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_DRIVER')")
    public ResponseEntity<RideResponse> completeRide(@PathVariable String id, Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("");
        return ResponseEntity.ok(rideService.completeRide(id, username, role));
    }
}
