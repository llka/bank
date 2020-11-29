package ru.ilka.bank.mapper;

import org.mapstruct.Mapper;
import ru.ilka.bank.dto.AccountDto;
import ru.ilka.bank.entity.Account;

import static ru.ilka.bank.mapper.MapperConstants.SPRING_MODE;

@Mapper(componentModel = SPRING_MODE)
public interface AccountMapper {
    AccountDto toDto(Account account);
}
