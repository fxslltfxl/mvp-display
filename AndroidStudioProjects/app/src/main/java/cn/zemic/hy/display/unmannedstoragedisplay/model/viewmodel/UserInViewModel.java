package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInViewModel implements Parcelable {

    public String UserNo;
    public String UserName;
    public String OrderNo;

    public UserInViewModel(String userNo, String userName, String orderNo) {
        UserNo = userNo;
        UserName = userName;
        OrderNo = orderNo;
    }

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

    public String getOrderNo() {
        return OrderNo == null ? "" : OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserNo);
        dest.writeString(this.UserName);
        dest.writeString(this.OrderNo);
    }

    protected UserInViewModel(Parcel in) {
        this.UserNo = in.readString();
        this.UserName = in.readString();
        this.OrderNo = in.readString();
    }

    public static final Creator<UserInViewModel> CREATOR = new Creator<UserInViewModel>() {
        @Override
        public UserInViewModel createFromParcel(Parcel source) {
            return new UserInViewModel(source);
        }

        @Override
        public UserInViewModel[] newArray(int size) {
            return new UserInViewModel[size];
        }
    };
}
