package rowley.androidarchitectureexample.landing.interactor;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Interactor for working with a zip code list
 */
public class ZipCodeListInteractor {
    private final String TAG = ZipCodeListInteractor.class.getSimpleName();
    private final String ZIP_CODE_CACHE_DATE = "zipCodeCachedDate";
    private final long ZIP_CODE_CACHE_THRESHOLD = DateUtils.DAY_IN_MILLIS * 5;

    private ZipCodeDemographicDataSqliteDao sqliteDao;
    private ZipCodeDemographicDataDao networkDao;
    private SharedPreferences userDefaultSharedPrefs;
    private ZipCodeListResponseListener responseListener;

    private Scheduler observationScheduler;
    private Subscription zipCodeSubscription;

    /**
     * Build an interactor for retrieving a list of zip codes that we can get data about
     * @param sqliteDao - Local gateway
     * @param networkDao - Network gateway
     * @param userDefaultSharedPrefs - User default shared preferences, used for tracking last data cache date
     * @param observationScheduler - Which thread do you want to receive result on?
     */
    public ZipCodeListInteractor(ZipCodeDemographicDataSqliteDao sqliteDao, ZipCodeDemographicDataNetworkDao networkDao,
                                 SharedPreferences userDefaultSharedPrefs, Scheduler observationScheduler) {
        this.sqliteDao = sqliteDao;
        this.networkDao = networkDao;
        this.userDefaultSharedPrefs = userDefaultSharedPrefs;
        this.observationScheduler = observationScheduler;
    }

    /**
     * Start this interactor and tell it how to tell us about results.
     * This method MUST be called before calling {@link #getZipCodeList()}
     * @param responseListener - the callback listener that will receive the list
     */
    public void startInteractor(ZipCodeListResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    /**
     * Get the list of zip codes about which we can later fetch data
     */
    public void getZipCodeList() {
        zipCodeSubscription = Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> result = null;

                long lastCache = userDefaultSharedPrefs.getLong(ZIP_CODE_CACHE_DATE, 0);
                long millisSinceDataCache = System.currentTimeMillis() - lastCache;

                if(millisSinceDataCache < ZIP_CODE_CACHE_THRESHOLD) {
                    result = sqliteDao.getZipCodes();
                } else {
                    result = networkDao.getZipCodes();
                    if(result != null) {
                        sqliteDao.saveZipCodes(result, true);
                        userDefaultSharedPrefs.edit().putLong(ZIP_CODE_CACHE_DATE, System.currentTimeMillis()).apply();
                    } else {
                        //fall back to cached data
                        result = sqliteDao.getZipCodes();
                    }
                }

                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(observationScheduler).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                responseListener.onListRetreived(strings);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, throwable.getMessage(), throwable);
                responseListener.onError();
            }
        });
    }

    /**
     * Stop the interactor. This should be called when any views at the other end of the chain are destroyed, giving the
     * interactor the opportunity to clean up after itself
     */
    public void stopInteractor() {
        if(zipCodeSubscription != null && !zipCodeSubscription.isUnsubscribed()) {
            zipCodeSubscription.unsubscribe();
        }
    }
}
