package lab7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementTextIO {

    public static final String TEXT_EXT = ".csv";

    private static final String SEP = ";";
    private static final String STREET_SEP = "|";
    private static final String SETTLEMENT_TAG = "SETTLEMENT";
    private static final String CROSSROAD_TAG = "CROSSROAD";
    private static final String COMMENT_PREFIX = "#";

    public static void saveToText(List<Settlement> settlements, String filePath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))) {

            pw.println("# Збережено автоматично — Лаб. 7");
            pw.println("# SETTLEMENT;name;population;area;type;foundedYear;lat;lon");
            pw.println("# CROSSROAD;name;streets(|-separated);lat;lon;regulated;veh/h;acc/y;ped/h");
            pw.println();

            for (Settlement s : settlements) {
                pw.println(String.join(SEP,
                        SETTLEMENT_TAG,
                        escape(s.getName()),
                        String.valueOf(s.getPopulation()),
                        String.valueOf(s.getArea()),
                        escape(s.getType()),
                        String.valueOf(s.getFoundedYear()),
                        String.valueOf(s.getLatitude()),
                        String.valueOf(s.getLongitude())
                ));

                for (Crossroad c : s.getCrossroads()) {
                    String streets = String.join(STREET_SEP, c.getStreets());
                    Crossroad.TrafficStats ts = c.getStats();

                    pw.println(String.join(SEP,
                            CROSSROAD_TAG,
                            escape(c.getCrossroadName()),
                            escape(streets),
                            String.valueOf(c.getLatitude()),
                            String.valueOf(c.getLongitude()),
                            String.valueOf(c.isRegulated()),
                            String.valueOf(ts.getVehiclesPerHour()),
                            String.valueOf(ts.getAccidentsPerYear()),
                            String.valueOf(ts.getPedestriansPerHour())
                    ));
                }

                pw.println();
            }
        }
    }

    public static void saveToText(Settlement settlement, String filePath) throws IOException {
        List<Settlement> list = new ArrayList<>();
        list.add(settlement);
        saveToText(list, filePath);
    }

    public static List<Settlement> loadFromText(String filePath) throws IOException {
        List<Settlement> result = new ArrayList<>();
        Settlement current = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {
                lineNum++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith(COMMENT_PREFIX)) {
                    continue;
                }

                String[] parts = line.split(SEP, -1);
                String tag = parts[0];

                if (SETTLEMENT_TAG.equals(tag)) {
                    if (parts.length < 8) {
                        throw new IOException("Рядок " + lineNum + ": недостатньо полів Settlement");
                    }

                    String name = unescape(parts[1]);
                    int population = Integer.parseInt(parts[2]);
                    double area = Double.parseDouble(parts[3]);
                    String type = unescape(parts[4]);
                    int founded = Integer.parseInt(parts[5]);
                    double lat = Double.parseDouble(parts[6]);
                    double lon = Double.parseDouble(parts[7]);

                    current = new Settlement(name, population, area, type, founded, lat, lon);
                    result.add(current);

                } else if (CROSSROAD_TAG.equals(tag)) {
                    if (parts.length < 9) {
                        throw new IOException("Рядок " + lineNum + ": недостатньо полів Crossroad");
                    }

                    if (current == null) {
                        throw new IOException("Рядок " + lineNum + ": CROSSROAD без Settlement");
                    }

                    String cName = unescape(parts[1]);
                    String[] streets = unescape(parts[2]).split("\\" + STREET_SEP, -1);
                    double cLat = Double.parseDouble(parts[3]);
                    double cLon = Double.parseDouble(parts[4]);
                    boolean regulated = Boolean.parseBoolean(parts[5]);
                    int veh = Integer.parseInt(parts[6]);
                    int acc = Integer.parseInt(parts[7]);
                    int ped = Integer.parseInt(parts[8]);

                    Crossroad c = new Crossroad(cName, streets, cLat, cLon, regulated, veh, acc, ped);
                    current.addCrossroad(c);

                } else {
                    throw new IOException("Рядок " + lineNum + ": невідомий тег \"" + tag + "\"");
                }
            }
        }

        return result;
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace(SEP, "\\" + SEP);
    }

    private static String unescape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\" + SEP, SEP);
    }
}