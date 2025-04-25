package com.example.skystayback.models;

import com.example.skystayback.enums.UserRol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user", schema = "skystay")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_code", nullable = false, length = 16)
    private String userCode;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, length = 60)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private Date birthDate;

    @Column(name = "nif", nullable = false, length = 50)
    private String nif;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "img", length = 500)
    private String img;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "rol", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getAuthority()));
    }

    @Override
    public String getUsername() {
        return email;
    }

}
