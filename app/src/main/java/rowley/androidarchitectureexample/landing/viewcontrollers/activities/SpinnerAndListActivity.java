package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.DemographicDataRecyclerAdapter;
import rowley.androidarchitectureexample.landing.adapters.RecyclerViewVerticalSpaceDivider;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeSpinnerAdapter;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataView;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListView;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataReadableDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataWritableDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Activity to present a zip code spinner and a data list in one view
 */
public class SpinnerAndListActivity extends AppCompatActivity
        implements ZipCodeListView, ZipCodeDemographicDataView, AdapterView.OnItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.zip_code_spinner)
    Spinner zipCodeSpinner;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    ZipCodeDemographicDataWritableDao localDao;
    @Inject
    ZipCodeDemographicDataReadableDao networkDao;
    @Inject
    SharedPreferences userDefaultSharedPrefs;

    private ZipCodeSpinnerAdapter zipCodeSpinnerAdapter;
    private ZipCodeListPresenter zipCodePresenter;

    private DemographicDataRecyclerAdapter dataAdapter;
    private ZipCodeDemographicDataPresenter dataPresenter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SpinnerAndListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spinner_and_list_activity);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);

        zipCodePresenter = new ZipCodeListPresenter(
                new ZipCodeListInteractor(localDao, networkDao, userDefaultSharedPrefs, AndroidSchedulers.mainThread()), this);
        zipCodePresenter.startPresenter();

        dataPresenter = new ZipCodeDemographicDataPresenter(
                this, new ZipCodeDemographicDataInteractor(localDao, networkDao, AndroidSchedulers.mainThread()));

        zipCodeSpinner.setOnItemSelectedListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewVerticalSpaceDivider decoration = new RecyclerViewVerticalSpaceDivider(this,
                getResources().getDimensionPixelOffset(R.dimen.recycler_decoration_side_padding));
        recyclerView.addItemDecoration(decoration);

        dataAdapter = new DemographicDataRecyclerAdapter();
        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    protected void onDestroy() {
        zipCodePresenter.stopPresenter();
        dataPresenter.stopPresenter();

        super.onDestroy();
    }

    @Override
    public void showZipCodeDemographicData(ZipCodeDataModel data) {
        dataAdapter.setData(data.getData());
    }

    @Override
    public void updateDataDisplay(String text) {
        //do nothing here
    }

    @Override
    public void displayError() {
        Toast.makeText(this, R.string.zip_code_data_error, Toast.LENGTH_LONG).show();
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
    public void showZipCodeListError() {
        Toast.makeText(this, getString(R.string.zip_code_list_error), Toast.LENGTH_SHORT).show();
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
