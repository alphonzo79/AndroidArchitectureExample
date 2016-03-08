package rowley.androidarchitectureexample.core.io.local;

/**
 * Interface for database config, allowing us to configure the database based on build (and testing)
 */
public interface IDatabaseConfig {
    String getDatabaseName();
    int getDatabaseVersion();
}
