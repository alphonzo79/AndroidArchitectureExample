package rowley.androidarchitectureexample.core.io.request;

import java.util.Map;
import java.util.TreeMap;

/**
 * Wrapper class to gather parameters and basic information for a request
 */
public class DataRequest {
    Map<String, String> getParams;
    Map<String, String> headers;
    RequestService service;
    String method;

    public DataRequest(RequestService service, String method) {
        getParams = new TreeMap<String, String>();
        headers = new TreeMap<String, String>();
        this.method = method;
        this.service = service;
    }

    public String getGetMethod() {
        return method;
    }

    public void addParam(String paramName, String paramValue) {
        getParams.put(paramName, paramValue);
    }

    public Map<String, String> getParams() {
        return getParams;
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public RequestService getService() {
        return service;
    }
}
