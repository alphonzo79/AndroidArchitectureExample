package rowley.androidarchitectureexample.landing.interactor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for the ZipCodeDemographicDataInteractor
 */
public class ZipCodeDemographicDataInteractorTest {

    @Mock
    ZipCodeDemographicDataLocalDao mockLocalDao;
    @Mock
    ZipCodeDemographicDataDao mockNetworkDao;
    @Mock
    ZipCodeDemographicDataResponseModel mockResponstListener;

    private ZipCodeDemographicDataInteractor interactor;

    @Before
    public void setup() {
        initMocks(this);

        interactor = new ZipCodeDemographicDataInteractor(mockLocalDao, mockNetworkDao, Schedulers.immediate());
        interactor.startInteractor(mockResponstListener);
    }

    @Test
    public void testGetDataForZipCodeFromNetwork() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        mockList.add(mockDataModel);
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        when(mockNetworkDao.getDataForZipCode(anyString())).thenReturn(mockZipCodeModel);

        String zipCode = "ZipCode";
        interactor.getDataForZipCode(zipCode);

        //Check this one first so we only have to do one timeout
        verify(mockResponstListener, timeout(100)).onDataRetrieved(mockZipCodeModel);
        verify(mockNetworkDao).getDataForZipCode(zipCode);
        verify(mockLocalDao).saveDataForZipCode(mockZipCodeModel);

        verifyNoMoreInteractions(mockNetworkDao);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockResponstListener);
    }

    @Test
    public void testGetDataForZipCodeFromLocalNullData() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        mockList.add(mockDataModel);
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        when(mockNetworkDao.getDataForZipCode(anyString())).thenReturn(null);
        when(mockLocalDao.getDataForZipCode(anyString())).thenReturn(mockZipCodeModel);

        String zipCode = "ZipCode";
        interactor.getDataForZipCode(zipCode);

        //Check this one first so we only have to do one timeout
        verify(mockResponstListener, timeout(100)).onDataRetrieved(mockZipCodeModel);
        verify(mockNetworkDao).getDataForZipCode(zipCode);
        verify(mockLocalDao).getDataForZipCode(zipCode);

        verifyNoMoreInteractions(mockNetworkDao);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockResponstListener);
    }

    @Test
    public void testGetDataForZipCodeFromLocalNullDataSet() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        mockList.add(mockDataModel);
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        ZipCodeDataModel emptyDataModel = new ZipCodeDataModel();

        when(mockNetworkDao.getDataForZipCode(anyString())).thenReturn(emptyDataModel);
        when(mockLocalDao.getDataForZipCode(anyString())).thenReturn(mockZipCodeModel);

        String zipCode = "ZipCode";
        interactor.getDataForZipCode(zipCode);

        //Check this one first so we only have to do one timeout
        verify(mockResponstListener, timeout(100)).onDataRetrieved(mockZipCodeModel);
        verify(mockNetworkDao).getDataForZipCode(zipCode);
        verify(mockLocalDao).getDataForZipCode(zipCode);

        verifyNoMoreInteractions(mockNetworkDao);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockResponstListener);
    }

    @Test
    public void testGetDataForZipCodeFromLocalEmptyDataSet() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        mockList.add(mockDataModel);
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        List<ZipCodeDemographicDataModel> emptyList = new ArrayList<>();
        ZipCodeDataModel badDataModel = new ZipCodeDataModel();
        badDataModel.setData(emptyList);

        when(mockNetworkDao.getDataForZipCode(anyString())).thenReturn(badDataModel);
        when(mockLocalDao.getDataForZipCode(anyString())).thenReturn(mockZipCodeModel);

        String zipCode = "ZipCode";
        interactor.getDataForZipCode(zipCode);

        //Check this one first so we only have to do one timeout
        verify(mockResponstListener, timeout(100)).onDataRetrieved(mockZipCodeModel);
        verify(mockNetworkDao).getDataForZipCode(zipCode);
        verify(mockLocalDao).getDataForZipCode(zipCode);

        verifyNoMoreInteractions(mockNetworkDao);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockResponstListener);
    }
}
