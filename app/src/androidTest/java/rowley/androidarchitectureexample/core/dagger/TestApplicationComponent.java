package rowley.androidarchitectureexample.core.dagger;

import javax.inject.Singleton;

import dagger.Component;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDaoTest;

/**
 * Application Component meant for testing. Injects test resources instead of standard
 */
@Singleton
@Component(
        modules = { TestApplicationModule.class }
)
public interface TestApplicationComponent extends ApplicationComponent {
    void inject(ZipCodeDemographicDataSqliteDaoTest zipCodeDemographicDataSqliteDaoTest);
}
