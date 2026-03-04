package com.placacheck.api.controller;

import com.placacheck.api.dto.VehicleRequest;
import com.placacheck.api.dto.VehicleResponse;
import com.placacheck.api.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> create(@Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> findAll() {
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleResponse>> search(
            @RequestParam(required = false) String plate,
            @RequestParam(required = false) String owner) {

        if (plate != null && !plate.isBlank()) {
            return ResponseEntity.ok(vehicleService.searchByPlate(plate));
        }
        if (owner != null && !owner.isBlank()) {
            return ResponseEntity.ok(vehicleService.searchByOwner(owner));
        }
        return ResponseEntity.ok(vehicleService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody VehicleRequest request) {
        return ResponseEntity.ok(vehicleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
