package sh.kaden.ics4u.simple.area;

import java.util.Scanner;

/**
 * Calculates the volume and area of a cone using a radius and height value.
 */
public class ConeProgram {

    /**
     * The main entrypoint.
     *
     * @param args cli args
     */
    public static void main(final String[] args) {
        new ConeProgram();
    }

    /**
     * Constructs {@code ConeProgram}.
     */
    public ConeProgram() {
        final double radius = this.ask("Radius of cone");
        final double height = this.ask("Height of cone");
        final double volume = this.volume(radius, height);
        final double surfaceArea = this.surfaceArea(radius, height);
        System.out.printf("This cone has a volume of %.2f and a surface area of %.2f.\n", volume, surfaceArea);
    }

    /**
     * Calculates the volume of the cone.
     *
     * @param radius the radius
     * @param height the height
     * @return the volume
     */
    public double volume(final double radius,
                         final double height) {
        return (Math.PI * Math.pow(radius, 2) * height) / 3;
    }

    /**
     * Calculates the surface area of a cone.
     *
     * @param radius the radius of the cone
     * @param height the height of the cone
     * @return the surface area
     */
    public double surfaceArea(final double radius,
                              final double height) {
        final double radiusSquared = Math.pow(radius, 2);
        final double slantSquared = radiusSquared + Math.pow(height, 2);

        return (Math.PI * radiusSquared) + Math.PI * radius * Math.sqrt(slantSquared);
    }

    /**
     * Asks a question and reads the response from {@link System#in} as a double.
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

}
