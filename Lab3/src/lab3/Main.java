package lab3;

import lab3.Settlement;
import lab3.Crossroad;
import lab3.UnregulatedCrossroad;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Settlement kyiv = new Settlement("Київ", 2967000, 847.66, "місто",
                482, 50.4501, 30.5234);
        Settlement lviv = new Settlement("Львів", 717000, 182.01, "місто",
                1256, 49.8397, 24.0297);
        Settlement odesa = new Settlement("Одеса", 1015000, 236.9, "місто",
                1794, 46.4825, 30.7233);
        Settlement kharkiv = new Settlement("Харків", 1421000, 350.0, "місто",
                1654, 49.9935, 36.2304);
        Settlement irpin = new Settlement("Ірпінь", 62000, 53.6, "місто",
                1956, 50.5218, 30.2507);
        Settlement kovalivka = new Settlement("Ковалівка", 3200, 12.5, "село",
                1650, 49.6814, 30.8547);
        Settlement myrhorod = new Settlement("Миргород", 38000, 35.7, "місто",
                1575, 49.9667, 33.6000);
        Settlement opishnya = new Settlement("Опішня", 5800, 18.3, "село",
                1658, 49.9667, 34.0500);

        Crossroad c1 = new Crossroad("Хрещатик / Прорізна",
                new String[]{"Хрещатик", "Прорізна"}, 50.4487, 30.5205, true);
        c1.setStats(new Crossroad.TrafficStats(1200, 3, 800));
        kyiv.addCrossroad(c1);

        UnregulatedCrossroad c2 = new UnregulatedCrossroad("Січових / Дмитрівська",
                new String[]{"Січових Стрільців", "Дмитрівська"}, 50.455, 30.495,
                "кільце", "", false, 40);
        c2.setStats(new Crossroad.TrafficStats(700, 6, 300));
        kyiv.addCrossroad(c2);

        Crossroad c3 = new Crossroad("Свободи", "Шевченка");
        c3.setStats(new Crossroad.TrafficStats(500, 2, 400));
        lviv.addCrossroad(c3);

        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  ЗАВДАННЯ 1: Collection — пошук, фільтрація, середнє     ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        SettlementCollection collection = new SettlementCollection();
        collection.add(kyiv);
        collection.add(lviv);
        collection.add(odesa);
        collection.add(kharkiv);
        collection.add(irpin);
        collection.add(kovalivka);
        collection.add(myrhorod);
        collection.add(opishnya);

        collection.printAll("Усі населені пункти");

        System.out.println("\n--- Пошук ---");

        Settlement found = collection.find(s -> s.getName().equals("Одеса"));
        System.out.println("Знайти 'Одеса': "
                + (found != null ? found.getName() + " (насел.: " + found.getPopulation() + ")" : "не знайдено"));

        found = collection.find(s -> s.getPopulation() > 1_000_000);
        System.out.println("Перший з населенням > 1 млн: "
                + (found != null ? found.getName() : "не знайдено"));

        List<Settlement> millionCities = collection.findAll(s -> s.getPopulation() > 1_000_000);
        System.out.print("Усі міста-мільйонники: ");
        for (Settlement s : millionCities) System.out.print(s.getName() + " ");
        System.out.println("(" + millionCities.size() + " шт.)");

        System.out.println("\n--- Унікальні елементи (Set) ---");
        collection.add(kyiv);
        System.out.println("Розмір з дублікатом: " + collection.size());
        Set<Settlement> unique = collection.getUnique();
        System.out.println("Унікальних (за назвою): " + unique.size());
        collection.remove(kyiv);

        System.out.println("\n--- Порівняння за різними критеріями ---");

        List<Settlement> byPopulation = collection.compareBy(
                Comparator.comparingInt(Settlement::getPopulation));
        SettlementCollection.printList(byPopulation, "За населенням (зростання)");

        List<Settlement> byArea = collection.compareBy(
                Comparator.comparingDouble(Settlement::getArea).reversed());
        SettlementCollection.printList(byArea, "За площею (спадання)");

        List<Settlement> byAge = collection.compareBy(
                Comparator.comparingInt(Settlement::getFoundedYear));
        SettlementCollection.printList(byAge, "За віком (найстаріші першими)");

        System.out.println("\n--- Фільтрація ---");

        List<Settlement> bigCities = collection.filter(s -> s.getPopulation() > 500_000);
        SettlementCollection.printList(bigCities, "Населення > 500 тис.");

        List<Settlement> withCrossroads = collection.filter(s -> s.getCrossroadCount() > 0);
        SettlementCollection.printList(withCrossroads, "Мають перехрестя");

        List<Settlement> villages = collection.filter(s -> s.getType().equals("село"));
        SettlementCollection.printList(villages, "Села");

        System.out.println("\n--- Середнє значення ---");

        double avgPop = collection.averageInt(Settlement::getPopulation);
        System.out.printf("Середнє населення: %,.0f осіб%n", avgPop);

        double avgArea = collection.averageDouble(Settlement::getArea);
        System.out.printf("Середня площа: %.2f км²%n", avgArea);

        double avgDensity = collection.averageDouble(Settlement::getPopulationDensity);
        System.out.printf("Середня густота: %.2f осіб/км²%n", avgDensity);

        double avgCrossroads = collection.averageInt(Settlement::getCrossroadCount);
        System.out.printf("Середня к-ть перехресть: %.2f%n", avgCrossroads);

        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  ЗАВДАННЯ 2: Сортування — 3 способи                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        System.out.println("\nКритерій: за населенням (зростання)\n");

        System.out.println("Спосіб 1: Анонімний клас");
        SettlementCollection.printList(collection.sortByPopulationAnonymous(), "Анонімний клас");

        System.out.println("\nСпосіб 2: Лямбда-вираз");
        SettlementCollection.printList(collection.sortByPopulationLambda(), "Лямбда");

        System.out.println("\nСпосіб 3: Comparator.comparingInt (посилання на метод)");
        SettlementCollection.printList(collection.sortByPopulationMethodRef(), "Method ref");

        System.out.println("\nДодатково — за назвою (Comparator.comparing + method ref):");
        SettlementCollection.printList(collection.sortByNameMethodRef(), "За назвою");

        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  ЗАВДАННЯ 3: Map — фільтрація, видалення, reduction       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        SettlementMap map = new SettlementMap(
                List.of(kyiv, lviv, odesa, kharkiv, irpin, kovalivka, myrhorod, opishnya));
        map.printAll("Усі населені пункти в Map");

        System.out.println("\n--- Фільтрація ---");

        Map<String, Settlement> cities = map.filter(s -> s.getType().equals("місто"));
        SettlementMap.printMap(cities, "Лише міста");

        Map<String, Settlement> large = map.filter(s -> s.getArea() > 100);
        SettlementMap.printMap(large, "Площа > 100 км²");

        System.out.println("\n--- Видалення ---");

        SettlementMap mapCopy = new SettlementMap(
                List.of(kyiv, lviv, odesa, kharkiv, irpin, kovalivka, myrhorod, opishnya));
        System.out.println("Розмір до видалення: " + mapCopy.size());

        int removed = mapCopy.removeIfNot(s -> s.getPopulation() > 50_000);
        System.out.println("Видалено (населення <= 50 тис.): " + removed);
        System.out.println("Розмір після: " + mapCopy.size());
        mapCopy.printAll("Після видалення малих");

        SettlementMap mapCopy2 = new SettlementMap(
                List.of(kyiv, lviv, odesa, kharkiv, irpin, kovalivka, myrhorod, opishnya));
        int removedVillages = mapCopy2.removeIf(s -> s.getType().equals("село"));
        System.out.println("\nВидалено сіл: " + removedVillages);
        mapCopy2.printAll("Після видалення сіл");

        System.out.println("\n--- Reduction ---");

        int totalPop = map.sumInt(Settlement::getPopulation);
        System.out.printf("Загальне населення: %,d осіб%n", totalPop);

        double totalArea = map.sumDouble(Settlement::getArea);
        System.out.printf("Загальна площа: %.2f км²%n", totalArea);

        map.maxInt(Settlement::getPopulation)
                .ifPresent(v -> System.out.printf("Макс населення: %,d%n", v));

        map.minInt(Settlement::getPopulation)
                .ifPresent(v -> System.out.printf("Мін населення: %,d%n", v));

        map.averageInt(Settlement::getPopulation)
                .ifPresent(v -> System.out.printf("Середнє населення: %,.0f%n", v));

        int totalCrossroads = map.sumInt(Settlement::getCrossroadCount);
        System.out.println("Загальна к-ть перехресть: " + totalCrossroads);

        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  ЗАВДАННЯ 4: Сортування за кількома ознаками              ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        System.out.println("\nКритерій: спочатку за типом (село → місто),");
        System.out.println("потім за населенням (спадання).\n");

        System.out.println("Спосіб 1: Анонімний клас");
        SettlementMap.printMap(map.sortMultiAnonymous(), "Анонімний клас");

        System.out.println("\nСпосіб 2: Лямбда-вираз");
        SettlementMap.printMap(map.sortMultiLambda(), "Лямбда");

        System.out.println("\nСпосіб 3: Comparator.comparing().thenComparing()");
        SettlementMap.printMap(map.sortMultiMethodRef(), "Method ref chain");

    }
}