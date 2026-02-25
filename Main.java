public class Main {
    public static void main(String[] args) {
        Motor motor1 = new Motor(1.6, 120, "Benzinli");
        Araba araba1 = new Araba("Toyota", "Corolla", 2023, motor1);

        Motor motor2 = new Motor(2.0, 150, "Dizel");
        Araba araba2 = new Araba("BMW", "320i", 2024, motor2);

        araba1.bilgiGoster();
        System.out.println();
        
        araba1.calistir();
        araba1.calistir();
        araba1.durdur();
        
        System.out.println("\n");
        araba2.bilgiGoster();
    }
}
