package sh.kaden.ics4u.pizza;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A calculator application to determine the price of a sh.kaden.ics4u.pizza.
 */
public class PizzaShopCalculator extends JFrame {

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

    private final List<ToppingPanel> toppings;
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
        this.toppings = new ArrayList<>();

        // Initialize toppings with name, file image path, amount, and cost.
        this.initializeTopping("pepperoni", "./src/sh/kaden/ics4u/pizza/pepperoni.png", 1, 0.3);
        this.initializeTopping("cheese", "./src/sh/kaden/ics4u/pizza/cheese.png", 1, 0.3);
        this.initializeTopping("mushrooms", "./src/sh/kaden/ics4u/pizza/mushroom.png", 0, 0.3);
        this.initializeTopping("onions", "./src/sh/kaden/ics4u/pizza/onion.png", 0, 0.3);
        this.initializeTopping("garlic", "./src/sh/kaden/ics4u/pizza/garlic.png", 0, 0.3);
        this.initializeTopping("olives", "./src/sh/kaden/ics4u/pizza/olive.png", 0, 0.3);

        // init window properties
        this.setSize(500, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.titlePanel = this.createTitlePanel();
        this.pizzaPanel = this.createPizzaPanel();
        this.priceBreakdownTable = this.createPriceBreakdown();

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

        // update price breakdown with no diameter
        this.updatePriceBreakdown(0);
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

        for (final ToppingPanel toppingPanel : this.toppings) {
            cost = cost + toppingPanel.count() * toppingPanel.cost();
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
        toppings.setPreferredSize(new Dimension(Integer.MAX_VALUE, 180));
        toppings.setLayout(tLayout);

        for (final ToppingPanel panel : this.toppings) {
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

        for (final ToppingPanel toppingPanel : this.toppings) {
            final int count = toppingPanel.count();
            final double cost = count * toppingPanel.cost();

            model.addRow(new Object[]{" -- " + count + " " + toppingPanel.toppingName(), COST_FORMAT.format(cost)});
        }

        model.addRow(new Object[]{" -- Base cost", COST_FORMAT.format(this.calculateBaseCost(diameter))});
        model.addRow(new Object[]{"Total", COST_FORMAT.format(this.calculatePizzaCost(diameter))});
    }

    /**
     * Creates a ToppingElement for {@code topping} and saves it in the toppings map.
     *
     * @param toppingName   the topping name
     * @param filePath      the path to the topping's image file
     * @param defaultAmount the amount to start off with
     * @param cost          the cost of adding this topping to the pizza
     */
    private void initializeTopping(final String toppingName,
                                   final String filePath,
                                   final int defaultAmount,
                                   final double cost) {
        final ToppingPanel topping = new ToppingPanel(this, toppingName, filePath, cost, defaultAmount);
        this.toppings.add(topping);
    }

    /**
     * Updates labels and the price break down with the current state of the input fields.
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

}
