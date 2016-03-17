package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeDataSpinnerAdapter;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataView;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Fragment for the landing view
 */
public class LandingFragment extends Fragment implements ZipCodeDemographicDataView, AdapterView.OnItemSelectedListener {

    @Bind(R.id.data_value_text_view)
    TextView dataValueTextView;
    @Bind(R.id.data_title_spinner)
    Spinner dataTitleSpinner;

    @Inject
    ZipCodeDemographicDataLocalDao localDao;
    @Inject
    ZipCodeDemographicDataDao networkDao;

    private ZipCodeDataSpinnerAdapter spinnerAdapter;
    private ZipCodeDemographicDataPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View view = inflater.inflate(R.layout.landing_fragment, parent, false);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        dataTitleSpinner.setOnItemSelectedListener(this);

        presenter = new ZipCodeDemographicDataPresenter(
                this, new ZipCodeDemographicDataInteractor(localDao, networkDao, AndroidSchedulers.mainThread()));

        return view;
    }

    @Override
    public void onDestroy() {
        presenter.stopPresenter();
        super.onDestroy();
    }

    @Override
    public void showProgressBar(boolean show) {
        ((LandingFragmentListener)getActivity()).showProgressBar(show);
    }

    @Override
    public void showZipCodeDemographicData(ZipCodeDataModel data) {
        if(spinnerAdapter == null) {
            spinnerAdapter = new ZipCodeDataSpinnerAdapter(data.getData());
            dataTitleSpinner.setAdapter(spinnerAdapter);
        } else {
            spinnerAdapter.setData(data.getData());
        }
    }

    @Override
    public void updateDataDisplay(String text) {
        dataValueTextView.setText(text);
    }

    @Override
    public void displayError() {
        Toast.makeText(getContext(), R.string.zip_code_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.onDataPointSelected(spinnerAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    public void setZipCodeSelected(String zipCode) {
        presenter.onZipCodeSelected(zipCode);
    }

    public interface LandingFragmentListener {
        void showProgressBar(boolean show);
    }
}
