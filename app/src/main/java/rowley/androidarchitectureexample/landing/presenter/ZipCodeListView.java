package rowley.androidarchitectureexample.landing.presenter;

import android.content.Context;

import java.util.List;

/**
 * View Interface for working with the zip code list
 */
public interface ZipCodeListView {
    void showProgressBar(boolean show);
    void showZipCodeList(List<String> zipCodeList);
    void showZipCodeListError();
    void notifyDemographicDataPresenterOfSelectedZipCode(String selectedZipCode);
}