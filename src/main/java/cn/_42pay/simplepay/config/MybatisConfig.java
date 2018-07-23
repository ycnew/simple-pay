package cn._42pay.simplepay.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;



@Configuration
@EnableTransactionManagement
@AutoConfigureAfter({DataSourceConfig.class})
@MapperScan(basePackages = {"cn._42pay.simplepay.db.dao"})
public class MybatisConfig implements EnvironmentAware {
    @Autowired
    private DataSource dataSource;

    private Environment environment;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        try {
            ResourcePatternResolver resourcePatternResolver;
            resourcePatternResolver = new PathMatchingResourcePatternResolver();

            SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
            bean.setDataSource(dataSource);
            bean.setTypeAliasesPackage(environment.getProperty("mybatis.typeAliasesPackage"));
            bean.setMapperLocations(resourcePatternResolver.getResources(environment.getProperty("mybatis.mapperLocations")));
            bean.setConfigLocation(new DefaultResourceLoader().getResource(environment.getProperty("mybatis.configLocation")));

            return bean.getObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * Set the {@code Environment} that this object runs in.
     *
     * @param environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment=environment;
    }
}
