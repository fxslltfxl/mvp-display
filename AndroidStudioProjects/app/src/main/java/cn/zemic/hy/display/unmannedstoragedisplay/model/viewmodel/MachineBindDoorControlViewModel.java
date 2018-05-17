package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author fxs
 */
public class MachineBindDoorControlViewModel implements Parcelable {
    public int Id;
    public String DoorNo;
    public String DoorId;
    public String DoorIP;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDoorNo() {
        return DoorNo == null ? "" : DoorNo;
    }

    public void setDoorNo(String doorNo) {
        DoorNo = doorNo;
    }

    public String getDoorId() {
        return DoorId == null ? "" : DoorId;
    }

    public void setDoorId(String doorId) {
        DoorId = doorId;
    }

    public String getDoorIP() {
        return DoorIP == null ? "" : DoorIP;
    }

    public void setDoorIP(String doorIP) {
        DoorIP = doorIP;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Id);
        dest.writeString(this.DoorNo);
        dest.writeString(this.DoorId);
        dest.writeString(this.DoorIP);
    }

    public MachineBindDoorControlViewModel() {
    }

    protected MachineBindDoorControlViewModel(Parcel in) {
        this.Id = in.readInt();
        this.DoorNo = in.readString();
        this.DoorId = in.readString();
        this.DoorIP = in.readString();
    }

    public static final Parcelable.Creator<MachineBindDoorControlViewModel> CREATOR = new Parcelable.Creator<MachineBindDoorControlViewModel>() {
        @Override
        public MachineBindDoorControlViewModel createFromParcel(Parcel source) {
            return new MachineBindDoorControlViewModel(source);
        }

        @Override
        public MachineBindDoorControlViewModel[] newArray(int size) {
            return new MachineBindDoorControlViewModel[size];
        }
    };
}
