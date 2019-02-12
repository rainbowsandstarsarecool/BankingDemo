package net.ukr.just_void;

public class CurrencyJsonElement {
    private String txt;
    private double rate;
    private String cc;


    public CurrencyJsonElement(String txt, double rate, String cc) {
        this.txt = txt;
        this.rate = rate;
        this.cc = cc;
    }

    public CurrencyJsonElement() {
    }


    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

}
