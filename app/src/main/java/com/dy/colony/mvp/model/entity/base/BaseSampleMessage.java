package com.dy.colony.mvp.model.entity.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.dy.colony.greendao.beans.FoodItemAndStandard;


/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
public class BaseSampleMessage implements Parcelable {


    public String getStandardAndLimitalues() {
        if (this instanceof FoodItemAndStandard) {
            FoodItemAndStandard standard = (FoodItemAndStandard) this;
            return standard.getStandardName() + "  " + standard.getCheckSign() + "   " + standard.getStandardValue() + "   " + standard.getCheckValueUnit();
        }
        return null;
    }

    public BaseSampleMessage() {
    }

    public String getSampleName() {
        if (this instanceof FoodItemAndStandard) {
            FoodItemAndStandard standard = (FoodItemAndStandard) this;
            return standard.getSampleName();
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected BaseSampleMessage(Parcel in) {
    }


}
