package lab1;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("  ЗАВДАННЯ 1: Масив об'єктів та тестування");
        System.out.println("========================================\n");

        Settlement[] settlements = new Settlement[5];

        settlements[0] = new Settlement("Київ", 2967000, 847.66, "місто", 482, 50.4501, 30.5234);
        settlements[1] = new Settlement("Львів", 717000, 182.01, "місто", 1256, 49.8397, 24.0297);
        settlements[2] = new Settlement("Одеса", 1015000, 236.9, "місто", 1794, 46.4825, 30.7233);
        settlements[3] = new Settlement("Ковалівка", 3200, 12.5, "село", 1650, 49.6814, 30.8547);
        settlements[4] = new Settlement("Ірпінь", 62000, 53.6, "місто", 1956, 50.5218, 30.2507);

        System.out.println("--- Інформація про всі населені пункти ---\n");
        for (Settlement s : settlements) {
            s.printInfo();
            System.out.println();
        }

        System.out.println("--- Тестові дії ---\n");

        settlements[0].increasePopulation(15000);
        settlements[4].increasePopulation(5.5);
        settlements[3].decreasePopulation(200);

        System.out.println("\nВік Києва у 2026 році: "
                + settlements[0].getAge(2026) + " років");
        System.out.println("Густота населення Одеси: "
                + String.format("%.2f", settlements[2].getPopulationDensity()) + " осіб/км²");

        System.out.println("\n========================================");
        System.out.println("  ЗАВДАННЯ 2: Конструктори та інкапсуляція");
        System.out.println("========================================\n");

        System.out.println("--- Використання різних конструкторів ---\n");

        Settlement s1 = new Settlement();
        System.out.println("Конструктор за замовчуванням:");
        s1.printInfo();

        Settlement s2 = new Settlement("Харків", 1421000); // конструктор з 2 параметрами
        System.out.println("Конструктор з 2 параметрами:");
        s2.printInfo();

        Settlement s3 = new Settlement("Дніпро", 968000, 405.0, "місто", 1776, 48.4656, 35.0468); // повний конструктор
        System.out.println("Повний конструктор:");
        s3.printInfo();

        System.out.println("--- Перевантаження методів ---\n");
        System.out.println("increasePopulation(int) — збільшення на абсолютне значення:");
        s3.increasePopulation(5000);
        System.out.println("increasePopulation(double) — збільшення у відсотках:");
        s3.increasePopulation(2.0);

        System.out.println("\n--- Дослідження інкапсуляції ---\n");

        // s3.population = -100; Помилка компіляції, поле private

        // Доступ тільки через методи:
        System.out.println("Населення до зміни: " + s3.getPopulation());
        s3.setPopulation(980000);
        System.out.println("Населення після setPopulation(980000): " + s3.getPopulation());
        s3.setPopulation(-100);
        System.out.println("Населення після setPopulation(-100): " + s3.getPopulation()
                + " (значення не змінилось — захист даних!)");

        System.out.println("\n========================================");
        System.out.println("  ЗАВДАННЯ 3: Статичне поле");
        System.out.println("========================================\n");

        System.out.println("Загальна кількість створених населених пунктів: "
                + Settlement.getTotalSettlements());
        System.out.println("Кожен об'єкт має унікальний ID завдяки статичному полю nextId:");
        System.out.println("  " + s1.getName() + " → ID=" + s1.getId());
        System.out.println("  " + s2.getName() + " → ID=" + s2.getId());
        System.out.println("  " + s3.getName() + " → ID=" + s3.getId());

        System.out.println("\n========================================");
        System.out.println("  ЗАВДАННЯ 4: Статичні методи");
        System.out.println("========================================\n");

        Settlement larger = Settlement.getLargerByPopulation(settlements[0], settlements[2]);
        System.out.println("Більший за населенням між Києвом та Одесою: "
                + larger.getName() + " (" + larger.getPopulation() + " осіб)");

        double distance = Settlement.calculateDistance(settlements[0], settlements[1]);
        System.out.printf("Відстань Київ — Львів (по прямій): %.2f км%n", distance);

        System.out.println("\nВикористання Math (статичний клас):");
        System.out.printf("Площа кола з радіусом 10 км: %.2f км²%n",
                Math.PI * Math.pow(10, 2));
        System.out.printf("√(площа Києва) = %.2f км%n",
                Math.sqrt(settlements[0].getArea()));

    }
}