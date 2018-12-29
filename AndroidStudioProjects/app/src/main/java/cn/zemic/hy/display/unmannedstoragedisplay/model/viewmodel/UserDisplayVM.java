package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

public class UserDisplayVM {
    public String UserNo;
    public String UserName;
    private String ApplyNo;


    public UserDisplayVM() {
    }

    public UserDisplayVM(String userNo, String userName, String applyNo) {
        UserNo = userNo;
        UserName = userName;
        ApplyNo = applyNo;
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

    public String getApplyNo() {
        return ApplyNo == null ? "" : ApplyNo;
    }

    public void setApplyNo(String applyNo) {
        ApplyNo = applyNo;
    }

    public UserDisplayVM userIn2UserDisplay(UserInViewModel user){
        this.ApplyNo = "";
        this.UserNo = user.getUserNo();
        this.UserName  = user.getUserName();
        return this;
    }

    public UserDisplayVM userOut2UserDisplay(UserOutVM user){
        this.ApplyNo = user.getApplyNo();
        this.UserNo = user.getUserNo();
        this.UserName  = user.getUserName();
        return this;
    }
}
