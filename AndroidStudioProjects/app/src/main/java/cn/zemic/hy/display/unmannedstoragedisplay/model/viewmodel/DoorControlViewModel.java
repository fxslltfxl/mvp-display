package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

public class DoorControlViewModel implements Parcelable {
    public boolean IsOpenDoor;
    public String WarehouseNo;
    public MachineBindDoorControlViewModel DoorControl;
    public UserInViewModel User;

    public boolean isOpenDoor() {
        return IsOpenDoor;
    }

    public void setOpenDoor(boolean openDoor) {
        IsOpenDoor = openDoor;
    }

    public String getWarehouseNo() {
        return WarehouseNo == null ? "" : WarehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        WarehouseNo = warehouseNo;
    }

    public MachineBindDoorControlViewModel getDoorControl() {
        return DoorControl;
    }

    public void setDoorControl(MachineBindDoorControlViewModel doorControl) {
        DoorControl = doorControl;
    }

    public UserInViewModel getUser() {
        return User;
    }

    public void setUser(UserInViewModel user) {
        User = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.IsOpenDoor ? (byte) 1 : (byte) 0);
        dest.writeString(this.WarehouseNo);
        dest.writeParcelable(this.DoorControl, flags);
        dest.writeParcelable(this.User, flags);
    }

    public DoorControlViewModel() {
    }

    protected DoorControlViewModel(Parcel in) {
        this.IsOpenDoor = in.readByte() != 0;
        this.WarehouseNo = in.readString();
        this.DoorControl = in.readParcelable(MachineBindDoorControlViewModel.class.getClassLoader());
        this.User = in.readParcelable(UserInViewModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<DoorControlViewModel> CREATOR = new Parcelable.Creator<DoorControlViewModel>() {
        @Override
        public DoorControlViewModel createFromParcel(Parcel source) {
            return new DoorControlViewModel(source);
        }

        @Override
        public DoorControlViewModel[] newArray(int size) {
            return new DoorControlViewModel[size];
        }
    };
}
