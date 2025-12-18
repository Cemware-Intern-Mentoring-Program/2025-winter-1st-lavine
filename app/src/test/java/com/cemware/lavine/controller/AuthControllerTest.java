package com.cemware.lavine.controller;

import com.cemware.lavine.dto.AuthResponse;
import com.cemware.lavine.dto.LoginRequest;
import com.cemware.lavine.dto.RefreshTokenRequest;
import com.cemware.lavine.dto.RegisterRequest;
import com.cemware.lavine.dto.TokenResponse;
import com.cemware.lavine.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController 테스트")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("회원가입 API - 성공")
    void register_Success() {
        RegisterRequest request = new RegisterRequest("홍길동", "hong@example.com", "password123");
        AuthResponse authResponse = AuthResponse.of(
                "access-token",
                "refresh-token",
                1L,
                "hong@example.com",
                "홍길동"
        );
        given(authService.register(any(RegisterRequest.class))).willReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.register(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("access-token");
        assertThat(response.getBody().refreshToken()).isEqualTo("refresh-token");
        assertThat(response.getBody().email()).isEqualTo("hong@example.com");
        assertThat(response.getBody().name()).isEqualTo("홍길동");
        assertThat(response.getBody().tokenType()).isEqualTo("Bearer");
    }

    @Test
    @DisplayName("로그인 API - 성공")
    void login_Success() {
        LoginRequest request = new LoginRequest("hong@example.com", "password123");
        AuthResponse authResponse = AuthResponse.of(
                "access-token",
                "refresh-token",
                1L,
                "hong@example.com",
                "홍길동"
        );
        given(authService.login(any(LoginRequest.class))).willReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("access-token");
        assertThat(response.getBody().refreshToken()).isEqualTo("refresh-token");
        assertThat(response.getBody().email()).isEqualTo("hong@example.com");
    }

    @Test
    @DisplayName("토큰 갱신 API - 성공")
    void refresh_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("old-refresh-token");
        TokenResponse tokenResponse = TokenResponse.of("new-access-token", "new-refresh-token");
        given(authService.refresh(anyString())).willReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = authController.refresh(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().accessToken()).isEqualTo("new-access-token");
        assertThat(response.getBody().refreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.getBody().tokenType()).isEqualTo("Bearer");
        verify(authService).refresh("old-refresh-token");
    }

    @Test
    @DisplayName("로그아웃 API - 성공")
    void logout_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("refresh-token");
        willDoNothing().given(authService).logout(anyString());

        ResponseEntity<Void> response = authController.logout(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(authService).logout("refresh-token");
    }
}
