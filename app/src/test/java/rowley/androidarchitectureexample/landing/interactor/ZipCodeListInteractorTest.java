package rowley.androidarchitectureexample.landing.interactor;

import android.content.SharedPreferences;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Tests for the ZipCodeListInteractor
 */
public class ZipCodeListInteractorTest {

    @Mock
    ZipCodeDemographicDataLocalDao mockLocalDao;
    @Mock
    ZipCodeDemographicDataDao mockNetworkDao;
    @Mock
    SharedPreferences mockSharedPreferences;
    @Mock
    SharedPreferences.Editor mockPrefsEditor;
    @Mock
    ZipCodeListResponseListener mockListener;

    private ZipCodeListInteractor interactor;

    @Before
    public void setup() {
        initMocks(this);

        interactor = new ZipCodeListInteractor(mockLocalDao, mockNetworkDao, mockSharedPreferences, Schedulers.immediate());
        interactor.startInteractor(mockListener);
    }

    @After
    public void cleanup() {
        interactor.stopInteractor();
    }

    @Test
    public void testGetZipCodeListUseLocal() {
        when(mockSharedPreferences.getLong(anyString(), anyLong())).thenReturn(System.currentTimeMillis() - (ZipCodeListInteractor.ZIP_CODE_CACHE_THRESHOLD/2));
        List<String> mockList = new ArrayList<>();
        when(mockLocalDao.getZipCodes()).thenReturn(mockList);

        interactor.getZipCodeList();
        //verify this one first so we only have to use one timeout
        verify(mockListener, timeout(100)).onListRetreived(mockList);
        verify(mockSharedPreferences).getLong(ZipCodeListInteractor.ZIP_CODE_CACHE_DATE_PREF, 0);
        verify(mockLocalDao).getZipCodes();

        verifyNoMoreInteractions(mockSharedPreferences);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockListener);

        verifyZeroInteractions(mockNetworkDao);
    }

    @Test
    public void testGetZipCodeListUseNetwork() {
        when(mockSharedPreferences.getLong(anyString(), anyLong())).thenReturn(System.currentTimeMillis() - (ZipCodeListInteractor.ZIP_CODE_CACHE_THRESHOLD * 2));
        when(mockSharedPreferences.edit()).thenReturn(mockPrefsEditor);
        when(mockPrefsEditor.putLong(anyString(), anyLong())).thenReturn(mockPrefsEditor);

        List<String> mockList = new ArrayList<>();
        when(mockNetworkDao.getZipCodes()).thenReturn(mockList);

        ArgumentCaptor<String> prefsKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);

        interactor.getZipCodeList();
        //verify this one first so we only have to use one timeout
        verify(mockListener, timeout(100)).onListRetreived(mockList);
        verify(mockSharedPreferences).getLong(ZipCodeListInteractor.ZIP_CODE_CACHE_DATE_PREF, 0);
        verify(mockNetworkDao).getZipCodes();
        verify(mockLocalDao).saveZipCodes(mockList, true);
        verify(mockSharedPreferences).edit();
        verify(mockPrefsEditor).putLong(prefsKeyCaptor.capture(), longCaptor.capture());
        verify(mockPrefsEditor).apply();

        assertEquals(ZipCodeListInteractor.ZIP_CODE_CACHE_DATE_PREF, prefsKeyCaptor.getValue());

        verifyNoMoreInteractions(mockSharedPreferences);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockListener);
        verifyNoMoreInteractions(mockNetworkDao);
        verifyNoMoreInteractions(mockPrefsEditor);
    }

    @Test
    public void testGetZipCodeListUseNetworkFallbackToLocal() {
        when(mockSharedPreferences.getLong(anyString(), anyLong())).thenReturn(System.currentTimeMillis() - (ZipCodeListInteractor.ZIP_CODE_CACHE_THRESHOLD * 2));

        List<String> mockList = new ArrayList<>();
        when(mockNetworkDao.getZipCodes()).thenReturn(null);
        when(mockLocalDao.getZipCodes()).thenReturn(mockList);

        ArgumentCaptor<String> prefsKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Long> longCaptor = ArgumentCaptor.forClass(Long.class);

        interactor.getZipCodeList();
        //verify this one first so we only have to use one timeout
        verify(mockListener, timeout(100)).onListRetreived(mockList);
        verify(mockSharedPreferences).getLong(ZipCodeListInteractor.ZIP_CODE_CACHE_DATE_PREF, 0);
        verify(mockNetworkDao).getZipCodes();
        verify(mockLocalDao).getZipCodes();

        verifyNoMoreInteractions(mockSharedPreferences);
        verifyNoMoreInteractions(mockLocalDao);
        verifyNoMoreInteractions(mockListener);
        verifyNoMoreInteractions(mockNetworkDao);
    }
}
