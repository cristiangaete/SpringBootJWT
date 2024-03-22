package com.example.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth.jwt.JwtServirse;
import com.example.user.Role;
import com.example.user.User;
import com.example.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtServirse JwtServirse;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUserName()).orElseThrow();
        String token = JwtServirse.getToken(user);
        return AuthResponse.builder().token(token)
        .build();
    }

    public AuthResponse register(RegisterRequest request) {
       User user  =  User.builder()
       .userName(request.getUsername())
       .password(request.getPassword())
       .firstName(request.getFirstname())
       .lastName(request.getLastName())
       .country(request.getCountry())
       .role(Role.USER)
       .build();

       userRepository.save(user);

       return AuthResponse.builder()
       .token(JwtServirse.getToken(user))
       .build();
    }

}
