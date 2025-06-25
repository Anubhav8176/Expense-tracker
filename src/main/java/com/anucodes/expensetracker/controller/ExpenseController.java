package com.anucodes.expensetracker.controller;



import com.anucodes.expensetracker.model.ExpenseDto;
import com.anucodes.expensetracker.request.ExpenseByPayment;
import com.anucodes.expensetracker.request.ExpenseRequest;
import com.anucodes.expensetracker.request.GetExpense;
import com.anucodes.expensetracker.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/getAll")
    public ResponseEntity getAllExpenses(@RequestBody ExpenseRequest request){
        try{
            System.out.println(request.getQuery());
            List<ExpenseDto> expenses = expenseService.getAllExpense(request.getQuery());
            return new ResponseEntity(expenses, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("The expense cannot be added!");
        }
    }

    @PostMapping("/add")
    public ResponseEntity addExpense(@RequestBody ExpenseDto expenseDto){

        try {

            if(expenseDto.getAmount().equals(0) || expenseDto.getAmount().toString().isEmpty()){
                return new  ResponseEntity("Amount cannot be empty!", HttpStatus.BAD_REQUEST);
            } else if (expenseDto.getPaymentMethod().equals("")) {
                return new ResponseEntity("Payment Method should not be empty!", HttpStatus.BAD_REQUEST);
            }
            Boolean isAdded = expenseService.addExpense(expenseDto);
            if (isAdded){
                return new ResponseEntity<>("Expense added successfully", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("Failed to add expense!", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-by-category")
    public ResponseEntity expensesByCategory(@RequestBody GetExpense category){
        try{
            if (category.getQuery().isEmpty()){
                return new ResponseEntity("Category cannot be empty!", HttpStatus.BAD_REQUEST);
            }else{
                List<ExpenseDto> categoryList = expenseService.filterByCategory(category);
                return new ResponseEntity<>(categoryList, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-by-payment")
    public ResponseEntity expenseByPayment(@RequestBody GetExpense payment){
        try{
            if (payment.getQuery().isEmpty()){
                return new ResponseEntity("Payment cannot be empty!", HttpStatus.BAD_REQUEST);
            }else{
                List<ExpenseDto> paymentList = expenseService.filterByPaymentMethod(payment);
                return new ResponseEntity<>(paymentList, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-by-amount")
    public ResponseEntity expenseByAmount(@RequestBody ExpenseByPayment expenseByPayment){
        try {
            if (expenseByPayment.getUpperLimit()==-1 || expenseByPayment.getLowerLimit()==-1){
                return new ResponseEntity<>("There must be a upperLimit and lowerLimit.", HttpStatus.BAD_REQUEST);
            }
            List<ExpenseDto> expenseList = expenseService.filterByAmount(expenseByPayment);
            return new ResponseEntity<>(expenseList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
