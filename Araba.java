public class Araba {
    private String marka;
    private String model;
    private int yil;
    private Motor motor;
    private boolean calisiyorMu;
    private int hiz;
    private int yakit;
    private int maxHiz;
    private boolean nitro;
    private int nitroMiktar;
    private int can;

    public Araba(String marka, String model, int yil, Motor motor) {
        this.marka = marka;
        this.model = model;
        this.yil = yil;
        this.motor = motor;
        this.calisiyorMu = false;
        this.hiz = 0;
        this.yakit = 100;
        this.maxHiz = motor.getGuc();
        this.nitro = false;
        this.nitroMiktar = 100;
        this.can = 3;
    }

    public void calistir() {
        if (!calisiyorMu && yakit > 0) {
            calisiyorMu = true;
            System.out.println(marka + " " + model + " çalıştırıldı.");
        }
    }

    public void durdur() {
        if (calisiyorMu) {
            calisiyorMu = false;
            hiz = 0;
            System.out.println(marka + " " + model + " durduruldu.");
        }
    }

    public void hizlan() {
        if (calisiyorMu && hiz < maxHiz && yakit > 0) {
            int artis = nitro ? 20 : 10;
            hiz += artis;
            if (hiz > maxHiz * (nitro ? 1.5 : 1)) hiz = (int)(maxHiz * (nitro ? 1.5 : 1));
            yakitTuket();
            if (nitro) nitroTuket();
        }
    }

    public void nitroAktif(boolean aktif) {
        if (aktif && nitroMiktar > 0) {
            nitro = true;
        } else {
            nitro = false;
        }
    }

    private void nitroTuket() {
        if (nitroMiktar > 0) {
            nitroMiktar -= 2;
            if (nitroMiktar <= 0) {
                nitroMiktar = 0;
                nitro = false;
            }
        }
    }

    public void nitroYukle() {
        nitroMiktar = 100;
    }

    public void hasarAl() {
        if (can > 0) can--;
    }

    public void canYukle() {
        can = 3;
    }

    public void yavasla() {
        if (hiz > 0) {
            hiz -= 10;
            if (hiz < 0) hiz = 0;
        }
    }

    public void yakitDoldur() {
        yakit = 100;
    }

    private void yakitTuket() {
        if (yakit > 0) {
            yakit -= 1;
            if (yakit <= 0) {
                yakit = 0;
                durdur();
            }
        }
    }

    public void bilgiGoster() {
        System.out.println("=== Araba Bilgileri ===");
        System.out.println("Marka: " + marka);
        System.out.println("Model: " + model);
        System.out.println("Yıl: " + yil);
        System.out.println("Motor: " + motor.getHacim() + "L - " + motor.getGuc() + " HP");
        System.out.println("Durum: " + (calisiyorMu ? "Çalışıyor" : "Durmuş"));
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public boolean getCalisiyorMu() {
        return calisiyorMu;
    }

    public String getBilgi() {
        return "Marka: " + marka + "\n" +
               "Model: " + model + "\n" +
               "Yıl: " + yil + "\n" +
               "Motor: " + motor.getHacim() + "L\n" +
               "Güç: " + motor.getGuc() + " HP\n" +
               "Hız: " + hiz + " km/h\n" +
               "Yakıt: %" + yakit + "\n" +
               "Durum: " + (calisiyorMu ? "Çalışıyor" : "Durmuş");
    }

    public int getHiz() {
        return hiz;
    }

    public int getYakit() {
        return yakit;
    }

    public int getMaxHiz() {
        return maxHiz;
    }

    public boolean isNitro() {
        return nitro;
    }

    public int getNitroMiktar() {
        return nitroMiktar;
    }

    public int getCan() {
        return can;
    }
}
