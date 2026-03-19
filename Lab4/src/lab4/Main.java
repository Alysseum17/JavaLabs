package lab4;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        Crossroad c1 = new Crossroad("Хрещатик / Прорізна",
                new String[]{"Хрещатик", "Прорізна"}, 50.4487, 30.5205, true);
        c1.setStats(new Crossroad.TrafficStats(1200, 3, 800));

        Crossroad c2 = new Crossroad("Хрещатик / Б. Хмельницького",
                new String[]{"Хрещатик", "Богдана Хмельницького"}, 50.4471, 30.5192, true);
        c2.setStats(new Crossroad.TrafficStats(900, 2, 600));

        Crossroad c3 = new Crossroad("Хрещатик", "Шевченка");
        c3.setStats(new Crossroad.TrafficStats(600, 1, 300));

        Crossroad c4 = new Crossroad("Шевченка / Саксаганського",
                new String[]{"Шевченка", "Саксаганського"}, 50.4385, 30.5115, true);
        c4.setStats(new Crossroad.TrafficStats(400, 1, 200));

        UnregulatedCrossroad u1 = new UnregulatedCrossroad("Прорізна / Пушкінська",
                new String[]{"Прорізна", "Пушкінська"}, 50.4490, 30.5170,
                "головна/другорядна", "Прорізна", true, 30);
        u1.setStats(new Crossroad.TrafficStats(300, 8, 400));

        UnregulatedCrossroad u2 = new UnregulatedCrossroad(
                "Саксаганського", "Жилянська", "рівнозначні");
        u2.setStats(new Crossroad.TrafficStats(150, 4, 100));

        UnregulatedCrossroad u3 = new UnregulatedCrossroad("Січових Стрільців / Дмитрівська",
                new String[]{"Січових Стрільців", "Дмитрівська"}, 50.4550, 30.4950,
                "кільце", "", false, 40);
        u3.setStats(new Crossroad.TrafficStats(700, 6, 300));

        Crossroad[] allCrossroads = {c1, c2, c3, c4, u1, u2, u3};

        Settlement kyiv = new Settlement("Київ", 2967000, 847.66, "місто", 482, 50.4501, 30.5234);
        for (Crossroad c : allCrossroads) kyiv.addCrossroad(c);

        Settlement lviv  = new Settlement("Львів",  717000, 182.01, "місто", 1256, 49.8397, 24.0297);
        Settlement odesa = new Settlement("Одеса", 1011000, 162.42, "місто", 1794, 46.4775, 30.7326);
        Settlement[] settlements = {kyiv, lviv, odesa};

        System.out.println("\n========================================");
        System.out.println("ЗАВДАННЯ 2: Дженерик-метод vs звичайний метод");
        System.out.println("========================================\n");

        System.out.println("Узагальнено методи пошуку та сортування з Lab 2.");
        System.out.println("Оригінальні методи прив'язані до Crossroad[] і конкретного критерію.");
        System.out.println("Дженерик-версії працюють для будь-якого T і будь-якого Comparator/Predicate.\n");

        Comparator<Crossroad> byDanger =
                Comparator.comparingDouble(c -> c.getStats().getDangerLevel());

        System.out.println("--- а) findMax ---\n");

        Crossroad worstOld = kyiv.findMostDangerousCrossroad();
        System.out.println("kyiv.findMostDangerousCrossroad()  — тільки для Crossroad[]:");
        System.out.println("  → " + worstOld.getCrossroadName()
                + " (небезпека: " + String.format("%.4f", worstOld.getStats().getDangerLevel()) + ")");

        Crossroad worstNew = GenericMethods.findMax(allCrossroads, byDanger);
        System.out.println("\nGenericMethods.findMax(T[], Comparator<T>)  — для будь-якого T[]:");
        System.out.println("  → " + worstNew.getCrossroadName()
                + " (небезпека: " + String.format("%.4f", worstNew.getStats().getDangerLevel()) + ")");

        Settlement largest = GenericMethods.findMax(settlements,
                Comparator.comparingInt(Settlement::getPopulation));
        System.out.println("\nТой самий findMax для Settlement[] (за населенням):");
        System.out.println("  → " + largest.getName()
                + " (" + String.format("%,d", largest.getPopulation()) + " осіб)");

        Integer[] nums = {42, 7, 99, 13, 56};
        System.out.println("\nТой самий findMax для Integer[]:");
        System.out.println("  → " + GenericMethods.findMax(nums, Integer::compareTo));

        System.out.println("\n--- б) bubbleSort ---\n");

        Crossroad[] sortedOld = Arrays.copyOf(allCrossroads, allCrossroads.length);
        for (int i = 0; i < sortedOld.length - 1; i++)
            for (int j = 0; j < sortedOld.length - 1 - i; j++)
                if (sortedOld[j].getStats().getAccidentsPerYear()
                        < sortedOld[j + 1].getStats().getAccidentsPerYear()) {
                    Crossroad tmp = sortedOld[j];
                    sortedOld[j] = sortedOld[j + 1];
                    sortedOld[j + 1] = tmp;
                }
        System.out.println("Ручне сортування з Lab 2 — лише Crossroad[], лише за ДТП (спадання):");
        for (Crossroad c : sortedOld)
            System.out.println("  " + c.getCrossroadName()
                    + " — ДТП/рік: " + c.getStats().getAccidentsPerYear());

        Crossroad[] sortedNew = Arrays.copyOf(allCrossroads, allCrossroads.length);
        GenericMethods.bubbleSort(sortedNew,
                Comparator.comparingDouble((Crossroad c) -> c.getStats().getDangerLevel()).reversed());
        System.out.println("\nGenericMethods.bubbleSort(T[], Comparator<T>) — за рівнем небезпеки:");
        for (Crossroad c : sortedNew)
            System.out.printf("  %-42s → %.4f%n",
                    c.getCrossroadName(), c.getStats().getDangerLevel());

        String[] cityNames = {"Одеса", "Київ", "Харків", "Львів", "Дніпро"};
        GenericMethods.bubbleSort(cityNames, String::compareTo);
        System.out.println("\nТой самий bubbleSort для String[] (алфавіт):");
        System.out.println("  " + GenericMethods.arrayToString(cityNames));

        System.out.println("\n--- в) linearSearch та countIf ---\n");

        Crossroad found = GenericMethods.linearSearch(allCrossroads,
                c -> c.getCrossroadName().contains("Хрещатик"));
        System.out.println("linearSearch(Crossroad[], \"Хрещатик\"): "
                + (found != null ? found.getCrossroadName() : "не знайдено"));

        System.out.println("countIf(Crossroad[], нерегульовані): "
                + GenericMethods.countIf(allCrossroads, c -> !c.isRegulated()));
        System.out.println("countIf(Settlement[], населення > 1 млн): "
                + GenericMethods.countIf(settlements, s -> s.getPopulation() > 1_000_000));

        System.out.println("\n--- г) sum<T extends Number> ---\n");

        Integer[] intArr = {10, 20, 30, 40};
        Double[]  dblArr = {1.5, 2.5, 3.5};
        System.out.println("sum(Integer[]): " + GenericMethods.sum(intArr));
        System.out.println("sum(Double[]):  " + GenericMethods.sum(dblArr));

        System.out.println("\n========================================");
        System.out.println("ЗАВДАННЯ 3: Дженерик-клас vs звичайний клас");
        System.out.println("========================================\n");

        System.out.println("Узагальнено клас Settlement з Lab 2 → GenericSettlement<T extends Crossroad>.");
        System.out.println("Settlement зберігав Crossroad[] — тип жорстко зашитий у полі класу.");
        System.out.println("GenericSettlement<T> зберігає T[], тип задається при інстанціюванні.\n");

        System.out.println("--- а) GenericSettlement<Crossroad> — поведінка як у оригінального Settlement ---\n");

        GenericSettlement<Crossroad> kyivGen = new GenericSettlement<>(
                "Київ", 2967000, 847.66, "місто", 482, 50.4501, 30.5234);
        for (Crossroad c : allCrossroads) kyivGen.addCrossroad(c);
        kyivGen.printInfo();

        Crossroad mostDang = kyivGen.findMostDangerous();
        System.out.println("\nfindMostDangerous() → " + mostDang.getCrossroadName()
                + " (небезпека: " + String.format("%.4f", mostDang.getStats().getDangerLevel()) + ")");

        List<Crossroad> khreshchatyk = kyivGen.findByStreet("Хрещатик");
        System.out.println("findByStreet(\"Хрещатик\") → знайдено: " + khreshchatyk.size());

        Crossroad maxTraffic = kyivGen.findMax(
                Comparator.comparingInt(c -> c.getStats().getVehiclesPerHour()));
        System.out.println("findMax(за авто/год) → " + maxTraffic.getCrossroadName()
                + " (" + maxTraffic.getStats().getVehiclesPerHour() + " авт/год)");

        System.out.println("\n--- б) GenericSettlement<UnregulatedCrossroad> — нова можливість ---\n");

        System.out.println("У Settlement Lab 2 всі підтипи Crossroad зберігались разом.");
        System.out.println("Щоб отримати UnregulatedCrossroad — потрібен ручний downcast.");
        System.out.println("GenericSettlement<UnregulatedCrossroad> гарантує тип на рівні компілятора.\n");

        GenericSettlement<UnregulatedCrossroad> kyivUnreg = new GenericSettlement<>(
                "Київ (нерегульовані)", 2967000);
        kyivUnreg.addCrossroad(u1);
        kyivUnreg.addCrossroad(u2);
        kyivUnreg.addCrossroad(u3);
        kyivUnreg.printInfo();

        UnregulatedCrossroad uc = kyivUnreg.getCrossroad(0);
        System.out.println("\ngetCrossroad(0) → " + uc.getCrossroadName()
                + ", пріоритет: " + uc.getPriorityType());

        UnregulatedCrossroad worstUnreg = kyivUnreg.findMostDangerous();
        System.out.println("findMostDangerous() → " + worstUnreg.getCrossroadName()
                + ", потрібен світлофор: " + worstUnreg.needsTrafficLight());

        List<UnregulatedCrossroad> roundabouts = kyivUnreg.findBy(
                u -> u.getPriorityType().equals("кільце"));
        System.out.println("findBy(кільце) → знайдено: " + roundabouts.size());

        System.out.println("\n--- в) Порівняння Settlement (Lab 2) vs GenericSettlement<T> ---\n");
        System.out.println("  Settlement.addCrossroad(Crossroad c)        → GenericSettlement<T>.addCrossroad(T c)");
        System.out.println("  Settlement.getCrossroad(i) : Crossroad       → GenericSettlement<T>.getCrossroad(i) : T");
        System.out.println("  Settlement.getCrossroads() : Crossroad[]     → GenericSettlement<T>.getCrossroads() : T[]");
        System.out.println("  Settlement.findCrossroadsByStreet(str)       → GenericSettlement<T>.findByStreet(str) : T[]");
        System.out.println("  Settlement.findMostDangerousCrossroad()      → GenericSettlement<T>.findMostDangerous() : T");
        System.out.println("  (немає)                                       → GenericSettlement<T>.findMax(Comparator<T>) : T");
        System.out.println("  (немає)                                       → GenericSettlement<T>.findBy(Predicate<T>) : T[]");
        System.out.println("\n  Звичайний клас: тип Crossroad жорстко зашитий, потрібен downcast для підтипів.");
        System.out.println("  Дженерік-клас: тип T фіксується при створенні, downcast не потрібен.");

        System.out.println("\n========================================");
        System.out.println("ЗАВДАННЯ 4: Методи з аргументом List<?>");
        System.out.println("========================================\n");

        List<Crossroad> crossroadList = Arrays.asList(allCrossroads);
        List<UnregulatedCrossroad> unregList = new ArrayList<>();
        unregList.add(u1); unregList.add(u2); unregList.add(u3);
        List<Integer> intList = Arrays.asList(10, 20, 30, 40, 50);
        List<String>  strList = new ArrayList<>(Arrays.asList("Kyiv", "Lviv", "Odesa"));

        System.out.println("--- Unbounded wildcard: List<?> ---\n");

        System.out.println("printList(List<Integer>):");
        WildcardUtils.printList(intList);
        System.out.println("\nprintList(List<String>):");
        WildcardUtils.printList(strList);

        System.out.println("\ncountNonNull(crossroadList): "
                + WildcardUtils.countNonNull(crossroadList));
        System.out.println("contains(intList, 30): " + WildcardUtils.contains(intList, 30));
        System.out.println("contains(intList, 99): " + WildcardUtils.contains(intList, 99));

        System.out.println("\n--- Upper bounded wildcard: List<? extends ...> ---\n");

        List<Integer> salaries  = Arrays.asList(50000, 70000, 45000, 90000);
        List<Double>  distances = Arrays.asList(847.66, 182.01, 162.42);

        System.out.println("sumNumbers(List<Integer>): " + WildcardUtils.sumNumbers(salaries));
        System.out.println("sumNumbers(List<Double>):  " + WildcardUtils.sumNumbers(distances));
        System.out.println("average(List<Double>):     "
                + String.format("%.2f", WildcardUtils.average(distances)));

        System.out.println("\nfindMax(List<Integer>): " + WildcardUtils.findMax(salaries));
        System.out.println("findMax(List<String>):  " + WildcardUtils.findMax(strList));

        Crossroad mostDangerous = WildcardUtils.findMostDangerous(crossroadList);
        System.out.println("\nfindMostDangerous(List<Crossroad>): "
                + mostDangerous.getCrossroadName());
        Crossroad mostDangerousU = WildcardUtils.findMostDangerous(unregList);
        System.out.println("findMostDangerous(List<UnregulatedCrossroad>): "
                + mostDangerousU.getCrossroadName());
        System.out.println("avgDangerLevel(crossroadList): "
                + String.format("%.4f", WildcardUtils.avgDangerLevel(crossroadList)));

        System.out.println("\n--- Lower bounded wildcard: List<? super ...> ---\n");

        List<Integer> exactInts  = new ArrayList<>();
        List<Number>  numberList = new ArrayList<>();
        List<Object>  objectList = new ArrayList<>();

        WildcardUtils.fillWithIntegers(exactInts,  1, 5);
        WildcardUtils.fillWithIntegers(numberList, 1, 5);
        WildcardUtils.fillWithIntegers(objectList, 1, 5);

        System.out.println("fillWithIntegers → List<Integer>: " + exactInts);
        System.out.println("fillWithIntegers → List<Number>:  " + numberList);
        System.out.println("fillWithIntegers → List<Object>:  " + objectList);

        List<Object> target = new ArrayList<>();
        WildcardUtils.copyCrossroads(unregList, target);
        System.out.println("\ncopyCrossroads(List<UnregulatedCrossroad> → List<Object>): "
                + target.size() + " елементів скопійовано");

        System.out.println("\n======================================================");
        System.out.println("ЗАВДАННЯ 5: Підстановочні типи у бібліотечних класах");
        System.out.println("======================================================\n");

        WildcardUtils.demonstrateStdlibWildcards();

    }
}