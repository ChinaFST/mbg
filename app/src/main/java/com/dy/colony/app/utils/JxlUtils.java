package com.dy.colony.app.utils;

import android.app.Activity;

import androidx.lifecycle.LifecycleObserver;

import com.apkfuns.logutils.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.dy.colony.Constants;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.DBHelper;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.FoodItemAndStandard;
import com.dy.colony.greendao.beans.JTJTestItem;
import com.dy.colony.greendao.daos.FGGDTestItemDao;
import com.dy.colony.greendao.daos.FoodItemAndStandardDao;
import com.dy.colony.greendao.daos.JTJTestItemDao;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.ui.widget.filePicker.LFilePicker;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


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
 *
 * @author wangzhenxiong
 * @date 2019/3/19
 */
public class JxlUtils implements LifecycleObserver {
    /**
     * 打开文件选择器
     */
    public static void openFileChose_File(Activity context) {
        //https://github.com/leonHua/LFilePicker
        //打开资源管理器
        String path="/storage/emulated/0/dayuan";
        File dir = new File("/storage/emulated/0/dayuan");
        if (FileUtils.createOrExistsDir(dir)) {
            path = dir.getAbsolutePath();
        }
        new LFilePicker()
                .withActivity(context)
                .withRequestCode(Constants.FILE_REQUESTCODE)
                .withFileFilter(new String[]{"xls"})
                .withMutilyMode(true)
                .withStartPath(path)
                .withChooseMode(true)
                .withBackIcon(0)
                .withTitle(context.getString(R.string.pleacechosefile))
                .start();
    }

    /**
     * 打开文件选择器
     */
    public static void openFileChose_Folder(Activity activity) {
        //打开资源管理器
        //给定默认路径，插入U盘和没插入U盘两个路径
        String path="/storage/emulated/0/dayuan";
        File dir = new File("/storage/emulated/0/dayuan");
        if (FileUtils.createOrExistsDir(dir)) {
            path = dir.getAbsolutePath();
        }
        new LFilePicker()
                .withActivity(activity)
                .withRequestCode(Constants.FOLDER_REQUESTCODE)
                .withMutilyMode(false)
                .withStartPath(path)
                .withBackIcon(0)
                .withChooseMode(false)
                .withTitle(activity.getString(R.string.pleacechosefile))
                .start();

    }

    /**
     * @param paths 文件地址
     * @param b     是否覆盖原检测项目
     * @return
     */

    public static Observable<List<String>> inToXlsFggd(List<String> paths, Boolean b) {
        List<FGGDTestItem> testItems = null;
        FGGDTestItemDao dao = DBHelper.getFGGDTestItemDao(MyAppLocation.myAppLocation);
        if (!b) {  //不保存  先加载出来，导入完成后删除
            testItems = dao.loadAll();
        }
        //String[] objects = paths.toArray(new String[0]);

        List<FGGDTestItem> finalTestItems = testItems;

        return Observable.just(paths).map(new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> paths) throws Exception {
                List<String> booleanList = new ArrayList<>();
                for (int k = 0; k < paths.size(); k++) {  //遍历路径集合
                    String s = paths.get(k);
                    Workbook book = Workbook.getWorkbook(new File(s));
                    Sheet sheet = book.getSheet(0);
                    int rows = sheet.getRows();
                    int columns = sheet.getColumns();
                    String result1 = sheet.getCell(2, 0).getContents();
                    String result2 = sheet.getCell(3, 0).getContents();
                    String result3 = sheet.getCell(4, 0).getContents();
                    LogUtils.d(result3 + result2 + result1 + columns);
                    if (!StringUtils.getString(R.string.str_yure_time).equals(result3) || !StringUtils.getString(R.string.str_wavelength_range).equals(result2) || !StringUtils.getString(R.string.method_name).equals(result1) || columns != 36) {
                        String text = s + MyAppLocation.myAppLocation.getString(R.string.notfggditemlist);
                        booleanList.add(text);
                        continue;
                    }
                    for (int i = 1; i < rows; i++) {
                        FGGDTestItem nc = new FGGDTestItem();
                        for (int j = 0; j < columns; j++) {
                            Cell cell = sheet.getCell(j, i);
                            String result = cell.getContents().replaceAll("#", ",");
                            if (result != null && !"".equals(result.trim()) && !"null".equals(result.trim())) {
                                switch (j) {
                                    case 0:
                                        nc.setId(null);
                                        break;
                                    case 1:
                                        nc.setProject_name(result);
                                        break;
                                    case 2:
                                        nc.setMethod(result);
                                        break;
                                    case 3:
                                        nc.setWavelength(Integer.parseInt(result));
                                        break;
                                    case 4:
                                        nc.setYuretime(Integer.parseInt(result));
                                        break;
                                    case 5:
                                        nc.setJiancetime(Integer.parseInt(result));
                                        break;
                                    case 6:
                                        nc.setPassword(result);
                                        break;
                                    case 7:
                                        nc.setBiaozhun_a0(result);
                                        break;
                                    case 8:
                                        nc.setBiaozhun_b0(result);
                                        break;
                                    case 9:
                                        nc.setBiaozhun_c0(result);
                                        break;
                                    case 10:
                                        nc.setBiaozhun_d0(result);
                                        break;
                                    case 11:
                                        nc.setBiaozhun_from0(result);
                                        break;
                                    case 12:
                                        nc.setBiaozhun_to0(result);
                                        break;
                                    case 13:
                                        nc.setBiaozhun_a1(result);
                                        break;
                                    case 14:
                                        nc.setBiaozhun_b1(result);
                                        break;
                                    case 15:
                                        nc.setBiaozhun_c1(result);
                                        break;
                                    case 16:
                                        nc.setBiaozhun_d1(result);
                                        break;
                                    case 17:
                                        nc.setBiaozhun_from1(result);
                                        break;
                                    case 18:
                                        nc.setBiaozhun_to1(result);
                                        break;
                                    case 19:
                                        nc.setJiaozhen_a(Double.valueOf(result));
                                        break;
                                    case 20:
                                        nc.setJiaozhen_b(Double.valueOf(result));
                                        break;
                                    case 21:
                                        nc.setJiaozhen_c(Double.valueOf(result));
                                        break;
                                    case 22:
                                        nc.setJiaozhen_d(Double.valueOf(result));
                                        break;
                                    case 23:
                                        nc.setYin_a(Double.valueOf(result));
                                        break;
                                    case 24:
                                        nc.setYin_b(Double.valueOf(result));
                                        break;
                                    case 25:
                                        nc.setYang_a(Double.valueOf(result));
                                        break;
                                    case 26:
                                        nc.setYang_b(Double.valueOf(result));
                                        break;
                                    case 27:
                                        nc.setKeyi_a(Double.valueOf(result));
                                        break;
                                    case 28:
                                        nc.setKeyi_b(Double.valueOf(result));
                                        break;
                                    case 29:
                                        nc.setUsetuise(Boolean.valueOf(result));
                                        break;
                                    case 30:
                                        nc.setCeshi(result);
                                        break;
                                    case 31:
                                        //nc.setShowhint(result);
                                        break;
                                    case 32:
                                        nc.setUnit_input(result);
                                        break;
                                    case 33:
                                        nc.setSerialNumber(Integer.valueOf(result));
                                        break;
                                    case 34:
                                        nc.setUnique_testproject(result);
                                        break;
                                    case 35:
                                        nc.setVersion(result);
                                        break;

                                }
                            }
                        }
                        dao.insert(nc);
                    }
                    String[] split = s.split("/");
                    String zjslink = split[split.length - 1];
                    Constants.FGITEMLINK = zjslink;
                    SPUtils.put(MyAppLocation.myAppLocation, Constants.KEY_FGITEMLINK, zjslink);
                    booleanList.add(s + MyAppLocation.myAppLocation.getString(R.string.importsuccess));
                }
                if (!b) {  //不保存  先加载出来，导入完成后删除
                    dao.deleteInTx(finalTestItems);
                }

                return booleanList;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * @param paths 文件地址
     * @param b     是否覆盖原检测项目
     * @return
     */

    public static Observable<List<String>> inToXlsJtj(List<String> paths, Boolean b) {

        List<JTJTestItem> testItems = null;
        JTJTestItemDao dao = DBHelper.getJTJTestItemDao(MyAppLocation.myAppLocation);
        if (!b) {  //不保存  先加载出来，导入完成后删除
            testItems = dao.loadAll();
        }
        //String[] objects = paths.toArray(new String[0]);

        List<JTJTestItem> finalTestItems = testItems;

        return Observable.just(paths).map(new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> paths) throws Exception {
                List<String> booleanList = new ArrayList<>();
                for (int k = 0; k < paths.size(); k++) {
                    String s = paths.get(k);
                    Workbook book = Workbook.getWorkbook(new File(s));
                    Sheet sheet = book.getSheet(0);
                    int rows = sheet.getRows(); //行
                    int columns = sheet.getColumns();//列
                    String text = s + StringUtils.getString(R.string.not_jtj_project);
                    String result1 = sheet.getCell(2, 0).getContents();
                    String result2 = sheet.getCell(3, 0).getContents();
                    String result3 = sheet.getCell(4, 0).getContents();
                    LogUtils.d(result3 + result2 + result1 + columns);
                    if (!StringUtils.getString(R.string.T1A_value).equals(result3) || !StringUtils.getString(R.string.c1_value).equals(result2) || !StringUtils.getString(R.string.methods_xiaoxian_bise).equals(result1) || columns != 19) {
                        booleanList.add(text);
                        continue;
                    }
                    for (int i = 1; i < rows; i++) {
                        JTJTestItem nc = new JTJTestItem();
                        for (int j = 0; j < columns; j++) {
                            Cell cell = sheet.getCell(j, i);
                            String result = cell.getContents().replaceAll("#", ",");
                            if (result != null && !"".equals(result.trim()) && !"null".equals(result.trim())) {
                                switch (j) {

                                    case 0:
                                        nc.setNumber(result);
                                        break;
                                    case 1:
                                        result = result.replaceAll("#", ",");
                                        nc.setProjectName(result);
                                        break;
                                    case 2:
                                        nc.setTestMethod(Integer.valueOf(result));
                                        break;
                                    case 3:
                                        nc.setC1(Double.valueOf(result));
                                        break;
                                    case 4:
                                        nc.setT1A(Double.valueOf(result));
                                        break;
                                    case 5:
                                        nc.setT1B(Double.valueOf(result));
                                        break;
                                    case 6:
                                        nc.setC1_t1A(Double.valueOf(result));

                                        break;
                                    case 7:
                                        nc.setC1_t1B(Double.valueOf(result));

                                        break;
                                    case 8:
                                        nc.setC2(Double.valueOf(result));

                                        break;
                                    case 9:
                                        nc.setT2A(Double.valueOf(result));

                                        break;
                                    case 10:
                                        nc.setT2B(Double.valueOf(result));

                                        break;
                                    case 11:
                                        nc.setC2_t2A(Double.valueOf(result));

                                        break;
                                    case 12:
                                        nc.setC2_t2B(Double.valueOf(result));

                                        break;
                                    case 13:
                                        nc.setSerialNumber(Integer.valueOf(result));

                                        break;
                                    case 14:
                                        nc.setTestTime(Integer.valueOf(result));

                                        break;
                                    case 15:
                                        nc.setPassword(result);

                                        break;
                                    case 16:
                                        nc.setUnique_testproject(result);

                                        break;
                                    case 17:
                                        nc.setItem_type(result);
                                        break;
                                    case 18:
                                        nc.setVersion(result);
                                        break;

                                }
                            }
                        }
                        dao.insert(nc);
                    }
                    String[] split = s.split("/");
                    String zjslink = split[split.length - 1];
                    Constants.JTJLINK = zjslink;
                    SPUtils.put(MyAppLocation.myAppLocation, Constants.KEY_JTJLINK, zjslink);
                    booleanList.add(s + "导入成功!");


                }
                if (!b) {  //不保存  先加载出来，导入完成后删除
                    dao.deleteInTx(finalTestItems);
                }

                return booleanList;
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * @param path
     * @param filename
     * @param sheetname
     * @param listall
     */

    public static Observable<Boolean> outTo_xls_1Sheet(String path, String filename, String sheetname, List<OutMoudle> listall) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {

                // TODO: 6/21/24 分段导出

                int number = listall.size() / 10000;
                if (listall.size() % 10000 != 0) {
                    number = number + 1;
                }
                LogUtils.d(listall.size() + " " + number);
                for (int i2 = 0; i2 < number; i2++) {
                    List<OutMoudle> list;
                    boolean islast = false;
                    if (i2 == number - 1) {
                        //最后一次
                        list = listall.subList(i2 * 10000, listall.size());
                        islast = true;
                    } else {
                        list = listall.subList(i2 * 10000, (i2 + 1) * 10000);
                    }
                    if (i2 != 0) {
                        list.add(0, listall.get(0));
                    }

                    WritableWorkbook book = null;
                    String pathname = path + "/" + (i2 + 1) + filename;
                    try {
                        boolean b = FileUtils.createOrExistsFile(pathname);
                        book = Workbook.createWorkbook(new File(pathname));
                        WritableSheet sheet = book.createSheet(sheetname, 0);


                        for (int i = 0; i < list.size(); i++) {

                            String[] split = list.get(i).getKey().toString().split(",");
                            for (int i1 = 0; i1 < split.length; i1++) {

                                if ("null".equals(split[i1].trim()) || "".equals(split[i1].trim()) || split[i1] == null) {
                                    Label label = new Label(i1, i, "");
                                    sheet.addCell(label);
                                } else {
                                    Label label = new Label(i1, i, split[i1] + "");
                                    sheet.addCell(label);
                                }
                            }
                        }
                        book.write();
                        ArmsUtils.snackbarText(pathname + MyAppLocation.myAppLocation.getString(R.string.exportsuccess));
                        emitter.onNext(true);
                        if (islast) {
                            emitter.onComplete();
                        }
                    } catch (IOException e) {
                        emitter.onNext(false);
                        if (islast) {
                            emitter.onComplete();
                        }
                        e.printStackTrace();
                        ArmsUtils.snackbarText(pathname + MyAppLocation.myAppLocation.getString(R.string.jxlerro));

                    } finally {
                        if (book != null) {
                            try {
                                //关闭文件
                                book.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        });

    }

    /**
     * @param paths
     * @param b     是否覆盖原检测项目
     * @return
     */

    public static Observable<List<String>> inToXlsFoodAndItem(List<String> paths, Boolean b) {
        List<FoodItemAndStandard> simple33List = null;
        FoodItemAndStandardDao dao = DBHelper.getFoodItemAndStandardDao(MyAppLocation.myAppLocation);
        if (!b) {  //不保存  先加载出来，导入完成后删除
            simple33List = dao.loadAll();
        }
        //String[] objects = paths.toArray(new String[0]);

        List<FoodItemAndStandard> finalTestItems = simple33List;

        return Observable.just(paths).map(new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> paths) throws Exception {
                List<String> booleanList = new ArrayList<>();
                for (int k = 0; k < paths.size(); k++) {  //遍历路径集合
                    String s = paths.get(k);
                    Workbook book = Workbook.getWorkbook(new File(s));
                    Sheet sheet = book.getSheet(0);
                    int rows = sheet.getRows();
                    int columns = sheet.getColumns();
                    String result1 = sheet.getCell(1, 0).getContents();
                    String result2 = sheet.getCell(2, 0).getContents();
                    String result3 = sheet.getCell(4, 0).getContents();
                    LogUtils.d(result1);
                    LogUtils.d(result2);
                    LogUtils.d(result3);
                    LogUtils.d(columns);
                    if (!MyAppLocation.myAppLocation.getString(R.string.parent_class_number_colon).equals(result3) || !MyAppLocation.myAppLocation.getString(R.string.contrast_symbols).equals(result2) || !MyAppLocation.myAppLocation.getString(R.string.Sample_ID).equals(result1) || columns < 12) {
                        booleanList.add(s + MyAppLocation.myAppLocation.getString(R.string.nosamplelist));
                        continue;
                    }
                    List<FoodItemAndStandard> list = new ArrayList<>();
                    for (int i = 1; i < rows; i++) {
                        FoodItemAndStandard nc = new FoodItemAndStandard();
                        for (int j = 0; j < columns; j++) {
                            Cell cell = sheet.getCell(j, i);
                            String result = cell.getContents();

                            if (result != null && !"".equals(result.trim()) && !"null".equals(result.trim())) {
                                switch (j) {
                                    case 0:
                                        nc.setId(null);
                                        break;
                                    case 1:
                                        nc.setCheckId(result);
                                        break;
                                    case 2:
                                        nc.setCheckSign(result);
                                        break;
                                    case 3:
                                        nc.setCheckValueUnit(result);
                                        break;
                                    case 4:
                                        nc.setFoodPCode(result);
                                        break;
                                    case 5:
                                        nc.setItemName(result.replaceAll("#", ","));
                                        break;
                                    case 6:
                                        nc.setSampleName(result);
                                        break;
                                    case 7:
                                        nc.setSampleNum(result);
                                        break;
                                    case 8:
                                        nc.setStandardName(result);
                                        break;
                                    case 9:
                                        nc.setStandardValue(result);
                                        break;
                                    case 10:
                                        nc.setuDate(result);
                                        break;
                                    case 11:
                                        nc.setFlag(Integer.valueOf(result));
                                        break;

                                }
                            }
                        }
                        list.add(nc);
                        if (list.size() > 1000) {
                            dao.insertInTx(list);
                            LogUtils.d(dao.count());
                            list.clear();
                        }
                    }
                    dao.insertInTx(list);
                    list.clear();
                    String[] split = s.split("/");
                    String zjslink = split[split.length - 1];
                    Constants.FOOLINKNAME = zjslink;
                    SPUtils.put(MyAppLocation.myAppLocation, Constants.KEY_FOOLINKNAME, zjslink);
                    booleanList.add(s + MyAppLocation.myAppLocation.getString(R.string.importsuccess));
                }
                if (!b) {  //不保存  先加载出来，导入完成后删除
                    dao.deleteInTx(finalTestItems);
                }

                return booleanList;
            }
        }).subscribeOn(Schedulers.io());
    }
}
