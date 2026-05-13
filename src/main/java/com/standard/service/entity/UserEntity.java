package com.standard.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import jakarta.persistence.Convert;
import jakarta.persistence.EntityListeners;
import com.standard.service.converter.PiiAttributeConverter;
import com.standard.service.listener.BlindIndexEntityListener;

@Data
@Entity
@Table(name = "users")
@EntityListeners(BlindIndexEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // PII fields mapped globally via YAML
    @Convert(converter = PiiAttributeConverter.class)
    private String email;
    @Convert(converter = PiiAttributeConverter.class)
    private String nationalId;

    // Blind Index Hash fields for searching
    @jakarta.persistence.Column(columnDefinition = "CHAR(44)")
    private String emailHash;
    private Integer emailHashVersion;
    
    @jakarta.persistence.Column(columnDefinition = "CHAR(44)")
    private String nationalIdHash;
    private Integer nationalIdHashVersion;
}
