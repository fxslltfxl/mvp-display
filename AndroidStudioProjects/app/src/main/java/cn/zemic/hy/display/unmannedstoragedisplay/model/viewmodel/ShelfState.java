package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShelfState implements Parcelable {

    public String WareHouseNo;
    public List<ShelfLedState> Shelfs;

    public String getWareHouseNo() {
        return WareHouseNo == null ? "" : WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public List<ShelfLedState> getShelfs() {
        if (Shelfs == null) {
            return new ArrayList<>();
        }
        return Shelfs;
    }

    public void setShelfs(List<ShelfLedState> shelfs) {
        Shelfs = shelfs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.WareHouseNo);
        dest.writeTypedList(this.Shelfs);
    }

    public ShelfState() {
    }

    protected ShelfState(Parcel in) {
        this.WareHouseNo = in.readString();
        this.Shelfs = in.createTypedArrayList(ShelfLedState.CREATOR);
    }

    public static final Parcelable.Creator<ShelfState> CREATOR = new Parcelable.Creator<ShelfState>() {
        @Override
        public ShelfState createFromParcel(Parcel source) {
            return new ShelfState(source);
        }

        @Override
        public ShelfState[] newArray(int size) {
            return new ShelfState[size];
        }
    };
}
