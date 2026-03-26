package com.dy.colony.app.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 　 ┏┓　  ┏┓+ +
 * 　┏┛┻━━ ━┛┻┓ + +
 * 　┃　　　　 ┃
 * 　┃　　　　 ┃  ++ + + +
 * 　┃████━████+
 * 　┃　　　　 ┃ +
 * 　┃　　┻　  ┃
 * 　┃　　　　 ┃ + +
 * 　┗━┓　  ┏━┛
 * 　  ┃　　┃
 * 　  ┃　　┃　　 + + +
 * 　  ┃　　┃
 * 　  ┃　　┃ + 神兽保佑,代码无bug
 * 　  ┃　　┃
 * 　  ┃　　┃　　+
 * 　  ┃　 　┗━━━┓ + +
 * 　　┃ 　　　　 ┣┓
 * 　　┃ 　　　 ┏┛
 * 　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　 ┃┫┫ ┃┫┫
 * 　　 ┗┻┛ ┗┻┛+ + + +
 *
 * @author: wangzx
 * @data: 2026/3/25 11:13
 * Description:
 */
public class GpioUtils {
    private static final String TAG = "GpioUtils";

    /**
     * 使用 Root 权限向指定节点写入数据
     * @param path  节点路径，例如 "/sys/class/gpiocontrol/gpiocontrol/gpiocontrol150"
     * @param value 要写入的值，例如 "1" 或 "0"
     * @return true 表示写入成功，false 表示失败
     */
    public static boolean writeGpioWithSu(String path, String value) {
        Process process = null;
        DataOutputStream os = null;
        try {
            // 申请 root 权限执行
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());

            // 拼接 echo 命令
            String command = "echo " + value + " > " + path + "\n";
            os.writeBytes(command);
            os.writeBytes("exit\n");
            os.flush();

            // 等待命令执行完成
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                Log.d(TAG, "GPIO 写入成功: " + command);
                return true;
            } else {
                Log.e(TAG, "GPIO 写入失败，错误码: " + exitValue);
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "执行 Shell 命令异常: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) os.close();
                if (process != null) process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接通过文件流向指定节点写入数据 (需确保节点有 0666 权限)
     */
    public static boolean writeGpioDirectly(String path, String value) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(value);
            writer.flush();
            Log.d(TAG, "GPIO IO 写入成功: " + path + " -> " + value);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "GPIO IO 写入失败，可能是权限不足 (Permission denied): " + e.getMessage());
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
