package rowley.androidarchitectureexample.nycdemographic.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.core.io.request.DataRequest;
import rowley.androidarchitectureexample.core.io.request.RequestService;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO for accessing Zip Code data from the network
 */
public class ZipCodeDemographicDataNetworkDao implements ZipCodeDemographicDataDao {
    private final String AUTH_HEADER_NAME = "X-App-Token";
    private final String AUTH_HEADER_VALUE;

    private final String ZIP_CODE_FIELD_NAME = "jurisdiction_name";

    private NetworkRequestHelper networkRequestHelper;

    public ZipCodeDemographicDataNetworkDao(NetworkRequestHelper networkRequestHelper, String appToken) {
        this.networkRequestHelper = networkRequestHelper;
        this.AUTH_HEADER_VALUE = appToken;
    }

    @Override
    public List<String> getZipCodes() {
        List<String> result = new ArrayList<String>();

        DataRequest request = new DataRequest(RequestService.NYC_DEMOGRAPHIC_DATA, null);
        request.addHeader(AUTH_HEADER_NAME, AUTH_HEADER_VALUE);

        JsonArray jsonArray = networkRequestHelper.sendRequestAndWait(request);
        if(jsonArray != null) {
            for(JsonElement element : jsonArray) {
                if(element instanceof JsonObject) {
                    JsonObject elementObj = element.getAsJsonObject();
                    if(elementObj.has(ZIP_CODE_FIELD_NAME)) {
                        result.add(elementObj.get(ZIP_CODE_FIELD_NAME).getAsString());
                    }
                }
            }
        }

        return result;
    }

    @Override
    public List<ZipCodeDataModel> getDataForAllZipCodes() {
        // TODO: 3/6/16
        return null;
    }

    @Override
    public ZipCodeDataModel getDataForZipCode(String zipCode) {
        // TODO: 3/6/16
        return null;
    }
}
