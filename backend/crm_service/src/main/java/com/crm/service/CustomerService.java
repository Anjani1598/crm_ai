package com.crm.service;

import com.crm.dto.customer.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerDTO getCustomerProfile(Long customerId);
    CustomerDTO updateCustomerProfile(Long customerId, CustomerDTO customerDTO);
    Page<CustomerDTO> getAllCustomers(Pageable pageable);
    CustomerDTO getCustomerByEmail(String email);
    boolean existsByEmail(String email);
} 