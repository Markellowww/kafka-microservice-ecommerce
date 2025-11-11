package com.markellowww.kafgen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author Markelloww
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String details;
}