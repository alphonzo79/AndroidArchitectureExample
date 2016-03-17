package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeSpinnerAdapter;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataView;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListView;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Activity to host the Spinner and raw TextView display
 */
public class SpinnerAndTextActivity extends AppCompatActivity
        implements ZipCodeListView, ZipCodeDemographicDataView, AdapterView.OnItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.zip_code_spinner)
    Spinner zipCodeSpinner;
    @Bind(R.id.text_view)
    TextView textView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    ZipCodeDemographicDataLocalDao localDao;
    @Inject
    ZipCodeDemographicDataDao networkDao;
    @Inject
    SharedPreferences userDefaultSharedPrefs;

    private ZipCodeSpinnerAdapter zipCodeSpinnerAdapter;
    private ZipCodeListPresenter zipCodePresenter;

    private ZipCodeDemographicDataPresenter dataPresenter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SpinnerAndTextActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_text_view_activity);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);

        zipCodePresenter = new ZipCodeListPresenter(
                new ZipCodeListInteractor(localDao, networkDao, userDefaultSharedPrefs, AndroidSchedulers.mainThread()), this);
        zipCodePresenter.startPresenter();

        dataPresenter = new ZipCodeDemographicDataPresenter(
                this, new ZipCodeDemographicDataInteractor(localDao, networkDao, AndroidSchedulers.mainThread()));

        zipCodeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        zipCodePresenter.stopPresenter();
        dataPresenter.stopPresenter();

        super.onDestroy();
    }

    @Override
    public void showZipCodeDemographicData(ZipCodeDataModel data) {
        StringBuilder builder = new StringBuilder();
        for(ZipCodeDemographicDataModel model : data.getData()) {
            if(builder.length() > 0) {
                builder.append("\n");
            }
            builder.append(model.getDisplayName()).append(" : ").append(model.getDisplayValue());
        }

        textView.setText(builder.toString());
    }

    @Override
    public void updateDataDisplay(String text) {
        //do nothing here
    }

    @Override
    public void displayError() {
        Toast.makeText(getContext(), R.string.zip_code_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showZipCodeList(List<String> zipCodeList) {
        if(zipCodeSpinnerAdapter == null) {
            zipCodeSpinnerAdapter = new ZipCodeSpinnerAdapter(zipCodeList);
            zipCodeSpinner.setAdapter(zipCodeSpinnerAdapter);
        } else {
            zipCodeSpinnerAdapter.setData(zipCodeList);
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void notifyDemographicDataPresenterOfSelectedZipCode(String selectedZipCode) {
        dataPresenter.onZipCodeSelected(selectedZipCode);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        zipCodePresenter.onZipCodeSelected(zipCodeSpinnerAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}
