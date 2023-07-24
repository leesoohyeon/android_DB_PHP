package com.example.accountbook_db_php;

public class ItemData {
    String in_date;
    int seq;
    String gubn;
    String meno;
    int money;

    public ItemData(){
    }
    public ItemData(String in_date, int seq, String gubn, String meno, int money) {
        this.in_date = in_date;
        this.seq = seq;
        this.gubn = gubn;
        this.meno = meno;
        this.money = money;
    }

    public String getIn_date() {
        return in_date;
    }

    public void setIn_date(String in_date) {
        this.in_date = in_date;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getGubn() {
        return gubn;
    }

    public void setGubn(String gubn) {
        this.gubn = gubn;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
