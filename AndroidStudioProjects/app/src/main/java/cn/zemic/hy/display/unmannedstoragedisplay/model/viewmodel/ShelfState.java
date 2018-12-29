package cn.zemic.hy.display.unmannedstoragedisplay.model.viewmodel;

public class ShelfState {

    private String ShelfNo;

    int state;

    public String getShelfNo() {
        return ShelfNo == null ? "" : ShelfNo;
    }

    public void setShelfNo(String shelfNo) {
        ShelfNo = shelfNo;
    }

    public ShelfState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
