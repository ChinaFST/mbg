package com.dy.colony.mvp.model.entity.base;


import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.greendao.beans.FGGDTestItem;
import com.dy.colony.greendao.beans.JTJTestItem;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
public class BaseProjectMessage {

    public String getUnit_input() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getUnit_input();
        } else if (this instanceof JTJTestItem) {
            return "ppb";
        }  else {
            return null;
        }

    }

    public String getBiaozhun_from0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_from0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }

    }

    public String getBiaozhun_from1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_from1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public String getBiaozhun_to0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_to0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public String getBiaozhun_to1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_to1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public String getBiaozhun_a0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_a0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }
    }

    public String getBiaozhun_b0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_b0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public String getBiaozhun_c0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_c0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public String getBiaozhun_d0() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_d0();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }
    }

    public String getBiaozhun_a1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_a1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }
    }

    public String getBiaozhun_b1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_b1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }
    }

    public String getBiaozhun_c1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_c1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }   else {
            return null;
        }
    }

    public String getBiaozhun_d1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getBiaozhun_d1();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return "";
        }  else {
            return null;
        }
    }

    public double getJiaozhen_a() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiaozhen_a();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public double getJiaozhen_b() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiaozhen_b();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public int getWavelength() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getWavelength();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public float getControValue() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getControValue();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getYang_a() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getYang_a();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public double getYang_b() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getYang_b();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getYin_a() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getYin_a();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getYin_b() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getYin_b();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public double getKeyi_a() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getKeyi_a();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public double getKeyi_b() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getKeyi_b();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getJiaozhen_c() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiaozhen_c();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getJiaozhen_d() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiaozhen_d();
        } else if (this instanceof JTJTestItem) {
            //JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public Long getId_base() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getId();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getId();
        }   else {
            return 0L;
        }
    }

    public String getUnique_base_p() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getUnique_testproject();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getUnique_testproject();
        }   else {
            return "";
        }
    }



    public String getProjectName() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getProject_name();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getProjectName();
        } else {
            return null;
        }
    }



    public String getMethod() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getMethod();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getTestMethod() == 1 ? MyAppLocation.myAppLocation.getString(R.string.method_xiaoxian) : MyAppLocation.myAppLocation.getString(R.string.method_bise);
        }
        return "";
    }

    public int getTestTime() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiancetime() + item.getYuretime();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return Integer.parseInt(item.getTestTime() + "");
        }  else {
            return 0;
        }
    }







    public double getC1() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getC1();
        }  else {
            return 0;
        }
    }

    public double getT1A() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getT1A();
        }  else {
            return 0;
        }
    }

    public double getC1_t1A() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getC1_t1A();
        }  else {
            return 0;
        }
    }

    public int getTestMethod() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public double getT1B() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return 0;
        } else {
            return 0;
        }
    }

    public double getC1_t1B() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return 0;
        }   else {
            return 0;
        }
    }

    public String getItem_type() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return "";
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            String type = item.getItem_type();
            return type;
        }   else {
            return "";
        }
    }


    public double getC2() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getC2();
        }  else {
            return 0;
        }
    }

    public double getT2A() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getT2A();
        }   else {
            return 0;
        }
    }

    public double getT2B() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getT2B();
        }   else {
            return 0;
        }
    }

    public double getC2_t2A() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getC2_t2A();
        }   else {
            return 0;
        }
    }

    public double getC2_t2B() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return 0;
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getC2_t2B();
        }  else {
            return 0;
        }
    }

    public String getPassword() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getPassword();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getPassword();
        }   else {
            return "";
        }
    }

    public String toMyStringProject() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.toMyStringProject();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.toMyStringProject();
        }   else {
            return "";
        }
    }

    public int getSerialNumber() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getSerialNumber();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getSerialNumber();
        }   else {
            return 0;
        }
    }

    public int getYuretime() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getYuretime();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return 0;
        }  else {
            return 0;
        }
    }

    public int getJiancetime() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getJiancetime();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getTestTime();
        }  else {
            return 0;
        }
    }

    public boolean isUsetuise() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.isUsetuise();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return false;
        }  else {
            return false;
        }
    }

    public String getVersion() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.getVersion();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.getVersion();
        }  else {
            return "";
        }
    }

    public String toMyStringMethod() {
        if (this instanceof FGGDTestItem) {
            FGGDTestItem item = (FGGDTestItem) this;
            return item.toMyStringMethod();
        } else if (this instanceof JTJTestItem) {
            JTJTestItem item = (JTJTestItem) this;
            return item.toMyStringMethod();
        }  else {
            return "";
        }
    }




}
