package com.demo.demandfarm.entity;

import com.demo.demandfarm.dto.error.ErrorResponse;
import com.demo.demandfarm.dto.error.ErrorWrapper;
import com.demo.demandfarm.error.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "GoTFamTreeEntity")
@NoArgsConstructor
@Slf4j
public class GoTFamTreeEntity {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "data",nullable = false, columnDefinition = "jsonb")
    private String data;

    @Getter
    @Setter
    @Column(name = "createdOn", updatable = false,nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdOn;

    @Getter
    @Setter
    @Column(name = "updatedOn", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedOnOn;

    public GoTFamTreeEntity(final JsonNode data) {
        setData(data);
    }

    public JsonNode getData() {
        try {
            return new ObjectMapper().readTree(data);
        } catch (IOException e) {
            log.error("Failed to convert String to JsonNode - {}",data,e);
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9007,"Make sure the data is in correct format"))));
        }
    }

    public void setData(final JsonNode data) {
        try {
        this.data = new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert JsonNode to String - {}",data,e);
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                    new ErrorWrapper(List.of(new ErrorResponse(9007,"Make sure the data is in correct format"))));
        }
    }
}
