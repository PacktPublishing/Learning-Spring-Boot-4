package com.learningspringboot4;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class UserAccount {

  @Id
  @GeneratedValue
  private Long id;

  private String username;
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_account_authorities", joinColumns = @JoinColumn(name = "user_account_id"))
  @Column(name = "authority")
  private List<String> authorities = new ArrayList<>();

  protected UserAccount() {}

  public UserAccount(String username, String password, String... authorities) {
    this.username = username;
    this.password = password;
    this.authorities = Arrays.asList(authorities);
  }

  public UserDetails asUser() {
    var grantedAuthorities = getAuthorities().stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

    return User.withDefaultPasswordEncoder()
            .username(getUsername())
            .password(getPassword())
            .authorities(grantedAuthorities)
            .build();
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public List<String> getAuthorities() {
    if (authorities == null) {
      this.authorities = new ArrayList<>();
    }
    return authorities;
  }

  public void setAuthorities(List<String> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof UserAccount that)) return false;
    return Objects.equals(id, that.id)
            && Objects.equals(username, that.username)
            && Objects.equals(password, that.password)
            && Objects.equals(authorities, that.authorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, authorities);
  }

  @Override
  public String toString() {
    return "UserAccount{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", authorities=" + authorities +
            '}';
  }
}