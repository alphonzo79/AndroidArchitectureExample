package rowley.androidarchitectureexample.core.dagger;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rowley.androidarchitectureexample.ExampleApplication;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.local.StandardDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;

/**
 * Main application module for injecting dependencies into the app.
 */
@Module
public class ApplicationModule {
    private ExampleApplication application;

    public ApplicationModule(ExampleApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public ExampleApplication provideExampleApplication() {
        return application;
    }
    
    @Provides
    @Singleton
    public NetworkRequestHelper provideNetworkRequestHelper(Context context) {
        return new NetworkRequestHelper(context);
    }

    @Provides
    @Singleton
    public IDatabaseConfig providesIDatabaseConfig() {
        return new StandardDatabaseConfig();
    }

    @Provides
    @Singleton
    public ZipCodeDemographicDataSqliteDao provideZipCodeDemographicDataSqliteDao(ExampleApplication application,
                                                                                  IDatabaseConfig config) {
        return new ZipCodeDemographicDataSqliteDao(application, config);
    }

    @Provides
    @Singleton
    public ZipCodeDemographicDataNetworkDao provideZipCodeDemographicDataNetworkDao(NetworkRequestHelper networkRequestHelper) {
        return new ZipCodeDemographicDataNetworkDao(networkRequestHelper, application.getString(R.string.app_token));
    }
}
