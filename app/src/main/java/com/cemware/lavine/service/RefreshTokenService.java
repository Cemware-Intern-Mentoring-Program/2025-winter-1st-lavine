package com.cemware.lavine.service;

import com.cemware.lavine.entity.RefreshToken;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.InvalidRefreshTokenException;
import com.cemware.lavine.repository.RefreshTokenRepository;
import com.cemware.lavine.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        deleteByUser(user);
        
        String token = jwtTokenProvider.createRefreshToken(user.getEmail());
        long validityInMillis = jwtTokenProvider.getRefreshTokenValidityInMilliseconds();
        LocalDateTime expiryDate = LocalDateTime.now().plusSeconds(validityInMillis / 1000);
        
        RefreshToken refreshToken = new RefreshToken(token, user, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("리프레시 토큰을 찾을 수 없습니다"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("리프레시 토큰이 만료되었습니다");
        }

        if (!jwtTokenProvider.validateRefreshToken(token)) {
            throw new InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다");
        }

        return refreshToken;
    }

    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    @Transactional
    public void deleteByToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("리프레시 토큰을 찾을 수 없습니다"));
        refreshTokenRepository.delete(refreshToken);
    }
}
