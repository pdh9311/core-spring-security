package study.corespringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.corespringsecurity.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
