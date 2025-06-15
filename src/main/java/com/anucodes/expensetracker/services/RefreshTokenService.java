package com.anucodes.expensetracker.services;


import com.anucodes.expensetracker.entities.RefreshToken;
import com.anucodes.expensetracker.entities.UserInfo;
import com.anucodes.expensetracker.repository.RefreshTokenRepository;
import com.anucodes.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${refreshtoken.expiration}")
    private Long expiration;

    //Function to create the refreshtoken
    public RefreshToken createRefreshToken(String username){
        UserInfo userInfo = userRepository.findByUsername(username);
        RefreshToken refreshToken;

        Optional<RefreshToken> dbRefreshToken = refreshTokenRepository.findByUserInfo(userInfo);

        if(dbRefreshToken.isPresent()){
            if (checkThatTokenExpired(dbRefreshToken.get())){
                refreshTokenRepository.delete(dbRefreshToken.get());
                throw new RuntimeException("The token is expired! Login again.");
            }else {
                refreshToken = dbRefreshToken.get();
            }
        }else{
            refreshToken = RefreshToken
                        .builder()
                        .token(UUID.randomUUID().toString())
                        .createdAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + expiration))
                        .userInfo(userInfo)
                        .build();

                refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }
  
    //Function to check the expiration
    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiration().compareTo(new Date(System.currentTimeMillis()))<0){
            refreshTokenRepository.delete(refreshToken);
            System.out.println("The token is expired: "+refreshToken);
            throw new RuntimeException(refreshToken.getToken()+" The token has expired. Login again!!");
        }

        return refreshToken;
    }

    //Function to find user by token
    public Optional<RefreshToken> findRefreshTokenByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public boolean checkThatTokenExpired(RefreshToken refreshToken){
        if(refreshToken.getExpiration().compareTo(new Date(System.currentTimeMillis()))<0){
            return true;
        }
        return false;
    }

}
