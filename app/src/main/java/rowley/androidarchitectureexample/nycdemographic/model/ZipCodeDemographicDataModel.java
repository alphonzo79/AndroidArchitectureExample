package rowley.androidarchitectureexample.nycdemographic.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model to hold demographic data for a given zip code as returned from https://data.cityofnewyork.us/resource/rreq-n6zk.json
 */
public class ZipCodeDemographicDataModel implements Parcelable {
    private String displayName;
    private String serverName;
    private String displayValue;

    public ZipCodeDemographicDataModel() {

    }

    protected ZipCodeDemographicDataModel(Parcel in) {
        displayName = in.readString();
        serverName = in.readString();
        displayValue = in.readString();
    }

    public static final Creator<ZipCodeDemographicDataModel> CREATOR = new Creator<ZipCodeDemographicDataModel>() {
        @Override
        public ZipCodeDemographicDataModel createFromParcel(Parcel in) {
            return new ZipCodeDemographicDataModel(in);
        }

        @Override
        public ZipCodeDemographicDataModel[] newArray(int size) {
            return new ZipCodeDemographicDataModel[size];
        }
    };

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayName);
        dest.writeString(serverName);
        dest.writeString(displayValue);
    }
}
