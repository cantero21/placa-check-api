package com.placacheck.api.dto;

public record VehicleResponse(
        Long id,
        String licensePlate,
        String ownerName,
        String workArea
) {}