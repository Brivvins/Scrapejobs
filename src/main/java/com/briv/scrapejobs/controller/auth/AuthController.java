package com.briv.scrapejobs.controller.auth;

import com.briv.scrapejobs.dto.auth.AuthResponse;
import com.briv.scrapejobs.dto.auth.RegisterRequest;
import com.briv.scrapejobs.dto.auth.LoginRequest;
import com.briv.scrapejobs.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        authService.Register(request);
//        return ResponseEntity.ok("User registered successfully");
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.Login(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
