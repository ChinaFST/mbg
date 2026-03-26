package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

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
@Entity
public class User implements Parcelable {
    @Id(autoincrement = true)
    public Long id;

    @NotNull
    private String username; //yonghuming
    @NotNull
    private String password;
    private String name;
    private String phone;
    private String jobname;
    private String unit;
    private String sex;
    private String email;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username == null ? "" : username;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone;
    }

    public String getJobname() {
        return jobname == null ? "" : jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname == null ? "" : jobname;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit == null ? "" : unit;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? "" : sex;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email == null ? "" : email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.jobname);
        dest.writeString(this.unit);
        dest.writeString(this.sex);
        dest.writeString(this.email);
    }

    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.username = in.readString();
        this.password = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.jobname = in.readString();
        this.unit = in.readString();
        this.sex = in.readString();
        this.email = in.readString();
    }

    @Generated(hash = 871544755)
    public User(Long id, @NotNull String username, @NotNull String password,
            String name, String phone, String jobname, String unit, String sex,
            String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.jobname = jobname;
        this.unit = unit;
        this.sex = sex;
        this.email = email;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public  User createFromParcel(Parcel source) {
            return new  User(source);
        }

        @Override
        public  User[] newArray(int size) {
            return new  User[size];
        }
    };
}
