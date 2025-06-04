package com.crm.controller;

import com.crm.dto.customer.CustomerDTO;
import com.crm.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/profile")
    @Operation(summary = "Get customer profile", description = "Retrieves the authenticated customer's profile details")
    public ResponseEntity<CustomerDTO> getCustomerProfile(@RequestParam Long customerId) {
        return ResponseEntity.ok(customerService.getCustomerProfile(customerId));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update customer profile", description = "Updates the authenticated customer's profile details")
    public ResponseEntity<CustomerDTO> updateCustomerProfile(
            @RequestParam Long customerId,
            @Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomerProfile(customerId, customerDTO));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customers", description = "Retrieves a paginated list of all customers")
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        return ResponseEntity.ok(customerService.getAllCustomers(pageable));
    }

    @GetMapping("/by-email/{email}")
    @Operation(summary = "Get customer by email", description = "Retrieves a customer's details by their email")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }
} 