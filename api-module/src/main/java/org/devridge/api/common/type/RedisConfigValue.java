package org.devridge.api.common.type;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfigValue {

    private String host;
    private Integer port;
}