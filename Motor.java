public class Motor {
    private double hacim;
    private int guc;
    private String tip;

    public Motor(double hacim, int guc, String tip) {
        this.hacim = hacim;
        this.guc = guc;
        this.tip = tip;
    }

    public double getHacim() {
        return hacim;
    }

    public int getGuc() {
        return guc;
    }

    public String getTip() {
        return tip;
    }
}
