package com.example.jwtdemo.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Represents a user entity that implements the UserDetails interface.
 * This allows the user entity to be used with Spring Security for authentication and authorization.
 */
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private  String lastName;
    private String password;
    private String email;
    // Use the @Enumerated annotation to specify the mapping of Enum type properties
    @Enumerated(EnumType.STRING)
    private Rols rols;
    // constructors

    public User() {
    }

    public User(Long id, String firstName, String lastName, String password, String email, Rols rols) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.rols = rols;
    }
// getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rols getRols() {
        return rols;
    }

    public void setRols(Rols rols) {
        this.rols = rols;
    }


    // spring security overides

    /**
     * Returns a collection of authorities (roles) assigned to the user.
     * In this case, it returns a single SimpleGrantedAuthority based on the user's role.
     * This collection is used by Spring Security for role-based access control.
     *
     * @return A collection of authorities (roles) assigned to the user.
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rols.name())); // for user rols
    }

    /**
     * Returns the user's password for authentication.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username (in this case, the user's email) for authentication.
     *
     * @return The username (email) associated with the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     * In this implementation, it always returns true, indicating that the account is not expired.
     *
     * @return True if the user's account is not expired, otherwise false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     * In this implementation, it always returns true, indicating that the account is not locked.
     *
     * @return True if the user's account is not locked, otherwise false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) are expired.
     * In this implementation, it always returns true, indicating that the credentials are not expired.
     *
     * @return True if the user's credentials are not expired, otherwise false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled and allowed to authenticate.
     * In this implementation, it always returns true, indicating that the user is enabled.
     *
     * @return True if the user is enabled, otherwise false.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    // tostring

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", rols=" + rols +
                '}';
    }
}
