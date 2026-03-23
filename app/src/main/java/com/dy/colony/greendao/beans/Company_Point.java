package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.MyAppLocation;
import com.dy.colony.R;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
@Entity
public class Company_Point extends BaseUntilMessage implements Parcelable, IExpandable, MultiItemEntity {
    @Transient
    protected boolean mExpandable = false;
    @Transient
    protected List<Company_Point_Unit> mSubItems;

    public boolean isExpandable() {
        return mExpandable;
    }

    public void setExpandable(boolean expandable) {
        mExpandable = expandable;
    }

    public void setSubItems(List<Company_Point_Unit> subItems) {
        mSubItems = subItems;
    }

    public String getuDate() {
        return uDate == null ? "" : uDate;
    }

    public void setuDate(String uDate) {
        this.uDate = uDate == null ? "" : uDate;
    }

    public static Creator<Company_Point> getCREATOR() {
        return CREATOR;
    }


    @Id(autoincrement = true)
    private Long id;
    private String contactMan; //联系人
    private String contactPhone; //联系方式
    private String organizationCode; //所属组织机构code
    private String regAddress;       //详细地址
    private String regCorpName;      //法人名称
    private String regId;            //表主键 -1 表示仪器自建
    private String regName;         //企业名称
    private String uDate;          //修改时间
    private int usId;             //
    private int flag;//标识平台下载和手动新增

    private Long priority;//优先级

    public String toMyStringMessage() {
        return "企业名称：" + regName + "\r\n" +
                "企业唯一ID：" + regId + "\r\n" +
                "所属组织机构code：" + organizationCode + "\r\n" +
                "法人：" + regCorpName + "\r\n" +
                "联系人：" + contactMan + "\r\n" +
                "联系方式：" + contactPhone + "\r\n" +
                "详细地址：" + regAddress + "\r\n" +
                "最后修改时间：" + uDate;
    }

    /*public OutMoudle<String> toJxlTitle() {

        return new OutMoudle<String>(MyAppLocation.myAppLocation.getString(R.string.company_point_jxl_title));
    }

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(id + "," + contactMan + "," + contactPhone + "," + organizationCode + "," + regAddress + ","
                + regCorpName + "," + regId + "," + regName + "," + uDate + "," + usId + "," + flag);
    }*/

    public int getUsId() {
        return this.usId;
    }

    public void setUsId(int usId) {
        this.usId = usId;
    }

    public String getUDate() {
        return this.uDate;
    }

    public void setUDate(String uDate) {
        this.uDate = uDate;
    }

    public String getRegName() {
        return this.regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getRegId() {
        return this.regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getRegCorpName() {
        return this.regCorpName;
    }

    public void setRegCorpName(String regCorpName) {
        this.regCorpName = regCorpName;
    }

    public String getRegAddress() {
        return this.regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getOrganizationCode() {
        return this.organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactMan() {
        return this.contactMan;
    }

    public void setContactMan(String contactMan) {
        this.contactMan = contactMan;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Company_Point() {
    }


    @Override
    public boolean isExpanded() {
        return mExpandable;
    }

    @Override
    public void setExpanded(boolean expanded) {
        mExpandable = expanded;
    }

    @Override
    public List<Company_Point_Unit> getSubItems() {
        return mSubItems;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mExpandable ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.mSubItems);
        dest.writeValue(this.id);
        dest.writeString(this.contactMan);
        dest.writeString(this.contactPhone);
        dest.writeString(this.organizationCode);
        dest.writeString(this.regAddress);
        dest.writeString(this.regCorpName);
        dest.writeString(this.regId);
        dest.writeString(this.regName);
        dest.writeString(this.uDate);
        dest.writeInt(this.usId);
        dest.writeInt(this.flag);
        dest.writeValue(this.priority);
    }

    protected Company_Point(Parcel in) {
        this.mExpandable = in.readByte() != 0;
        this.mSubItems = in.createTypedArrayList(Company_Point_Unit.CREATOR);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.contactMan = in.readString();
        this.contactPhone = in.readString();
        this.organizationCode = in.readString();
        this.regAddress = in.readString();
        this.regCorpName = in.readString();
        this.regId = in.readString();
        this.regName = in.readString();
        this.uDate = in.readString();
        this.usId = in.readInt();
        this.flag = in.readInt();
        this.priority = (Long) in.readValue(Long.class.getClassLoader());
    }

    @Generated(hash = 2007683686)
    public Company_Point(Long id, String contactMan, String contactPhone, String organizationCode, String regAddress,
                         String regCorpName, String regId, String regName, String uDate, int usId, int flag, Long priority) {
        this.id = id;
        this.contactMan = contactMan;
        this.contactPhone = contactPhone;
        this.organizationCode = organizationCode;
        this.regAddress = regAddress;
        this.regCorpName = regCorpName;
        this.regId = regId;
        this.regName = regName;
        this.uDate = uDate;
        this.usId = usId;
        this.flag = flag;
        this.priority = priority;
    }

    public static final Creator<Company_Point> CREATOR = new Creator<Company_Point>() {
        @Override
        public Company_Point createFromParcel(Parcel source) {
            return new Company_Point(source);
        }

        @Override
        public Company_Point[] newArray(int size) {
            return new Company_Point[size];
        }
    };
}
