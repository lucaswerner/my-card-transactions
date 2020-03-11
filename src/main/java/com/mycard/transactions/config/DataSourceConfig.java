package com.mycard.transactions.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource getDataSource() throws PropertyVetoException {
        final ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();

        comboPooledDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        comboPooledDataSource.setJdbcUrl("jdbc:mysql://localhost:3306/my_card_transactions?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        comboPooledDataSource.setUser("root");
        comboPooledDataSource.setPassword("mypassword");

        comboPooledDataSource.setInitialPoolSize(50);
        comboPooledDataSource.setMinPoolSize(50);
        comboPooledDataSource.setMaxPoolSize(50);

        return comboPooledDataSource;
    }
}
