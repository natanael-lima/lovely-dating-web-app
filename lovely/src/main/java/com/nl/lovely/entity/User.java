package com.nl.lovely.entity;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.nl.lovely.enums.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="users", uniqueConstraints= @UniqueConstraint(columnNames = "username"))
public class User implements UserDetails{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@Column(name = "username", nullable=false)
    private String username;
	@Column(name = "password")
    private String password;
	@Column(name = "lastname")
    private String lastname;
	@Column(name = "name")
	private String name;
	@Column(name = "loggedIn")
	private Boolean loggedIn;
	@Enumerated(EnumType.STRING)
	private RoleType role; //ADMIN-USER
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserProfile profile;
   
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public UserDetails orElseThrow() {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public String toString() {
	    return "User [id=" + id + ", username=" + username + ", profile=" + profile + "]";
	}

    
    
}
