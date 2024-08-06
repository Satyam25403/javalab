import java.awt.*;
import javax.swing.*;

public class CounterApp extends JFrame {
    private JLabel label;
    private JTextField textField;
    private JRadioButton upButton;
    private JRadioButton downButton;
    private JComboBox<Integer> stepSizeComboBox;
    private JButton updateButton;
    private int count = 0;

    public CounterApp() {

        setTitle("Counter Application");
        setLayout(new FlowLayout());
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label = new JLabel("Counter:");
        textField = new JTextField("0", 10);
        textField.setEditable(false);

        upButton = new JRadioButton("Up");
        upButton.setSelected(true);
        downButton = new JRadioButton("Down");

        ButtonGroup group = new ButtonGroup();
        group.add(upButton);
        group.add(downButton);

        
        Integer[] steps = {1, 2, 5, 10};
        stepSizeComboBox = new JComboBox<>(steps);

        updateButton = new JButton("Count");
        updateButton.addActionListener(e -> updateCount());

        add(label);
        add(textField);
        add(upButton);
        add(downButton);
        add(stepSizeComboBox);
        add(updateButton);

        setVisible(true);
    }

    private void updateCount() {
        int stepSize = (Integer) stepSizeComboBox.getSelectedItem();
        if (upButton.isSelected()) {
            count += stepSize;
        } else if (downButton.isSelected()) {
            count -= stepSize;
        }
        textField.setText(String.valueOf(count));
    }

    public static void main(String[] args) {
        new CounterApp();
    }
}