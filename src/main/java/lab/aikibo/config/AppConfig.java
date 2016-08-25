package lab.aikibo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lab.aikibo.services.Client;
import lab.aikibo.services.ClientImpl;

@Configuration
public class AppConfig {
  @Bean(name="client")
  public Client connect() {
    return new ClientImpl();
  }
}
