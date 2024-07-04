package roommate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        chainBuilder.authorizeHttpRequests(
                        configurer -> configurer
                                .anyRequest().authenticated())
                                .oauth2Login(config -> config.userInfoEndpoint(
                                        info -> info.userService(new AppUserService())
                                ))
        ;
        return chainBuilder.build();
    }

    // CLIENT_ID=30ba36ad4344bcb356f6
    // CLIENT_SECRET=b32fea03c6820f43f53604ef06134e993a030462

}
