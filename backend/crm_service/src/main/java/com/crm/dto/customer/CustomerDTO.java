package com.crm.dto.customer;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String company;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CustomerDTO(Long id, String name, String email, String phone, String company, String position, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.company = company;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}