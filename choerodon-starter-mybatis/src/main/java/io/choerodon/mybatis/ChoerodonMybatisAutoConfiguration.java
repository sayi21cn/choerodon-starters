package io.choerodon.mybatis;

import io.choerodon.mybatis.autoconfigure.MapperOverrideProperties;
import io.choerodon.mybatis.common.CustomProvider;
import io.choerodon.mybatis.util.OGNL;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;

@Configuration
@ComponentScan
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@MapperScan(basePackages = "io.choerodon.**.mapper")
@PropertySource("classpath:default-choerodon-mybatis-config.properties")
public class ChoerodonMybatisAutoConfiguration {

    @Value("${db.type}")
    private String dbType;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChoerodonMybatisAutoConfiguration.class);

    @Autowired(required = false)
    private CustomProvider customProvider;

    @PostConstruct
    public void setLanguageProvider() {
        if (customProvider == null) {
            LOGGER.warn("请实现 CustomProvider 接口以提供当前语言。");
        } else {
            OGNL.customProvider = customProvider;
        }
    }

    @Bean
    @Primary
    public MybatisProperties mybatisProperties(){
        return new MapperOverrideProperties();
    }


}
