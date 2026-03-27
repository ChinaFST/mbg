package com.dy.colony.app.utils;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.List;

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
 * Created by wangzhenxiong on 2019/3/14.
 */
public class CheckNullUtils {
    public static boolean checkNull(EditText editText){
        boolean equals = "".equals(editText.getText().toString().trim());
        return equals;
    }
    public static boolean checkNull(AutoCompleteTextView editText){
        boolean equals = "".equals(editText.getText().toString().trim());
        return equals;
    }

    public static boolean checkNull(List<AutoCompleteTextView> editText){
        for (int i = 0; i < editText.size(); i++) {
            boolean equals = "".equals(editText.get(i).getText().toString().trim());
            if (equals){
              return false;
            }
        }
        return true;
    }

}
