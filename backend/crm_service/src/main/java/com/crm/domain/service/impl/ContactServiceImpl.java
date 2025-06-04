package com.crm.domain.service.impl;

import com.crm.domain.model.Contact;
import com.crm.domain.repository.ContactRepository;
import com.crm.domain.service.ContactService;
import com.crm.domain.service.EmailService;
import com.crm.dto.ContactDto;
import com.crm.mapper.ContactMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final EmailService emailService;
    private final RestTemplate restTemplate;

    @Value("${app.product.name:CRM System}")
    private String productName;

    @Value("${app.login.url:http://localhost:3000/login}")
    private String loginUrl;

    @Value("${app.support.email:support@company.com}")
    private String supportEmail;

    @Value("${app.help.center.url:http://localhost:3000/help}")
    private String helpCenterUrl;

    @Value("${app.company.name:Your Company}")
    private String companyName;

    @Value("${serviceURL.auth-service:http://localhost:8083}")
    private String authServiceUrl;

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDto> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDto> findById(Long id) {
        return contactRepository.findById(id)
                .map(contact -> {
                    ContactDto contactDto = contactMapper.toDto(contact);
                    
                    // Fetch interactions for the contact

                    // Populate interaction summary fields

                    // Calculate average response time (this is a placeholder, actual logic may vary)
                    // For now, setting a dummy value or leaving it null/calculating later
                    // contactDto.setAverageResponseTime("N/A"); 
                    

                                
                    // Fetch and set recent activities (e.g., last 5 interactions)
                    // Limit to a reasonable number of recent activities, e.g., 5
//                    contactDto.setRecentActivities(recentActivities.size() > 5 ? recentActivities.subList(0, 5) : recentActivities);
                    
                    return contactDto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContactDto> findByEmail(String email) {
        return contactRepository.findByEmail(email)
                .map(contactMapper::toDto);
    }

    @Override
    public ContactDto create(ContactDto contactDto) {
        if (contactRepository.existsByEmail(contactDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        Contact contact = contactMapper.toEntity(contactDto);
        Contact savedContact = contactRepository.save(contact);
        
        // Generate a temporary password
        String tempPassword = generateTemporaryPassword();
        
        // Register user in auth service
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("userName", contact.getEmail());
            requestBody.put("userEmail", contact.getEmail());
            requestBody.put("userMobileNo", contact.getPhone() != null ? contact.getPhone() : "");
            requestBody.put("userPassword", tempPassword);
            requestBody.put("userRole", "ROLE_CUSTOMER");
            requestBody.put("contactId",savedContact.getId());

            log.info("register request {}",requestBody);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            restTemplate.postForEntity(authServiceUrl + "/auth/sign-up", request, String.class);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent contact creation from failing
            System.err.println("Failed to register user in auth service: " + e.getMessage());
        }
        
        // Send credentials email
        String fullName = (contact.getFirstName() != null ? contact.getFirstName() : "") + 
                         (contact.getLastName() != null ? " " + contact.getLastName() : "");
        fullName = fullName.trim().isEmpty() ? "User" : fullName;

        log.info("password {}",tempPassword);

        emailService.sendCredentialsEmail(
            fullName,
            contact.getEmail(),
            contact.getEmail(), // Using email as username
            tempPassword,
            productName,
            loginUrl,
            supportEmail,
            helpCenterUrl,
            companyName
        );

        
        return contactMapper.toDto(savedContact);
    }

    private String generateTemporaryPassword() {
        // Generate a random 8-character password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }

    @Override
    public ContactDto update(Long id, ContactDto contactDto) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found"));
        
        if (!contact.getEmail().equals(contactDto.getEmail()) && 
            contactRepository.existsByEmail(contactDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        contactMapper.updateEntity(contactDto, contact);
        return contactMapper.toDto(contactRepository.save(contact));
    }

    @Override
    public void delete(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new EntityNotFoundException("Contact not found");
        }
        contactRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return contactRepository.existsByEmail(email);
    }
} 