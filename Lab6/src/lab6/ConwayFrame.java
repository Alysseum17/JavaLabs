package lab6;

import javax.swing.*;
import java.awt.*;

public class ConwayFrame extends JFrame {
    private final ConwayPanel panel;
    private final Timer timer;
    private final JLabel genLabel = new JLabel("Покоління: 0");

    public ConwayFrame() {
        super("Клітинний автомат «Гра Життя» — користувацький стан");
        panel = new ConwayPanel(100, 60);
        timer = new Timer(80, e -> {
            panel.step();
            genLabel.setText("Покоління: " + panel.getGeneration());
        });

        JButton btnStart = new JButton("▶ Старт");
        JButton btnPause = new JButton("⏸ Пауза");
        JButton btnReset = new JButton("⏹ Скинути");
        btnPause.setEnabled(false);

        btnStart.addActionListener(e -> {
            timer.start();
            btnStart.setEnabled(false); btnPause.setEnabled(true);
        });
        btnPause.addActionListener(e -> {
            timer.stop();
            btnStart.setEnabled(true); btnPause.setEnabled(false);
        });
        btnReset.addActionListener(e -> {
            timer.stop(); panel.reset();
            genLabel.setText("Покоління: 0");
            btnStart.setEnabled(true); btnPause.setEnabled(false);
        });

        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.CENTER,12,6));
        ctrl.add(new JLabel("Намалюйте стан мишею →"));
        ctrl.add(btnStart); ctrl.add(btnPause); ctrl.add(btnReset);
        ctrl.add(genLabel);

        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(ctrl, BorderLayout.SOUTH);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConwayFrame().setVisible(true));
    }
}