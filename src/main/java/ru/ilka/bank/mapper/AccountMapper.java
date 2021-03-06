package ru.ilka.bank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ilka.bank.dto.AccountDto;
import ru.ilka.bank.domain.db.Account;

import static ru.ilka.bank.mapper.MapperConstants.SPRING_MODE;

@Mapper(componentModel = SPRING_MODE)
public interface AccountMapper {
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "currencyCode", source = "currency.code")
    AccountDto toDto(Account account);
}
