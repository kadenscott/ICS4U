package pizza;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Main class for the Pizza Shop Calculator assignment.
 */
public class PizzaShopCalculator extends JFrame {

    private static final Topping PEPPERONI = new Topping("pepperonis", 0.3);
    private static final Topping MUSHROOM = new Topping("mushrooms", 0.3);
    private static final Topping CHEESE = new Topping("cheese", 0.3);
    private static final Topping SAUSAGE = new Topping("sausage", 0.3);
    private static final Topping ONION = new Topping("onions", 0.3);
    private static final Topping BLACK_OLIVE = new Topping("black olives", 0.3);
    private static final Topping FRESH_GARLIC = new Topping("fresh garlic", 0.3);
    private static final Topping TOMATO = new Topping("tomato", 0.3);

    private static final double LABOUR_COST = 4.00; // labour cost for 1 pizza
    private static final double STORE_COST = 2.5; // store cost for 1 pizza
    private static final double MATERIAL_MULTIPLIER = 0.5; // price per inch of diameter
    private static final Font FONT_BIG = new Font("Serif", Font.BOLD, 20);
    private static final Font FONT_SMALL = new Font("Serif", Font.BOLD, 15);
    private static final DecimalFormat COST_FORMAT = new DecimalFormat("$0.00");

    /**
     * Entrypoint for {@link PizzaShopCalculator}.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PizzaShopCalculator();
    }

    private final Map<Topping, ToppingPanel> toppings;
    private final JTable priceBreakdownTable;
    private final JPanel mainPanel;
    private final JPanel pizzaPanel;
    private final JPanel titlePanel;
    private JLabel priceLabel;
    private JLabel invalidLabel;
    private JLabel diameterLabel;
    private JTextField diameterField;

    /**
     * Constructs {@code PizzaShopCalculator}.
     */
    public PizzaShopCalculator() {
        super("Pizza Shop Calculator");
        this.toppings = new HashMap<>();
        this.initializeTopping(PEPPERONI, 1);
        this.initializeTopping(CHEESE, 1);
        this.initializeTopping(MUSHROOM, 0);
        this.initializeTopping(SAUSAGE, 0);
        this.initializeTopping(ONION, 0);
        this.initializeTopping(TOMATO, 0);

        // init window properties
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.titlePanel = this.createTitlePanel();
        this.pizzaPanel = this.createPizzaPanel();
        this.priceBreakdownTable = this.createPriceBreakdown();
        this.updatePriceBreakdown(0);

        // create main panel
        this.mainPanel = new JPanel();
        final BoxLayout layout = new BoxLayout(this.mainPanel, BoxLayout.PAGE_AXIS);
        this.mainPanel.setLayout(layout);
        this.mainPanel.add(this.titlePanel);
        this.mainPanel.add(this.pizzaPanel);
        this.mainPanel.add(this.createToppingsPanel());
        this.mainPanel.add(this.priceBreakdownTable);

        // create box layout for content pane and add panels
        final Container container = this.getContentPane();
        container.add(this.mainPanel);
    }

    /**
     * Calculates the price of a pizza.
     *
     * @param diameter the diameter of the pizza in inches
     * @return the pizza cost
     */
    private double calculatePizzaCost(final double diameter) {
        return LABOUR_COST + STORE_COST + this.calculateMaterials(diameter);
    }

    /**
     * Returns the base cost of the pizza.
     *
     * @param diameter the diameter
     * @return the base cost
     */
    private double calculateBaseCost(final double diameter) {
        return diameter * MATERIAL_MULTIPLIER;
    }

    /**
     * Calculates the cost of the pizza's materials.
     *
     * @param diameter the diameter
     * @return the pizza cost
     */
    private double calculateMaterials(final double diameter) {
        double cost = this.calculateBaseCost(diameter);

        for (final ToppingPanel toppingPanel : this.toppings.values()) {
            cost = cost + toppingPanel.count * toppingPanel.topping.cost;
        }

        return cost;
    }

    /**
     * Constructs the toppings panel.
     *
     * @return the panel
     */
    private JPanel createToppingsPanel() {
        // construct toppings panel
        final JPanel toppings = new JPanel();
        final GridLayout tLayout = new GridLayout(3, 2);
        toppings.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        toppings.setLayout(tLayout);

        for (final ToppingPanel panel : this.toppings.values()) {
            toppings.add(panel);
        }

        return toppings;
    }

    /**
     * Creates the title panel.
     *
     * @return the title panel
     */
    private JPanel createTitlePanel() {
        final int panelHeight = 30;

        final JPanel panel = new JPanel();
        final SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);

        final JLabel title = new JLabel("Pizza Shop Calculator");
        title.setFont(FONT_BIG);
        panel.add(title);

        final JLabel author = new JLabel("by Kaden");
        author.setFont(FONT_SMALL);

        panel.add(title);
        panel.add(author);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, title, 0, SpringLayout.HORIZONTAL_CENTER, panel);
        layout.putConstraint(SpringLayout.NORTH, author, 3, SpringLayout.NORTH, panel);
        layout.putConstraint(SpringLayout.WEST, author, 10, SpringLayout.EAST, title);

        panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, panelHeight));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelHeight));

        return panel;
    }

    /**
     * Constructs the {@link JPanel} that contains the pizza elements.
     *
     * @return the pizza panel
     */
    private JPanel createPizzaPanel() {
        final Dimension dimension = new Dimension(Integer.MAX_VALUE, 60);
        // create the 'invalid text' label - will only appear when non-numerical text is entered
        this.invalidLabel = new JLabel("The text you have entered is invalid. It must be a number.");
        this.invalidLabel.setForeground(new Color(130, 22, 14));
        this.invalidLabel.setVisible(false);

        // create the 'invalid text' label - will only appear when non-numerical text is entered
        this.priceLabel = new JLabel();
        this.priceLabel.setForeground(Color.BLACK);
        this.priceLabel.setVisible(false);

        // create diameter label
        this.diameterLabel = new JLabel("Diameter of the pizza (inches): ");

        // create the diameter entry field
        this.diameterField = new JTextField("");
        this.diameterField.setColumns(5);

        // add a document listener that runs the consumer whenever textField is updated
        this.diameterField.getDocument().addDocumentListener(new UpdatingDocumentListener(this.diameterField, (text) -> this.update()));

        // create diameter panel - contains diameter, price, and invalid labels
        final JPanel diameterPanel = new JPanel();
        final SpringLayout sLayout = new SpringLayout();
        diameterPanel.setBackground(Color.GRAY);
        diameterPanel.setLayout(sLayout);

        // position labels
        sLayout.putConstraint(SpringLayout.NORTH, this.diameterLabel, 10, SpringLayout.NORTH, diameterPanel);
        sLayout.putConstraint(SpringLayout.WEST, this.diameterLabel, 10, SpringLayout.WEST, diameterPanel);

        sLayout.putConstraint(SpringLayout.NORTH, this.invalidLabel, 10, SpringLayout.SOUTH, this.diameterLabel);
        sLayout.putConstraint(SpringLayout.WEST, this.invalidLabel, 10, SpringLayout.WEST, diameterPanel);

        sLayout.putConstraint(SpringLayout.NORTH, this.priceLabel, 10, SpringLayout.SOUTH, this.diameterLabel);
        sLayout.putConstraint(SpringLayout.WEST, this.priceLabel, 10, SpringLayout.WEST, diameterPanel);

        // position input
        sLayout.putConstraint(SpringLayout.WEST, this.diameterField, 10, SpringLayout.EAST, this.diameterLabel);
        sLayout.putConstraint(SpringLayout.NORTH, this.diameterField, 7, SpringLayout.NORTH, diameterPanel);


        // add labels to panel
        diameterPanel.add(this.diameterLabel);
        diameterPanel.add(this.diameterField);
        diameterPanel.add(this.invalidLabel);
        diameterPanel.add(this.priceLabel);

        diameterPanel.setPreferredSize(dimension);
        diameterPanel.setMaximumSize(dimension);

        return diameterPanel;
    }

    /**
     * Creates the price breakdown {@link JTable}.
     *
     * @return the JTable
     */
    private JTable createPriceBreakdown() {
        final String[] columns = {
                "Cost type", "Price"
        };

        final DefaultTableModel model = new DefaultTableModel(columns, 4 + this.toppings.size());

        return new JTable(model);
    }

    /**
     * Updates the price breakdown table with new values.
     *
     * @param diameter the diameter of the pizza, used for price calculation
     */
    private void updatePriceBreakdown(final double diameter) {
        final DefaultTableModel model = (DefaultTableModel) this.priceBreakdownTable.getModel();
        for (int i = model.getRowCount() - 1; i > -1; i--) {
            model.removeRow(i);
        }

        final double materials = this.calculateMaterials(diameter);

        model.addRow(new Object[]{"Labour", COST_FORMAT.format(LABOUR_COST)});
        model.addRow(new Object[]{"Handling", COST_FORMAT.format(STORE_COST)});
        model.addRow(new Object[]{"Materials", COST_FORMAT.format(materials)});

        for (final ToppingPanel toppingPanel : this.toppings.values()) {
            final int count = toppingPanel.count;
            final double cost = count * toppingPanel.topping.cost;

            model.addRow(new Object[]{" -- " + count + " " + toppingPanel.topping.name, COST_FORMAT.format(cost)});
        }

        model.addRow(new Object[]{" -- Base cost", COST_FORMAT.format(this.calculateBaseCost(diameter))});
        model.addRow(new Object[]{"Total", COST_FORMAT.format(this.calculatePizzaCost(diameter))});
    }

    /**
     * Creates a ToppingElement for {@code topping} and saves it in the toppings map.
     *
     * @param topping       the topping
     * @param defaultAmount the amount to start off with
     */
    private void initializeTopping(final Topping topping,
                                   final int defaultAmount) {
        final ToppingPanel panel = new ToppingPanel(this, topping, defaultAmount);
        this.toppings.put(topping, panel);
    }

    /**
     * Updates values in the interface.
     */
    protected void update() {
        final String text = this.diameterField.getText();

        if (text.isEmpty()) {
            // if the text is empty, hide all labels
            this.priceLabel.setVisible(false);
            this.invalidLabel.setVisible(false);
            this.updatePriceBreakdown(0);
            return;
        }

        try {
            // attempt to parse text into a Double
            final double diameter = Double.parseDouble(text);
            final double price = this.calculatePizzaCost(diameter);
            this.priceLabel.setText("The price of the pizza is " + COST_FORMAT.format(price));
            this.priceLabel.setVisible(true);
            this.invalidLabel.setVisible(false);
            this.updatePriceBreakdown(diameter);
        } catch (final Exception e) {
            // text could not be parsed to double -- is invalid text, remove price label
            this.priceLabel.setVisible(false);
            this.invalidLabel.setVisible(true);
            this.updatePriceBreakdown(0);
        }
    }

    /**
     * A class that extends {@link JPanel} to render the topping count.
     */
    private static final class ToppingPanel extends JPanel {

        private static final Dimension DIMENSION = new Dimension(50, 50);
        private final PizzaShopCalculator app;
        private final Topping topping;
        private final JLabel toppingLabel;
        private final JButton incrementButton;
        private final JButton decrementButton;
        private int count;

        /**
         * Constructs {@code ToppingPanel}.
         *
         * @param app           the app
         * @param topping       the topping
         * @param defaultAmount the default amount to add to the pizza
         */
        public ToppingPanel(final PizzaShopCalculator app,
                            final Topping topping,
                            final int defaultAmount) {
            this.app = app;
            this.topping = topping;
            this.count = defaultAmount;

            final SpringLayout spring = new SpringLayout();

            final JPanel titlePanel = new JPanel();
            this.toppingLabel = new JLabel(this.countName() + " " + this.topping.name);
            titlePanel.add(this.toppingLabel);

            final JPanel buttonPanel = new JPanel();
            this.incrementButton = new JButton("+");
            this.decrementButton = new JButton("-");
            this.incrementButton.addActionListener(this::handleIncrement);
            this.decrementButton.addActionListener(this::handleDecrement);
            buttonPanel.add(this.incrementButton);
            buttonPanel.add(this.decrementButton);
            buttonPanel.setLayout(new GridLayout(0, 2));


            this.add(titlePanel);
            this.add(buttonPanel);
            this.setLayout(spring);

            spring.putConstraint(SpringLayout.NORTH, titlePanel, 10, SpringLayout.NORTH, this);
            spring.putConstraint(SpringLayout.NORTH, buttonPanel, 10, SpringLayout.SOUTH, titlePanel);
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
            this.count = count + 1;
            this.update();
        }

        private void handleDecrement(final ActionEvent event) {
            if (count == 0) {
                return;
            } else {
                this.count = count - 1;
            }
            this.update();
        }

        private void update() {
            this.toppingLabel.setText(this.countName() + " " + this.topping.name);
            this.app.update();
        }

    }

    /**
     * Represents a topping that can be added to a pizza.
     */
    private static final class Topping {

        private final String name;
        private final double cost;

        /**
         * Constructs {@code Topping}.
         *
         * @param name the topping name e.g. 'pepperoni'
         * @param cost the cost of one of this topping
         */
        public Topping(final String name,
                       final double cost) {
            this.name = name;
            this.cost = cost;
        }

    }

    /**
     * Watches for updates on the provided {@link JTextField} and calls a consumer whenever the text is modified.
     */
    private static final class UpdatingDocumentListener implements DocumentListener {

        private final JTextField field;
        private final Consumer<String> textConsumer;

        /**
         * Constructs {@code DiameterUpdateListener}.
         *
         * @param field        the field
         * @param textConsumer the text consumer that is called whenever a modification is made to {@code field}
         */
        public UpdatingDocumentListener(final JTextField field,
                                        final Consumer<String> textConsumer) {
            this.field = field;
            this.textConsumer = textConsumer;
        }

        @Override
        public void insertUpdate(final DocumentEvent event) {
            this.textConsumer.accept(this.field.getText());
        }

        @Override
        public void removeUpdate(final DocumentEvent event) {
            this.textConsumer.accept(this.field.getText());
        }

        @Override
        public void changedUpdate(final DocumentEvent event) {
            this.textConsumer.accept(this.field.getText());
        }
    }

}
