package com.dy.colony.greendao.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.entity.IExpandable;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.dy.colony.mvp.model.entity.OutMoudle;
import com.dy.colony.mvp.model.entity.base.BaseUntilMessage;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
@Entity
public class Company_Point_Unit extends BaseUntilMessage implements Parcelable, IExpandable, MultiItemEntity {
    @Id(autoincrement = true)
    private Long id;
    private String cdId;//唯一标识
    private String cdIdNum;//档口号
    private String cdName;//经营户名称
    private String cdeuslicence;//营业执照
    private String ciAddr;//详细地址
    private String ciContMan;//联系人
    private String ciContType;//联系方式
    private String ciName;//所属备件单位名称
    private String ciPost;//邮编
    private String cicorpName;//法人姓名
    private String contactMan;//所属备件单位联系人
    private String contactPhone;//所属备件单位联系电话
    private String organizationCode;//所属组织机构code
    private String regAddress;//所属备件单位详细地址
    private String regCorpName;//所属备件单位法人名称
    private String regId;//所属备件单位唯一标识
    private String regName;//所属备件单位企业名称
    private String uDate;//更新时间
    private int usId;//
    private int flag;//

    private Long priority;//优先级

    public String toMyStringMessage() {
        return "经营户名称：" + cdName + "\r\n" +
                "档口号：" + cdIdNum + "\r\n" +
                "营业执照：" + cdeuslicence + "\r\n" +
                "联系人：" + ciContMan + "\r\n" +
                "联系方式：" + ciContType + "\r\n" +
                "详细地址：" + ciAddr + "\r\n" +
                "所属单位名称：" + regName + "\r\n" +
                "所属单位唯一ID：" + regId + "\r\n" +
                "所属单位组织机构code：" + organizationCode + "\r\n" +
                "所属单位法人：" + regCorpName + "\r\n" +
                "所属单位联系人：" + contactMan + "\r\n" +
                "所属单位联系方式：" + contactPhone + "\r\n" +
                "所属单位详细地址：" + regAddress + "\r\n" +
                "最后修改时间：" + uDate;
    }

   /* public OutMoudle<String> toJxlTitle() {
        return new OutMoudle<String>(MyAppLocation.myAppLocation.getString(R.string.company_unit_jxl_title_1) +
                MyAppLocation.myAppLocation.getString(R.string.company_unit_jxl_title_2));
    }*/

    public OutMoudle<String> toJxlString() {
        return new OutMoudle<String>(id + "," + cdId + "," + cdIdNum + "," + cdName + "," + cdeuslicence + ","
                + ciAddr + "," + ciContMan + "," + ciContType + "," + ciName + "," + ciPost + "," + cicorpName + "," + contactMan + ","
                + contactPhone + "," + organizationCode + "," + regAddress + "," + regCorpName + "," + regId + "," + regName + "," + uDate
                + "," + usId + "," + flag);
    }

    public String getUDate() {
        return this.uDate;
    }

    public void setUDate(String uDate) {
        this.uDate = uDate;
    }

    public String getRegId() {
        return this.regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public String getCicorpName() {
        return this.cicorpName;
    }

    public void setCicorpName(String cicorpName) {
        this.cicorpName = cicorpName;
    }

    public String getCiPost() {
        return this.ciPost;
    }

    public void setCiPost(String ciPost) {
        this.ciPost = ciPost;
    }

    public String getCiName() {
        return this.ciName;
    }

    public void setCiName(String ciName) {
        this.ciName = ciName;
    }

    public String getCiContType() {
        return this.ciContType;
    }

    public void setCiContType(String ciContType) {
        this.ciContType = ciContType;
    }

    public String getCiContMan() {
        return this.ciContMan;
    }

    public void setCiContMan(String ciContMan) {
        this.ciContMan = ciContMan;
    }

    public String getCiAddr() {
        return this.ciAddr;
    }

    public void setCiAddr(String ciAddr) {
        this.ciAddr = ciAddr;
    }

    public String getCdeuslicence() {
        return this.cdeuslicence;
    }

    public void setCdeuslicence(String cdeuslicence) {
        this.cdeuslicence = cdeuslicence;
    }

    public String getCdName() {
        return this.cdName;
    }

    public void setCdName(String cdName) {
        this.cdName = cdName;
    }

    public String getCdIdNum() {
        return this.cdIdNum;
    }

    public void setCdIdNum(String cdIdNum) {
        this.cdIdNum = cdIdNum;
    }

    public String getCdId() {
        return this.cdId;
    }

    public void setCdId(String cdId) {
        this.cdId = cdId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        LogUtils.d(id + "");
        this.id = id;
    }

    public Company_Point_Unit() {
    }


    public int getUsId() {
        return this.usId;
    }

    public void setUsId(int usId) {
        this.usId = usId;
    }

    public String getRegName() {
        return this.regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.cdId);
        dest.writeString(this.cdIdNum);
        dest.writeString(this.cdName);
        dest.writeString(this.cdeuslicence);
        dest.writeString(this.ciAddr);
        dest.writeString(this.ciContMan);
        dest.writeString(this.ciContType);
        dest.writeString(this.ciName);
        dest.writeString(this.ciPost);
        dest.writeString(this.cicorpName);
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
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    protected Company_Point_Unit(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.cdId = in.readString();
        this.cdIdNum = in.readString();
        this.cdName = in.readString();
        this.cdeuslicence = in.readString();
        this.ciAddr = in.readString();
        this.ciContMan = in.readString();
        this.ciContType = in.readString();
        this.ciName = in.readString();
        this.ciPost = in.readString();
        this.cicorpName = in.readString();
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
    }

    @Generated(hash = 692116361)
    public Company_Point_Unit(Long id, String cdId, String cdIdNum, String cdName, String cdeuslicence,
                              String ciAddr, String ciContMan, String ciContType, String ciName, String ciPost, String cicorpName,
                              String contactMan, String contactPhone, String organizationCode, String regAddress,
                              String regCorpName, String regId, String regName, String uDate, int usId, int flag, Long priority) {
        this.id = id;
        this.cdId = cdId;
        this.cdIdNum = cdIdNum;
        this.cdName = cdName;
        this.cdeuslicence = cdeuslicence;
        this.ciAddr = ciAddr;
        this.ciContMan = ciContMan;
        this.ciContType = ciContType;
        this.ciName = ciName;
        this.ciPost = ciPost;
        this.cicorpName = cicorpName;
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


    public static final Creator<Company_Point_Unit> CREATOR = new Creator<Company_Point_Unit>() {
        @Override
        public Company_Point_Unit createFromParcel(Parcel source) {
            return new Company_Point_Unit(source);
        }

        @Override
        public Company_Point_Unit[] newArray(int size) {
            return new Company_Point_Unit[size];
        }
    };

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public void setExpanded(boolean expanded) {

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getSubItems() {
        return new ArrayList<>();
    }


    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return 1;
    }

    public Long getPriority() {
        return this.priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }
}
