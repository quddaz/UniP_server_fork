package unip.universityInParty.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("unip.universityInParty")
public class OpenFeignConfig {
}
