package sh.kaden.ics4u.pizza;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A class that extends {@link JPanel} to render the topping count. This class also adds two buttons to increase and
 * decrease the amount of toppings on the pizza.
 * <p>
 * You can use the methods {@link #cost()}, {@link #count()} and {@link #toppingName()} to get the state of the topping.
 */
public final class ToppingPanel extends JPanel {

    private final PizzaShopCalculator app;
    private final String toppingName;
    private final double cost;
    private int count;

    private final JPanel titlePanel;
    private final JPanel buttonPanel;
    private final JLabel toppingLabel;
    private final JButton incrementButton;
    private final JButton decrementButton;

    /**
     * Constructs {@code ToppingPanel}.
     *
     * @param app           the app
     * @param toppingName   the topping name
     * @param defaultAmount the default amount to add to the sh.kaden.ics4u.pizza
     */
    public ToppingPanel(final PizzaShopCalculator app,
                        final String toppingName,
                        final double cost,
                        final int defaultAmount) {
        this.app = app;
        this.toppingName = toppingName;
        this.count = defaultAmount;
        this.cost = cost;

        // create title panel
        this.titlePanel = new JPanel();
        this.toppingLabel = new JLabel(this.countName() + " " + this.toppingName);
        this.titlePanel.add(this.toppingLabel);

        // create button panel
        this.buttonPanel = new JPanel();
        this.incrementButton = new JButton("+");
        this.decrementButton = new JButton("-");
        this.incrementButton.addActionListener(this::handleIncrement);
        this.decrementButton.addActionListener(this::handleDecrement);
        this.buttonPanel.add(this.incrementButton);
        this.buttonPanel.add(this.decrementButton);
        this.buttonPanel.setLayout(new GridLayout(0, 2));

        // add button  & title panels to this frame
        this.add(this.titlePanel);
        this.add(this.buttonPanel);

        // set layout
        final SpringLayout spring = new SpringLayout();
        spring.putConstraint(SpringLayout.HORIZONTAL_CENTER, this.titlePanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        spring.putConstraint(SpringLayout.HORIZONTAL_CENTER, this.buttonPanel, 0, SpringLayout.HORIZONTAL_CENTER, this);
        spring.putConstraint(SpringLayout.NORTH, this.titlePanel, 10, SpringLayout.NORTH, this);
        spring.putConstraint(SpringLayout.NORTH, this.buttonPanel, 2, SpringLayout.SOUTH, this.titlePanel);
        this.setLayout(spring);

    }

    /**
     * Returns the topping count.
     *
     * @return the topping count
     */
    public int count() {
        return this.count;
    }

    /**
     * Returns the name of this topping.
     *
     * @return the topping name
     */
    public String toppingName() {
        return this.toppingName;
    }

    /**
     * Returns the cost of this topping.
     *
     * @return the topping cost
     */
    public double cost() {
        return this.cost;
    }

    /**
     * Returns the name to give for the amount of toppings.
     *
     * @return the count name
     */
    private String countName() {
        if (this.count == 0) {
            return "no";
        } else if (this.count == 1) {
            return "regular";
        } else if (this.count == 2) {
            return "extra";
        } else if (this.count == 3) {
            return "double";
        } else if (this.count == 4) {
            return "triple";
        } else if (this.count == 5) {
            return "quadruple";
        } else if (this.count == 6) {
            return "extreme";
        } else if (this.count == 7) {
            return "mega";
        } else if (this.count == 8) {
            return "giga";
        } else if (this.count == 9) {
            return "supreme";
        } else {
            return "too much";
        }
    }

    private void handleIncrement(final ActionEvent event) {
        this.count = this.count + 1;
        this.update();
    }

    private void handleDecrement(final ActionEvent event) {
        this.count = this.count == 0 ? this.count : this.count - 1;
        this.update();
    }

    private void update() {
        this.toppingLabel.setText(this.countName() + " " + this.toppingName);
        this.app.update();
    }

}
