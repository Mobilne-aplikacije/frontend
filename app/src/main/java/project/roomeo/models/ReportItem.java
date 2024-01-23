package project.roomeo.models;

public class ReportItem {
    private String name;
    private int resNum;
    private int profit;

    public ReportItem(String name, int resNum, int profit) {
        this.name = name;
        this.resNum = resNum;
        this.profit = profit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResNum() {
        return resNum;
    }

    public void setResNum(int resNum) {
        this.resNum = resNum;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }
}
