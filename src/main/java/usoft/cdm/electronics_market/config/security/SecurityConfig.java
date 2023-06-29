package usoft.cdm.electronics_market.config.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import usoft.cdm.electronics_market.service.UserService;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {

        return new JwtAuthenticationFilter();
    }


    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // Ngăn chặn request từ một domain khác
                .and().csrf().disable()
                .exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/login", "/api/v1/homepage/**", "/api/v1/user/login-google").permitAll() // Cho phép tất cả mọi người truy cập vào địa chỉ này
                .antMatchers("/api/v1/bill", "/api/v1/bill/getCart", "/api/v1/bill/shop", "/api/v1/suggested-product/all", "/api/v1/flash-sale/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/product", "/api/v1/hot-category/all").permitAll()
                .antMatchers("/api/v1/bill/getHistory", "/api/v1/bill/getById", "api/v1/product/search-hot-brand").permitAll()
                .antMatchers("/api/v1/footer", "/api/v1/footer/getAllCustomerCare", "/api/v1/footer/getCustomerCareById", "/api/v1/footer/getSocialNetwork").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/product", "/api/v1/hot-category/all", "/api/v1/hot-brand/all").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/category", "/api/v1/promotion/all", "/api/v1/branch/getById").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/product/search-category", "/api/v1/product/search-hot-brand").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/footer/support-list-all", "/api/v1/footer/support-id").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/review/all", "/api/v1/review/all-newest").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api/v1/branch/**", "/api/v1/branch", "/api/v1/product", "/api/v1/product/**", "/api/v1/category/**", "/api/v1/category", "/api/v1/manager-homepage/**", "/api/v1/manager-homepage", "/api/v1/warehouse", "/api/v1/warehouse/**")
                .hasAnyAuthority("1", "2")
                .antMatchers(HttpMethod.POST, "/api/v1/user").hasAnyAuthority("1")
                .anyRequest().authenticated();// Tất cả các request khác đều cần phải xác thực mới được truy cập

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
