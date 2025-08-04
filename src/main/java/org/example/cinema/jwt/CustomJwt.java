package org.example.cinema.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomJwt extends JwtAuthenticationToken {

    public CustomJwt(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
    }

    // ✅ Email dell’utente
    @Override
    public String getName() {
        return getToken().getClaimAsString("email");
    }

    // ✅ Nome dell’utente (se Keycloak lo fornisce come "given_name")
    public String getFirstname() {
        return getToken().getClaimAsString("given_name");
    }

    // ✅ Cognome dell’utente (se Keycloak lo fornisce come "family_name")
    public String getLastname() {
        return getToken().getClaimAsString("family_name");
    }

    // ✅ Username se presente nel token (opzionale)
    public String getPreferredUsername() {
        return getToken().getClaimAsString("preferred_username");
    }
}
