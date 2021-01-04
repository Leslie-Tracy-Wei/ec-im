package ec.im.webServer.base.spring.boot.autoconfigure;


import ec.im.webServer.base.mybatis.CommonXMLMapperBuilder;
import ec.im.webServer.base.mybatis.KeyGenMode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

@Configuration
// 只有引入SqlSession 才会构建该bean
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
//  @ConfigurationProperties注解主要用来把properties配置文件转化为bean来使用的，
//  而@EnableConfigurationProperties注解的作用是@ConfigurationProperties注解生效
@EnableConfigurationProperties(MybatisProperties.class)
// 在加载配置的类之后再加载当前类
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DatabaseAutoConfigure {
    @Autowired
    private MybatisProperties properties;
    @Autowired
    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    @Value("${mybatis.keyGenMode:IDENTITY}")
    private String keyGenMode;
    @Value("${kye.mybatis.generic.enable:true}")
    private boolean genericEnable;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        CommonXMLMapperBuilder builder = new CommonXMLMapperBuilder();
        builder.setBaseResultMap("BaseResultMap");
        builder.setBaseTableName("BaseTable");
        builder.setGenerationType("GenerationType");
        builder.setBaseColumns("BaseColumns");
        builder.setKeyGenMode(KeyGenMode.parse(StringUtils.defaultString(keyGenMode, KeyGenMode.IDENTITY.getCode())));

        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        if(this.properties.getConfigLocation()!=null){
            factory.setConfigLocation(this.resourceLoader.getResource(this.properties.getConfigLocation()));
        }
        factory.setTypeAliasesPackage(this.properties.getTypeAliasesPackage());
        factory.setTypeHandlersPackage(this.properties.getTypeHandlersPackage());

        Resource[] resources = this.properties.resolveMapperLocations();
        if (genericEnable) {
            resources = builder.builderCommonMapper(resources);
        }
        factory.setMapperLocations(resources);
//		factory.setMapperLocations(this.properties.resolveMapperLocations());

        return factory.getObject();
    }
}
