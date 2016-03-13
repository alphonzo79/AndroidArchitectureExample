package rowley.androidarchitectureexample.nycdemographic.loader;

import android.content.Context;

import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * Loader for retrieving data from a single zip code
 */
public class ZipCodeDataLoader extends BaseLoader<ZipCodeDataModel> {
    private ZipCodeDemographicDataDao sqliteDao;
    private ZipCodeDemographicDataDao networkDao;

    private String zipCode;

    public ZipCodeDataLoader(Context context, IDatabaseConfig databaseConfig,
                             NetworkRequestHelper networkRequestHelper, String appToken, String zipCode) {
        super(context);

        sqliteDao = new ZipCodeDemographicDataSqliteDao(context, databaseConfig);
        networkDao = new ZipCodeDemographicDataNetworkDao(context, networkRequestHelper, appToken);
        this.zipCode = zipCode;
    }

    @Override
    public ZipCodeDataModel loadInBackground() {
        result = null;

        result = networkDao.getDataForZipCode(zipCode);
        if(result == null || result.getData() == null || result.getData().isEmpty()) {
            //fall back to local cache
            result = sqliteDao.getDataForZipCode(zipCode);
        } else {
            //cache for possible later use
            ((ZipCodeDemographicDataSqliteDao)sqliteDao).saveDataForZipCode(result);
        }

        return result;
    }
}
