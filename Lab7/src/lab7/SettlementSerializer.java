package lab7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SettlementSerializer {

    public static final String BINARY_EXT = ".ser";

    public static void saveSettlement(Settlement settlement, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filePath)))) {
            oos.writeObject(settlement);
        }
    }

    public static Settlement loadSettlement(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(filePath)))) {
            return (Settlement) ois.readObject();
        }
    }

    public static void saveSettlements(List<Settlement> settlements, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filePath)))) {
            oos.writeObject(new ArrayList<>(settlements));
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Settlement> loadSettlements(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(filePath)))) {
            return (List<Settlement>) ois.readObject();
        }
    }

    public static void saveCrossroad(Crossroad crossroad, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filePath)))) {
            oos.writeObject(crossroad);
        }
    }

    public static Crossroad loadCrossroad(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(filePath)))) {
            return (Crossroad) ois.readObject();
        }
    }

    public static boolean fileExists(String filePath) {
        File f = new File(filePath);
        return f.exists() && f.isFile() && f.canRead();
    }
}