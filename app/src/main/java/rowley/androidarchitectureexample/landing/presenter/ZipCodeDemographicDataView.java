package rowley.androidarchitectureexample.landing.presenter;

import android.content.Context;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * View Interface for displaying ZipCodeDemographicData for a given zip code
 */
public interface ZipCodeDemographicDataView {
    void showProgressBar(boolean show);
    void showZipCodeDemographicData(ZipCodeDataModel data);
    void updateDataDisplay(String text);
    void displayZipCodeDataError();
}
