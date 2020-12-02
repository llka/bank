package ru.ilka.bank.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.ilka.bank.domain.Balance;
import ru.ilka.bank.dto.BalanceDto;

import static ru.ilka.bank.mapper.MapperConstants.SPRING_MODE;

@Mapper(componentModel = SPRING_MODE)
public interface BalanceMapper {
    @Mapping(target = "currencyCode", source = "currency.code")
    BalanceDto toDto(Balance balance);
}
