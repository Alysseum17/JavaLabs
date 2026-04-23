package lab7;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SettlementAnimationFrame().setVisible(true);
        });
    }
}
