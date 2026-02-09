
package GUIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CalculatorPanel extends JPanel {
    private JTextField display;
    private JLabel resultLabel;

    private double result = 0;
    private String operator = "";
    private boolean startNewNumber = true;
    private boolean isProcessing = false; // Prevent double input
    private boolean isCalculatorActive = false; // Calculator mode lock/unlock

    // Modern color palette
    private static final Color BACKGROUND_COLOR = new Color(30, 30, 35);
    private static final Color DISPLAY_BG = new Color(45, 45, 50);
    private static final Color NUMBER_BTN = new Color(60, 60, 65);
    private static final Color NUMBER_BTN_HOVER = new Color(75, 75, 80);
    private static final Color OPERATOR_BTN = new Color(100, 180, 80);
    private static final Color OPERATOR_BTN_HOVER = new Color(120, 200, 100);
    private static final Color SPECIAL_BTN = new Color(80, 80, 90);
    private static final Color SPECIAL_BTN_HOVER = new Color(100, 100, 110);
    private static final Color EQUALS_BTN = new Color(255, 149, 0);
    private static final Color EQUALS_BTN_HOVER = new Color(255, 169, 30);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color SECONDARY_TEXT = new Color(180, 180, 180);

    public CalculatorPanel() {
        setPreferredSize(new Dimension(420, 520));
        setLayout(new BorderLayout(0, 15));
        setBackground(BACKGROUND_COLOR);
        updateBorder(); // Set initial border based on active state

        // Create display panel
        JPanel displayPanel = createDisplayPanel();
        add(displayPanel, BorderLayout.NORTH);

        // Create buttons panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.CENTER);

        // Add keyboard support
        setupKeyboardSupport();

        // Make panel focusable to receive keyboard events
        setFocusable(true);
    }

    private void updateBorder() {
        if (isCalculatorActive) {
            // Active: Green border to indicate calculator mode is ON
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(100, 200, 100), 3),
                    new EmptyBorder(17, 17, 17, 17)));
        } else {
            // Inactive: Normal border
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        repaint();
    }

    private void setupKeyboardSupport() {
        // Use InputMap and ActionMap instead of KeyListener for better embedded support
        // WHEN_IN_FOCUSED_WINDOW allows keys to work when the parent window has focus
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // F12 key to ACTIVATE/TOGGLE calculator mode
        inputMap.put(KeyStroke.getKeyStroke("F12"), "activateCalculator");
        actionMap.put("activateCalculator", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCalculatorActive = !isCalculatorActive; // Toggle for easier access
                updateBorder();
                if (isCalculatorActive) {
                    requestFocusInWindow();
                }
            }
        });

        // Number keys (0-9) - both regular and numpad
        // Only work when calculator is ACTIVE
        for (int i = 0; i <= 9; i++) {
            final String num = String.valueOf(i);
            // Regular number keys
            inputMap.put(KeyStroke.getKeyStroke(String.valueOf(i).charAt(0)), "number" + i);
            // Numpad keys
            inputMap.put(KeyStroke.getKeyStroke("NUMPAD" + i), "number" + i);
            actionMap.put("number" + i, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isCalculatorActive) {
                        processInput(num);
                    }
                }
            });
        }

        // Decimal point
        inputMap.put(KeyStroke.getKeyStroke('.'), "decimal");
        inputMap.put(KeyStroke.getKeyStroke("DECIMAL"), "decimal");
        actionMap.put("decimal", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput(".");
                }
            }
        });

        // Operators - numpad
        setupOperatorKey(inputMap, actionMap, "ADD", "add", "+");
        setupOperatorKey(inputMap, actionMap, "SUBTRACT", "subtract", "-");
        setupOperatorKey(inputMap, actionMap, "MULTIPLY", "multiply", "*");
        setupOperatorKey(inputMap, actionMap, "DIVIDE", "divide", "/");

        // Operators - regular keyboard
        inputMap.put(KeyStroke.getKeyStroke("shift 8"), "multiply2");
        actionMap.put("multiply2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("*");
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("shift EQUALS"), "plus2");
        actionMap.put("plus2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("+");
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("SLASH"), "divide2");
        actionMap.put("divide2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("/");
                }
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("MINUS"), "minus2");
        actionMap.put("minus2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("-");
                }
            }
        });

        // Percentage
        inputMap.put(KeyStroke.getKeyStroke("shift 5"), "percent");
        actionMap.put("percent", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("%");
                }
            }
        });

        // Equals
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "equals");
        inputMap.put(KeyStroke.getKeyStroke("EQUALS"), "equals2");
        actionMap.put("equals", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("=");
                }
            }
        });
        actionMap.put("equals2", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("=");
                }
            }
        });

        // ESC to DEACTIVATE calculator mode (returns keyboard to dashboard)
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "deactivate");
        actionMap.put("deactivate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isCalculatorActive = false;
                updateBorder();
            }
        });

        // DELETE to clear calculator (only when active)
        inputMap.put(KeyStroke.getKeyStroke("DELETE"), "clear");
        actionMap.put("clear", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("C");
                }
            }
        });

        // Backspace - only works when calculator is active
        inputMap.put(KeyStroke.getKeyStroke("BACK_SPACE"), "backspace");
        actionMap.put("backspace", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput("⌫");
                }
            }
        });
    }

    private void setupOperatorKey(InputMap inputMap, ActionMap actionMap,
            String keyName, String actionName, String operator) {
        inputMap.put(KeyStroke.getKeyStroke(keyName), actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isCalculatorActive) {
                    processInput(operator);
                }
            }
        });
    }

    private void processInput(String cmd) {
        // Prevent double input from simultaneous keyboard/button events
        if (isProcessing) {
            return;
        }

        isProcessing = true;
        try {
            // Simulate button click for the given command
            ButtonClickListener listener = new ButtonClickListener();
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, cmd);
            listener.actionPerformed(event);
        } finally {
            // Always reset the flag
            SwingUtilities.invokeLater(() -> isProcessing = false);
        }
    }

    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBackground(DISPLAY_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 75), 1),
                new EmptyBorder(15, 20, 15, 20)));

        // Result label (shows previous calculation)
        resultLabel = new JLabel(" ");
        resultLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        resultLabel.setForeground(SECONDARY_TEXT);
        resultLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(resultLabel, BorderLayout.NORTH);

        // Main display
        display = new JTextField("0");
        display.setFont(new Font("Segoe UI", Font.BOLD, 42));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setFocusable(false); // Prevent display from consuming keyboard events
        display.setBackground(DISPLAY_BG);
        display.setForeground(TEXT_COLOR);
        display.setBorder(null);
        display.setCaretColor(TEXT_COLOR);
        panel.add(display, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 12, 12));
        panel.setBackground(BACKGROUND_COLOR);

        String[][] buttons = {
                { "C", "±", "%", "/" },
                { "7", "8", "9", "*" },
                { "4", "5", "6", "-" },
                { "1", "2", "3", "+" },
                { "0", ".", "=", "⌫" }
        };

        for (String[] row : buttons) {
            for (String text : row) {
                if (text.equals("")) {
                    JPanel emptyPanel = new JPanel();
                    emptyPanel.setBackground(BACKGROUND_COLOR);
                    panel.add(emptyPanel);
                    continue;
                }

                JButton button = createModernButton(text);
                button.addActionListener(new ButtonClickListener());

                // Transfer focus back to panel after button click to prevent double input
                button.addActionListener(e -> {
                    CalculatorPanel.this.requestFocusInWindow();
                });

                panel.add(button);
            }
        }

        return panel;
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Determine button color based on type
                Color bgColor = getButtonColor(text, false);
                Color hoverColor = getButtonColor(text, true);

                // Use hover color if mouse is over button
                if (getModel().isRollover()) {
                    bgColor = hoverColor;
                }

                // Draw rounded rectangle background
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Add subtle shadow/border
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 24));
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    private Color getButtonColor(String text, boolean hover) {
        // Equals button
        if (text.equals("=")) {
            return hover ? EQUALS_BTN_HOVER : EQUALS_BTN;
        }
        // Operator buttons
        else if ("/*-+".contains(text)) {
            return hover ? OPERATOR_BTN_HOVER : OPERATOR_BTN;
        }
        // Special function buttons (including backspace)
        else if ("C±%⌫".contains(text)) {
            return hover ? SPECIAL_BTN_HOVER : SPECIAL_BTN;
        }
        // Number buttons
        else {
            return hover ? NUMBER_BTN_HOVER : NUMBER_BTN;
        }
    }

    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            if ("0123456789.".contains(cmd)) {
                if (startNewNumber) {
                    display.setText(cmd.equals(".") ? "0." : cmd);
                    startNewNumber = false;
                } else {
                    String currentText = display.getText();

                    // Don't allow multiple decimal points
                    if (cmd.equals(".") && currentText.contains(".")) {
                        return;
                    }

                    // Replace leading zero with new digit (except for decimals)
                    if (currentText.equals("0") && !cmd.equals(".")) {
                        display.setText(cmd);
                    } else {
                        display.setText(currentText + cmd);
                    }
                }
            } else if (cmd.equals("C")) {
                display.setText("0");
                resultLabel.setText(" ");
                operator = "";
                result = 0;
                startNewNumber = true;
            } else if (cmd.equals("⌫")) {
                // Backspace - delete last character
                String currentText = display.getText();
                if (currentText.length() > 1) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    display.setText("0");
                }
            } else if (cmd.equals("±")) {
                double val = Double.parseDouble(display.getText());
                display.setText(formatNumber(val * -1));
            } else if (cmd.equals("%")) {
                double val = Double.parseDouble(display.getText());
                display.setText(formatNumber(val / 100));
            } else if (cmd.equals("=")) {
                if (!operator.isEmpty()) {
                    resultLabel.setText(String.format("%s %s %s =",
                            formatNumber(result), operator, display.getText()));
                    calculate(Double.parseDouble(display.getText()));
                } else {
                    // No operation, just keep current display
                    resultLabel.setText(display.getText() + " =");
                }
                operator = "";
                startNewNumber = true;
            } else { // operator
                if (!operator.isEmpty()) {
                    calculate(Double.parseDouble(display.getText()));
                } else {
                    result = Double.parseDouble(display.getText());
                }
                resultLabel.setText(String.format("%s %s", formatNumber(result), cmd));
                operator = cmd;
                startNewNumber = true;
            }
        }

        private void calculate(double number) {
            switch (operator) {
                case "+":
                    result += number;
                    break;
                case "-":
                    result -= number;
                    break;
                case "*":
                    result *= number;
                    break;
                case "/":
                    if (number != 0) {
                        result /= number;
                    } else {
                        // Modern error dialog
                        JOptionPane.showMessageDialog(CalculatorPanel.this,
                                "Cannot divide by zero",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        result = 0;
                    }
                    break;
                default:
                    result = number;
            }
            display.setText(formatNumber(result));
        }

        private String formatNumber(double num) {
            // Remove unnecessary decimal places
            if (num == (long) num) {
                return String.format("%d", (long) num);
            } else {
                return String.format("%.8f", num).replaceAll("0*$", "").replaceAll("\\.$", "");
            }
        }
    }

    // For testing standalone
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modern Calculator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setBackground(new Color(30, 30, 35));
            frame.add(new CalculatorPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
