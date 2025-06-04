package com.crm.domain.service.impl;

import com.crm.domain.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${email.service.url:http://localhost:8000}")
    private String emailServiceUrl;

    private final RestTemplate restTemplate;

    public EmailServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void sendCredentialsEmail(String customerName, String customerEmail, String username,
                                   String password, String productName, String loginUrl,
                                   String supportEmail, String helpCenterUrl, String companyName) {
        String url = emailServiceUrl + "/send-credentials-email";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("customer_name", customerName);
        requestBody.put("customer_email", customerEmail);
        requestBody.put("username", username);
        requestBody.put("password", password);
        requestBody.put("product_name", productName);
        requestBody.put("login_url", loginUrl);
        requestBody.put("support_email", supportEmail);
        requestBody.put("help_center_url", helpCenterUrl);
        requestBody.put("company_name", companyName);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            // Log the error but don't throw it to prevent contact creation from failing
            // You might want to add proper logging here
            System.err.println("Failed to send credentials email: " + e.getMessage());
        }
    }
} 