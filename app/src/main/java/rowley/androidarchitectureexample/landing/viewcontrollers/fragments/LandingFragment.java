package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeDataSpinnerAdapter;
import rowley.androidarchitectureexample.nycdemographic.loader.ZipCodeDataLoader;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

/**
 * Fragment for the landing view
 */
public class LandingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ZipCodeDataModel>, AdapterView.OnItemSelectedListener {
    private final int ZIP_CODE_DATA_LOADER_ID = 2;
    private final String ZIP_CODE_BUNDLE_KEY = "zipCode";

    @Bind(R.id.data_value_text_view)
    TextView dataValueTextView;
    @Bind(R.id.data_title_spinner)
    Spinner dataTitleSpinner;

    @Inject
    IDatabaseConfig databaseConfig;
    @Inject
    NetworkRequestHelper networkRequestHelper;

    private ZipCodeDataSpinnerAdapter spinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View view = inflater.inflate(R.layout.landing_fragment, parent, false);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        dataTitleSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public Loader<ZipCodeDataModel> onCreateLoader(int id, Bundle args) {
        if(id == ZIP_CODE_DATA_LOADER_ID) {
            String zipCode = args.getString(ZIP_CODE_BUNDLE_KEY);
            ZipCodeDataLoader loader = new ZipCodeDataLoader(
                    getActivity(), databaseConfig, networkRequestHelper, getString(R.string.app_token), zipCode);
            return loader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ZipCodeDataModel> loader, ZipCodeDataModel data) {
        ((LandingFragmentListener)getActivity()).showProgressBar(false);

        if(spinnerAdapter == null) {
            spinnerAdapter = new ZipCodeDataSpinnerAdapter(data.getData());
            dataTitleSpinner.setAdapter(spinnerAdapter);
        } else {
            spinnerAdapter.setData(data.getData());
        }

        dataValueTextView.setText(data.getData().get(dataTitleSpinner.getSelectedItemPosition()).getDisplayValue());
    }

    @Override
    public void onLoaderReset(Loader<ZipCodeDataModel> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ZipCodeDemographicDataModel data = spinnerAdapter.getItem(position);
        dataValueTextView.setText(data.getDisplayValue());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    public void setZipCodeSelected(String zipCode) {
        ((LandingFragmentListener)getActivity()).showProgressBar(true);

        Bundle args = new Bundle();
        args.putString(ZIP_CODE_BUNDLE_KEY, zipCode);

        if(getLoaderManager().getLoader(ZIP_CODE_DATA_LOADER_ID) == null) {
            getLoaderManager().initLoader(ZIP_CODE_DATA_LOADER_ID, args, this);
        } else {
            getLoaderManager().restartLoader(ZIP_CODE_DATA_LOADER_ID, args, this);
        }

    }

    public interface LandingFragmentListener {
        void showProgressBar(boolean show);
    }
}
