package daggerok.data.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class User implements Serializable {

    private static final long serialVersionUID = 7707921330348029757L;

    @Id String id;
    boolean enabled;
    @NonNull String username;
    @NonNull String password;
    List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");

    public void setGrantedAuthorities(final List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public void setGrantedAuthorities(final String ... roles) {
        grantedAuthorities = AuthorityUtils.createAuthorityList(roles);
    }
}
