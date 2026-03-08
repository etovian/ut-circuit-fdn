package com.utcfdn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatabaseSchemaVerificationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void verifyTablesExist() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet rs = metaData.getTables(null, "public", "app_health_check", new String[]{"TABLE"})) {
                assertTrue(rs.next(), "Table 'app_health_check' should exist in the 'public' schema of 'ut_circuit' database");
            }
        }
    }
}
