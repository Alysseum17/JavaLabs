package lab5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {

    private final List<Settlement> settlements = new ArrayList<>();
    private final List<Crossroad> crossroads = new ArrayList<>();

    private JTextArea logArea;
    private JList<Settlement> settlementList;
    private DefaultListModel<Settlement> settlementListModel;
    private JList<Crossroad> crossroadList;
    private DefaultListModel<Crossroad> crossroadListModel;

    private Timer simulationTimer;
    private int simulationTick = 0;
    private JLabel timerLabel;
    private JButton startTimerBtn, stopTimerBtn;

    private static final int MIN_POPULATION = 0;
    private static final int MAX_POPULATION = 50_000_000;
    private static final double MIN_AREA = 0.0;
    private static final double MAX_AREA = 1_000_000.0;
    private static final int MIN_YEAR = 0;
    private static final int MAX_YEAR = 2025;

    public MainFrame() {
        super("Моделювання населених пунктів — Лабораторна №5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        initDemoData();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("🏙️ Населені пункти", buildSettlementsTab());
        tabs.addTab("🚦 Перехрестя", buildCrossroadsTab());
        tabs.addTab("⏱️ Таймер / Симуляція", buildTimerTab());
        tabs.addTab("λ Лямбда-дослідження", buildLambdaTab());

        add(tabs, BorderLayout.CENTER);

        logArea = new JTextArea(6, 80);
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(180, 255, 180));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("📋 Журнал подій"));
        add(logScroll, BorderLayout.SOUTH);

        log("✅ Програму запущено. Завантажено " + settlements.size() +
                " населених пунктів та " + crossroads.size() + " перехресть.");
    }

    private JPanel buildSettlementsTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        settlementListModel = new DefaultListModel<>();
        settlements.forEach(settlementListModel::addElement);
        settlementList = new JList<>(settlementListModel);
        settlementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        settlementList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        JScrollPane listScroll = new JScrollPane(settlementList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Список населених пунктів"));
        listScroll.setPreferredSize(new Dimension(320, 0));

        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Детальна інформація"));

        settlementList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Settlement sel = settlementList.getSelectedValue();
                if (sel != null) detailArea.setText(sel.getFullInfo());
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        JButton addBtn = new JButton("➕ Додати");
        JButton editBtn = new JButton("✏️ Редагувати");
        JButton deleteBtn = new JButton("🗑️ Видалити");
        JButton resultsBtn = new JButton("📊 Результати");

        styleButton(addBtn, new Color(70, 160, 70));
        styleButton(editBtn, new Color(70, 130, 200));
        styleButton(deleteBtn, new Color(200, 70, 70));
        styleButton(resultsBtn, new Color(150, 100, 200));

        addBtn.addActionListener(e -> showSettlementDialog(null));
        editBtn.addActionListener(e -> {
            Settlement sel = settlementList.getSelectedValue();
            if (sel == null) { showError("Оберіть населений пункт для редагування."); return; }
            showSettlementDialog(sel);
        });
        deleteBtn.addActionListener(e -> {
            Settlement sel = settlementList.getSelectedValue();
            if (sel == null) { showError("Оберіть населений пункт для видалення."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Видалити «" + sel.getName() + "»?", "Підтвердження", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                settlements.remove(sel);
                settlementListModel.removeElement(sel);
                detailArea.setText("");
                log("🗑️ Видалено: " + sel.getName());
            }
        });
        resultsBtn.addActionListener(e -> showSimulationResults());

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(resultsBtn);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailScroll);
        split.setDividerLocation(320);

        panel.add(split, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCrossroadsTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        crossroadListModel = new DefaultListModel<>();
        crossroads.forEach(crossroadListModel::addElement);
        crossroadList = new JList<>(crossroadListModel);
        crossroadList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        crossroadList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        JScrollPane listScroll = new JScrollPane(crossroadList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Список перехресть"));
        listScroll.setPreferredSize(new Dimension(320, 0));

        JTextArea detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane detailScroll = new JScrollPane(detailArea);
        detailScroll.setBorder(BorderFactory.createTitledBorder("Детальна інформація"));

        crossroadList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Crossroad sel = crossroadList.getSelectedValue();
                if (sel != null) detailArea.setText(sel.getFullInfo());
            }
        });

        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        JButton addBtn = new JButton("➕ Додати");
        JButton editBtn = new JButton("✏️ Редагувати");
        JButton deleteBtn = new JButton("🗑️ Видалити");

        styleButton(addBtn, new Color(70, 160, 70));
        styleButton(editBtn, new Color(70, 130, 200));
        styleButton(deleteBtn, new Color(200, 70, 70));

        addBtn.addActionListener(e -> showCrossroadDialog(null));
        editBtn.addActionListener(e -> {
            Crossroad sel = crossroadList.getSelectedValue();
            if (sel == null) { showError("Оберіть перехрестя для редагування."); return; }
            showCrossroadDialog(sel);
        });
        deleteBtn.addActionListener(e -> {
            Crossroad sel = crossroadList.getSelectedValue();
            if (sel == null) { showError("Оберіть перехрестя для видалення."); return; }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Видалити перехрестя «" + sel.getCrossroadName() + "»?",
                    "Підтвердження", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                crossroads.remove(sel);
                crossroadListModel.removeElement(sel);
                detailArea.setText("");
                log("🗑️ Видалено перехрестя: " + sel.getCrossroadName());
            }
        });

        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailScroll);
        split.setDividerLocation(320);

        panel.add(split, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildTimerTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 4, 4));

        timerLabel = new JLabel("⏱️ Таймер зупинено. Тактів: 0", SwingConstants.CENTER);
        timerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        timerLabel.setBorder(BorderFactory.createEtchedBorder());

        JLabel infoLabel = new JLabel(
                "<html><center>Кожні 2 секунди симулятор моделює приріст населення (+0.1%) у всіх населених пунктах</center></html>",
                SwingConstants.CENTER);
        topPanel.add(timerLabel);
        topPanel.add(infoLabel);

        JTextArea simArea = new JTextArea();
        simArea.setEditable(false);
        simArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane simScroll = new JScrollPane(simArea);
        simScroll.setBorder(BorderFactory.createTitledBorder("📈 Лог симуляції"));

        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Параметри"));
        JLabel intervalLabel = new JLabel("Інтервал (мс):");
        JTextField intervalField = new JTextField("2000", 6);
        settingsPanel.add(intervalLabel);
        settingsPanel.add(intervalField);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        startTimerBtn = new JButton("▶ Запустити симуляцію");
        stopTimerBtn = new JButton("⏹ Зупинити");
        JButton clearBtn = new JButton("🗑️ Очистити лог");

        styleButton(startTimerBtn, new Color(70, 160, 70));
        styleButton(stopTimerBtn, new Color(200, 70, 70));
        stopTimerBtn.setEnabled(false);

        simulationTimer = new Timer(2000, e -> {
            simulationTick++;
            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("[%s] Такт #%d — приріст населення (+0.1%%):%n", time, simulationTick));

            for (Settlement s : settlements) {
                int before = s.getPopulation();
                s.increasePopulation(0.1);
                sb.append(String.format("  %-20s: %,d → %,d%n",
                        s.getName(), before, s.getPopulation()));
            }
            sb.append("─".repeat(55)).append("\n");

            simArea.append(sb.toString());
            simArea.setCaretPosition(simArea.getDocument().getLength());

            timerLabel.setText("⏱️ Активно. Тактів: " + simulationTick);
            settlementListModel.clear();
            settlements.forEach(settlementListModel::addElement);
        });

        startTimerBtn.addActionListener(e -> {
            try {
                int interval = Integer.parseInt(intervalField.getText().trim());
                if (interval < 500) { showError("Мінімальний інтервал: 500 мс"); return; }
                simulationTimer.setDelay(interval);
                simulationTimer.start();
                startTimerBtn.setEnabled(false);
                stopTimerBtn.setEnabled(true);
                intervalField.setEnabled(false);
                log("▶ Симуляцію запущено (інтервал: " + interval + " мс)");
            } catch (NumberFormatException ex) {
                showError("Некоректний інтервал: введіть число в мілісекундах.");
            }
        });

        stopTimerBtn.addActionListener(e -> {
            simulationTimer.stop();
            startTimerBtn.setEnabled(true);
            stopTimerBtn.setEnabled(false);
            intervalField.setEnabled(true);
            timerLabel.setText("⏱️ Зупинено. Тактів: " + simulationTick);
            log("⏹ Симуляцію зупинено. Всього тактів: " + simulationTick);
        });

        clearBtn.addActionListener(e -> {
            simArea.setText("");
            simulationTick = 0;
            timerLabel.setText("⏱️ Таймер зупинено. Тактів: 0");
        });

        btnPanel.add(startTimerBtn);
        btnPanel.add(stopTimerBtn);
        btnPanel.add(clearBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(settingsPanel, BorderLayout.NORTH);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(simScroll, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildLambdaTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JTextArea resArea = new JTextArea();
        resArea.setEditable(false);
        resArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(resArea);
        scroll.setBorder(BorderFactory.createTitledBorder("Результат виконання"));

        JPanel btnPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        btnPanel.setBorder(BorderFactory.createTitledBorder("Демонстраційні приклади"));

        JButton btn1 = new JButton("1. Анонімний клас");
        JButton btn2 = new JButton("2. Лямбда-вираз");
        JButton btn3 = new JButton("3. Посилання на метод");
        JButton btn4 = new JButton("4. Лямбда + сортування");
        JButton btn5 = new JButton("5. Посилання на статику");
        JButton btn6 = new JButton("6. Порівняння усіх");

        styleButton(btn1, new Color(180, 100, 50));
        styleButton(btn2, new Color(70, 130, 200));
        styleButton(btn3, new Color(70, 160, 70));
        styleButton(btn4, new Color(150, 50, 180));
        styleButton(btn5, new Color(50, 160, 160));
        styleButton(btn6, new Color(200, 160, 30));


        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                sb.append("=== Анонімний клас (класичний підхід) ===\n\n");
                sb.append("Код:\n");
                sb.append("  btn.addActionListener(new ActionListener() {\n");
                sb.append("      public void actionPerformed(ActionEvent e) {\n");
                sb.append("          // дія...\n      }\n  });\n\n");

                Comparator<Settlement> cmpByName = new Comparator<Settlement>() {
                    @Override
                    public int compare(Settlement a, Settlement b) {
                        return a.getName().compareTo(b.getName());
                    }
                };
                Settlement[] arr = settlements.toArray(new Settlement[0]);
                Arrays.sort(arr, cmpByName);
                sb.append("Сортування за назвою (анонімний Comparator):\n");
                for (Settlement s : arr) sb.append("  ").append(s).append("\n");
                resArea.setText(sb.toString());
                log("🔬 Демо: Анонімний клас");
            }
        });

        btn2.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Лямбда-вираз ===\n\n");
            sb.append("Код:\n");
            sb.append("  btn.addActionListener(e -> { ... });\n\n");

            Comparator<Settlement> cmpByPop = (a, b) -> Integer.compare(a.getPopulation(), b.getPopulation());
            Settlement[] arr = settlements.toArray(new Settlement[0]);
            Arrays.sort(arr, cmpByPop);
            sb.append("Сортування за населенням (лямбда Comparator):\n");
            for (Settlement s : arr) sb.append("  ").append(s).append("\n");

            sb.append("\n💡 Переваги лямбд:\n");
            sb.append("  ✔ Компактний синтаксис\n");
            sb.append("  ✔ Читабельність для простих операцій\n");
            sb.append("  ✔ Легко інлайнити\n");
            sb.append("  ✘ Складні логіки важче дебажити\n");
            resArea.setText(sb.toString());
            log("🔬 Демо: Лямбда-вираз");
        });

        btn3.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Посилання на метод ===\n\n");
            sb.append("Код:\n");
            sb.append("  settlements.forEach(System.out::println);\n");
            sb.append("  // або: arr.sort(Settlement::compareTo)\n\n");

            Settlement[] arr = settlements.toArray(new Settlement[0]);
            Arrays.sort(arr, Settlement::compareTo);
            sb.append("Сортування через Settlement::compareTo (по населенню):\n");
            for (Settlement s : arr) sb.append("  ").append(s).append("\n");

            sb.append("\n💡 Переваги посилань на метод:\n");
            sb.append("  ✔ Найкомпактніший запис\n");
            sb.append("  ✔ Чітко показує, який метод викликається\n");
            sb.append("  ✔ Перевикористання існуючих методів\n");
            sb.append("  ✘ Менш гнучко, ніж лямбда\n");
            resArea.setText(sb.toString());
            log("🔬 Демо: Посилання на метод");
        });

        btn4.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Лямбда + багатокритерійне сортування ===\n\n");
            Settlement[] arr = settlements.toArray(new Settlement[0]);

            Arrays.sort(arr, (a, b) -> {
                int typeCmp = a.getType().compareTo(b.getType());
                return typeCmp != 0 ? typeCmp : a.getName().compareTo(b.getName());
            });

            sb.append("Сортування: тип → назва:\n");
            for (Settlement s : arr) {
                sb.append(String.format("  [%-12s] %s%n", s.getType(), s.getName()));
            }
            resArea.setText(sb.toString());
            log("🔬 Демо: Лямбда + багатокритерійне сортування");
        });

        btn5.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Посилання на статичний метод ===\n\n");
            sb.append("Код:\n");
            sb.append("  List<Integer> pops = new ArrayList<>();\n");
            sb.append("  settlements.forEach(s -> pops.add(s.getPopulation()));\n\n");

            List<Integer> pops = new ArrayList<>();
            settlements.forEach(s -> pops.add(s.getPopulation()));

            int max = pops.stream().mapToInt(Integer::intValue).max().orElse(0);
            int min = pops.stream().mapToInt(Integer::intValue).min().orElse(0);
            double avg = pops.stream().mapToInt(Integer::intValue).average().orElse(0);

            sb.append("Населення по містах:\n");
            settlements.forEach(s ->
                    sb.append(String.format("  %-18s: %,d%n", s.getName(), s.getPopulation())));
            sb.append(String.format("%nМакс: %,d | Мін: %,d | Сер: %,.0f%n", max, min, avg));

            sb.append("\n💡 Integer::intValue — посилання на метод екземпляру класу Integer\n");
            resArea.setText(sb.toString());
            log("🔬 Демо: Посилання на статичний метод + Stream");
        });

        btn6.addActionListener(e -> {
            resArea.setText(
                    "=== Порівняння способів реалізації обробників подій ===\n\n" +
                            "┌─────────────────────┬───────────┬───────────┬───────────────────┐\n" +
                            "│ Критерій            │ Анонімний │  Лямбда   │ Посилання на метод│\n" +
                            "├─────────────────────┼───────────┼───────────┼───────────────────┤\n" +
                            "│ Стислість коду      │    ✘      │    ✔✔     │       ✔✔✔         │\n" +
                            "│ Читабельність       │    ✔✔     │    ✔✔✔    │       ✔✔✔         │\n" +
                            "│ Підтримка Java      │  1.1+     │   8.0+    │       8.0+        │\n" +
                            "│ Доступ до this      │  ✔ (свій) │  ✔ (зовн) │    ✔ (зовн)      │\n" +
                            "│ Перевикористання    │    ✘      │    ✘      │       ✔✔          │\n" +
                            "│ Гнучкість логіки    │    ✔✔✔    │    ✔✔     │       ✔           │\n" +
                            "│ Дебагінг            │    ✔✔     │    ✔      │       ✔✔          │\n" +
                            "└─────────────────────┴───────────┴───────────┴───────────────────┘\n\n" +
                            "📌 Висновки:\n" +
                            "  • Анонімні класи — підходять для складної логіки з кількома методами\n" +
                            "    або коли потрібен окремий this-контекст.\n\n" +
                            "  • Лямбда-вирази — оптимальні для функціональних інтерфейсів\n" +
                            "    (ActionListener, Comparator, Runnable). Підвищують читабельність.\n\n" +
                            "  • Посилання на методи — найкомпактніший варіант коли логіка\n" +
                            "    вже реалізована в існуючому методі класу.\n\n" +
                            "  Рекомендація: використовувати лямбди як основний підхід,\n" +
                            "  анонімні класи — лише для складних багатометодних інтерфейсів.\n"
            );
            log("🔬 Демо: Порівняльна таблиця");
        });

        btnPanel.add(btn1);
        btnPanel.add(btn2);
        btnPanel.add(btn3);
        btnPanel.add(btn4);
        btnPanel.add(btn5);
        btnPanel.add(btn6);

        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void showSettlementDialog(Settlement existing) {
        boolean isEdit = existing != null;
        JDialog dialog = new JDialog(this, isEdit ? "Редагувати населений пункт" : "Додати населений пункт", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(8, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        JTextField nameField      = new JTextField(isEdit ? existing.getName() : "");
        JTextField populationField= new JTextField(isEdit ? String.valueOf(existing.getPopulation()) : "");
        JTextField areaField      = new JTextField(isEdit ? String.valueOf(existing.getArea()) : "");
        JComboBox<SettlementType> typeBox = new JComboBox<>(SettlementType.values());
        if (isEdit) typeBox.setSelectedItem(existing.getSettlementType());
        JTextField yearField      = new JTextField(isEdit ? String.valueOf(existing.getFoundedYear()) : "");
        JTextField latField       = new JTextField(isEdit ? String.valueOf(existing.getLatitude()) : "");
        JTextField lonField       = new JTextField(isEdit ? String.valueOf(existing.getLongitude()) : "");

        form.add(new JLabel("Назва:")); form.add(nameField);
        form.add(new JLabel("Населення:")); form.add(populationField);
        form.add(new JLabel("Площа (км²):")); form.add(areaField);
        form.add(new JLabel("Тип:")); form.add(typeBox);
        form.add(new JLabel("Рік заснування:")); form.add(yearField);
        form.add(new JLabel("Широта:")); form.add(latField);
        form.add(new JLabel("Довгота:")); form.add(lonField);

        JButton saveBtn = new JButton(isEdit ? "💾 Зберегти" : "➕ Додати");
        JButton cancelBtn = new JButton("❌ Скасувати");
        styleButton(saveBtn, new Color(70, 160, 70));

        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) throw new IllegalArgumentException("Назва не може бути порожньою!");

                int pop = Integer.parseInt(populationField.getText().trim());
                if (pop < MIN_POPULATION || pop > MAX_POPULATION)
                    throw new IllegalArgumentException("Населення: від " + MIN_POPULATION + " до " + MAX_POPULATION);

                double area = Double.parseDouble(areaField.getText().trim());
                if (area < MIN_AREA || area > MAX_AREA)
                    throw new IllegalArgumentException("Площа: від " + MIN_AREA + " до " + MAX_AREA);

                int year = Integer.parseInt(yearField.getText().trim());
                if (year < MIN_YEAR || year > MAX_YEAR)
                    throw new IllegalArgumentException("Рік: від " + MIN_YEAR + " до " + MAX_YEAR);

                double lat = Double.parseDouble(latField.getText().trim());
                if (lat < -90 || lat > 90)
                    throw new IllegalArgumentException("Широта: від -90 до 90");

                double lon = Double.parseDouble(lonField.getText().trim());
                if (lon < -180 || lon > 180)
                    throw new IllegalArgumentException("Довгота: від -180 до 180");

                SettlementType type = (SettlementType) typeBox.getSelectedItem();

                if (isEdit) {
                    existing.setName(name);
                    existing.setPopulation(pop);
                    existing.setArea(area);
                    existing.setType(type);
                    existing.setFoundedYear(year);
                    existing.setLatitude(lat);
                    existing.setLongitude(lon);
                    refreshSettlementList();
                    log("✏️ Оновлено: " + name);
                } else {
                    Settlement s = new Settlement(name, pop, area,
                            type.getDescription(), year, lat, lon);
                    settlements.add(s);
                    settlementListModel.addElement(s);
                    log("➕ Додано: " + name + " (нас.: " + pop + ")");
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showError("Введіть коректні числові значення!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showCrossroadDialog(Crossroad existing) {
        boolean isEdit = existing != null;
        JDialog dialog = new JDialog(this, isEdit ? "Редагувати перехрестя" : "Додати перехрестя", true);
        dialog.setSize(430, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(7, 2, 6, 6));
        form.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));

        JTextField nameField    = new JTextField(isEdit ? existing.getCrossroadName() : "");
        JTextField streetsField = new JTextField(isEdit ? String.join(", ", existing.getStreets()) : "");
        JTextField latField     = new JTextField(isEdit ? String.valueOf(existing.getLatitude()) : "");
        JTextField lonField     = new JTextField(isEdit ? String.valueOf(existing.getLongitude()) : "");
        JCheckBox regBox        = new JCheckBox("", isEdit && existing.isRegulated());
        JTextField vehField     = new JTextField(isEdit ? String.valueOf(existing.getStats().getVehiclesPerHour()) : "0");
        JTextField accField     = new JTextField(isEdit ? String.valueOf(existing.getStats().getAccidentsPerYear()) : "0");

        form.add(new JLabel("Назва перехрестя:"));
        form.add(nameField);
        form.add(new JLabel("Вулиці (через кому):"));
        form.add(streetsField);
        form.add(new JLabel("Широта:"));
        form.add(latField);
        form.add(new JLabel("Довгота:"));
        form.add(lonField);
        form.add(new JLabel("Регульоване:"));
        form.add(regBox);
        form.add(new JLabel("Авто/год:"));
        form.add(vehField);
        form.add(new JLabel("ДТП/рік:"));
        form.add(accField);

        JButton saveBtn = new JButton(isEdit ? "💾 Зберегти" : "➕ Додати");
        JButton cancelBtn = new JButton("❌ Скасувати");
        styleButton(saveBtn, new Color(70, 160, 70));

        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) throw new IllegalArgumentException("Назва не може бути порожньою!");

                String[] streets = Arrays.stream(streetsField.getText().split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);
                if (streets.length < 2)
                    throw new IllegalArgumentException("Вкажіть щонайменше 2 вулиці!");

                double lat = Double.parseDouble(latField.getText().trim());
                double lon = Double.parseDouble(lonField.getText().trim());
                boolean reg = regBox.isSelected();
                int veh = Integer.parseInt(vehField.getText().trim());
                int acc = Integer.parseInt(accField.getText().trim());
                if (veh < 0 || acc < 0)
                    throw new IllegalArgumentException("Статистика не може бути від'ємною!");

                if (isEdit) {
                    existing.setCrossroadName(name);
                    existing.setStreets(streets);
                    existing.setLatitude(lat);
                    existing.setLongitude(lon);
                    existing.setRegulated(reg);
                    existing.getStats().setVehiclesPerHour(veh);
                    existing.getStats().setAccidentsPerYear(acc);
                    refreshCrossroadList();
                    log("✏️ Оновлено перехрестя: " + name);
                } else {
                    Crossroad c = new Crossroad(name, streets, lat, lon, reg, veh, acc, 0);
                    crossroads.add(c);
                    crossroadListModel.addElement(c);
                    log("➕ Додано перехрестя: " + name);
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showError("Введіть коректні числові значення!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showSimulationResults() {
        if (settlements.isEmpty()) { showError("Немає населених пунктів для аналізу."); return; }

        StringBuilder sb = new StringBuilder();
        sb.append("════ РЕЗУЛЬТАТИ МОДЕЛЮВАННЯ ════\n\n");
        sb.append(String.format("Всього населених пунктів: %d%n", settlements.size()));
        sb.append(String.format("Всього перехресть: %d%n%n", crossroads.size()));

        long totalPop = settlements.stream().mapToLong(Settlement::getPopulation).sum();
        double totalArea = settlements.stream().mapToDouble(Settlement::getArea).sum();
        Settlement largest = settlements.stream()
                .max(Comparator.comparingInt(Settlement::getPopulation)).orElse(null);
        Settlement smallest = settlements.stream()
                .min(Comparator.comparingInt(Settlement::getPopulation)).orElse(null);

        sb.append(String.format("Загальне населення: %,d осіб%n", totalPop));
        sb.append(String.format("Загальна площа: %.2f км²%n", totalArea));
        if (largest != null)
            sb.append(String.format("Найбільший: %s (%,d осіб)%n", largest.getName(), largest.getPopulation()));
        if (smallest != null)
            sb.append(String.format("Найменший: %s (%,d осіб)%n", smallest.getName(), smallest.getPopulation()));

        sb.append("\n════ ПЕРЕХРЕСТЯ ════\n");
        long regulated = crossroads.stream().filter(Crossroad::isRegulated).count();
        sb.append(String.format("Регульованих: %d | Нерегульованих: %d%n",
                regulated, crossroads.size() - regulated));

        crossroads.stream()
                .max(Comparator.comparingDouble(Crossroad::getDangerIndex))
                .ifPresent(c -> sb.append(String.format(
                        "Найнебезпечніше: %s (індекс: %.2f)%n",
                        c.getCrossroadName(), c.getDangerIndex())));

        JOptionPane.showMessageDialog(this, sb.toString(), "Результати", JOptionPane.INFORMATION_MESSAGE);
        log("📊 Переглянуто результати симуляції");
    }

    private void initDemoData() {
        settlements.add(new Settlement("Київ", 2967360, 839.0, "Столиця", 482, 50.4501, 30.5234));
        settlements.add(new Settlement("Харків", 1421125, 350.0, "Велике місто", 1654, 49.9935, 36.2304));
        settlements.add(new Settlement("Одеса", 1010783, 162.0, "Велике місто", 1794, 46.4825, 30.7233));
        settlements.add(new Settlement("Біла Церква", 201861, 66.3, "Місто", 1032, 49.7990, 30.1170));
        settlements.add(new Settlement("Васильків", 37000, 22.5, "Місто", 988, 50.1768, 30.3194));

        crossroads.add(new Crossroad("Хрещатик / Бессарабська",
                new String[]{"Хрещатик", "Бессарабська пл."}, 50.4444, 30.5206, true, 3200, 5, 1800));
        crossroads.add(new Crossroad("Велика Васильківська / Льва Толстого",
                new String[]{"Велика Васильківська", "Льва Толстого"}, 50.4319, 30.5172, true, 2800, 3, 1500));
        crossroads.add(new Crossroad("Проспект Науки / пр. Леніна (Харків)",
                new String[]{"Пр. Науки", "Пр. Незалежності"}, 49.9960, 36.2305, false, 1800, 8, 900));
    }

    private void refreshSettlementList() {
        settlementListModel.clear();
        settlements.forEach(settlementListModel::addElement);
    }

    private void refreshCrossroadList() {
        crossroadListModel.clear();
        crossroads.forEach(crossroadListModel::addElement);
    }

    private void log(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + time + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }
}