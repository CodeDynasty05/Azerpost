package com.azerpost.app.model.entity;

import com.azerpost.app.model.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "authorities")
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    private Role authority;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Override
    public String getAuthority() {
        return authority.name();
    }
}
