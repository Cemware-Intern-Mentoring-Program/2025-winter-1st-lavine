package com.cemware.lavine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드 제외
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,                 // 에러 메시지
        String path,                    // 요청 경로
        List<ValidationError> errors    // Validation 에러 상세 목록
) {
    
    
    @Builder
    public record ValidationError(
            String field,       // 검증 실패한 필드명
            String rejectedValue,
            String message
    ) {}
    
    // Validation X
    public static ErrorResponse of(int status, String error, String message, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();
    }
    
    // Validation O
    public static ErrorResponse ofValidation(int status, String error, String message, String path, List<ValidationError> errors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .errors(errors)
                .build();
    }
}

