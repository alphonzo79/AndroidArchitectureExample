package rowley.androidarchitectureexample.landing.interactor;

import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataReadableDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataWritableDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Interactor for managing demographic data for an individual zip code
 */
public class ZipCodeDemographicDataInteractor {

    private ZipCodeDemographicDataWritableDao localDao;
    private ZipCodeDemographicDataReadableDao networkDao;
    private Scheduler observationScheduler;
    private Subscription zipCodeDataSubscription;

    private ZipCodeDemographicDataResponseModel responseListener;

    public ZipCodeDemographicDataInteractor(
            ZipCodeDemographicDataWritableDao localDao, ZipCodeDemographicDataReadableDao networkDao, Scheduler observationScheduler) {
        this.localDao = localDao;
        this.networkDao = networkDao;
        this.observationScheduler = observationScheduler;
    }

    public void startInteractor(ZipCodeDemographicDataResponseModel responseListener) {
        this.responseListener = responseListener;
    }

    public void getDataForZipCode(final String zipCode) {
        zipCodeDataSubscription = Observable.create(new Observable.OnSubscribe<ZipCodeDataModel>() {
            @Override
            public void call(Subscriber<? super ZipCodeDataModel> subscriber) {
                ZipCodeDataModel result = networkDao.getDataForZipCode(zipCode);
                if(result != null && result.getData() != null && !result.getData().isEmpty()) {
                    localDao.saveDataForZipCode(result);
                } else {
                    result = localDao.getDataForZipCode(zipCode);
                }

                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(observationScheduler).subscribe(new Action1<ZipCodeDataModel>() {
            @Override
            public void call(ZipCodeDataModel zipCodeDataModel) {
                responseListener.onDataRetrieved(zipCodeDataModel);
            }
        });
    }

    public void stopInteractor() {
        if(zipCodeDataSubscription != null && !zipCodeDataSubscription.isUnsubscribed()) {
            zipCodeDataSubscription.unsubscribe();
        }
    }
}
