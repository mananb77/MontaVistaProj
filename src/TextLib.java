import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class TextLib {

    public static final int AMAZON_REVIEW_START = 854;
    public static final int AMAZON_REVIEW_END = 1395;

    public static String readFileAsString(String filename) {
        Scanner scanner;
        StringBuilder output = new StringBuilder();

        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output.append(line.trim() + "\n");
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
        while (boundaryIndex != BreakIterator.DONE) {
            String sentence = text.substring(prevIndex, boundaryIndex).trim();
            if (sentence.length() > 0)
                output.add(sentence);
            prevIndex = boundaryIndex;
            boundaryIndex = breakIterator.next();
        }

        String sentence = text.substring(prevIndex).trim();
        if (sentence.length() > 0)
            output.add(sentence);

        return output;
    }

    public static ArrayList<Document> readAmazonReviewFile(String filename) {
        Scanner scanner = null;
        ArrayList<Document> reviews = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(filename));

            int count = 0;
            for (int i = 0; i < AMAZON_REVIEW_START - 1; i++) {
                scanner.nextLine();
                count++;
            }

            while (scanner.hasNextLine() && count <= AMAZON_REVIEW_END) {
                String line = scanner.nextLine();

                Document d = getReview(line);
                reviews.add(d);
                count++;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return reviews;
    }

    public static ArrayList<Document> readSampleReviewFile(String filename) {
        Scanner scanner = null;
        ArrayList<Document> reviews = new ArrayList<>();

        try {
            scanner = new Scanner(new FileReader(filename));

            int count = 0;
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Document d = getReview(line);
                reviews.add(d);
                count++;
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return reviews;
    }

    private static Document getReview(String line) {
        String[] data = line.split(",");

//        String review = data[19].trim();
//        int rating = Integer.parseInt(data[17].trim());

        int rating = Integer.parseInt(data[0].trim());
        String review = data[1].trim();

        return new Document(review, rating);
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
