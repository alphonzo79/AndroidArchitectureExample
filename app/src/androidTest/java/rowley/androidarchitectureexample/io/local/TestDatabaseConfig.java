package rowley.androidarchitectureexample.io.local;

import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;

/**
 * Config for test database so we don't interfere with production data during testing
 */
public class TestDatabaseConfig implements IDatabaseConfig {
    @Override
    public String getDatabaseName() {
        return "TestAndroidExampleDb";
    }

    @Override
    public int getDatabaseVersion() {
        return 1;
    }
}
