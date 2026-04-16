package lab6;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SettlementAnimationFrame extends JFrame {
    private List<Settlement> settlements;
    private SettlementMapPanel mapPanel;
    private static Timer animTimer;
    private int tick = 0;
    private final Random rnd = new Random(42);

    private JLabel statusLabel;
    private JButton btnStart, btnPause, btnStop;
    private JTextArea logArea;
    private JSlider speedSlider;

    public SettlementAnimationFrame() {
        super("Анімація моделі населених пунктів — Лаб. 6");
        settlements = createDemoData();
        mapPanel    = new SettlementMapPanel(settlements);
        setupUI();
        animTimer = new Timer(600, e -> doAnimationStep());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack(); setLocationRelativeTo(null);
    }

    private void setupUI() {
        setLayout(new BorderLayout(4,4));
        add(mapPanel, BorderLayout.CENTER);
        add(buildControlPanel(), BorderLayout.NORTH);
        logArea = new JTextArea(5,50);
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced",Font.PLAIN,11));
        JScrollPane sp = new JScrollPane(logArea);
        sp.setBorder(BorderFactory.createTitledBorder("Журнал подій"));
        add(sp, BorderLayout.SOUTH);
    }

    private JPanel buildControlPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
        p.setBorder(BorderFactory.createTitledBorder("Управління анімацією"));
        btnStart = styledBtn("▶ Старт",  new Color(60,160,60));
        btnPause = styledBtn("⏸ Пауза",  new Color(200,150,0));
        btnStop  = styledBtn("⏹ Зупинити",new Color(180,50,50));
        btnPause.setEnabled(false); btnStop.setEnabled(false);
        btnStart.addActionListener(e -> onStart());
        btnPause.addActionListener(e -> onPause());
        btnStop .addActionListener(e -> onStop());
        speedSlider = new JSlider(100,2000,600);
        speedSlider.setInverted(true);
        speedSlider.setPreferredSize(new Dimension(120,30));
        speedSlider.addChangeListener(e->animTimer.setDelay(speedSlider.getValue()));
        statusLabel = new JLabel("Стан: зупинено | Крок: 0");
        p.add(btnStart); p.add(btnPause); p.add(btnStop);
        p.add(new JLabel("  Швидкість:"));
        p.add(new JLabel("Пов.")); p.add(speedSlider); p.add(new JLabel("Шв."));
        p.add(Box.createHorizontalStrut(15)); p.add(statusLabel);
        return p;
    }

    private JButton styledBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
        return b;
    }

    private void onStart() {
        animTimer.start();
        btnStart.setEnabled(false); btnPause.setEnabled(true);
        btnStop.setEnabled(true);
        log("▶ Анімацію запущено");
    }
    private void onPause() {
        animTimer.stop();
        btnStart.setEnabled(true); btnPause.setEnabled(false);
        statusLabel.setText("Стан: пауза | Крок: "+tick);
        log("⏸ Пауза на кроці "+tick);
    }
    private void onStop() {
        animTimer.stop(); tick = 0;
        settlements = createDemoData();
        remove(mapPanel);
        mapPanel = new SettlementMapPanel(settlements);
        add(mapPanel, BorderLayout.CENTER);
        revalidate(); repaint();
        btnStart.setEnabled(true); btnPause.setEnabled(false);
        btnStop.setEnabled(false);
        statusLabel.setText("Стан: зупинено | Крок: 0");
        log("⏹ Зупинено і скинуто");
    }

    private void doAnimationStep() {
        tick++;
        for (Settlement s : settlements) {
            double pct = rnd.nextGaussian() * 0.3;
            int delta = (int)(s.getPopulation() * pct / 100.0);
            try {
                if (delta > 0) s.increasePopulation(delta);
                else if (delta < 0) s.decreasePopulation(-delta);
            } catch (IllegalArgumentException ignored) {}
            if (rnd.nextDouble() < 0.04 && s.getCrossroadCount() < 20) {
                String[] streets = {
                    "Вул. " + (char)('А' + rnd.nextInt(10)),
                    "Вул. " + (char)('К' + rnd.nextInt(10))
                };
                Crossroad nc = new Crossroad(
                    "Перехрестя-" + tick, streets,
                    s.getLatitude()  + rnd.nextGaussian()*0.002,
                    s.getLongitude() + rnd.nextGaussian()*0.002,
                    rnd.nextBoolean(),
                    rnd.nextInt(3000)+200, rnd.nextInt(15), rnd.nextInt(1500));
                s.addCrossroad(nc);
                log("Крок "+tick+": нове перехрестя в "+s.getName());
            }
        }
        mapPanel.repaint();
        statusLabel.setText("Стан: виконується | Крок: "+tick);
        if (tick % 5 == 0)
            settlements.stream()
                .max(Comparator.comparingInt(Settlement::getPopulation))
                .ifPresent(s -> log("Крок "+tick+": лідер — "+s.getName()
                    +" ("+String.format("%,d",s.getPopulation())+" ос.)"));
    }

    private void log(String msg) {
        logArea.append(msg+"\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private List<Settlement> createDemoData() {
        List<Settlement> list = new ArrayList<>();
        list.add(new Settlement("Київ",        2967360, 839.0, "Столиця",      482,  50.4501, 30.5234));
        list.add(new Settlement("Харків",      1421125, 350.0, "Велике місто", 1654, 49.9935, 36.2304));
        list.add(new Settlement("Одеса",       1010783, 162.0, "Велике місто", 1794, 46.4825, 30.7233));
        list.add(new Settlement("Дніпро",       993703, 405.0, "Велике місто", 1776, 48.4500, 34.9981));
        list.add(new Settlement("Запоріжжя",    722713, 334.0, "Велике місто", 1770, 47.8388, 35.1396));
        list.add(new Settlement("Львів",        717803, 182.0, "Велике місто", 1256, 49.8397, 24.0297));

        list.add(new Settlement("Біла Церква",  201861, 66.3,"Місто", 1032, 49.2000, 28.8000));
        list.add(new Settlement("Васильків",     37000, 22.5,"Місто",  988, 49.5000, 29.2000));
        list.add(new Settlement("Узин",          12500,  8.1,"Місто", 1400, 48.8000, 28.4000));

        list.add(new Settlement("Ходосівка",     2800,   4.2, "Село", 1650, 49.0500, 31.9000));
        list.add(new Settlement("Григорівка",    1200,   2.1, "Село", 1820, 49.5500, 36.8000));
        list.add(new Settlement("Шевченкове",    3500,   5.0, "Село", 1780, 48.5000, 32.3000));
        list.add(new Settlement("Малютянка",      980,   1.8, "Село", 1900, 47.5000, 31.5000));

        list.get(0).addCrossroad(new Crossroad("Хрещатик/Бессарабська",
                new String[]{"Хрещатик","Бессарабська пл."},
                50.4444, 30.5206, true, 3200, 5, 1800));
        list.get(0).addCrossroad(new Crossroad("Велика Васильківська/Льва Толстого",
                new String[]{"Велика Васильківська","Льва Толстого"},
                50.4319, 30.5172, true, 2800, 3, 1500));
        list.get(5).addCrossroad(new Crossroad("Проспект Свободи/Дорошенка",
                new String[]{"Проспект Свободи","Дорошенка"},
                49.8431, 24.0267, false, 1200, 6, 900));
        return list;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->new SettlementAnimationFrame().setVisible(true));
    }
}