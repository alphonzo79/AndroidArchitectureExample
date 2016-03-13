package rowley.androidarchitectureexample.landing.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter for the zip code spinner
 */
public class ZipCodeSpinnerAdapter extends BaseAdapter {

    private List<String> data;

    public ZipCodeSpinnerAdapter(List<String> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public String getItem(int position) {
        return data != null && data.size() < position ? data.get(position) : "";
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

        ((TextView)convertView).setText(getItem(position));

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
    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
