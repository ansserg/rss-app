package com.nexign.dmf.rss.rssvw.config;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class DBConnectConfig {
    @Value("${app.jdbc.driver}")
    private String driver;
    @Value("${db.jdbc.url}")
    private String url;
    @Value("${db.acquireIncrement:1}")
    private Integer acquireIncrement;
    @Value("${db.minPoolSize:1}")
    private Integer minPoolSize;
    @Value("${db.maxPoolSize:1}")
    private Integer maxPoolSize;
    @Value("${db.maxIdleTime:300}")
    private Integer maxIdleTime;
    @Value("${db.unreturnedConnectionTimeout:3600}")
    private Integer unreturnedConnectionTimeout;
    @Value("${db.checkoutTimeout:10}")
    private Integer checkoutTimeout;
    @Value("${db.initialPoolSize:1}")
    private Integer initialPoolSize;
}
