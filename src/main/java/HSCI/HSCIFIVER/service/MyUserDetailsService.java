package HSCI.HSCIFIVER.service;

import HSCI.HSCIFIVER.entity.User;
import HSCI.HSCIFIVER.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(s);
        if(user == null)return null;

        return new org.springframework.security.core.userdetails.User(user
        .getUsername(),
                user.getPassword(),user.getAuthorities());

    }
}