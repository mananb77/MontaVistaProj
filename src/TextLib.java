import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class TextLib {

    public static String readFileAsString(String filename) {
        Scanner scanner;
        StringBuilder output = new StringBuilder();

        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output.append(line.trim()+"\n");
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return output.toString();
    }

    public static ArrayList<String> splitIntoSentences(String text) {
        ArrayList<String> output = new ArrayList<>();

        Locale locale = Locale.US;
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(locale);
        breakIterator.setText(text);

        int prevIndex = 0;
        int boundaryIndex = breakIterator.first();
        while(boundaryIndex != BreakIterator.DONE) {
            String sentence = text.substring(prevIndex, boundaryIndex).trim();
            if (sentence.length()>0)
                output.add(sentence);
            prevIndex = boundaryIndex;
            boundaryIndex = breakIterator.next();
        }

        String sentence = text.substring(prevIndex).trim();
        if (sentence.length()>0)
            output.add(sentence);

        return output;
    }

    public static ArrayList<Review> readAmazonReviewFile(String filename) {
        Scanner scanner = null;
        ArrayList<Review> reviews = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(filename));

            int count = 0;
            for (int i = 0; i < 853; i++) {
                scanner.nextLine();
                count++;
            }

            while (scanner.hasNextLine() && count <= 1395) {
                String line = scanner.nextLine();

                Review d = getReview( line );
                reviews.add(d);
                count++;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return reviews;
    }

    private static Review getReview(String line) {
        String[] data = line.split(",");

        String review = data[19].trim();
        int rating = Integer.parseInt(data[17].trim());

        return new Review( review, rating );
    }

    public static ArrayList<String> saveWordsIntoList(String filename) {
        Scanner scanner = null;
        ArrayList<String> words = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(filename));
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                words.add(line);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return words;
    }


}
