package kim.turbo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = passwordEncoder().encode("123");
        auth.inMemoryAuthentication().withUser("turbo").password(password).roles("admin");

    }
}