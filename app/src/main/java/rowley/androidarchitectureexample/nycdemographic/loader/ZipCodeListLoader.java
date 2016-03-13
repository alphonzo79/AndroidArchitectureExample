package rowley.androidarchitectureexample.nycdemographic.loader;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import java.util.List;

import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;

/**
 * Loader for the zip code list
 */
public class ZipCodeListLoader extends BaseLoader<List<String>> {
    private ZipCodeDemographicDataDao sqliteDao;
    private ZipCodeDemographicDataDao networkDao;
    private long millisSinceDataCache;

    private final String SHARED_PREFS_NAME = "zipCodeData";
    private final String ZIP_CODE_CACHE_DATE = "zipCodeCachedDate";

    private final long ZIP_CODE_CACHE_THRESHOLD = DateUtils.DAY_IN_MILLIS * 5;

    public ZipCodeListLoader(Context context, IDatabaseConfig databaseConfig,
                             NetworkRequestHelper networkRequestHelper, String appToken) {
        super(context);

        sqliteDao = new ZipCodeDemographicDataSqliteDao(context, databaseConfig);
        networkDao = new ZipCodeDemographicDataNetworkDao(context, networkRequestHelper, appToken);

        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        long lastCache = prefs.getInt(ZIP_CODE_CACHE_DATE, 0);
        millisSinceDataCache = System.currentTimeMillis() - lastCache;
    }

    @Override
    public List<String> loadInBackground() {
        result = null;

        if(millisSinceDataCache < ZIP_CODE_CACHE_THRESHOLD) {
            result = sqliteDao.getZipCodes();
        } else {
            networkDao.getZipCodes();
        }

        return result;
    }
}
