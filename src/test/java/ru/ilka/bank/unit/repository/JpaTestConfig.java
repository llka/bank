package ru.ilka.bank.unit.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableJpaAuditing
@EnableTransactionManagement
@EntityScan(basePackages = "ru.ilka.bank.domain.db")
@EnableJpaRepositories(basePackages = "ru.ilka.bank.repository")
public class JpaTestConfig {
}
