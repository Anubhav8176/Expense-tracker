package com.anucodes.expensetracker.model;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private Long phoneNumber;
    private Integer age;
}
