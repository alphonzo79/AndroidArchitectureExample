package rowley.androidarchitectureexample.landing.presenter;

import java.util.List;

import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListResponseListener;

/**
 * View Presenter for working with a zip code list
 */
public class ZipCodeListPresenter implements ZipCodeListResponseListener {
    private ZipCodeListInteractor interactor;
    private ZipCodeListView view;

    public ZipCodeListPresenter(ZipCodeListInteractor interactor, ZipCodeListView view) {
        this.interactor = interactor;
        this.view = view;
    }

    @Override
    public void onListRetreived(List<String> zipCodeList) {
        view.showProgressBar(false);
        if(zipCodeList != null && !zipCodeList.isEmpty()) {
            view.showZipCodeList(zipCodeList);
        } else {
            view.showError(view.getContext().getString(R.string.zip_code_list_error));
        }
    }

    @Override
    public void onError() {
        view.showProgressBar(false);
        view.showError(view.getContext().getString(R.string.zip_code_list_error));
    }

    public void startPresenter() {
        view.showProgressBar(true);
        interactor.startInteractor(this);
        interactor.getZipCodeList();
    }

    public void stopPresenter() {
        interactor.stopInteractor();
    }

    public void onZipCodeSelected(String selectedZipCode) {
        view.showProgressBar(true);
        view.notifyDemographicDataPresenterOfSelectedZipCode(selectedZipCode);
    }
}
