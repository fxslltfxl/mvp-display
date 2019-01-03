package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class ShelfLedState implements Parcelable {
    public String ShelfNo;

    public String ShelfState;

    public String getShelfNo() {
        return ShelfNo == null ? "" : ShelfNo;
    }

    public void setShelfNo(String shelfNo) {
        ShelfNo = shelfNo;
    }

    public String getShelfState() {
        return ShelfState == null ? "" : ShelfState;
    }

    public void setShelfState(String shelfState) {
        ShelfState = shelfState;
    }

    public ShelfLedState(String shelfNo, String shelfState) {
        ShelfNo = shelfNo;
        ShelfState = shelfState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ShelfNo);
        dest.writeString(this.ShelfState);
    }

    public ShelfLedState() {
    }

    protected ShelfLedState(Parcel in) {
        this.ShelfNo = in.readString();
        this.ShelfState = in.readString();
    }

    public static final Parcelable.Creator<ShelfLedState> CREATOR = new Parcelable.Creator<ShelfLedState>() {
        @Override
        public ShelfLedState createFromParcel(Parcel source) {
            return new ShelfLedState(source);
        }

        @Override
        public ShelfLedState[] newArray(int size) {
            return new ShelfLedState[size];
        }
    };
}
