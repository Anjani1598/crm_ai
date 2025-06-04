package com.crm.domain.service;

public interface EmailService {
    void sendCredentialsEmail(String customerName, String customerEmail, String username, 
                            String password, String productName, String loginUrl, 
                            String supportEmail, String helpCenterUrl, String companyName);
} 