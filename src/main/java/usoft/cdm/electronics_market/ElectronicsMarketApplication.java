package usoft.cdm.electronics_market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@SpringBootApplication
@EnableJpaAuditing
public class ElectronicsMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicsMarketApplication.class, args);
    }

}
