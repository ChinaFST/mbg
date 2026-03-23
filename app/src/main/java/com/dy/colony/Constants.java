package com.dy.colony;

import android.app.Application;

import com.dy.colony.app.utils.CRC8Util;
import com.dy.colony.app.utils.SPUtils;
import com.dy.colony.greendao.beans.User;
import com.dy.colony.usbhelp.UsbControl;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/5
 */
public class Constants {
    /**
     * {@link #ISREMBERUSERNAME}是否记住用户密码（登录页面）<p>
     * {@link #KEY_REMBERUSERNAME}<p>
     */
    public static boolean ISREMBERUSERNAME;
    public static String KEY_REMBERUSERNAME = "KEY_REMBERUSERNAME";

    /**
     * 保存当前用户的key
     */
    public static final String KEY_USERINFOR_JSON = "KEY_USERINFOR_JSON";

    /**
     * 当前登录用户 仪器本身
     */
    public static User NOWUSER;

    public static String KEY_AUTO_UPLOAD_ENABLED = "auto_upload_enabled";
    public static int MINWIDTH = 250;
    public static String KEY_MINWIDTH = "KEY_MINWIDTH";
    public static int MINHEIGHT = 40;
    public static String KEY_MINHEIGHT = "KEY_MINHEIGHT";

    public static int LIMITANGLEA = 10;
    public static String KEY_LIMITANGLEA = "KEY_LIMITANGLEA";

    public static int LIMITANGLEB = 80;
    public static String KEY_LIMITANGLEB = "KEY_LIMITANGLEB";

    public static int LIMITCHANNL = 20;
    public static String KEY_LIMITCHANNL = "KEY_LIMITCHANNL";

    public static int MINWDIFFERENCEVALUE = 10;
    public static String KEY_MINWDIFFERENCEVALUE = "KEY_MINWDIFFERENCEVALUE";

    public static int MINHDIFFERENCEVALUE = 10;
    public static String KEY_MINHDIFFERENCEVALUE = "KEY_MINHDIFFERENCEVALUE";
    /**
     * 仪器百度定位数据 经纬度
     */
    public static double LONTITUDE, LATITUDE;
    /**
     * 仪器百度定位数据  地址
     */
    public static String ADDR_WF = "";

    /**
     * 仪器列表一页显示行数
     */
    public static int page_num = 100;

    /**
     * 仪器列表一页显示行数
     */
    public static int page_num_unit = 15;

    /**
     * 分光光度流水号
     */
    public static int FGGD_SEARINUM;
    public static String KEY_FGGD_SEARINUM = "key_fggd_searinum";

    public static String JUMP_PARAM_TYPE="type";

    public static int PLATFORM_TAG;
    /**
     * 胶体金流水号
     */
    public static int JTJ_SEARINUM;
    /**
     * 胶体金流水号key
     */
    public static String KEY_JTJ_SEARINUM = "key_jtj_searinum";

    public static final String controBitmapNocardFileName = "ControBitmapNoCard";
    /**
     * 胶体金摄像头模块
     */
    //public static int MYVID_P = 1046, MYPID_P = 20497;
    public static int MYVID_P = 7119, MYPID_P = 2826;

    /**
     * 真菌毒素 胶体金多联卡
     */
    public static UsbControl mControl;

    public static final byte[] SPECTRAL_AD_CALIBRATION_REQUEST_1 = new byte[]{0x7e, 0x11, 0x02, 0x00, 0x01, 0x32, 0x46, (byte) 0xaa},
            SPECTRAL_AD_CALIBRATION_REQUEST_2 = new byte[]{0x7e, 0x11, 0x02, 0x00, 0x02, 0x32, 0x47, (byte) 0xaa}, SPECTRAL_DATA_REQUEST_DY1000 = new byte[]{0x7e, 0x15, 0x00, 0x00, 0x15, 0x7e};

    public static byte[] COLLAURUM_DATA_REQUEST_P = new byte[]{0x7E, 0x15, 0x00, 0x00, (byte) 0xcb, 0x7E}, COLLAURUM_NUMBER_ASK_P = new byte[]{0x7e, 0x1b, 0x00, 0x00, (byte) CRC8Util.FindCRC(new byte[]{0x1b, 0x00, 0x00}), 0x7e},
            COLLAURUM_STATE_REQUEST_P = new byte[]{0x7E, 0x13, 0x00, 0x00, (byte) CRC8Util.FindCRC(new byte[]{0x13, 0x00, 0x00}), 0x7E};

    /**
     * 串口相关 两种Android板子的串口名称是不一样的，具体看代码
     */
    public static final String DATA_SERIAPort = "/dev/ttyS4", NEW_DATA_SERIAPort = "/dev/ttyS0", DATA_SERIALBaudRate = "115200";
    /**
     * 外接胶体金模块的检测区域参数<p>
     * {@link #drowrectheight} 所画的红色框的高<p>
     * {@link #drowrectwidth} 所画的红色框的宽<p>
     * {@link #cardSpacing} 两个通道的间距，三个卡条之间的距离是一样的<p>
     */

    public static int drowrectheight = 20, drowrectwidth = 100, cardSpacing = 60;

    /**
     * 获取摄像头偏移参数
     */
    public static byte[] COLLAURUM_GET_ARGMENT = {0x7E, 0x20, 0x00, 0x00, 0x79, 0x7E};

    /**
     * 分光光度透光率限制值
     * 当透光率小于0.97时认为是放入比色皿
     */
    public static float FGLIMITVALUE_LOW = 0.97f;

    /**
     * 当大于1.01时认为通道异常  安徽省局的改成1.5f
     */
    public static float FGLIMITVALUE_HEIGHT() {

        return 1.01f;
    }


    /**
     * @return 分光光度抑制率法的对照值限制 默认必须大于0.15 安徽省局修改为0.01
     */
    public static float FGCONTROVALUE_YIZHILVFA() {

        return 0.15F;
    }

    /**
     * 初始化软件启动需要的全局变量
     *
     * @param application
     */
    public static void init(Application application) {
        //是否记住用户（登录界面）
        Constants.ISREMBERUSERNAME = (boolean) SPUtils.get(application, Constants.KEY_REMBERUSERNAME, false);
    }


    /**
     * 获取分光光度检测的流水号
     *
     * @return 流水号
     */
    public static synchronized String getFGGDSearinum() {
        //为0时需要初始化
        if (FGGD_SEARINUM == 0) {
            FGGD_SEARINUM = (int) SPUtils.get(MyAppLocation.myAppLocation, KEY_FGGD_SEARINUM, 0);
        }
        FGGD_SEARINUM++;
        SPUtils.put(MyAppLocation.myAppLocation, KEY_FGGD_SEARINUM, FGGD_SEARINUM);
        return getString(FGGD_SEARINUM);
    }

    /**
     * 获取胶体金检测的流水号
     *
     * @return 流水号
     */
    public static synchronized String getJtjSearinum() {
        //为0时需要初始化
        if (JTJ_SEARINUM == 0) {
            JTJ_SEARINUM = (int) SPUtils.get(MyAppLocation.myAppLocation, KEY_JTJ_SEARINUM, 0);
            //System.out.println("读取存储流水号："+JTJ_SEARINUM);
        }
        JTJ_SEARINUM++;
        //System.out.println("流水号："+JTJ_SEARINUM);
        SPUtils.put(MyAppLocation.myAppLocation, KEY_JTJ_SEARINUM, JTJ_SEARINUM);
        return getString(JTJ_SEARINUM);
    }


    /**
     * 将int 转换为5位数的流水号，不足5位用0补齐
     *
     * @param i 流水号（int）
     * @return 流水号（string）
     */
    private static String getString(int i) {
        String nums = "";
        if (0 <= i && i < 10) {
            nums = "0000" + i;
        } else if (10 <= i && i < 100) {
            nums = "000" + i;
        } else if (100 <= i && i < 1000) {
            nums = "00" + i;
        } else if (1000 <= i && i < 10000) {
            nums = "0" + i;
        } else {
            nums = "" + i;
        }
        return nums;
    }
} 
