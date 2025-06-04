package com.crm.domain.service;

import com.crm.domain.model.Contact;
import com.crm.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactService {
    Page<ContactDto> findAll(Pageable pageable);
    Optional<ContactDto> findById(Long id);
    Optional<ContactDto> findByEmail(String email);
    ContactDto create(ContactDto contactDto);
    ContactDto update(Long id, ContactDto contactDto);
    void delete(Long id);
    boolean existsByEmail(String email);
} 