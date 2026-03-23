package com.dy.colony.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dy.colony.MyAppLocation;
import com.dy.colony.greendao.daos.Company_PointDao;
import com.dy.colony.greendao.daos.Company_Point_UnitDao;
import com.dy.colony.greendao.daos.DaoMaster;
import com.dy.colony.greendao.daos.DaoSession;
import com.dy.colony.greendao.daos.Detection_Record_FGGD_NCDao;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.greendao.daos.JTJPointDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.greendao.daos.Simple33Dao;
import com.dy.colony.greendao.daos.UserDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;


import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.IOException;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * Created by wangzhenxiong on 2019/2/18.
 */
public class DBHelper extends DaoMaster.OpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {

                    @Override
                    public void onCreateAllTables(Database db, boolean ifNotExists) {
                        DaoMaster.createAllTables(db, ifNotExists);
                    }

                    @Override
                    public void onDropAllTables(Database db, boolean ifExists) {
                        DaoMaster.dropAllTables(db, ifExists);
                    }
                }

                , UserDao.class
                , Simple33Dao.class
                , JTJTestItemDao.class
                , FGGDTestItemDao.class
                , FoodItemAndStandardDao.class
                , JTJPointDao.class
                , JTJPointDao.class
                , Detection_Record_FGGD_NCDao.class
                , Company_PointDao.class
                , Company_Point_UnitDao.class

        );

    }

    public static UserDao getUserDao(Context context) {
        DBHelper helper = new DBHelper(context, "USER-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        return userDao;
    }


    public static Simple33Dao getSimple33Dao(Context context) {
        DBHelper helper = new DBHelper(context, "SIMPLE33-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Simple33Dao simple33Dao = daoSession.getSimple33Dao();
        return simple33Dao;
    }

    public static JTJTestItemDao getJTJTestItemDao(Context context) {
        DBHelper helper = new DBHelper(context, "JTJTEST_ITEM-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        JTJTestItemDao jtjTestItemDao = daoSession.getJTJTestItemDao();
        return jtjTestItemDao;
    }

    public static FGGDTestItemDao getFGGDTestItemDao(Context context) {
        DBHelper helper = new DBHelper(context, "FGGDTEST_ITEM-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FGGDTestItemDao fggdTestItemDao = daoSession.getFGGDTestItemDao();
        return fggdTestItemDao;
    }

    public static FoodItemAndStandardDao getFoodItemAndStandardDao(Context context) {
        DBHelper helper = new DBHelper(context, "FOOD_ITEM_AND_STANDARD-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FoodItemAndStandardDao foodItemAndStandardDao = daoSession.getFoodItemAndStandardDao();
        return foodItemAndStandardDao;
    }

    private static DBHelper helperJTJPointDao;

    public static JTJPointDao getJTJPointDao(Context context) {
        if (null == helperJTJPointDao) {
            helperJTJPointDao = new DBHelper(MyAppLocation.myAppLocation, "JTJPOINT-db", null);
        }
        DaoMaster daoMaster = new DaoMaster(helperJTJPointDao.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        JTJPointDao jtjPointDao = daoSession.getJTJPointDao();
        return jtjPointDao;
    }

    public static Company_PointDao getCompany_PointDao(Context context) {
        DBHelper helper = new DBHelper(context, "COMPANY__POINT-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Company_PointDao company_pointDao = daoSession.getCompany_PointDao();
        return company_pointDao;
    }

    public static Company_Point_UnitDao getCompany_Point_UnitDao(Context context) {
        DBHelper helper = new DBHelper(context, "COMPANY__POINT__UNIT-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Company_Point_UnitDao companyPointUnitDao = daoSession.getCompany_Point_UnitDao();
        return companyPointUnitDao;
    }

    private static DBHelper helperDetection_Record_FGGD_NCDao;

    public static Detection_Record_FGGD_NCDao getDetection_Record_FGGD_NCDao(Context context) {
        if (null == helperDetection_Record_FGGD_NCDao) {

            helperDetection_Record_FGGD_NCDao = new DBHelper(MyAppLocation.myAppLocation, "DETECTION__RECORD__FGGD__NC-db", null);
        }
        DaoMaster daoMaster = new DaoMaster(helperDetection_Record_FGGD_NCDao.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        Detection_Record_FGGD_NCDao detectionRecordFggdNcDao = daoSession.getDetection_Record_FGGD_NCDao();
        return detectionRecordFggdNcDao;
    }
}
