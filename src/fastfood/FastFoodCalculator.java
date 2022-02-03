package fastfood;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A calculator application to calculate the cost of fast food items.
 */
public class FastFoodCalculator extends JFrame {

    private static final double HST = 0.13;
    private static final double BURGER_COST = 6.89;
    private static final double FRIES_COST = 2.29;
    private static final double DRINK_COST = 1.69;

    protected final ItemEntryPanel burgerPanel;
    protected final ItemEntryPanel friesPanel;
    protected final ItemEntryPanel drinksPanel;
    protected final ItemEntryPanel tenderPanel;
    private double burgers;
    private double fries;
    private double softDrinks;
    private double tendered;

    /**
     * Entrypoint for {@link FastFoodCalculator}.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new FastFoodCalculator();
    }

    /**
     * Constructs {@code FastFoodCalculator}.
     */
    public FastFoodCalculator() {
        super("Fast Food Calculator");
        this.burgers = 0;
        this.fries = 0;
        this.softDrinks = 0;
        this.tendered = 0;

        this.burgerPanel = new ItemEntryPanel(this, "Enter the number of burgers: ");
        this.friesPanel = new ItemEntryPanel(this, "Enter the number of fries: ");
        this.drinksPanel = new ItemEntryPanel(this, "Enter the number of drinks: ");
        this.tenderPanel = new ItemEntryPanel(this, "Amount tendered (optional): ");


        // init window properties
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        final ItemsPanel panel = new ItemsPanel(this.burgerPanel, this.friesPanel, this.drinksPanel, this.tenderPanel);

        this.getContentPane().add(panel);
    }

    /**
     * A panel that contains a label and a text field.
     */
    private static class ItemEntryPanel extends JPanel {

        private final JLabel label;
        private final JTextField field;
        private final FastFoodCalculator calculator;

        /**
         * Constructs {@code ItemEntryPanel}.
         *
         * @param text the text to display
         */
        public ItemEntryPanel(final FastFoodCalculator calculator,
                              final String text) {
            this.calculator = calculator;
            this.label = new JLabel(text);
            this.field = new JTextField();
            this.field.setColumns(10);

            this.add(label);
            this.add(field);
        }

        /**
         * Returns the text value of the field in this entry.
         *
         * @return the text value
         */
        String text() {
            return this.field.getText();
        }

    }

    /**
     * A panel that displays a collection of {@code ItemEntryPanel}s.
     */
    private static class ItemsPanel extends JPanel {

        private static final Dimension DIMENSION = new Dimension(Integer.MAX_VALUE, 100);

        public ItemsPanel(final ItemEntryPanel... panels) {
            final JPanel container = new JPanel();

            for (final ItemEntryPanel panel : panels) {
                container.add(panel);
            }

            container.setPreferredSize(DIMENSION);
            container.setMaximumSize(DIMENSION);

            this.add(container);
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        }

    }

    private static class ResultPanel extends JPanel {

        private final FastFoodCalculator calculator;

        public ResultPanel(final FastFoodCalculator calculator) {
            this.calculator = calculator;
        }

    }

}
