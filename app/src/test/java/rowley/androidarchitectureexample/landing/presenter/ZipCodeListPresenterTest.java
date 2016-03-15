package rowley.androidarchitectureexample.landing.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for the zip code list presenter
 */
public class ZipCodeListPresenterTest {

    @Mock
    ZipCodeListInteractor mockInteractor;
    @Mock
    ZipCodeListView mockView;

    private ZipCodeListPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);
        presenter = new ZipCodeListPresenter(mockInteractor, mockView);
    }

    @Test
    public void testStartPresenter() {
        presenter.startPresenter();

        verify(mockView).showProgressBar(true);
        verify(mockInteractor).startInteractor(presenter);
        verify(mockInteractor).getZipCodeList();

        verifyNoMoreInteractions(mockView);
        verifyNoMoreInteractions(mockInteractor);
    }

    @Test
    public void testStopPresenter() {
        presenter.stopPresenter();

        verify(mockInteractor).stopInteractor();
        verifyNoMoreInteractions(mockInteractor);
        verifyZeroInteractions(mockView);
    }

    @Test
    public void testOnZipCodeSelected() {
        String mockZipCode = "12345";

        presenter.onZipCodeSelected(mockZipCode);

        verify(mockView).showProgressBar(true);
        verify(mockView).notifyDemographicDataPresenterOfSelectedZipCode(mockZipCode);
        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnListRetrievedGoodList() {
        List<String> mockList = new ArrayList<>(Arrays.asList(new String[]{"12345", "23456"}));

        presenter.onListRetreived(mockList);

        verify(mockView).showProgressBar(false);
        verify(mockView).showZipCodeList(mockList);
        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnListRetrievedNullList() {
        String errorMessage = "This is an error message";
        Context mockContext = mock(Context.class);
        when(mockView.getContext()).thenReturn(mockContext);
        when(mockContext.getString(anyInt())).thenReturn(errorMessage);

        presenter.onListRetreived(null);

        verify(mockView).showProgressBar(false);
        verify(mockView).getContext();
        verify(mockContext).getString(anyInt());
        verify(mockView).showError(errorMessage);
        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }

    @Test
    public void testOnListRetrievedEmptyList() {
        String errorMessage = "This is an error message";
        Context mockContext = mock(Context.class);
        when(mockView.getContext()).thenReturn(mockContext);
        when(mockContext.getString(anyInt())).thenReturn(errorMessage);

        presenter.onListRetreived(new ArrayList<String>());

        verify(mockView).showProgressBar(false);
        verify(mockView).getContext();
        verify(mockContext).getString(anyInt());
        verify(mockView).showError(errorMessage);
        verifyNoMoreInteractions(mockView);
        verifyZeroInteractions(mockInteractor);
    }
}
