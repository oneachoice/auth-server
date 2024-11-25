package oneachoice.auth.database.mysql;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "oneachoice.auth.database.mysql.repository",
        entityManagerFactoryRef = "mysqlEntityManagerFactoryBean",
        transactionManagerRef = "mysqlTransactionManager"

)
public class MysqlDatabaseConfig {

    @Primary
    @Bean
    public PlatformTransactionManager mysqlTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(mysqlEntityManagerFactoryBean().getObject());

        return jpaTransactionManager;
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();


        entityManagerFactoryBean.setDataSource(mysqlDataSource());
        entityManagerFactoryBean.setPackagesToScan(new String[]{"oneachoice.auth.database.mysql.entity"});
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());


        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        entityManagerFactoryBean.setJpaPropertyMap(properties);


        return entityManagerFactoryBean;
    }

    @Primary
    @Bean
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://127.0.0.1:3306/auth?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true")
                .username("root")
                .password("admin")
                .build();
    }
}
