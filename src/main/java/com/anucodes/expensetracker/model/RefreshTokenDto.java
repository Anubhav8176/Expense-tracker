package com.anucodes.expensetracker.model;


import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class RefreshTokenDto {
    String token;
}
