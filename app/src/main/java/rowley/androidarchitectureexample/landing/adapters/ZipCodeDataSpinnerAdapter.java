package rowley.androidarchitectureexample.landing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

/**
 * Adapter for handling the demographic data points for a given zip code
 */
public class ZipCodeDataSpinnerAdapter extends BaseAdapter {

    private List<ZipCodeDemographicDataModel> data;

    public ZipCodeDataSpinnerAdapter(List<ZipCodeDemographicDataModel> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public ZipCodeDemographicDataModel getItem(int position) {
        return data != null && data.size() > position ? data.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        }

        ((TextView)convertView).setText(getItem(position).getDisplayName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /**
     * Set the data for the adapter. This method will call {@link #notifyDataSetChanged()} internally,
     * so the caller does not need to
     * @param data - The data set for the adapter
     */
    public void setData(List<ZipCodeDemographicDataModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
