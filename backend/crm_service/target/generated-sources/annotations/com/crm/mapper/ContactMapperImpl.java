package com.crm.mapper;

import com.crm.domain.model.Contact;
import com.crm.dto.ContactDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T02:32:29+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Microsoft)"
)
@Component
public class ContactMapperImpl implements ContactMapper {

    @Override
    public ContactDto toDto(Contact contact) {
        if ( contact == null ) {
            return null;
        }

        ContactDto contactDto = new ContactDto();

        contactDto.setId( contact.getId() );
        contactDto.setFirstName( contact.getFirstName() );
        contactDto.setLastName( contact.getLastName() );
        contactDto.setEmail( contact.getEmail() );
        contactDto.setPhone( contact.getPhone() );
        contactDto.setCompany( contact.getCompany() );
        contactDto.setPosition( contact.getPosition() );
        contactDto.setCreatedAt( contact.getCreatedAt() );
        contactDto.setUpdatedAt( contact.getUpdatedAt() );

        return contactDto;
    }

    @Override
    public Contact toEntity(ContactDto dto) {
        if ( dto == null ) {
            return null;
        }

        Contact contact = new Contact();

        contact.setFirstName( dto.getFirstName() );
        contact.setLastName( dto.getLastName() );
        contact.setEmail( dto.getEmail() );
        contact.setPhone( dto.getPhone() );
        contact.setCompany( dto.getCompany() );
        contact.setPosition( dto.getPosition() );

        return contact;
    }

    @Override
    public void updateEntity(ContactDto dto, Contact contact) {
        if ( dto == null ) {
            return;
        }

        contact.setFirstName( dto.getFirstName() );
        contact.setLastName( dto.getLastName() );
        contact.setEmail( dto.getEmail() );
        contact.setPhone( dto.getPhone() );
        contact.setCompany( dto.getCompany() );
        contact.setPosition( dto.getPosition() );
    }
}
