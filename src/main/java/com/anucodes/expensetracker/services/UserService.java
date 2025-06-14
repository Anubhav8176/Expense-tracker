package com.anucodes.expensetracker.services;


import com.anucodes.expensetracker.entities.UserInfo;
import com.anucodes.expensetracker.model.LogInDto;
import com.anucodes.expensetracker.model.UserInfoDto;
import com.anucodes.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserInfoDto RegisterUser(UserInfoDto userInfoDto){

        try{

            String encodedPassword = passwordEncoder.encode(userInfoDto.getPassword());

            if(
                    !userInfoDto.getUsername().isEmpty() &&
                    !userInfoDto.getEmail().isEmpty()
            ){
                UserInfo userToBeRegister = UserInfo
                        .builder()
                        .username(userInfoDto.getUsername())
                        .password(encodedPassword)
                        .name(userInfoDto.getName())
                        .email(userInfoDto.getEmail())
                        .phoneNumber(userInfoDto.getPhoneNumber())
                        .age(userInfoDto.getAge())
                        .build();

                userRepository.save(userToBeRegister);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return userInfoDto;
    }

    public UserInfoDto LogInUser(LogInDto logInDto){
        try {
            UserInfo userInfo = userRepository.getUserInfoByUsername(logInDto.getUsername()).get();
            boolean isPasswordMatches = passwordEncoder.matches(logInDto.getPassword(), userInfo.getPassword());
            if (isPasswordMatches && userInfo!=null){

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                logInDto.getUsername(),
                                logInDto.getPassword()
                        )
                );

                return UserInfoDto
                        .builder()
                        .username(userInfo.getUsername())
                        .password(userInfo.getPassword())
                        .name(userInfo.getName())
                        .email(userInfo.getEmail())
                        .phoneNumber(userInfo.getPhoneNumber())
                        .age(userInfo.getAge())
                        .build();
            }else {
                throw new RuntimeException("Username and Password Doesn't match!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Boolean isUserExist(String username){
        UserInfo user = userRepository.findByUsername(username);
        if (Objects.nonNull(user)){
            return true;
        }
        return false;
    }

}
