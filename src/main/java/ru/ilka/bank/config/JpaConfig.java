package ru.ilka.bank.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan(basePackages = "ru.ilka.bank.domain.db")
@EnableJpaRepositories(basePackages = "ru.ilka.bank.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {
}
