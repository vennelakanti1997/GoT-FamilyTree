package com.demo.demandfarm.dto;

import com.demo.demandfarm.dto.error.ErrorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class BaseDto {

    private Date createdOn;

    @JsonProperty("success")
    private boolean success;

    private Object body;

    private List<ErrorResponse> errors;
}
