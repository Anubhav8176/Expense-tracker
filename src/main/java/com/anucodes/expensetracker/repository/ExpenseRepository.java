package com.anucodes.expensetracker.repository;

import com.anucodes.expensetracker.entities.Expense;
import com.anucodes.expensetracker.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {

    List<Expense> getAllByUserInfo(UserInfo userInfo);
    List<Expense> getAllByPaymentMethod(String paymentMethod);
    List<Expense> getAllByCategory(String category);
    List<Expense> getAllByAmountBetween(Integer upperLimit, Integer lowerLimit);

}
