package rowley.androidarchitectureexample.nycdemographic.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rowley.androidarchitectureexample.SimpleActivityTestBase;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.dagger.TestApplicationComponent;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * Tests for the ZipCodeDemographicDataSqliteDao
 */
public class ZipCodeDemographicDataSqliteDaoTest extends SimpleActivityTestBase {
    @Inject
    ZipCodeDemographicDataSqliteDao dao;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ((TestApplicationComponent)DaggerInjector.getInstance().getApplicationComponent()).inject(this);
    }

    @Override
    public void tearDown() throws Exception {
        SQLiteDatabase db = dao.getWritableDatabase();
        db.beginTransaction();

        try {
            db.delete(dao.getNycDemogragicDataTableName(), null, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        super.tearDown();
    }

    public void testGetZipCodes() {
        //set up
        int[] zipCodes = new int[] {1234, 1734, 1434, 1534, 1334, 1634} ;
        for(int zipCode : zipCodes) {
            saveDataToDb(zipCode, "");
        }

        List<String> foundZips = dao.getZipCodes();

        assertEquals(zipCodes.length, foundZips.size());
        for(int i = 0; i < foundZips.size() - 1; i++) {
            assertTrue(Integer.valueOf(foundZips.get(i)) < Integer.valueOf(foundZips.get(i + 1)));
        }
    }

    public void testGetDataForAllZipCodes() {
        //set up
        int[] zipCodes = new int[] {1234, 1734, 1434, 1534, 1334, 1634} ;
        for(int zipCode : zipCodes) {
            saveDataToDb(zipCode, ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON);
        }

        List<ZipCodeDataModel> foundModels = dao.getDataForAllZipCodes();

        assertEquals(zipCodes.length, foundModels.size());
        assertNotNull(foundModels.get(2));

        ZipCodeDataModel testModel = foundModels.get(2);
        assertEquals("10001", testModel.getJurisdictionName());
        assertNotNull(testModel.getData());
        assertFalse(testModel.getData().isEmpty());
        assertEquals(46, testModel.getData().size());
        assertNotNull(testModel.getData().get(2));
        assertEquals("percent_ethnicity_total", testModel.getData().get(2).getServerName());
        assertEquals("Ethnicity - Percent Responded", testModel.getData().get(2).getDisplayName());
        assertEquals("100%", testModel.getData().get(2).getDisplayValue());
    }

    public void testSaveDataForZipCodes() {
        List<ZipCodeDataModel> modelList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            ZipCodeDataModel model =
                    dao.getGson().fromJson(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, ZipCodeDataModel.class);
            model.setJurisdictionName(String.valueOf(i + 10000));
            modelList.add(model);
        }

        dao.saveDataForZipCodes(modelList);

        Cursor cursor = dao.getReadableDatabase().rawQuery("SELECT count(*) FROM " + dao.getNycDemogragicDataTableName(), null);
        cursor.moveToFirst();
        assertEquals(10, cursor.getInt(0));

        String foundJson = getJsonForZipCode(10001);
        assertEquals(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, foundJson);
    }

    public void testGetDataForZipCode() {
        //set up
        int[] zipCodes = new int[] {1234, 1734, 1434, 1534, 1334, 1634} ;
        for(int zipCode : zipCodes) {
            saveDataToDb(zipCode, ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON);
        }

        ZipCodeDataModel foundModel = dao.getDataForZipCode(String.valueOf(zipCodes[3]));
        assertEquals("10001", foundModel.getJurisdictionName());
        assertNotNull(foundModel.getData());
        assertFalse(foundModel.getData().isEmpty());
        assertEquals(46, foundModel.getData().size());
        assertNotNull(foundModel.getData().get(14));
        assertEquals("percent_white_non_hispanic", foundModel.getData().get(14).getServerName());
        assertEquals("White Non-Hispanic - Percentage", foundModel.getData().get(14).getDisplayName());
        assertEquals("2%", foundModel.getData().get(14).getDisplayValue());
    }

    public void testSaveDataForZipCode() {
        ZipCodeDataModel model = dao.getGson().fromJson(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, ZipCodeDataModel.class);
        dao.saveDataForZipCode(model);

        String foundJson = getJsonForZipCode(10001);
        assertEquals(ZipCodeDataDaoTestUtil.SAMPLE_DEMOGRAPHIC_DATA_JSON, foundJson);
    }

    private void saveDataToDb(int zipCode, String json) {
        SQLiteDatabase db = dao.getWritableDatabase();
        db.beginTransaction();

        SQLiteStatement stmt = null;
        try {
            stmt = db.compileStatement("INSERT INTO " + dao.getNycDemogragicDataTableName() + " (" +
                    dao.getNycDemographicDataZipCodeColumnName() + ", " +
                    dao.getNycDemographicDataJsonColumnName() + ") VALUES (?, ?);");
            stmt.bindLong(1, zipCode);
            stmt.bindString(2, json);

            stmt.execute();
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            if(stmt != null) {
                stmt.clearBindings();
                stmt.close();
            }
        }
    }

    private String getJsonForZipCode(int zipCode) {
        String result = "";

        SQLiteDatabase db = dao.getReadableDatabase();

        Cursor cursor = db.query(dao.getNycDemogragicDataTableName(), new String[] {dao.getNycDemographicDataJsonColumnName()},
                dao.getNycDemographicDataZipCodeColumnName() + "=?", new String[] {String.valueOf(zipCode)}, null, null, null);
        if(cursor != null) {
            try {
                while(cursor.moveToNext()) {
                    result = cursor.getString(0);
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }
}
