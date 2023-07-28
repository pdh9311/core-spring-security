package study.corespringsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import study.corespringsecurity.domain.Account;
import study.corespringsecurity.domain.AccountDto;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "id", ignore = true)
    Account toAccount(AccountDto accountDto);

}
