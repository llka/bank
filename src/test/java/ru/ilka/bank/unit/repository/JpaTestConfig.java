package ru.ilka.bank.unit.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@TestConfiguration
@EntityScan(basePackages = "ru.ilka.bank.domain.db")
@EnableJpaRepositories(basePackages = "ru.ilka.bank.repository")
@EnableJpaAuditing
public class JpaTestConfig {
}
