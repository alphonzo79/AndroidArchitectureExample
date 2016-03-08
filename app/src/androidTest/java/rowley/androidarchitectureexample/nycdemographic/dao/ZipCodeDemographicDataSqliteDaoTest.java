package rowley.androidarchitectureexample.nycdemographic.dao;

import javax.inject.Inject;

import rowley.androidarchitectureexample.SimpleActivityTestBase;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.dagger.TestApplicationComponent;

/**
 * Tests for the ZipCodeDemographicDataSqliteDao
 */
public class ZipCodeDemographicDataSqliteDaoTest extends SimpleActivityTestBase {
    @Inject
    ZipCodeDemographicDataSqliteDao dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ((TestApplicationComponent)DaggerInjector.getInstance().getApplicationComponent()).inject(this);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetZipCodes() {

    }
}
