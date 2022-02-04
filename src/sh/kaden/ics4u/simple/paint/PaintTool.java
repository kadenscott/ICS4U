package sh.kaden.ics4u.simple.paint;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * A utility to find the required paint for a room.
 */
public class PaintTool {

    private static final double PAINT_CAN_COST = 52.75; // dollars
    private static final double PAINT_CAN_AREA = 40; // meters squared
    private static final DecimalFormat COST_FORMAT = new DecimalFormat("$##.00");

    /**
     * {@code PaintTool}'s entrypoint.
     *
     * @param args the cli args
     */
    public static void main(final String[] args) {
        new PaintTool();
    }

    private final Cuboid room;
    private final Cuboid door;
    private final Cuboid[] windows;

    /**
     * Constructs {@code PaintTool}.
     */
    public PaintTool() {
        this.room = this.newCuboid("room");
        this.door = this.newCuboid("door");
        this.windows = new Cuboid[]{
                this.newCuboid("window A"),
                this.newCuboid("window B")
        };

        final double roomArea = room.area();
        final double paintableArea = roomArea - door.area() - windows[0].area() - windows[1].area();
        final double cans = Math.ceil(paintableArea / PAINT_CAN_AREA);

        System.out.println("The paintable area of this room is "+paintableArea+" m^2");
        System.out.println("That would be "+cans+" cans");
        System.out.println("That would cost you "+COST_FORMAT.format(cans * PAINT_CAN_COST));
    }

    /**
     * Creates a new cuboid by asking questions.
     *
     * @param name the name
     * @return the new cuboid
     */
    private Cuboid newCuboid(final String name) {
        final double length = this.ask("Length of " + name);
        final double height = this.ask("Height of " + name);
        final double width = this.ask("Width of " + name);

        return new Cuboid(name, length, width, height);
    }

    /**
     * Asks a question and reads the response from {@link System#in}.
     *
     * @param question the question
     * @return the response
     */
    private double ask(final String question) {
        final Scanner scanner = new Scanner(System.in);
        System.out.print(question + ": ");
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (final Exception e) {
            System.out.println("Invalid input for question '" + question + "'.");
            System.exit(1);
            return 0;

        }
    }

    /**
     * Stores the length, width, and height of a cuboid.
     */
    private static class Cuboid {

        public final String name;
        public final double length;
        public final double width;
        public final double height;

        /**
         * Constructs {@code Cuboid}.
         *
         * @param name   the name
         * @param length the length
         * @param width  the width
         * @param height the height
         */
        public Cuboid(final String name,
                      final double length,
                      final double width,
                      final double height) {
            this.name = name;
            this.length = length;
            this.width = width;
            this.height = height;
        }

        /**
         * Returns the total surface area of the cuboid.
         *
         * @return the TSA
         */
        public double area() {
            return 2 * (length * width + width * height + length * height);
        }

    }

}
