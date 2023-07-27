package study.corespringsecurity.security.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import study.corespringsecurity.domain.Account;

import java.util.Collection;

@Getter
public class AccountContext extends User {

    private Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.getUsername(), account.getPassword(), authorities);
        this.account = account;
    }

}
