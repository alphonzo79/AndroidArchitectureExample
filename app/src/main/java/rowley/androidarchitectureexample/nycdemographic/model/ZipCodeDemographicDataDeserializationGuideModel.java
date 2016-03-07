package rowley.androidarchitectureexample.nycdemographic.model;

/**
 * The nature of the data coming back from https://data.cityofnewyork.us/resource/rreq-n6zk.json means that we need to do some
 * manual work to build up display strings and put them in the desired order. We will store some information in the file system
 * about known properties from the returned json and use this model in a map to help construct the client-side data model
 */
public class ZipCodeDemographicDataDeserializationGuideModel {
    private int sortOrder;
    private String displayName;
    private boolean isPercentage;

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isPercentage() {
        return isPercentage;
    }

    public void setPercentage(boolean percentage) {
        isPercentage = percentage;
    }
}
