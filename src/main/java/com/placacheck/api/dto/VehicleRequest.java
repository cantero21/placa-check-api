package com.placacheck.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VehicleRequest(

        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 10, message = "La placa no puede exceder 10 caracteres")
        String licensePlate,

        @NotBlank(message = "El nombre del propietario es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String ownerName,

        @NotBlank(message = "El área de trabajo es obligatoria")
        @Size(max = 100, message = "El área no puede exceder 100 caracteres")
        String workArea
) {}