package com.anucodes.expensetracker.controller;


import com.anucodes.expensetracker.entities.RefreshToken;
import com.anucodes.expensetracker.model.AuthResponse;
import com.anucodes.expensetracker.model.LogInDto;
import com.anucodes.expensetracker.model.RefreshTokenDto;
import com.anucodes.expensetracker.model.UserInfoDto;
import com.anucodes.expensetracker.services.JwtService;
import com.anucodes.expensetracker.services.RefreshTokenService;
import com.anucodes.expensetracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;


    @PostMapping("/register")
    public ResponseEntity RegisterUser(@RequestBody UserInfoDto userInfoDto) {
        try{

            if (userService.isUserExist(userInfoDto.getUsername())){
                return new ResponseEntity("Already Exist", HttpStatus.BAD_REQUEST);
            }

             UserInfoDto userResponse = userService.RegisterUser(userInfoDto);

            String jwtToken = jwtService.generateJwtToken(userResponse.getUsername());
            Date expiration = jwtService.extractExpiration(jwtToken);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userResponse.getUsername());

            AuthResponse logInResponse = new AuthResponse(
                    refreshToken.getToken(),
                    expiration,
                    jwtToken
            );

            return new ResponseEntity<>(logInResponse, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logIn")
    public ResponseEntity LogInUser(@RequestBody LogInDto logInDto) {
        try{
            UserInfoDto userResponse = userService.LogInUser(logInDto);

            if (!userResponse.getUsername().isEmpty()){

                String jwtToken = jwtService.generateJwtToken(userResponse.getUsername());
                Date expiration = jwtService.extractExpiration(jwtToken);
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(userResponse.getUsername());

                AuthResponse logInResponse = new AuthResponse(
                    refreshToken.getToken(),
                    expiration,
                    jwtToken
                );

                return new ResponseEntity<>(logInResponse, HttpStatus.OK);

            }else{
                return new ResponseEntity("Didn't able to make tokens for login!", HttpStatus.BAD_GATEWAY);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity verifyRefreshToken(@RequestBody RefreshTokenDto refreshTokenDto){
        try{
            return refreshTokenService.findRefreshTokenByToken(refreshTokenDto.getToken())
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUserInfo)
                    .map(userInfo ->{

                        String jwtToken = jwtService.generateJwtToken(userInfo.getUsername());
                        String refreshToken = refreshTokenService.createRefreshToken(userInfo.getUsername()).getToken();

                        return new ResponseEntity<>(
                                AuthResponse.builder()
                                        .refreshToken(refreshToken)
                                        .jwtToken(jwtToken)
                                        .expiration(jwtService.extractExpiration(jwtToken))
                                        .build(),
                                HttpStatus.OK
                        );
                    })
                    .orElseThrow (()->new RuntimeException("RefreshToken not found in database!"));

        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
