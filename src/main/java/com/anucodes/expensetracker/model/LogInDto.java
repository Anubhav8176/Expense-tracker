package com.anucodes.expensetracker.model;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogInDto {
    private String username;
    private String password;
}
