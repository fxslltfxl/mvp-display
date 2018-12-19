package cn.zemic.hy.display.unmannedstoragedisplay.model.bean;

/**
 * @author fxs
 */
public class AlarmInfoBean {

    /**
     * UserNo : sample string 1
     * UserName : sample string 2
     * WarnType : sample string 3
     * MatterNo : sample string 4
     * MatterName : sample string 5
     * WarnContent : sample string 6
     * WareHouseNo : sample string 7
     * ShelfNo : sample string 8
     * UnitNo : sample stri
     * 0000000000ng 9
     */

    private String UserNo;
    private String UserName;
    private String WarnType;
    private String MatterNo;
    private String MatterName;
    private String WarnContent;
    private String WareHouseNo;
    private String ShelfNo;
    private String UnitNo;

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
}
