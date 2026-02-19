package lab2;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("ЗАВДАННЯ 1: Агрегування, спадкування, поліморфізм");
        System.out.println("========================================\n");

        Settlement kyiv = new Settlement("Київ", 2967000, 847.66, "місто",
                482, 50.4501, 30.5234);
        Settlement lviv = new Settlement("Львів", 717000, 182.01, "місто",
                1256, 49.8397, 24.0297);


        Crossroad c1 = new Crossroad("Хрещатик / Прорізна",
                new String[]{"Хрещатик", "Прорізна"},
                50.4487, 30.5205, true);
        c1.setStats(new Crossroad.TrafficStats(1200, 3, 800));

        Crossroad c2 = new Crossroad("Хрещатик / Б. Хмельницького",
                new String[]{"Хрещатик", "Богдана Хмельницького"},
                50.4471, 30.5192, true);
        c2.setStats(new Crossroad.TrafficStats(900, 2, 600));

        Crossroad c3 = new Crossroad("Хрещатик", "Шевченка");
        c3.setStats(new Crossroad.TrafficStats(600, 1, 300));

        Crossroad c4 = new Crossroad("Шевченка / Саксаганського",
                new String[]{"Шевченка", "Саксаганського"},
                50.4385, 30.5115, true);
        c4.setStats(new Crossroad.TrafficStats(400, 1, 200));

        UnregulatedCrossroad c5 = new UnregulatedCrossroad("Прорізна / Пушкінська",
                new String[]{"Прорізна", "Пушкінська"},
                50.4490, 30.5170,
                "головна/другорядна", "Прорізна", true, 30);
        c5.setStats(new Crossroad.TrafficStats(300, 8, 400));

        UnregulatedCrossroad c6 = new UnregulatedCrossroad(
                "Саксаганського", "Жилянська", "рівнозначні");
        c6.setStats(new Crossroad.TrafficStats(150, 4, 100));

        UnregulatedCrossroad c7 = new UnregulatedCrossroad("Січових Стрільців / Дмитрівська",
                new String[]{"Січових Стрільців", "Дмитрівська"},
                50.4550, 30.4950,
                "кільце", "", false, 40);
        c7.setStats(new Crossroad.TrafficStats(700, 6, 300));

        kyiv.addCrossroad(c1);
        kyiv.addCrossroad(c2);
        kyiv.addCrossroad(c3);
        kyiv.addCrossroad(c4);
        kyiv.addCrossroad(c5);
        kyiv.addCrossroad(c6);
        kyiv.addCrossroad(c7);

        Crossroad lc1 = new Crossroad("Свободи / Шевченка",
                new String[]{"Свободи", "Шевченка"},
                49.8425, 24.0283, true);
        lc1.setStats(new Crossroad.TrafficStats(500, 2, 400));
        lviv.addCrossroad(lc1);

        System.out.println("--- Повна інформація про населені пункти ---\n");
        kyiv.printInfo();
        lviv.printInfo();

        System.out.println("--- Масив населених пунктів ---\n");
        Settlement[] settlements = {kyiv, lviv};
        for (Settlement s : settlements) {
            s.printShortInfo();
        }

        System.out.println("\n--- Поліморфізм: getSafetyLevel() для перехресть Києва ---\n");
        Crossroad[] kyivCrossroads = kyiv.getCrossroads();
        for (Crossroad c : kyivCrossroads) {
            System.out.println(c.getCrossroadName() + ": " + c.getSafetyLevel());
        }

        System.out.println("\n--- instanceof ---\n");
        for (Crossroad c : kyivCrossroads) {
            if (c instanceof UnregulatedCrossroad) {
                UnregulatedCrossroad uc = (UnregulatedCrossroad) c;
                System.out.println(c.getCrossroadName()
                        + " → UnregulatedCrossroad, пріоритет: " + uc.getPriorityType()
                        + ", потрібен світлофор: " + uc.needsTrafficLight());
            } else {
                System.out.println(c.getCrossroadName() + " → Crossroad (регульоване)");
            }
        }

        System.out.println("\n--- Пошук перехресть вулиці \"Хрещатик\" у Києві ---\n");
        Crossroad[] khreshchatyk = kyiv.findCrossroadsByStreet("Хрещатик");
        System.out.println("Знайдено " + khreshchatyk.length + " перехресть:");
        for (Crossroad c : khreshchatyk) {
            System.out.println("  • " + c.getCrossroadName());
        }

        System.out.println("\n--- Пошук перехресть вулиці \"Шевченка\" у Києві ---\n");
        Crossroad[] shevchenka = kyiv.findCrossroadsByStreet("Шевченка");
        System.out.println("Знайдено " + shevchenka.length + " перехресть:");
        for (Crossroad c : shevchenka) {
            System.out.println("  • " + c.getCrossroadName());
        }

        Crossroad danger = kyiv.findMostDangerousCrossroad();
        System.out.println("\nНайнебезпечніше перехрестя Києва: " + danger.getCrossroadName()
                + " (" + danger.getStats().getAccidentsPerYear() + " ДТП/рік)");

        System.out.println("\n--- Методи класу-нащадка ---\n");
        System.out.println(c6.getCrossroadName() + " — тип: " + c6.getPriorityType());
        c6.setMainRoad("Саксаганського");
        System.out.println("Після зміни — тип: " + c6.getPriorityType());

        System.out.println("========================================");
        System.out.println("ЗАВДАННЯ 2: Вкладений клас та локальний клас");
        System.out.println("========================================\n");

        System.out.println("--- Вкладений клас Crossroad.TrafficStats ---\n");
        System.out.println("TrafficStats — static nested class всередині Crossroad.");
        System.out.println("Доступ ззовні: Crossroad.TrafficStats\n");

        Crossroad.TrafficStats customStats = new Crossroad.TrafficStats(450, 2, 200);
        System.out.println("Створено TrafficStats ззовні:");
        customStats.printStats();

        System.out.println("\nДоступ до вкладеного класу через об'єкт зовнішнього:");
        System.out.println("c1.getStats().getVehiclesPerHour() = "
                + c1.getStats().getVehiclesPerHour());

        System.out.println("\n--- Локальний клас InfoFormatter ---\n");
        System.out.println("Визначений всередині методу Crossroad.printInfo().");
        System.out.println("Доступний ТІЛЬКИ в цьому методі.");
        System.out.println("Має доступ до полів зовнішнього об'єкта (streets, latitude...).");
        System.out.println("Демонстрація (виклик printInfo):\n");
        c1.printInfo();

        System.out.println("========================================");
        System.out.println("ЗАВДАННЯ 3: Операції з масивами");
        System.out.println("========================================\n");

        Crossroad[] allCrossroads = kyiv.getCrossroads();

        System.out.println("--- а) Пошук перехрестя за назвою ---\n");
        String searchName = "Хрещатик / Прорізна";
        Crossroad found = null;
        for (Crossroad c : allCrossroads) {
            if (c.getCrossroadName().equals(searchName)) {
                found = c;
                break;
            }
        }
        System.out.println("Пошук \"" + searchName + "\": "
                + (found != null ? "знайдено, ID=" + found.getId() : "не знайдено"));

        System.out.println("\n--- б) Мін / макс за кількістю вулиць ---\n");
        Crossroad maxS = allCrossroads[0], minS = allCrossroads[0];
        for (Crossroad c : allCrossroads) {
            if (c.getNumberOfStreets() > maxS.getNumberOfStreets()) maxS = c;
            if (c.getNumberOfStreets() < minS.getNumberOfStreets()) minS = c;
        }
        System.out.println("Макс вулиць: " + maxS.getCrossroadName()
                + " (" + maxS.getNumberOfStreets() + ")");
        System.out.println("Мін вулиць: " + minS.getCrossroadName()
                + " (" + minS.getNumberOfStreets() + ")");

        System.out.println("\n--- в) Сортування за кількістю ДТП (спадання) ---\n");
        Crossroad[] sorted = Arrays.copyOf(allCrossroads, allCrossroads.length);
        for (int i = 0; i < sorted.length - 1; i++) {
            for (int j = 0; j < sorted.length - 1 - i; j++) {
                if (sorted[j].getStats().getAccidentsPerYear()
                        < sorted[j + 1].getStats().getAccidentsPerYear()) {
                    Crossroad temp = sorted[j];
                    sorted[j] = sorted[j + 1];
                    sorted[j + 1] = temp;
                }
            }
        }
        for (Crossroad c : sorted) {
            System.out.println("  " + c.getCrossroadName()
                    + " — ДТП/рік: " + c.getStats().getAccidentsPerYear());
        }

        System.out.println("\n--- г) Копіювання масиву ---\n");
        Crossroad[] copy1 = new Crossroad[allCrossroads.length];
        System.arraycopy(allCrossroads, 0, copy1, 0, allCrossroads.length);
        System.out.println("System.arraycopy — скопійовано " + copy1.length + " елементів");

        Crossroad[] copy2 = Arrays.copyOf(allCrossroads, allCrossroads.length);
        System.out.println("Arrays.copyOf    — скопійовано " + copy2.length + " елементів");

        Crossroad[] copy3 = new Crossroad[allCrossroads.length];
        for (int i = 0; i < allCrossroads.length; i++) copy3[i] = allCrossroads[i];
        System.out.println("Ручне копіювання — скопійовано " + copy3.length + " елементів");

        System.out.println("\ncopy1[0] == allCrossroads[0]: " + (copy1[0] == allCrossroads[0])
                + " (поверхневе копіювання — той самий об'єкт)");

    }
}