package com.gmail.bogatyr.alexander.intech.dto;

import com.gmail.bogatyr.alexander.intech.constant.App;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander Bogatyrenko on 18.08.16.
 * <p>
 * This class represents...
 */
public class UserDTO {

    public interface Registration {} //validation group for registration page
    public interface ChangePass {} //validation group for change password page
    public interface CreateUser {} //validation group for create user page
    public interface UpdateUser {} //validation group for update user page

    private Long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email(groups = {Registration.class, CreateUser.class, UpdateUser.class})
    @Size(max = 100)
    private String email;

    @NotBlank(groups = {Registration.class, CreateUser.class, UpdateUser.class})
    @NotNull(groups = {Registration.class, CreateUser.class, UpdateUser.class})
    @Pattern(regexp = App.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @NotBlank(groups = {Registration.class, CreateUser.class, ChangePass.class})
    @NotNull(groups = {Registration.class, CreateUser.class, ChangePass.class})
    @Size(min = 1, max = 60)
    private String password;

    @NotBlank(groups = {ChangePass.class})
    @NotNull(groups = {ChangePass.class})
    @Size(min = 1, max = 60)
    private String newPassword;

    @NotNull(groups = {UpdateUser.class})
    private Set<String> authorities = new HashSet<>();

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserDTO{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", newPassword='").append(newPassword).append('\'');
        sb.append(", authorities=").append(authorities);
        sb.append('}');
        return sb.toString();
    }
}
