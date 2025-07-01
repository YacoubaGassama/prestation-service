package uahb.m1gl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "topics")
public class ConfigData {
    private String compteCreateTopicRequestName;
    private String compteCreateTopicResponseName;
    private String customerCreateTopicRequestName;
    private String customerCreateTopicResponseName;
    private String paiementCreateTopicRequestName;
    private String paiementCreateTopicResponseName;
}
