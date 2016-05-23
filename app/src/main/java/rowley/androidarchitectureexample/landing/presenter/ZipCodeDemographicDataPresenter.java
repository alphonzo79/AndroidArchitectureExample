package rowley.androidarchitectureexample.landing.presenter;

import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataResponseModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

/**
 * Presenter for managing display of demographic data for a given zip code
 */
public class ZipCodeDemographicDataPresenter implements ZipCodeDemographicDataResponseModel {
    private ZipCodeDemographicDataView view;
    private ZipCodeDemographicDataInteractor interactor;

    public ZipCodeDemographicDataPresenter(ZipCodeDemographicDataView view, ZipCodeDemographicDataInteractor interactor) {
        this.view = view;
        this.interactor = interactor;

        interactor.startInteractor(this);
    }

    @Override
    public void onDataRetrieved(ZipCodeDataModel data) {
        view.showProgressBar(false);
        if(data != null && data.getData() != null && !data.getData().isEmpty()) {
            view.showZipCodeDemographicData(data);
            view.updateDataDisplay(data.getData().get(0).getDisplayValue());
        } else {
            view.displayZipCodeDataError();
        }
    }

    public void onZipCodeSelected(String zipCode) {
        view.showProgressBar(true);
        interactor.getDataForZipCode(zipCode);
    }

    public void onDataPointSelected(ZipCodeDemographicDataModel data) {
        view.updateDataDisplay(data.getDisplayValue());
    }

    public void stopPresenter() {
        interactor.stopInteractor();
    }
}
