package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.DemographicDataRecyclerAdapter;
import rowley.androidarchitectureexample.landing.adapters.RecyclerViewVerticalSpaceDivider;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeDemographicDataView;
import rowley.androidarchitectureexample.landing.viewcontrollers.activities.ActionBarSpinnerActivity;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Fragment to present zip code demographic data in a RecyclerView
 */
public class DataListFragment extends Fragment implements
        ZipCodeDemographicDataView, ActionBarSpinnerActivity.ZipCodeSelectionReceiver {

    @Inject
    ZipCodeDemographicDataLocalDao localDao;
    @Inject
    ZipCodeDemographicDataDao networkDao;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private DemographicDataRecyclerAdapter adapter;

    private ZipCodeDemographicDataPresenter presenter;

    public static DataListFragment newInstance() {
        return new DataListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View view = inflater.inflate(R.layout.data_list_fragment, parent, false);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this, view);

        presenter = new ZipCodeDemographicDataPresenter(
                this, new ZipCodeDemographicDataInteractor(localDao, networkDao, AndroidSchedulers.mainThread()));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewVerticalSpaceDivider decoration = new RecyclerViewVerticalSpaceDivider(getContext(),
                getResources().getDimensionPixelOffset(R.dimen.recycler_decoration_side_padding));
        recyclerView.addItemDecoration(decoration);

        adapter = new DemographicDataRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DataFragmentListener) {
            ((DataFragmentListener)context).registerZipCodeSelectionReceiver(this);
        } else {
            throw new IllegalStateException("The hosting context must implement the DataFragmentListener interface");
        }
    }

    @Override
    public void onDestroy() {
        presenter.stopPresenter();
        super.onDestroy();
    }

    @Override
    public void showProgressBar(boolean show) {
        ((DataFragmentListener)getActivity()).showProgressBar(show);
    }

    @Override
    public void showZipCodeDemographicData(ZipCodeDataModel data) {
        adapter.setData(data.getData());
    }

    @Override
    public void updateDataDisplay(String text) {
        //do nothing. We display everything in the RecyclerView here
    }

    @Override
    public void displayError() {
        Toast.makeText(getContext(), R.string.zip_code_data_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onZipCodeSelected(String zipCode) {
        presenter.onZipCodeSelected(zipCode);
    }
}
