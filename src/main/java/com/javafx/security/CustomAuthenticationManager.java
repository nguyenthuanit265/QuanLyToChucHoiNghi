package com.javafx.security;

import com.javafx.entity.Account;
import com.javafx.entity.Role;
import com.javafx.repository.AccountRepository;
import com.javafx.repository.impl.AccountRepositoryImpl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthenticationManager {
    AccountRepository accountRepository = new AccountRepositoryImpl();

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getPrincipal() + "";
        String password = authentication.getCredentials() + "";

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new BadCredentialsException("1000");
        }
        if (!BCrypt.checkpw(password, account.getPassword())) {
            throw new BadCredentialsException("1000");
        }

        List<Role> userRole = (List<Role>) account.getRole();
        return new UsernamePasswordAuthenticationToken(email, null, userRole.stream().map(x -> new SimpleGrantedAuthority(x.getName())).collect(Collectors.toList()));
    }
}
