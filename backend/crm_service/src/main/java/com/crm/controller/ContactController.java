package com.crm.controller;

import com.crm.domain.service.ContactService;
import com.crm.dto.ContactDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Tag(name = "Contact Management", description = "APIs for managing contacts")
public class ContactController {
    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Get all contacts", description = "Retrieves a paginated list of all contacts")
    public ResponseEntity<Page<ContactDto>> getAllContacts(Pageable pageable) {
        return ResponseEntity.ok(contactService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact by ID", description = "Retrieves a contact by their ID")
    public ResponseEntity<ContactDto> getContactById(@PathVariable Long id) {
        return contactService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get contact by email", description = "Retrieves a contact by their email address")
    public ResponseEntity<ContactDto> getContactByEmail(@PathVariable String email) {
        return contactService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new contact", description = "Creates a new contact")
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactDto contactDto) {
        try {
            return ResponseEntity.ok(contactService.create(contactDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contact", description = "Updates an existing contact")
    public ResponseEntity<ContactDto> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(contactService.update(id, contactDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact", description = "Deletes a contact by their ID")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 