import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class MainCalcWindow {
    private JFrame window = new JFrame();
    private JTextField input = new JTextField();

    private MainCalcWindow() {    // устанавливаем параметры окна
        window.setSize(285, 405);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.white);
        window.setLayout(null);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        enter_area();       // подготавливаем окно ввода и обработчик клавиатуры
        month_button();     // подготовка кнопок

        window.setVisible(true);
    }

    private void enter_area() {
        input.setFont(new Font("Arial", Font.BOLD, 24));
        input.setBounds(16, 10, 248, 36);
        input.setBackground(Color.white);
        input.setHorizontalAlignment(JTextField.RIGHT);

        window.add(input);

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatcher());
    }

//     обработка клавиатуры
    class KeyDispatcher implements KeyEventDispatcher {
        public boolean dispatchKeyEvent(KeyEvent e) {   // аргумент - событие клавы
            if ((e.getKeyCode() == KeyEvent.VK_ENTER)  && (e.getID() == KeyEvent.KEY_PRESSED)) {   // если нажат ентер - считаем результат
                result();
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) { // если нажат эскейп - очищаем поле ввода
                input.setText("");
            }
            return false;
        }
    }

    private void month_button() {
        int num = 0;
        String[] arr = {"1", "2", "3", "С", "4", "5", "6", "*", "7", "8", "9", "-", "0", ".", "+", "/", "(", ")", "="};
        JButton[] jbutton_n = new JButton[arr.length];  // создаем массив кнопок

        for (int e = 0; e < 5; e++) {
            for (int r = 0; r < 4; r++) {
                jbutton_n[num] = new JButton();         // заполняем этот массив собственно кнопками

                // настраиваем вид кнопок, расположение
                jbutton_n[num].setFont(new Font("Arial", Font.PLAIN, 36));
                jbutton_n[num].setText(arr[num]);
                jbutton_n[num].setMargin(new Insets(0, 0, 0, 0));
                if (num < arr.length - 1) {
                    jbutton_n[num].setBounds(16 + r * 62, 55 + e * 62, 60, 60);
                } else {
                    jbutton_n[num].setBounds(16 + r * 62, 55 + e * 62, 122, 60);
                }
                jbutton_n[num].setFocusable(false);

                window.add(jbutton_n[num]);     // вешаем кнопки в окно

                // добавляем кнопкам слушателя событий
                ActionListener num_button = new GoNumListener();
                jbutton_n[num].addActionListener(num_button);

                if (num < arr.length - 1) {
                    num++;
                } else {
                    break;
                }
            }
        }
    }

    // слушатель событий кнопок
    public class GoNumListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String name = ((JButton) e.getSource()).getText();  // получаем текст с кнопки в переменную
            if (!(name.equals("=") || name.equals("С"))) {
                                            // если нажата не "=" и не "С" - добавляем в поле ввода значение кнопки
                input.setText(input.getText() + name);
            } else if (name.equals("=")) {  // если нажата "=" - считаем результат
                result();
            } else {                        // если нажата "С" - очищаем поле ввода
                input.setText("");
            }
            window.repaint();               // перерисовываем окно
        }
    }

    // подсчет результата
    private void result() {
        Calculations c = new Calculations();
        List<String> postfix = c.parse(input.getText());

        if(Calculations.isError.equals("")) {
            System.out.println(c.calculate(postfix));
            input.setText(c.calculate(postfix));
        } else {
            System.out.println(Calculations.isError);
            input.setText(Calculations.isError);
        }
    }

    // запуск программы
    public static void main(String[] args) {
        new MainCalcWindow();
    }
}