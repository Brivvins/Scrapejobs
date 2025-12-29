package com.briv.scrapejobs.dto.auth;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}
