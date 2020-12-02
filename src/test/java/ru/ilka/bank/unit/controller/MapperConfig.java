package ru.ilka.bank.unit.controller;


import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.ilka.bank.mapper.AccountMapper;
import ru.ilka.bank.mapper.BalanceMapper;

@TestConfiguration
public class MapperConfig {

    @Bean
    public AccountMapper accountMapper() {
        return Mappers.getMapper(AccountMapper.class);
    }

    @Bean
    public BalanceMapper balanceMapper() {
        return Mappers.getMapper(BalanceMapper.class);
    }
}
