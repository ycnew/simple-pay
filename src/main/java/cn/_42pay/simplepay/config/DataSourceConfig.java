package cn._42pay.simplepay.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

/**
 * Created by kevin on 2018/6/13.
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {
	@Bean(name="dataSource")
	@ConfigurationProperties(prefix = "druid.datasource")
	public DataSource dataSource() {
		return new DruidDataSource();
	}
}
