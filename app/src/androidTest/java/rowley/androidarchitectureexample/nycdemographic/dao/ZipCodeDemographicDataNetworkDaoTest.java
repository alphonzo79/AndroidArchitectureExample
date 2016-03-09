package rowley.androidarchitectureexample.nycdemographic.dao;

import com.google.gson.JsonParser;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import rowley.androidarchitectureexample.SimpleActivityTestBase;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.core.io.request.DataRequest;
import rowley.androidarchitectureexample.core.io.request.RequestService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for the network DAO for zip code demo data
 */
public class ZipCodeDemographicDataNetworkDaoTest extends SimpleActivityTestBase {
    private final String AUTH_HEADER_NAME = "X-App-Token";

    private ZipCodeDemographicDataNetworkDao dao;
    private JsonParser jsonParser = new JsonParser();

    @Mock
    NetworkRequestHelper mockNetworkRequestHelper;

    private final String MOCK_APP_TOKEN = "abcd1234";

    @Override
    public void setUp() throws Exception {
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        initMocks(this);
        dao = new ZipCodeDemographicDataNetworkDao(getActivity(), mockNetworkRequestHelper, MOCK_APP_TOKEN);
    }

    public void testGetZipCodes() {
        when(mockNetworkRequestHelper.sendRequestAndWait(any(DataRequest.class))).
                thenReturn(jsonParser.parse(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_ARRAY).getAsJsonArray());

        ArgumentCaptor<DataRequest> dataRequestArgumentCaptor = ArgumentCaptor.forClass(DataRequest.class);

        List<String> zipCodes = dao.getZipCodes();

        verify(mockNetworkRequestHelper).sendRequestAndWait(dataRequestArgumentCaptor.capture());
        DataRequest foundRequest = dataRequestArgumentCaptor.getValue();
        verifyMostOfTheDataRequest(foundRequest);
        assertTrue(foundRequest.getParams().isEmpty());

        assertNotNull(zipCodes);
        assertEquals(13, zipCodes.size());

        //All of them are the same in this test, just look at each and make sure they are all there
        for(String zipCode : zipCodes) {
            assertNotNull(zipCode);
            assertEquals("10001", zipCode);
        }
    }

    public void testGetDataForAllZipCodes() {

    }

    public void testGetDataForZipCode() {

    }

    private void verifyMostOfTheDataRequest(DataRequest foundRequest) {
        assertEquals(RequestService.NYC_DEMOGRAPHIC_DATA, foundRequest.getService());
        assertNull(foundRequest.getGetMethod());
        assertNotNull(foundRequest.getHeaders());
        Map<String, String> headers = foundRequest.getHeaders();
        assertEquals(1, headers.size());
        assertTrue(headers.containsKey(AUTH_HEADER_NAME));
        assertEquals(MOCK_APP_TOKEN, headers.get(AUTH_HEADER_NAME));
        assertNotNull(foundRequest.getParams());
    }
}
