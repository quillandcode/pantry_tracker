package com.quillandcode.pantry_tracker.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateVisitRequest(
    @NotBlank(message = "First name is required")
    String firstName,

    @NotBlank(message = "Last name is required")
    String lastName,
    
    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Must be a valid 5-digit or 9-digit ZIP code")
    String zipCode,

    @NotNull(message = "Household size is required")
    @Min(value = 1, message = "Household size must be at least 1")
    Integer householdSize
) {}