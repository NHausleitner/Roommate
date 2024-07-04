package roommate.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

@Service
public class AppUserService implements OAuth2UserService {

    private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
    private final Set<String> admins = getAdmins();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User originalUser = defaultService.loadUser(userRequest);
        Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
        if (admins.contains(originalUser.getAttribute("login"))){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "id");
    }

    public static HashSet<String> getAdmins(){
        Yaml yaml = new Yaml();
        InputStream input = AppUserService.class.getClassLoader().getResourceAsStream("application.yaml");
        Map<String, Object> yamlData = yaml.load(input);
        return new HashSet<>((Collection) yamlData.get("admins"));
    }

}