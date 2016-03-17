package rowley.androidarchitectureexample.landing.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

/**
 * ViewHolder for an individual data point
 */
public class DemographicDataViewHolder extends RecyclerView.ViewHolder {
    private final static String LABEL_STRING_FORMAT = "%s:";

    @Bind(R.id.label)
    TextView labelTextView;
    @Bind(R.id.value)
    TextView valueTextView;

    public DemographicDataViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(ZipCodeDemographicDataModel data) {
        labelTextView.setText(String.format(DemographicDataViewHolder.LABEL_STRING_FORMAT, data.getDisplayName()));
        valueTextView.setText(data.getDisplayValue());
    }
}
