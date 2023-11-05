package com.example.uberapp_tim22.tools;

public class LightSensor {

    public static void main(String[] args) {
        // Simulacija vrednosti senzora osvetljenja
        double lightValue = 10; // Početna vrednost osvetljenja (možete promeniti)
        double darkThreshold = 50; // Prag za detekciju mraka (možete promeniti)

        System.out.println("Senzor osvetljenja je pokrenut.");
        System.out.println("Trenutna vrednost osvetljenja: " + lightValue);

        // Simulacija kontinuiranog očitavanja senzora
        while (true) {
            // Očitajte vrednost sa senzora (simulacija)
            lightValue = simulateLightSensor();

            // Provera da li je detektovan mrak
            if (lightValue <= darkThreshold) {
                System.out.println("Detektovan je mrak! Palim svetlo.");
                // Ovde možete izvršiti akciju kao što je upaljena LED sijalica
            }

            // Pauzirajte senzor na neko vreme (simulacija)
            try {
                Thread.sleep(1000); // Pauza od 1 sekunde
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Simulacija očitavanja sa senzora (generisanje slučajnih vrednosti)
    private static double simulateLightSensor() {
        // Generišite slučajnu vrednost osvetljenja između 0 i 1000 (prilagodite)
        return Math.random() * 1000;
    }
}
