# 🏎️ Ultra Car Racing Simulator

## Java OOP Araba Simülasyonu Projesi

### 📋 Özellikler

#### 🎮 Oyun Özellikleri
- **Çoklu Araba Seçimi**: 5 farklı süper araba (Ferrari, Lamborghini, Porsche, McLaren, Bugatti)
- **Menü Sistemi**: Profesyonel başlangıç menüsü
- **Power-Up Sistemi**: Nitro, Yakıt, Can power-up'ları
- **Drift Sistemi**: Drift yaparak combo puanı kazanma
- **Engel Sistemi**: Dinamik engeller ve çarpışma sistemi
- **Skor Sistemi**: Combo ve mesafe bazlı puanlama
- **Can Sistemi**: 3 can ile oyun mekaniği

#### 🎨 Görsel Özellikler
- **Gelişmiş Grafikler**: Anti-aliasing ve gradient efektler
- **Parçacık Sistemi**: Egzoz dumanı, drift dumanı, patlama efektleri
- **Animasyonlar**: Smooth araba hareketi ve rotasyon
- **HUD Sistemi**: Hız, yakıt, nitro göstergeleri
- **Renkli Power-Up'lar**: Görsel olarak ayırt edilebilir power-up'lar

#### 🌦️ Ek Sistemler (Yeni Eklenenler)
- **Hava Durumu Sistemi**: Yağmur, kar, sis efektleri
- **Gece/Gündüz Döngüsü**: Dinamik gökyüzü renkleri, güneş ve ay
- **Leaderboard**: En yüksek skorları kaydetme sistemi
- **Araç Özelleştirme**: Renk, spoiler, neon ışık seçenekleri

### 🎯 Kontroller

- **W / ↑**: Hızlan
- **S / ↓**: Yavaşla
- **A / ←**: Sola git
- **D / →**: Sağa git
- **SHIFT**: Nitro boost
- **SPACE**: Drift
- **F**: Yakıt doldur
- **P**: Pause
- **ESC**: Menüye dön

### 🏗️ OOP Prensipleri

#### Kullanılan Sınıflar:
1. **Motor.java**: Motor özellikleri (Encapsulation)
2. **Araba.java**: Araba sınıfı (Composition - Motor içerir)
3. **UltraCarGame.java**: Ana oyun sınıfı
4. **WeatherSystem.java**: Hava durumu sistemi
5. **DayNightCycle.java**: Gece/gündüz döngüsü
6. **Leaderboard.java**: Skor tablosu
7. **CarCustomization.java**: Araç özelleştirme

#### OOP Özellikleri:
- ✅ Encapsulation (private değişkenler, public metodlar)
- ✅ Composition (Araba içinde Motor)
- ✅ Inheritance (Panel sınıfları)
- ✅ Polymorphism (Farklı araba tipleri)
- ✅ Abstraction (Interface kullanımı)

### 🚀 Çalıştırma

```bash
# Derleme
javac *.java

# Çalıştırma
java UltraCarGame
```

### 📦 Dosya Yapısı

```
├── Motor.java              # Motor sınıfı
├── Araba.java              # Araba sınıfı
├── Main.java               # Basit konsol testi
├── ArabaGUI.java           # Basit GUI versiyonu
├── ArabaGUI2.java          # Gelişmiş GUI versiyonu
├── ExtremeCarSimulator.java # Extreme versiyon
├── UltraCarGame.java       # Ultra gelişmiş oyun
├── SuperCarGame.java       # En gelişmiş versiyon
├── WeatherSystem.java      # Hava durumu sistemi
├── DayNightCycle.java      # Gece/gündüz döngüsü
├── Leaderboard.java        # Skor tablosu
└── CarCustomization.java   # Araç özelleştirme
```

### 🎓 Öğrenilen Konular

- Java Swing ile GUI programlama
- Graphics2D ile 2D çizim
- Timer ve animasyon
- Event handling (KeyListener)
- ArrayList ve koleksiyonlar
- File I/O işlemleri
- Enum kullanımı
- Inner class yapıları

### 🔧 Gereksinimler

- Java JDK 8 veya üzeri
- 1400x900 minimum ekran çözünürlüğü

### 👨‍💻 Geliştirici

Tolga Olgunsoy
- GitHub: [@tolgaolgunsoy1](https://github.com/tolgaolgunsoy1)

### 📝 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

---

**Keyifli Oyunlar! 🏁**
