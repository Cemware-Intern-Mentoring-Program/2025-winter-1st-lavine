package com.cemware.lavine.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s을(를) 찾을 수 없습니다. id: %d", resourceName, id));
    }
}

