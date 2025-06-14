package com.anucodes.expensetracker.services;


import com.anucodes.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (userRepository.getUserInfoByUsername(username).isPresent()){
            return userRepository.getUserInfoByUsername(username).get();
        }else {
            throw new UsernameNotFoundException("User Not present!");
        }
    }
}
