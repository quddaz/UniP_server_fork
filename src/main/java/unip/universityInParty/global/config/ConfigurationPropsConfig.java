package unip.universityInParty.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import unip.universityInParty.domain.oauth.utils.jwt.JwtProperties;

@Configuration
@EnableConfigurationProperties(value = JwtProperties.class)
public class ConfigurationPropsConfig {
}