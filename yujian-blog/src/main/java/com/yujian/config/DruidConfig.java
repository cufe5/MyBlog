package com.yujian.config;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {


    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    public DataSource druidDataSource(){
        return new DruidXADataSource();
    }


    //后台监控:web.xml   ServletRegistrationBean
    //因为SpringBoot内置了 servlet容器   所以没有web.xml  替代方法ServletRegistrationBean
    @Bean
    public ServletRegistrationBean StatViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

        //后台需要有人登录   账号密码配置
        HashMap<String, String> initParameters = new HashMap<>();

        //登录的两个key是固定的
        initParameters.put("loginPassword","020323");
        initParameters.put("loginUsername","mubei");

        //允许谁可以访问
        //空全部人可以访问
        initParameters.put("allow","");
        //禁止某人访问
        initParameters.put("mubei","端口");

        //设置初始化参数
        bean.setInitParameters(initParameters);
        return bean;
    }

    //filter过滤器
    @Bean
    public FilterRegistrationBean b(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());
        //可以过滤哪些请求
        HashMap<String, String> initParameters = new HashMap<>();
        //不过滤
        initParameters.put("exclusion","*.js,*.css,/druid/**");

        bean.setInitParameters(initParameters);
        return  bean;
    }


}
