package rowley.androidarchitectureexample.core.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rowley.androidarchitectureexample.ExampleApplication;
import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.local.StandardDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.io.local.TestDatabaseConfig;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;

/**
 * Module for injecting test components into instrumented tests
 */
@Module
public class TestApplicationModule {
    private ApplicationModule standardModule;

    public TestApplicationModule(ApplicationModule standardModule) {
        this.standardModule = standardModule;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return standardModule.provideApplicationContext();
    }

    @Provides
    @Singleton
    public ExampleApplication provideExampleApplication() {
        return standardModule.provideExampleApplication();
    }

    @Provides
    @Singleton
    public NetworkRequestHelper provideNetworkRequestHelper(Context context) {
        return standardModule.provideNetworkRequestHelper(context);
    }

    @Provides
    @Singleton
    public IDatabaseConfig providesIDatabaseConfig() {
        return new TestDatabaseConfig();
    }

    @Provides
    @Singleton
    public ZipCodeDemographicDataSqliteDao provideZipCodeDemographicDataSqliteDao(ExampleApplication application,
                                                                                  IDatabaseConfig config) {
        return new ZipCodeDemographicDataSqliteDao(application, config);
    }

    @Provides
    @Singleton
    public ZipCodeDemographicDataNetworkDao provideZipCodeDemographicDataNetworkDao(Context context,
                                                                                    NetworkRequestHelper networkRequestHelper) {
        return standardModule.provideZipCodeDemographicDataNetworkDao(context, networkRequestHelper);
    }
}
