package com.cemware.lavine.service;

import com.cemware.lavine.dto.AuthResponse;
import com.cemware.lavine.dto.LoginRequest;
import com.cemware.lavine.dto.RegisterRequest;
import com.cemware.lavine.entity.User;
import com.cemware.lavine.exception.DuplicateEmailException;
import com.cemware.lavine.repository.UserRepository;
import com.cemware.lavine.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateEmailException(request.email());
        }

        User user = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        
        User savedUser = userRepository.save(user);
        String accessToken = jwtTokenProvider.createAccessToken(savedUser.getEmail());

        return AuthResponse.of(
                accessToken,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName()
        );
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());

        return AuthResponse.of(
                accessToken,
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }
}

