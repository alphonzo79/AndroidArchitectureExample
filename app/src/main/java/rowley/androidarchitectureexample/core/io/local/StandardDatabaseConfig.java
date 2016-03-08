package rowley.androidarchitectureexample.core.io.local;

/**
 * DatabaseConfig for the standard settings
 */
public class StandardDatabaseConfig implements IDatabaseConfig {
    @Override
    public String getDatabaseName() {
        return "AndroidArchExampleDB";
    }

    @Override
    public int getDatabaseVersion() {
        return 1;
    }
}
