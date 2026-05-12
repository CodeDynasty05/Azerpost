package com.azerpost.app.model.entity;

import com.azerpost.app.model.enums.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserAuthorityEntityTest {

    @Test
    void addingAuthorityToUserDoesNotRecurseThroughHashCode() {
        User user = new User();
        user.setUsername("alice");

        Authority authority = new Authority();
        authority.setAuthority(Role.ROLE_USER);
        authority.setUser(user);

        assertDoesNotThrow(() -> user.getAuthorities().add(authority));
        assertEquals(1, user.getAuthorities().size());
    }
}
