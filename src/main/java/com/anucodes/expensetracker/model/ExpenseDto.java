package com.anucodes.expensetracker.model;


import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Integer amount;
    private String token;
    private String description;
    private String category;
    private Date date;
    private String paymentMethod;
}
