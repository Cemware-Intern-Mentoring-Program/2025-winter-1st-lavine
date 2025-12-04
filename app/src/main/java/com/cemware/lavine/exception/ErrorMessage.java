package com.cemware.lavine.exception;

public class ErrorMessage {
    
    private ErrorMessage() { // 유틸 클래스 인스턴스화 방지
        
    }
    
    public static final String USER_NOT_FOUND = "유저를 찾을 수 없습니다. id: %d";
    public static final String GROUP_NOT_FOUND = "그룹을 찾을 수 없습니다. id: %d";
    public static final String TASK_NOT_FOUND = "할 일을 찾을 수 없습니다. id: %d";
    
    public static String userNotFound(Long id) {
        return String.format(USER_NOT_FOUND, id);
    }
    
    public static String groupNotFound(Long id) {
        return String.format(GROUP_NOT_FOUND, id);
    }
    
    public static String taskNotFound(Long id) {
        return String.format(TASK_NOT_FOUND, id);
    }
}

