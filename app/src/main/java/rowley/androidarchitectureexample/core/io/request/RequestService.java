package rowley.androidarchitectureexample.core.io.request;

/**
 * Holder class for constants related to a data request.
 */
public enum RequestService {
    NYC_DEMOGRAPHIC_DATA("https://data.cityofnewyork.us/resource/rreq-n6zk.json");

    RequestService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
}
