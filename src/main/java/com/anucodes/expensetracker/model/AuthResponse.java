package com.anucodes.expensetracker.model;

import lombok.*;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String refreshToken;
    private Date expiration;
    private String jwtToken;
}
