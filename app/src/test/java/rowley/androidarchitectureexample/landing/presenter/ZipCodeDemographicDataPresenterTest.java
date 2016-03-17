package rowley.androidarchitectureexample.landing.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import rowley.androidarchitectureexample.landing.interactor.ZipCodeDemographicDataInteractor;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDemographicDataModel;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for the ZipCodeDemographicDataPresenter
 */
public class ZipCodeDemographicDataPresenterTest {

    @Mock
    ZipCodeDemographicDataView mockView;
    @Mock
    ZipCodeDemographicDataInteractor mockInteractor;

    private ZipCodeDemographicDataPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);

        presenter = new ZipCodeDemographicDataPresenter(mockView, mockInteractor);
        verify(mockInteractor).startInteractor(presenter);
    }

    @Test
    public void testOnDataReceivedGoodData() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        mockList.add(mockDataModel);
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        presenter.onDataRetrieved(mockZipCodeModel);

        verify(mockView).showProgressBar(false);
        verify(mockView).showZipCodeDemographicData(mockZipCodeModel);
        verify(mockView).updateDataDisplay(displayValue);

        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnDataReceivedNullData() {
        presenter.onDataRetrieved(null);

        verify(mockView).showProgressBar(false);
        verify(mockView).displayError();

        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnDataReceivedNullDataSet() {
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();

        presenter.onDataRetrieved(mockZipCodeModel);

        verify(mockView).showProgressBar(false);
        verify(mockView).displayError();

        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnDataReceivedEmptyDataSet() {
        List<ZipCodeDemographicDataModel> mockList = new ArrayList<>();
        ZipCodeDataModel mockZipCodeModel = new ZipCodeDataModel();
        mockZipCodeModel.setData(mockList);

        presenter.onDataRetrieved(mockZipCodeModel);

        verify(mockView).showProgressBar(false);
        verify(mockView).displayError();

        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnZipCodeSelected() {
        String zipCode = "zipCode";

        presenter.onZipCodeSelected(zipCode);

        verify(mockView).showProgressBar(true);
        verify(mockInteractor).getDataForZipCode(zipCode);
        verifyNoMoreInteractions(mockView);
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    public void testOnDataPointSelected() {
        String displayValue = "displayValue";
        ZipCodeDemographicDataModel mockDataModel = new ZipCodeDemographicDataModel();
        mockDataModel.setDisplayValue(displayValue);

        presenter.onDataPointSelected(mockDataModel);

        verify(mockView).updateDataDisplay(displayValue);

        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testStopPresenter() {
        presenter.stopPresenter();

        verify(mockInteractor).stopInteractor();
        verifyNoMoreInteractions(mockInteractor);
        verifyZeroInteractions(mockView);
    }
}
