package com.demo.demandfarm.dto.error;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer errorCode;

    private String errorMessage;
}
