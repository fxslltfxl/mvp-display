package cn.zemic.hy.display.unmannedstoragedisplay.model.bean;

public class FilterLedForMobileUiCommand {
    public String LedId;

    public FilterLedForMobileUiCommand(String ledId) {
        LedId = ledId;
    }

    public String getLedId() {
        return LedId == null ? "" : LedId;
    }

    public void setLedId(String ledId) {
        LedId = ledId;
    }
}
