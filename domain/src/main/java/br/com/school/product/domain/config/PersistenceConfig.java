package br.com.school.product.domain.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("br.com.school.product.domain")
@EntityScan("br.com.school.product.domain")
public class PersistenceConfig {

}
