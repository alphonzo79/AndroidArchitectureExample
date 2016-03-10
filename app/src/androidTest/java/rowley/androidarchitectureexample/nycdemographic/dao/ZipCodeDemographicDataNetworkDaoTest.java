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
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

import static org.mockito.Matchers.any;
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
        when(mockNetworkRequestHelper.sendRequestAndWait(any(DataRequest.class))).
                thenReturn(jsonParser.parse(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_ARRAY).getAsJsonArray());

        ArgumentCaptor<DataRequest> dataRequestArgumentCaptor = ArgumentCaptor.forClass(DataRequest.class);

        List<ZipCodeDataModel> foundData = dao.getDataForAllZipCodes();

        verify(mockNetworkRequestHelper).sendRequestAndWait(dataRequestArgumentCaptor.capture());
        DataRequest foundRequest = dataRequestArgumentCaptor.getValue();
        verifyMostOfTheDataRequest(foundRequest);
        assertTrue(foundRequest.getParams().isEmpty());

        assertNotNull(foundData);
        assertEquals(13, foundData.size());

        //All of them are the same in this test, check them all the same
        for(ZipCodeDataModel model : foundData) {
            assertEquals("10001", model.getJurisdictionName());
            assertEquals(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, model.getOriginalJson());
            assertNotNull(model.getData());
            List<ZipCodeDemographicDataModel> demoData = model.getData();
            assertEquals(46, demoData.size());

            //Check one randomly
            assertNotNull(demoData.get(24));
            ZipCodeDemographicDataModel testModel = demoData.get(24);
            assertEquals("percent_permanent_resident_alien", testModel.getServerName());
            assertEquals("Resident Alien - Percentage", testModel.getDisplayName());
            assertEquals("5%", testModel.getDisplayValue());
        }
    }

    public void testGetDataForZipCode() {
        when(mockNetworkRequestHelper.sendRequestAndWait(any(DataRequest.class))).
                thenReturn(jsonParser.parse(ZipCodeDataDaoTestUtil.SAMPLE_SINGLE_ZIP_DATA_ARRAY).getAsJsonArray());

        ArgumentCaptor<DataRequest> dataRequestArgumentCaptor = ArgumentCaptor.forClass(DataRequest.class);

        String zipCode = "12345";

        ZipCodeDataModel foundModel = dao.getDataForZipCode(zipCode);

        verify(mockNetworkRequestHelper).sendRequestAndWait(dataRequestArgumentCaptor.capture());
        DataRequest foundRequest = dataRequestArgumentCaptor.getValue();
        verifyMostOfTheDataRequest(foundRequest);
        assertEquals(1, foundRequest.getParams().size());
        assertTrue(foundRequest.getParams().containsKey("jurisdiction_name"));
        assertEquals(zipCode, foundRequest.getParams().get("jurisdiction_name"));

        assertEquals("10001", foundModel.getJurisdictionName());
        assertEquals(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, foundModel.getOriginalJson());
        assertNotNull(foundModel.getData());
        List<ZipCodeDemographicDataModel> demoData = foundModel.getData();
        assertEquals(46, demoData.size());

        //Check one randomly
        assertNotNull(demoData.get(17));
        ZipCodeDemographicDataModel testData = demoData.get(17);
        assertEquals("count_ethnicity_unknown", testData.getServerName());
        assertEquals("Ethnicity Unknown - Count", testData.getDisplayName());
        assertEquals("0", testData.getDisplayValue());
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
