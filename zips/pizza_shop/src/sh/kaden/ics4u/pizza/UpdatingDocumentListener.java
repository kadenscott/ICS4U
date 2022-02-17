package sh.kaden.ics4u.pizza;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.function.Consumer;

/**
 * This class will listen for updates on a {@link JTextField}. When the text inside the field is updated,
 * a {@link Consumer<String>} will be called with the updated text.
 */
public final class UpdatingDocumentListener implements DocumentListener {

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

