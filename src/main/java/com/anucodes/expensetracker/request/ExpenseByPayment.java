package com.anucodes.expensetracker.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseByPayment {
    String username;
    Integer upperLimit;
    Integer lowerLimit;
}
