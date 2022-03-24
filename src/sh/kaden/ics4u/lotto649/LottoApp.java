package sh.kaden.ics4u.lotto649;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Assignment 'Lotto Array Assign 1'
 */
public class LottoApp {

    // Static constant variables, I don't like magic numbers.
    private final int MIN_TICKET_COUNT = 1;
    private final int MAX_TICKET_COUNT = 100;
    private final int MIN_TICKET_NUMBER = 1;
    private final int MAX_TICKET_NUMBER = 49;
    private final int TICKET_NUMBER_COUNT = 6;
    private static final Random RANDOM = new Random();

    /**
     * The command-line entrypoint for {@code LottoApp}.
     *
     * @param args the program arguments
     */
    public static void main(final String[] args) {
        new LottoApp().start();
    }

    private final Scanner scanner;
    private final int[] winningNumbers; // an array containing the winning numbers
    private int[][] tickets; // An array of tickets and their corresponding numbers
    private int ticketCount; // How many tickets we're generating

    /**
     * Initializes the {@code LottoApp}.
     */
    public LottoApp() {
        this.scanner = new Scanner(System.in);
        this.winningNumbers = this.generateRandomNumbers(TICKET_NUMBER_COUNT);
    }

    /**
     * Starts the lottery application.
     *
     * @implNote I understand the assignment was to do this sequentially, but I still wanted to use methods to provide
     * a logical separation between various parts of the program.
     */
    public void start() {
        this.introduce();
        this.askTicketCount();
        this.generateTickets();
        this.checkTickets();
    }

    /**
     * Introduces the game to the user.
     */
    private void introduce() {
        System.out.println("Welcome to the lottery!");
        System.out.println();
        System.out.println("You will be asked to enter the number of tickets to generate.");
        System.out.println("Afterwards, the program will generate a winning ticket, and provide a list of any");
        System.out.println("matching tickets.");
        System.out.println();
    }

    /**
     * Asks the user for the ticket count and stores it in {@link #ticketCount}.
     */
    private void askTicketCount() {
        System.out.print("How many tickets would you like to generate? (between " + MIN_TICKET_COUNT + " and " + MAX_TICKET_COUNT + "): ");
        this.ticketCount = this.scanner.nextInt();

        if (this.ticketCount < MIN_TICKET_COUNT || this.ticketCount > MAX_TICKET_COUNT) {
            System.out.println("You did not provide a number within " + MIN_TICKET_COUNT + " and " + MAX_TICKET_COUNT + ".");
            System.exit(1);
        }
    }

    /**
     * Generates the lottery tickets and prints out the winning numbers.
     */
    private void generateTickets() {
        this.tickets = new int[this.ticketCount][];

        for (int i = 0; i < this.tickets.length; i++) {
            final int[] numbers = this.generateRandomNumbers(TICKET_NUMBER_COUNT);
            this.tickets[i] = numbers;
        }

        try {
            System.out.print("Generating " + this.ticketCount + " tickets");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.print(".");
            Thread.sleep(1000);
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("There was a problem generating the lottery tickets.");
            System.out.println("The program will now exit.");
            System.exit(1);
        }

        System.out.println("Generated " + this.ticketCount + " tickets.");
        System.out.println("The winning numbers are " + Arrays.toString(this.winningNumbers) + ".");
    }

    /**
     * Check tickets to see if there are any winners.
     */
    private void checkTickets() {
        // Create a hashmap that will map a list of ticket numbers to how many matches the ticket contains.
        final Map<Integer, List<Integer>> matchingMap = new HashMap<>();

        for (int i = 0; i < this.tickets.length; i++) {
            final int ticketNumber = i;
            int[] ticket = this.tickets[ticketNumber];
            int matches = 0;
            for (int j = 0; j < ticket.length; j++) {
                if (this.winningTicketContains(ticket[j])) {
                    matches++;
                }
            }
            matchingMap.compute(matches, (key, list) -> {
                if (list == null) {
                    list = new ArrayList<>();
                }

                list.add(ticketNumber);

                return list;
            });
        }

        for (final Map.Entry<Integer, List<Integer>> matchesEntry : matchingMap.entrySet()) {
            final Integer matchAmount = matchesEntry.getKey();
            final List<Integer> tickets = matchesEntry.getValue();
            System.out.println(tickets.size() + " tickets match " + matchAmount + " numbers with the winning numbers.");
            if (matchAmount >= 3) {
                for (final int ticket : tickets) {
                    System.out.println("  - ticket " + ticket + ": " + Arrays.toString(this.getTicketNumbersFor(ticket)));
                }
            }
        }
    }

    /**
     * Returns true if the winning numbers contain the provided number.
     *
     * @param number the number
     * @return the ticket
     */
    private boolean winningTicketContains(final int number) {
        for (final int winningNumber : this.winningNumbers) {
            if (winningNumber == number) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the ticket numbers for the provided ticket.
     *
     * @param ticketIndex the ticket index (i.e. ticket "1", ticket "2", etc)
     * @return the tickets
     */
    private int[] getTicketNumbersFor(final int ticketIndex) {
        return this.tickets[ticketIndex];
    }

    /**
     * Generates an array of random numbers with {@code length}.
     * <p>
     * This method will ensure no duplicate numbers are included in the array.
     *
     * @param length the length
     * @return the integer array
     */
    private int[] generateRandomNumbers(final int length) {
        int[] arr = new int[length];
        for (int i = 0; i < length; i++) {
            int candidate = randomInt(MIN_TICKET_NUMBER, MAX_TICKET_NUMBER);
            boolean added = false;
            while (!added) {
                boolean contains = false;
                for (int j = 0; j < length; j++) {
                    if (arr[j] == candidate) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    arr[i] = candidate;
                    added = true;
                } else {
                    candidate = randomInt(MIN_TICKET_NUMBER, MAX_TICKET_NUMBER);
                }
            }
        }
        return arr;
    }

    /**
     * Returns a random integer between min (inclusive) and max (inclusive).
     *
     * @param min the min number
     * @param max the max number
     * @return a random number
     */
    private static int randomInt(final int min,
                                 final int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }


}
