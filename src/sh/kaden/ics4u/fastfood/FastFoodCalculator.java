package sh.kaden.ics4u.fastfood;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.DecimalFormat;

/**
 * A calculator application to calculate the cost of fast food items.
 */
public class FastFoodCalculator extends JFrame {

    private static final double HST = 0.13;
    private static final double BURGER_COST = 6.89;
    private static final double FRIES_COST = 2.29;
    private static final double DRINK_COST = 1.69;
    private static final DecimalFormat COST_FORMAT = new DecimalFormat("$0.00");

    protected final ItemEntryPanel burgerPanel;
    protected final ItemEntryPanel friesPanel;
    protected final ItemEntryPanel drinksPanel;
    protected final ItemEntryPanel tenderPanel;
    protected final ResultsPanel results;

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
        this.burgerPanel = new ItemEntryPanel(this, "Enter the number of burgers: ");
        this.friesPanel = new ItemEntryPanel(this, "Enter the number of fries: ");
        this.drinksPanel = new ItemEntryPanel(this, "Enter the number of drinks: ");
        this.tenderPanel = new ItemEntryPanel(this, "Amount tendered (optional): ");


        // init window properties
        this.setSize(500, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        final JPanel main = new JPanel();
        final ItemsPanel items = new ItemsPanel(this.burgerPanel, this.friesPanel, this.drinksPanel, this.tenderPanel);
        this.results =  new ResultsPanel(this);

        main.add(items);
        main.add(this.results);
        main.setLayout(new GridLayout(2, 0));
        this.getContentPane().add(main);
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

            final JPanel container = new JPanel();
            this.label = new JLabel(text);
            this.field = new JTextField();
            this.field.setColumns(10);
            this.field.setText("0");

            // add a document listener that updates the results panel when text is updated
            this.field.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    calculator.results.update();
                }
                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    calculator.results.update();
                }
                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    calculator.results.update();
                }
            });

            container.add(label);
            container.add(field);

            this.add(container);
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

        private static final Dimension DIMENSION = new Dimension(Integer.MAX_VALUE, 200);

        /**
         * Constructs {@code ItemsPanel}.
         *
         * @param panels the item entries to add to this panel
         */
        public ItemsPanel(final ItemEntryPanel... panels) {
            for (final ItemEntryPanel panel : panels) {
                this.add(panel);
            }

            final BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
            this.setLayout(layout);

            this.setPreferredSize(DIMENSION);
            this.setMaximumSize(DIMENSION);
        }
    }

    /**
     * A panel that displays the results of the price calculations.
     */
    private static class ResultsPanel extends JPanel {

        private static final Dimension DIMENSION = new Dimension(Integer.MAX_VALUE, 200);
        private final FastFoodCalculator calculator;
        private final JLabel invalidLabel;
        private final JLabel totalNoTaxLabel;
        private final JLabel taxLabel;
        private final JLabel totalTaxLabel;
        private final JLabel tenderedLabel;
        private double burgers = 0;
        private double fries = 0;
        private double drinks = 0;
        private double tendered = -1; // -1  to hide

        public ResultsPanel(final FastFoodCalculator calculator) {
            this.calculator = calculator;

            this.totalNoTaxLabel = new JLabel();
            this.taxLabel = new JLabel();
            this.totalTaxLabel = new JLabel();
            this.tenderedLabel = new JLabel();
            this.invalidLabel = new JLabel("You entered invalid value(s). Please double-check the inputs.");
            this.invalidLabel.setForeground(new Color(130, 22, 14));
            this.invalidLabel.setVisible(false);

            this.add(invalidLabel);
            this.add(totalNoTaxLabel);
            this.add(taxLabel);
            this.add(totalTaxLabel);
            this.add(tenderedLabel);

            final BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);

            this.setLayout(layout);
            this.setPreferredSize(DIMENSION);
            this.setMaximumSize(DIMENSION);
        }

        public void update() {
            final boolean valid = this.validateAndParse();

            this.totalNoTaxLabel.setVisible(valid);
            this.totalTaxLabel.setVisible(valid);
            this.taxLabel.setVisible(valid);
            this.invalidLabel.setVisible(!valid);
            this.tenderedLabel.setVisible(valid && this.tendered != -1);

            final double total = (this.burgers * BURGER_COST) + (this.fries * FRIES_COST) + (this.drinks * DRINK_COST);
            final double taxes = total * HST;
            final double totalTaxes = total + taxes;
            this.totalNoTaxLabel.setText("Total before taxes: "+COST_FORMAT.format(total));
            this.taxLabel.setText("Tax: "+COST_FORMAT.format(taxes)+ " (HST)");
            this.totalTaxLabel.setText("Final total: "+COST_FORMAT.format(totalTaxes));
            this.tenderedLabel.setText("Change: "+COST_FORMAT.format(this.tendered - totalTaxes));
        }

        /**
         * Validates the {@link ItemEntryPanel}s on {@link FastFoodCalculator}, and stores
         * their values.
         *
         * @return true if validation was successful, false if invalid input was provided
         */
        private boolean validateAndParse() {
            final String burgerText = this.calculator.burgerPanel.text();
            final String friesText = this.calculator.friesPanel.text();
            final String drinksText = this.calculator.drinksPanel.text();
            final String tenderedText = this.calculator.tenderPanel.text();

            try {
                this.burgers = Double.parseDouble(burgerText);
                this.fries = Double.parseDouble(friesText);
                this.drinks = Double.parseDouble(drinksText);

                if (tenderedText.equals("0")) {
                    this.tendered = -1;
                } else if (!tenderedText.isEmpty()) {
                    this.tendered = Double.parseDouble(tenderedText);
                } else {
                    this.tendered = -1;
                }

                return true;
            } catch (final Exception e) {
                return false;
            }
        }
    }
}
