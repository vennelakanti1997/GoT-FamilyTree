package com.demo.demandfarm.error;

import com.demo.demandfarm.dto.error.ErrorWrapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class BusinessException extends RuntimeException{
    private HttpStatus httpStatus;
    private ErrorWrapper errorResponseWrapper;
}
