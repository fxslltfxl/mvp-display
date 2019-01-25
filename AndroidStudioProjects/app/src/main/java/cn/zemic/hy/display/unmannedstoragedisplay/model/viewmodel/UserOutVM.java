package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class UserOutVM implements Parcelable {
    private String UserNo;

    private String UserName;

    private String ApplyNo;

    private String Status;

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

    public String getApplyNo() {
        return ApplyNo == null ? "" : ApplyNo;
    }

    public void setApplyNo(String applyNo) {
        ApplyNo = applyNo;
    }

    public String getStatus() {
        return Status == null ? "" : Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserNo);
        dest.writeString(this.UserName);
        dest.writeString(this.ApplyNo);
        dest.writeString(this.Status);
    }

    public UserOutVM() {
    }

    protected UserOutVM(Parcel in) {
        this.UserNo = in.readString();
        this.UserName = in.readString();
        this.ApplyNo = in.readString();
        this.Status = in.readString();
    }

    public static final Creator<UserOutVM> CREATOR = new Creator<UserOutVM>() {
        @Override
        public UserOutVM createFromParcel(Parcel source) {
            return new UserOutVM(source);
        }

        @Override
        public UserOutVM[] newArray(int size) {
            return new UserOutVM[size];
        }
    };
}
