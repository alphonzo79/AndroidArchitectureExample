package rowley.androidarchitectureexample.landing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

/**
 * Recycler adapter for individual data points
 */
public class DemographicDataRecyclerAdapter extends RecyclerView.Adapter<DemographicDataViewHolder> {

    private List<ZipCodeDemographicDataModel> data;

    @Override
    public DemographicDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DemographicDataViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.data_point_recycler_item, null));
    }

    @Override
    public void onBindViewHolder(DemographicDataViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void setData(List<ZipCodeDemographicDataModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
