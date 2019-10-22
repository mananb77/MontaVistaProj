import java.util.ArrayList;

public class ReviewTester {
    public static ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
    public static ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

    public static void main(String[] args) {
        ArrayList<Document> reviews = TextLib.readAmazonReviewFile("data/Texts/allfeatures-ose-final.csv");
        double averageError = compareToRealValues( reviews );
        System.out.println("Average deviation: " + averageError);
    }

    private static double compareToRealValues ( ArrayList<Document> reviews ) {
        double error = 0;

        for ( Document review : reviews ) {
            double prediction = calculateRating( review );
            error += compareToRealValue( prediction, review.getRating() );
        }

        return error / reviews.size();
    }

    private static double calculateRating(Document review) {

        ArrayList<String> sentences = review.splitIntoSentences( review.getText() );
        int total = 0;

        for ( String sentence : sentences ) {
            ArrayList<String> words = review.splitIntoWords( review.getText() );

            double positive = 0;
            double negative = 0;
            double rating = 0;

            for ( String word : words ) {
                if (positiveWords.contains(word)) {
                    positive++;
                } else if (negativeWords.contains(word)) {
                    negative++;
                }
            }

            double ratio = (positive/words.size()) / (negative/words.size());

            if (ratio <= 0.4) {
                rating = 1;
            } else if (ratio > 0.4 && ratio < 0.6) {
                rating = 2;
            } else if (ratio >= 0.6) {
                rating = 3;
            }

            total += rating;
        }

        return total / sentences.size();
    }

    private static double compareToRealValue( double prediction, double rating ) {
        return Math.abs(prediction - rating);
    }
}
