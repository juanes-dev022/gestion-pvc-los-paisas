package com.gestionpvc.infrastructures.config;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import com.zaxxer.hikari.HikariConfig;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class DbConfigurator {
    private DbConfigurator(){}

    public static DataSource createDataSource() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dotenv.get("JDBC_URL", "jdbc:sqlite:database/inventario.db"));
        config.setDriverClassName("org.sqlite.JDBC");
        config.setMaximumPoolSize(Integer.parseInt(dotenv.get("POOL_MAX_SIZE", "5")));
        config.addDataSourceProperty("foreign_keys", "true");
        
        HikariDataSource dataSource = new HikariDataSource(config);
        
        // Ejecutar schema.sql al inicializar
        initializeDatabase(dataSource);
        
        return dataSource;
    }

    private static void initializeDatabase(DataSource dataSource) {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String schemaPath = "database/schema.sql";
            String schema = new String(Files.readAllBytes(Paths.get(schemaPath)));
            
            // Ejecutar cada comando separado por ;
            for (String sql : schema.split(";")) {
                String trimmed = sql.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
            
            System.out.println("✅ Base de datos inicializada correctamente");
        } catch (Exception e) {
            System.err.println("⚠️ Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
