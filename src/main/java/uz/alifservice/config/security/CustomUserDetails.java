package uz.alifservice.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.alifservice.domain.auth.Role;
import uz.alifservice.domain.auth.User;
import uz.alifservice.enums.GeneralStatus;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private GeneralStatus status;
    private User user;

    public CustomUserDetails(User user, Set<Role> roleList) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.authorities = roleList.stream().map(item -> new SimpleGrantedAuthority(item.getRoleName())).toList();
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(GeneralStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
