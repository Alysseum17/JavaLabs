package lab4;

import lab2.Crossroad;
import lab2.SettlementType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * Дженерік-версія класу {@code Settlement} з Lab 2.
 *
 * <p><b>Завдання 3.</b> Узагальнення класу {@code Settlement}.
 *
 * <p>У Lab 2 {@code Settlement} жорстко зберігав {@code Crossroad[] crossroads}.
 * Це означало, що якщо знадобиться Settlement, що зберігає тільки
 * {@code UnregulatedCrossroad}, або майбутній підтип {@code RailwayCrossroad}, —
 * довелося б або дублювати клас, або скрізь робити downcast вручну.
 *
 * <p>Параметр {@code T extends Crossroad} дозволяє:
 * <pre>
 *   GenericSettlement&lt;Crossroad&gt;            — як оригінальний Settlement
 *   GenericSettlement&lt;UnregulatedCrossroad&gt; — тільки нерегульовані перехрестя
 * </pre>
 *
 * <p><b>Порівняння з оригінальним Settlement:</b>
 * <pre>
 *   Settlement (Lab 2)                        GenericSettlement&lt;T extends Crossroad&gt;
 *   ──────────────────────────────────────────────────────────────────────────────
 *   Crossroad[] crossroads                 →  T[] crossroads
 *   addCrossroad(Crossroad c)              →  addCrossroad(T c)
 *   getCrossroad(int i) : Crossroad        →  getCrossroad(int i) : T   (без downcast)
 *   getCrossroads()     : Crossroad[]      →  getCrossroads()     : T[] (без downcast)
 *   findCrossroadsByStreet(String)         →  findByStreet(String) : T[]
 *   findMostDangerousCrossroad() : Crossroad →  findMostDangerous() : T (без downcast)
 *   (немає)                                →  findMax(Comparator&lt;T&gt;) : T
 * </pre>
 *
 * @param <T> тип перехресть; має бути {@code Crossroad} або його підтип
 */
public class GenericSettlement<T extends Crossroad> {

    // =====================================================================
    //  Поля — ті самі, що в Settlement, але crossroads тепер T[]
    // =====================================================================

    private String name;
    private int population;
    private double area;
    private SettlementType type;
    private int foundedYear;
    private double latitude;
    private double longitude;
    private final int id;

    // new T[n] неможливо через type erasure, тому кастуємо один раз.
    // Це стандартний патерн (так само зроблено в ArrayList у JDK).
    @SuppressWarnings("unchecked")
    private final T[] crossroads = (T[]) new Crossroad[MAX_CROSSROADS];
    private int crossroadCount;

    private static final int MAX_CROSSROADS = 100;
    private static int nextId = 1;
    private static int totalSettlements = 0;

    // =====================================================================
    //  Конструктори — ті самі сигнатури, що в Settlement
    // =====================================================================

    public GenericSettlement(String name, int population, double area,
                             String type, int foundedYear,
                             double latitude, double longitude) {
        this.name = name;
        this.population = population;
        this.area = area;
        this.type = SettlementType.fromString(type);
        this.foundedYear = foundedYear;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = nextId++;
        totalSettlements++;
    }

    public GenericSettlement(String name, int population) {
        this(name, population, 0, "місто", 2000, 0, 0);
    }

    // =====================================================================
    //  Методи роботи з перехрестями — тепер повертають T, а не Crossroad
    // =====================================================================

    /**
     * Додає перехрестя типу T.
     * В оригіналі: {@code addCrossroad(Crossroad c)} — приймав будь-який Crossroad,
     * компілятор не заважав змішувати типи в одному Settlement.
     * Тут тип T фіксується при створенні об'єкта, тому змішати неможливо.
     */
    public void addCrossroad(T c) {
        if (crossroadCount < MAX_CROSSROADS) crossroads[crossroadCount++] = c;
        else System.out.println("Помилка: досягнуто максимум перехресть!");
    }

    /**
     * Повертає елемент типу T без downcast.
     * В оригіналі повертав {@code Crossroad}, і щоб отримати
     * {@code UnregulatedCrossroad} доводилося кастувати вручну.
     */
    public T getCrossroad(int index) {
        if (index >= 0 && index < crossroadCount) return crossroads[index];
        return null;
    }

    /**
     * Повертає копію масиву типу T[].
     * В оригіналі повертав {@code Crossroad[]}, що втрачало інформацію про підтип.
     */
    public T[] getCrossroads() {
        return Arrays.copyOf(crossroads, crossroadCount);
    }

    /**
     * Пошук за довільним предикатом.
     * В оригіналі метод {@code findCrossroadsByStreet(String)} мав жорстко задану
     * умову. Тут умова передається ззовні — метод universal.
     */
    public T[] findBy(Predicate<T> predicate) {
        // Два проходи: спочатку рахуємо, потім заповнюємо (як в оригіналі)
        int count = 0;
        for (int i = 0; i < crossroadCount; i++)
            if (predicate.test(crossroads[i])) count++;

        T[] result = Arrays.copyOf(crossroads, count);
        int idx = 0;
        for (int i = 0; i < crossroadCount; i++)
            if (predicate.test(crossroads[i])) result[idx++] = crossroads[i];
        return result;
    }

    /**
     * Пошук за назвою вулиці — зберігає інтерфейс оригіналу.
     */
    public T[] findByStreet(String streetName) {
        return findBy(c -> c.containsStreet(streetName));
    }

    /**
     * Знаходить найнебезпечніше перехрестя.
     * В оригіналі делегував до {@code Crossroad.findMostDangerous(Crossroad[])},
     * що повертав {@code Crossroad} — потрібен downcast.
     * Тут повертає T одразу.
     */
    public T findMostDangerous() {
        if (crossroadCount == 0) return null;
        T worst = crossroads[0];
        for (int i = 1; i < crossroadCount; i++)
            if (crossroads[i].getStats().getDangerLevel() > worst.getStats().getDangerLevel())
                worst = crossroads[i];
        return worst;
    }

    /**
     * Нова можливість відсутня в оригіналі: пошук екстремуму за довільним
     * компаратором. Стало можливим завдяки параметру типу T.
     */
    public T findMax(Comparator<T> comparator) {
        if (crossroadCount == 0) return null;
        T max = crossroads[0];
        for (int i = 1; i < crossroadCount; i++)
            if (comparator.compare(crossroads[i], max) > 0) max = crossroads[i];
        return max;
    }

    // =====================================================================
    //  Решта методів — ті самі, що в Settlement
    // =====================================================================

    public String getName()          { return name; }
    public int getPopulation()       { return population; }
    public double getArea()          { return area; }
    public int getFoundedYear()      { return foundedYear; }
    public double getLatitude()      { return latitude; }
    public double getLongitude()     { return longitude; }
    public int getId()               { return id; }
    public int getCrossroadCount()   { return crossroadCount; }

    public double getPopulationDensity() { return area > 0 ? population / area : 0; }

    public void printInfo() {
        System.out.println("=== GenericSettlement<T> [ID=" + id + "]: " + name + " ===");
        System.out.println("Тип: " + type + ", населення: " + String.format("%,d", population));
        System.out.printf("Площа: %.2f км², густота: %.2f осіб/км²%n", area, getPopulationDensity());
        System.out.println("Рік заснування: " + foundedYear);
        System.out.println("Перехресть: " + crossroadCount);
    }

    public static int getTotalSettlements() { return totalSettlements; }
}