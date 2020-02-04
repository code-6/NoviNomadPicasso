package stanislav.tun.novinomad.picasso.persistance.pojos;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@JsonRootName(value = "user")
public class User extends AbstractEntity{
    private String userName;
    private String password;
    private boolean enabled = false;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<SimpleGrantedAuthority> authorities = new HashSet<>();

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(String userName, String password, boolean enabled,  Set<SimpleGrantedAuthority> authorities) {
        this.userName = userName;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public void addAuthority(SimpleGrantedAuthority authority){
        authorities.add(authority);
    }

    public void addAuthority(String authority){
        authorities.add(new SimpleGrantedAuthority(authority));
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
