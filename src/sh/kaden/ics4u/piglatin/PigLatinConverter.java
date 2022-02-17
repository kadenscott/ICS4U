package sh.kaden.ics4u.piglatin;

import java.util.Scanner;

/**
 * Converts a word from stdin and converts it to pig latin.
 */
public class PigLatinConverter {

    public static void main(final String[] args) {
        new PigLatinConverter();
    }

    /**
     * Checks to see if {@code c} is a vowel letter.
     *
     * @param c the character to check
     * @return true if vowel, false if not
     */
    private static boolean isVowel(final char c) {
        return (c == 'A' || c == 'a' || c == 'E' || c == 'e' || c == 'I' || c == 'i' || c == 'O' || c == 'o' || c == 'U' || c == 'u');
    }

    /**
     * Checks to see if {@code c} is a consonant letter.
     *
     * @param c the character to check
     * @return true if consonant, false if not
     */
    private static boolean isConsonant(final char c) {
        return Character.isAlphabetic(c) && !isVowel(c);
    }

    /**
     * Converts a word to pig latin.
     *
     * @param word the word
     * @return the word in pig latin
     */
    private static String convert(final String word) {
        final int length = word.length();
        int index = -1;
        for (int i = 0; i < length; i++) {
            if (isVowel(word.charAt(i))) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            // return original word, no vowels to piglatin with
            return word;
        }

        return word.substring(index) + word.substring(0, index) + "ay";
    }


    /**
     * Constructs {@code PigLatinConverter}.
     * <p>
     * Calling this constructor will run the pig latin program.
     */
    public PigLatinConverter() {
        final String word = this.ask("Word to pig-latin-ize");
        System.out.println("'"+word+"' in pig-latin: "+convert(word));
    }

    /**
     * Asks a question and reads the response from {@link System#in}.
     *
     * @param question the question
     * @return the response
     */
    private String ask(final String question) {
        final Scanner scanner = new Scanner(System.in);
        System.out.print(question + ": ");
        return scanner.nextLine();
    }

}
