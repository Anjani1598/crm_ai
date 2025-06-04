package com.crm.mapper;

import com.crm.domain.model.Customer;
import com.crm.dto.customer.CustomerDTO;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T02:32:29+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;
        String phone = null;
        String company = null;
        String position = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = customer.getId();
        name = customer.getName();
        email = customer.getEmail();
        phone = customer.getPhone();
        company = customer.getCompany();
        position = customer.getPosition();
        createdAt = customer.getCreatedAt();
        updatedAt = customer.getUpdatedAt();

        CustomerDTO customerDTO = new CustomerDTO( id, name, email, phone, company, position, createdAt, updatedAt );

        return customerDTO;
    }

    @Override
    public Customer toEntity(CustomerDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Customer customer = new Customer();

        customer.setName( dto.getName() );
        customer.setEmail( dto.getEmail() );
        customer.setPhone( dto.getPhone() );
        customer.setCompany( dto.getCompany() );
        customer.setPosition( dto.getPosition() );

        return customer;
    }

    @Override
    public void updateEntity(CustomerDTO dto, Customer customer) {
        if ( dto == null ) {
            return;
        }

        customer.setName( dto.getName() );
        customer.setEmail( dto.getEmail() );
        customer.setPhone( dto.getPhone() );
        customer.setCompany( dto.getCompany() );
        customer.setPosition( dto.getPosition() );
    }
}
