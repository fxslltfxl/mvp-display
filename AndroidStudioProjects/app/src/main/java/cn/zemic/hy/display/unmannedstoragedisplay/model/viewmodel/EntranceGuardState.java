package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class EntranceGuardState implements Parcelable {

    public String WareHouseNo;

    public String MachineIP;

    public String ConnectState;

    public String getWareHouseNo() {
        return WareHouseNo == null ? "" : WareHouseNo;
    }

    public void setWareHouseNo(String wareHouseNo) {
        WareHouseNo = wareHouseNo;
    }

    public String getMachineIP() {
        return MachineIP == null ? "" : MachineIP;
    }

    public void setMachineIP(String machineIP) {
        MachineIP = machineIP;
    }

    public String getConnectState() {
        return ConnectState == null ? "" : ConnectState;
    }

    public void setConnectState(String connectState) {
        ConnectState = connectState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.WareHouseNo);
        dest.writeString(this.MachineIP);
        dest.writeString(this.ConnectState);
    }

    public EntranceGuardState() {
    }

    protected EntranceGuardState(Parcel in) {
        this.WareHouseNo = in.readString();
        this.MachineIP = in.readString();
        this.ConnectState = in.readString();
    }

    public static final Parcelable.Creator<EntranceGuardState> CREATOR = new Parcelable.Creator<EntranceGuardState>() {
        @Override
        public EntranceGuardState createFromParcel(Parcel source) {
            return new EntranceGuardState(source);
        }

        @Override
        public EntranceGuardState[] newArray(int size) {
            return new EntranceGuardState[size];
        }
    };
}
