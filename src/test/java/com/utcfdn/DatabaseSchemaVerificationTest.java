package com.utcfdn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DatabaseSchemaVerificationTest {

    @Autowired
    private DataSource dataSource;

    @Test
    void verifyTablesExist() throws SQLException {
        try (var connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            
            // Try different schema names and case for compatibility (H2 vs PostgreSQL)
            boolean found = false;
            String[] schemasToTry = {null, "public", "PUBLIC"};
            String[] tableNamesToTry = {"app_health_check", "APP_HEALTH_CHECK"};
            for (String schema : schemasToTry) {
                for (String tableName : tableNamesToTry) {
                    try (ResultSet rs = metaData.getTables(null, schema, tableName, new String[]{"TABLE"})) {
                        if (rs.next()) {
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
            }
            assertTrue(found, "Table 'app_health_check' should exist.");
        }
    }
}
