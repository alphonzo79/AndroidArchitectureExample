package rowley.androidarchitectureexample.landing.interactor;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * Response model to communicate back from the ZipCodeDemographicDataInteractor
 */
public interface ZipCodeDemographicDataResponseModel {
    void onDataRetrieved(ZipCodeDataModel data);
}
