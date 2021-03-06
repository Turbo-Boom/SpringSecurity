package kim.turbo.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * Security 配置文件
 *
 * @author turbo
 * @email turbo-boom@outlook.com
 * @date 2020-12-22 16:11
 * @deprecated 继承 WebSecurityConfiguration 并重写 configure
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // 注入数据源
    @Autowired
    private DataSource dataSource;

    // 配置对象
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 没有权限访问自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");
        // 退出配置
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/test/hello").permitAll();

        http.formLogin()  // 自定义自己编写的登陆页面
                .loginPage("/login.html") // 登陆页面设置
                .loginProcessingUrl("/user/login") //登陆访问路径
                .defaultSuccessUrl("/success.html").permitAll() //登陆成功后跳转路径
                .and().authorizeRequests()
                .antMatchers("/", "/test/hello", "/user/login").permitAll() // 不需要认证路径
                // 当前登陆用户，只有admin权限才能访问这个路径
                // 针对某一个用户权限设置
//                .antMatchers("/test/index").hasAuthority("admins") // 针对某一个用户权限设置
                .antMatchers("/test/index").hasAnyAuthority("admins,manager")
//                .antMatchers("/test/index").hasRole("sale") // 一个角色
//                .antMatchers("/test/index").hasAnyRole("sale,role") // 多个角色
                .anyRequest().authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository()) // 自动登陆
                .tokenValiditySeconds(60) //设置有效时长 单位秒
                .userDetailsService(userDetailsService) //
                .and().csrf().disable();// 关闭csrf防护

    }
}
