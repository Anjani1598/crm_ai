package com.crm.controller;

import com.crm.domain.service.ContactService;
import com.crm.dto.ContactDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @InjectMocks
    private ContactController contactController;

    private ContactDto sampleContactDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleContactDto = new ContactDto();
        sampleContactDto.setId(1L);
        sampleContactDto.setName("John Doe");
        sampleContactDto.setEmail("john.doe@example.com");
    }

    @Test
    void getAllContacts_ShouldReturnPageOfContacts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ContactDto> contactPage = new PageImpl<>(Arrays.asList(sampleContactDto));
        when(contactService.findAll(any(Pageable.class))).thenReturn(contactPage);

        ResponseEntity<Page<ContactDto>> response = contactController.getAllContacts(pageable);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getTotalElements());
        verify(contactService).findAll(pageable);
    }

    @Test
    void getContactById_WhenContactExists_ShouldReturnContact() {
        when(contactService.findById(1L)).thenReturn(Optional.of(sampleContactDto));

        ResponseEntity<ContactDto> response = contactController.getContactById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleContactDto, response.getBody());
        verify(contactService).findById(1L);
    }

    @Test
    void getContactById_WhenContactDoesNotExist_ShouldReturnNotFound() {
        when(contactService.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<ContactDto> response = contactController.getContactById(1L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(contactService).findById(1L);
    }

    @Test
    void getContactByEmail_WhenContactExists_ShouldReturnContact() {
        when(contactService.findByEmail("john.doe@example.com")).thenReturn(Optional.of(sampleContactDto));

        ResponseEntity<ContactDto> response = contactController.getContactByEmail("john.doe@example.com");

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleContactDto, response.getBody());
        verify(contactService).findByEmail("john.doe@example.com");
    }

    @Test
    void getContactByEmail_WhenContactDoesNotExist_ShouldReturnNotFound() {
        when(contactService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        ResponseEntity<ContactDto> response = contactController.getContactByEmail("nonexistent@example.com");

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());
        verify(contactService).findByEmail("nonexistent@example.com");
    }

    @Test
    void createContact_ShouldReturnCreatedContact() {
        when(contactService.create(any(ContactDto.class))).thenReturn(sampleContactDto);

        ResponseEntity<?> response = contactController.createContact(sampleContactDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleContactDto, response.getBody());
        verify(contactService).create(sampleContactDto);
    }

    @Test
    void updateContact_ShouldReturnUpdatedContact() {
        when(contactService.update(anyLong(), any(ContactDto.class))).thenReturn(sampleContactDto);

        ResponseEntity<ContactDto> response = contactController.updateContact(1L, sampleContactDto);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleContactDto, response.getBody());
        verify(contactService).update(1L, sampleContactDto);
    }

    @Test
    void deleteContact_ShouldReturnNoContent() {
        doNothing().when(contactService).delete(1L);

        ResponseEntity<Void> response = contactController.deleteContact(1L);

        assertNotNull(response);
        assertEquals(204, response.getStatusCode().value());
        verify(contactService).delete(1L);
    }
} 