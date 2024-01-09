package com.stit.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

// java annotation
@Configuration
//@ComponentScan("com.stit")
public class RootApplicationContextConfig {

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("oracle.jdbc.OracleDriver");
//--customer
//        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@192.168.0.6:1521:jherpdb"); 
//        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@61.218.236.217:1521:jherpdb");
        hikariConfig.setUsername("jherp");
        hikariConfig.setPassword("jherp2012");       

//--office
        hikariConfig.setJdbcUrl("jdbc:oracle:thin:@192.168.1.56:1526:jhdb");
//        hikariConfig.setUsername("jherp");
//        hikariConfig.setPassword("jherp2012");

        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(20);
        hikariConfig.setConnectionTestQuery("SELECT 1 from dual");
        hikariConfig.setPoolName("springHikariCP");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

}
