/** 
 * @author Martin
 */
package org.unixusergroup.tutorial_time.infrastructure;

import org.springframework.core.convert.converter.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/student/**").hasRole("Student")
                .requestMatchers("/api/lecturer/**").hasRole("Lecturer")
                .requestMatchers("/api/scheduler/**").hasRole("Lecturer")
                .requestMatchers("/api/module/**").hasRole("Lecturer")
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt((jwt) -> jwt.jwtAuthenticationConverter(new CustomAuthenticationConverter())))
            .cors(Customizer.withDefaults());
        return http.build();
    }

    public static class CustomAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
        public AbstractAuthenticationToken convert(Jwt jwt) {
            /* Get all the groups from the given JWT */
            List<String> groups = jwt.getClaimAsStringList("groups");
            /* Convert all already granted authorities from the JWT */
            JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
            Collection<GrantedAuthority> authorities = new ArrayList<>(converter.convert(jwt));
            /* Add groups to authorities if found */
            if(groups != null) {
                for(String group : groups) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + group));
                }
            }
            return new JwtAuthenticationToken(jwt, authorities);
        }
    }
}
