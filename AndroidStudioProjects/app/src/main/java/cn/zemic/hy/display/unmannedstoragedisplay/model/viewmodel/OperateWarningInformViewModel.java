package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fxs
 */
public class OperateWarningInformViewModel implements Parcelable {
    /**
     * 员工编号
     */
    public String UserNo;
    /**
     * 姓名
     */
    public String UserName;
    /**
     * 异常类型
     */
    public String WarnType;
    /**
     * 物料编号
     */
    public String MatterNo;
    /**
     * 物料名称
     */
    public String MatterName;
    /**
     * 异常信息
     */
    public String WarnContent;
    public String WareHouseNo;
    public String ShelfNo;
    public String UnitNo;


    public String getUserNo() {
        return UserNo == null ? "" : UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getUserName() {
        return UserName == null ? "" : UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getWarnType() {
        return WarnType == null ? "" : WarnType;
    }

    public void setWarnType(String warnType) {
        WarnType = warnType;
    }

    public String getMatterNo() {
        return MatterNo == null ? "" : MatterNo;
    }

    public void setMatterNo(String matterNo) {
        MatterNo = matterNo;
    }

    public String getMatterName() {
        return MatterName == null ? "" : MatterName;
    }

    public void setMatterName(String matterName) {
        MatterName = matterName;
    }

    public String getWarnContent() {
        return WarnContent == null ? "" : WarnContent;
    }

    public void setWarnContent(String warnContent) {
        WarnContent = warnContent;
    }

    public String getWareHouseNo() {
        return WareHouseNo == null ? "" : WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public String getShelfNo() {
        return ShelfNo == null ? "" : ShelfNo;
    }

    public void setShelfNo(String shelfNo) {
        ShelfNo = shelfNo;
    }

    public String getUnitNo() {
        return UnitNo == null ? "" : UnitNo;
    }

    public void setUnitNo(String unitNo) {
        UnitNo = unitNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserNo);
        dest.writeString(this.UserName);
        dest.writeString(this.WarnType);
        dest.writeString(this.MatterNo);
        dest.writeString(this.MatterName);
        dest.writeString(this.WarnContent);
        dest.writeString(this.WareHouseNo);
        dest.writeString(this.ShelfNo);
        dest.writeString(this.UnitNo);
    }

    public OperateWarningInformViewModel() {
    }

    protected OperateWarningInformViewModel(Parcel in) {
        this.UserNo = in.readString();
        this.UserName = in.readString();
        this.WarnType = in.readString();
        this.MatterNo = in.readString();
        this.MatterName = in.readString();
        this.WarnContent = in.readString();
        this.WareHouseNo = in.readString();
        this.ShelfNo = in.readString();
        this.UnitNo = in.readString();
    }

    public static final Parcelable.Creator<OperateWarningInformViewModel> CREATOR = new Parcelable.Creator<OperateWarningInformViewModel>() {
        @Override
        public OperateWarningInformViewModel createFromParcel(Parcel source) {
            return new OperateWarningInformViewModel(source);
        }

        @Override
        public OperateWarningInformViewModel[] newArray(int size) {
            return new OperateWarningInformViewModel[size];
        }
    };
}
