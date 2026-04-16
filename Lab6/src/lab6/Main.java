package lab6;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Карта населених пунктів", "Гра Життя"};
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Оберіть режим запуску:",
                    "Лаб. 6",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]
            );
            if (choice == 0) new SettlementAnimationFrame().setVisible(true);
            else if (choice == 1) new ConwayFrame().setVisible(true);
        });
    }
}
