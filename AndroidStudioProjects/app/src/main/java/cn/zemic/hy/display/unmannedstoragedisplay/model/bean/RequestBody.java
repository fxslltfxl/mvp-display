package cn.zemic.hy.display.unmannedstoragedisplay.model.bean;

public class RequestBody {
    public String UserNo;

    public String getUserNo() {
        return UserNo == null ? "" : UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }
}
