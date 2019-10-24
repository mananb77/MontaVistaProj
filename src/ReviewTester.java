import java.util.ArrayList;

public class ReviewTester {
    public static ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
    public static ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

    public static void main(String[] args) {
        ArrayList<Document> reviews = TextLib.readAmazonReviewFile("data/updated_reviews.csv");

//        ArrayList<Document> reviews = TextLib.readSampleReviewFile("data/sample_reviews.csv");
        double averageError = compareToRealValues( reviews );
        System.out.println("Average deviation: " + averageError);
    }

    private static double compareToRealValues ( ArrayList<Document> reviews ) {
        double error = 0;

        for ( Document review : reviews ) {
            double prediction = calculateRating( review );
            System.out.println(prediction + " " + review.getRating());
            error += compareToRealValue( prediction, review.getRating() );
        }

        return error / reviews.size();
    }

    private static double calculateRating(Document review) {

        ArrayList<String> sentences = review.splitIntoSentences( review.getText() );
        double positive = 0;
        double negative = 0;
        double rating = -1;
        double wordCount = 0;

        for ( String sentence : sentences ) {
            ArrayList<String> words = review.splitIntoWords( review.getText() );
            wordCount += words.size();

            for ( String word : words ) {
                if (positiveWords.contains(word)) {
                    positive++;
                } else if (negativeWords.contains(word)) {
                    negative++;
                }
            }
        }

        double ratio = 0;

        if (positive == 0) {
            rating = 0;
        } else if (negative == 0) {
            rating = 3;
        } else {
            ratio = (positive) / (negative); // to prevent the div by 0 error
        }

            /*
            I hate this product and it sucks.
            1/7 / 2/7 = 0
             */

        if (ratio <= 0.2) {
            rating = 1;
        } else if (ratio > 0.2 && ratio <= 0.4) {
            rating = 2;
        }else if (ratio > 0.4 && ratio < 0.6) {
            rating = 3;
        } else if (ratio >= 0.6 && ratio < 0.8) {
            rating = 4;
        } else if (ratio >= 0.8) {
            rating = 5;
        }

        return (double)total / sentences.size();    // average the rating of all sentences to give final rating
    }

    private static double compareToRealValue( double prediction, double rating ) {
        return Math.abs(prediction - rating);
    }
}