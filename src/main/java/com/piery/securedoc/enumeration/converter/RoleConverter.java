package com.piery.securedoc.enumeration.converter;

import com.piery.securedoc.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        return (authority != null) ? authority.getValue() : null;
    }

    @Override
    public Authority convertToEntityAttribute(String code) {
        if(code == null) return null;
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
