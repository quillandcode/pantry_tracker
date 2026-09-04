package com.quillandcode.pantry_tracker.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CreateVisitRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenValidRequest_thenNoViolations() {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", "75009", 3);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void whenValid9DigitZipCode_thenNoViolations() {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", "75009-1234", 3);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void whenBlankVisitorName_thenViolation(String invalidName) {
        CreateVisitRequest request = new CreateVisitRequest(invalidName, "75009", 3);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("visitorName")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"7500", "ABCDE", "750091234", "75009-", "75009-12345"})
    void whenInvalidZipCode_thenViolation(String invalidZip) {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", invalidZip, 3);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("zipCode")));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void whenHouseholdSizeLessThanOne_thenViolation(int invalidSize) {
        CreateVisitRequest request = new CreateVisitRequest("Jane Doe", "75009", invalidSize);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("householdSize")));
    }

    @Test
    void whenNullValues_thenViolations() {
        CreateVisitRequest request = new CreateVisitRequest(null, null, null);
        Set<ConstraintViolation<CreateVisitRequest>> violations = validator.validate(request);

        assertEquals(3, violations.size());
    }
}