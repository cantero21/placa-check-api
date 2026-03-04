package com.placacheck.api.service;

import com.placacheck.api.dto.VehicleRequest;
import com.placacheck.api.dto.VehicleResponse;
import com.placacheck.api.entity.Vehicle;
import com.placacheck.api.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleResponse create(VehicleRequest request) {
        if (vehicleRepository.findByLicensePlateIgnoreCase(request.licensePlate()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un vehículo con la placa: " + request.licensePlate());
        }

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.licensePlate().toUpperCase())
                .ownerName(request.ownerName())
                .workArea(request.workArea())
                .build();

        return toResponse(vehicleRepository.save(vehicle));
    }

    public List<VehicleResponse> searchByPlate(String plate) {
        return vehicleRepository.findByLicensePlateContainingIgnoreCase(plate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<VehicleResponse> searchByOwner(String owner) {
        return vehicleRepository.findByOwnerNameContainingIgnoreCase(owner)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado con id: " + id));

        vehicle.setLicensePlate(request.licensePlate().toUpperCase());
        vehicle.setOwnerName(request.ownerName());
        vehicle.setWorkArea(request.workArea());

        return toResponse(vehicleRepository.save(vehicle));
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new IllegalArgumentException("Vehículo no encontrado con id: " + id);
        }
        vehicleRepository.deleteById(id);
    }

    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private VehicleResponse toResponse(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getLicensePlate(),
                vehicle.getOwnerName(),
                vehicle.getWorkArea()
        );
    }
}