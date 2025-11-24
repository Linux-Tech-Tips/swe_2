/** 
 * @author Martin, Ethan
 */
package org.unixusergroup.tutorial_time.api.rest;

import org.unixusergroup.tutorial_time.infrastructure.SecurityConfiguration;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collection;
import java.util.Arrays;

/** Utility class with factory mehtods to create mock Jwt Authentication objects */
public class MockAuthUtil {

    /** Utility method to get a mock JWT object for testing purposes */
    public static Jwt createMockJwt(String id, Collection<String> groups) {
            Jwt.Builder jwtBuilder = Jwt.withTokenValue("MOCK_TOKEN");
            /* If empty ID, don't include the claim */
            if(id.equals(""))
                return jwtBuilder.header("alg", "none")
                                 .claim("groups", groups)
                                 .build();
            /* Create a token with a claim for ID and groups/roles */
            return jwtBuilder.header("alg", "none")
                             .claim("groups", groups)
                             .claim("uni_id", id)
                             .build();
    }

    /** Utility method to get a mock JWT object for testing purposes, with a single group */
    public static Jwt createMockJwt(String id, String defaultGroup) {
        return createMockJwt(id, Arrays.asList(new String[] {defaultGroup}));
    }

    /** Utility method to get a mock AbstractAuthenticationToken with a converted JWT with a single group/role */
    public static AbstractAuthenticationToken createMockJwtToken(String id, String defaultGroup) {
        SecurityConfiguration.CustomAuthenticationConverter converter = new SecurityConfiguration.CustomAuthenticationConverter();
        return converter.convert(createMockJwt(id, defaultGroup));
    }

}
