package com.azerpost.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SchemaConstraintIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void usersTableHasExpectedUniquenessAndNullability() {
        assertTrue(hasUniqueConstraint("users", "username"));
        assertTrue(hasUniqueConstraint("users", "email"));
        assertTrue(isColumnNotNull("users", "username"));
        assertTrue(isColumnNotNull("users", "email"));
    }

    @Test
    void shipmentsTrackingNumberIsUnique() {
        assertTrue(hasUniqueConstraint("shipments", "tracking_number"));
    }

    @Test
    void sessionsTableExistsWithExpectedColumns() {
        assertTrue(tableExists("sessions"));
        assertTrue(isColumnNotNull("sessions", "user_id"));
        assertTrue(isColumnNotNull("sessions", "refresh_token"));
        assertTrue(isColumnNotNull("sessions", "created_at"));
        assertFalse(columnExists("users", "refresh_token"));
        assertFalse(columnExists("users", "session_version"));
    }

    private boolean hasUniqueConstraint(String tableName, String columnName) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.table_constraints tc
                JOIN information_schema.key_column_usage kcu
                  ON tc.constraint_name = kcu.constraint_name
                 AND tc.table_schema = kcu.table_schema
                 AND tc.table_name = kcu.table_name
                WHERE tc.table_schema = 'public'
                  AND tc.table_name = ?
                  AND tc.constraint_type = 'UNIQUE'
                  AND kcu.column_name = ?
                """, Integer.class, tableName, columnName);

        return count != null && count > 0;
    }

    private boolean isColumnNotNull(String tableName, String columnName) {
        String nullable = jdbcTemplate.queryForObject("""
                SELECT is_nullable
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = ?
                  AND column_name = ?
                """, String.class, tableName, columnName);

        return nullable != null && nullable.equals("NO");
    }

    private boolean tableExists(String tableName) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = 'public'
                  AND table_name = ?
                """, Integer.class, tableName);

        return count != null && count > 0;
    }

    private boolean columnExists(String tableName, String columnName) {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.columns
                WHERE table_schema = 'public'
                  AND table_name = ?
                  AND column_name = ?
                """, Integer.class, tableName, columnName);

        return count != null && count > 0;
    }
}
