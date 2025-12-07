package com.ujjwal.rideshare.service;

import com.ujjwal.rideshare.dto.CreateRideRequest;
import com.ujjwal.rideshare.dto.RideResponse;
import com.ujjwal.rideshare.exception.BadRequestException;
import com.ujjwal.rideshare.exception.NotFoundException;
import com.ujjwal.rideshare.model.Ride;
import com.ujjwal.rideshare.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;

    public RideResponse createRide(CreateRideRequest request, String username) {
        Ride ride = new Ride();
        ride.setUserId(username);
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDropLocation(request.getDropLocation());
        ride.setStatus("REQUESTED");
        ride.setCreatedAt(new Date());

        Ride savedRide = rideRepository.save(ride);
        return mapToResponse(savedRide);
    }

    public List<RideResponse> getUserRides(String username) {
        List<Ride> rides = rideRepository.findByUserId(username);
        return rides.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<RideResponse> getPendingRides() {
        List<Ride> rides = rideRepository.findByStatus("REQUESTED");
        return rides.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public RideResponse acceptRide(String rideId, String driverUsername) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (!ride.getStatus().equals("REQUESTED")) {
            throw new BadRequestException("Ride is not available for acceptance");
        }

        ride.setDriverId(driverUsername);
        ride.setStatus("ACCEPTED");

        Ride updatedRide = rideRepository.save(ride);
        return mapToResponse(updatedRide);
    }

    public RideResponse completeRide(String rideId, String username, String role) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new NotFoundException("Ride not found"));

        if (role.equals("ROLE_USER") && !ride.getUserId().equals(username)) {
            throw new BadRequestException("You are not authorized to complete this ride");
        }
        if (role.equals("ROLE_DRIVER") && !ride.getDriverId().equals(username)) {
            throw new BadRequestException("You are not authorized to complete this ride");
        }

        if (!ride.getStatus().equals("ACCEPTED")) {
            throw new BadRequestException("Only accepted rides can be completed");
        }

        ride.setStatus("COMPLETED");
        Ride updatedRide = rideRepository.save(ride);
        return mapToResponse(updatedRide);
    }

    private RideResponse mapToResponse(Ride ride) {
        return new RideResponse(
                ride.getId(),
                ride.getUserId(),
                ride.getDriverId(),
                ride.getPickupLocation(),
                ride.getDropLocation(),
                ride.getStatus(),
                ride.getCreatedAt()
        );
    }
}
