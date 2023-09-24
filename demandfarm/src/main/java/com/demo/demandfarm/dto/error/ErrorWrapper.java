package com.demo.demandfarm.dto.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorWrapper {
    List<ErrorResponse>errors;
}
