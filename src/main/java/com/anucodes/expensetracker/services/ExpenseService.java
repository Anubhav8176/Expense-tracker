package com.anucodes.expensetracker.services;


import com.anucodes.expensetracker.entities.Expense;
import com.anucodes.expensetracker.entities.RefreshToken;
import com.anucodes.expensetracker.entities.UserInfo;
import com.anucodes.expensetracker.model.ExpenseDto;
import com.anucodes.expensetracker.repository.ExpenseRepository;
import com.anucodes.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    //Add a new Expense
    public Boolean addExpense(ExpenseDto expenseDto){

        try {

            String token = expenseDto.getToken();

            Optional<RefreshToken> refreshToken = refreshTokenService.findRefreshTokenByToken(token);

            if (refreshToken.isPresent()){
                Expense expense = Expense.builder()
                        .amount(expenseDto.getAmount())
                        .description(expenseDto.getDescription())
                        .category(expenseDto.getCategory())
                        .userInfo(refreshToken.get().getUserInfo())
                        .date(expenseDto.getDate())
                        .paymentMethod(expenseDto.getPaymentMethod())
                        .build();

                System.out.println(expense);

                expenseRepository.save(expense);
                return true;
            }else {
                throw new RuntimeException("Invalid refresh token! Check again.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //View All Expense
    public List<ExpenseDto> getAllExpense(String username){
        try{
            List<ExpenseDto> returnList = new ArrayList<>();

            System.out.println(username);

            UserInfo userInfo = userRepository.findByUsername(username);

            System.out.println(userInfo);

            List<Expense> expenses = expenseRepository.getAllByUserInfo(userInfo);

            System.out.println(expenses);

            for (Expense e : expenses) {
                ExpenseDto expIndi = ExpenseDto.builder()
                        .amount(e.getAmount())
                        .description(e.getDescription())
                        .category(e.getCategory())
                        .date(e.getDate())
                        .paymentMethod(e.getPaymentMethod())
                        .build();

                returnList.add(expIndi);
            }
            return returnList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Filter by Payment Method
    public List<ExpenseDto> filterByPaymentMethod(String paymentMethod){
        try{
            List<ExpenseDto> returnList = new ArrayList<>();
            List<Expense> expenses = expenseRepository.getAllByPaymentMethod(paymentMethod);
            System.out.println(expenses);
            for (Expense e : expenses) {
                ExpenseDto expIndi = ExpenseDto.builder()
                        .amount(e.getAmount())
                        .description(e.getDescription())
                        .category(e.getCategory())
                        .date(e.getDate())
                        .paymentMethod(e.getPaymentMethod())
                        .build();

                returnList.add(expIndi);
            }
            return returnList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Filter by Category
    public List<ExpenseDto> filterByCategory(String category){
        try{
            List <ExpenseDto> returnList = new ArrayList<>();
            List<Expense> expenses = expenseRepository.getAllByCategory(category);
            for (Expense e : expenses) {
                ExpenseDto expIndi = ExpenseDto.builder()
                        .amount(e.getAmount())
                        .description(e.getDescription())
                        .category(e.getCategory())
                        .date(e.getDate())
                        .paymentMethod(e.getPaymentMethod())
                        .build();

                returnList.add(expIndi);
            }
            return returnList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Filter by Amount
    public List<ExpenseDto> filterByAmount(Integer upperLimit, Integer lowerLimit){
        try {
            List<ExpenseDto> finalList = new ArrayList<>();

            List<Expense> fromRepo = expenseRepository.getAllByAmountBetween(upperLimit, lowerLimit);

            for (Expense e : fromRepo) {
                ExpenseDto expIndi = ExpenseDto.builder()
                        .amount(e.getAmount())
                        .description(e.getDescription())
                        .category(e.getCategory())
                        .date(e.getDate())
                        .paymentMethod(e.getPaymentMethod())
                        .build();

                finalList.add(expIndi);
            }

            return finalList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
