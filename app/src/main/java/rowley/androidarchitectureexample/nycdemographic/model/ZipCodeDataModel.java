package rowley.androidarchitectureexample.nycdemographic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent the data for a single zip code
 */
public class ZipCodeDataModel implements Parcelable {
    private String jurisdictionName;
    private List<ZipCodeDemographicDataModel> data;
    private String originalJson;

    public ZipCodeDataModel() {

    }

    protected ZipCodeDataModel(Parcel in) {
        jurisdictionName = in.readString();
        data = new ArrayList<>();
        in.readTypedList(data, ZipCodeDemographicDataModel.CREATOR);
        originalJson = in.readString();
    }

    public static final Creator<ZipCodeDataModel> CREATOR = new Creator<ZipCodeDataModel>() {
        @Override
        public ZipCodeDataModel createFromParcel(Parcel in) {
            return new ZipCodeDataModel(in);
        }

        @Override
        public ZipCodeDataModel[] newArray(int size) {
            return new ZipCodeDataModel[size];
        }
    };

    public String getJurisdictionName() {
        return jurisdictionName;
    }

    public void setJurisdictionName(String jurisdictionName) {
        this.jurisdictionName = jurisdictionName;
    }

    public List<ZipCodeDemographicDataModel> getData() {
        return data;
    }

    public void setData(List<ZipCodeDemographicDataModel> data) {
        this.data = data;
    }

    public String getOriginalJson() {
        return originalJson;
    }

    public void setOriginalJson(String originalJson) {
        this.originalJson = originalJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jurisdictionName);
        dest.writeTypedList(data);
        dest.writeString(originalJson);
    }
}
