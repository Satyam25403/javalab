import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SwingArithmetics extends JFrame implements ActionListener {
    private JTextField num1Field, num2Field, resultField;
    private JButton[] buttons;

    public SwingArithmetics() {
        // Set up the frame
        setTitle("Swing Arithmetics");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        
        // Create components
        num1Field = new JTextField();
        num2Field = new JTextField();
        resultField = new JTextField();
        resultField.setEditable(false);

        inputPanel.add(new JLabel("Number 1:"));
        inputPanel.add(num1Field);
        inputPanel.add(new JLabel("Number 2:"));
        inputPanel.add(num2Field);
        inputPanel.add(new JLabel("Result:"));
        inputPanel.add(resultField);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2));
        String[] buttonLabels = {"+", "-", "*", "/", "%", "CLEAR"};
        buttons = new JButton[buttonLabels.length];

        for (int i = 0; i < buttonLabels.length; i++) {
            buttons[i] = createButton(buttonLabels[i]);
            buttonPanel.add(buttons[i]);
        }

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        int num1 = Integer.parseInt(num1Field.getText());
        int num2 = Integer.parseInt(num2Field.getText());
        int result = 0;
        switch (command) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
            case "%":
                result = num1 % num2;
                break;
            case "CLEAR":
                num1Field.setText("");
                num2Field.setText("");
                resultField.setText("");
                return;
        }

        resultField.setText(String.valueOf(result));
        
    }

    public static void main(String[] args) {
        new SwingArithmetics();
    }
}
