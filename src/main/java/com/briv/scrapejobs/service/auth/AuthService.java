package com.briv.scrapejobs.service.auth;

import com.briv.scrapejobs.dto.auth.LoginRequest;
import com.briv.scrapejobs.dto.auth.RegisterRequest;
import com.briv.scrapejobs.domain.user.User;
import com.briv.scrapejobs.domain.user.Role;
import com.briv.scrapejobs.repository.user.UserRepository;
import com.briv.scrapejobs.security.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

//    public void Register(RegisterRequest request) {
//        String email = request.getEmail();
//        String encodedPassword = passwordEncoder.encode(request.getPassword());
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        User newUser = User.builder()
//                .email(email)
//                .password(encodedPassword)
//                .role(Role.USER)
//                .enabled(true)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        userRepository.save(newUser);
//    }


    public String Login(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtService.generateToken(userDetails);
    }
}
