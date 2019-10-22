import java.util.ArrayList;

public class ReviewTester {
    public static void main(String[] args) {
        ArrayList<Review> reviews = TextLib.readAmazonReviewFile("data/Texts/allfeatures-ose-final.csv");
        ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
        ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

        double averageError = compareToRealValues( reviews );
        System.out.println("Average deviation: " + averageError);
    }

    private static double compareToRealValues ( ArrayList<Review> reviews ) {
        double error = 0;

        for ( Review review : reviews ) {
            String text = review.getText();

            double prediction = calculateRating( review );

            error += compareToRealValue( prediction, review.getRating() );
        }

        return error / reviews.size();
    }

    private static double calculateRating(Document review) {
        ArrayList sentence =
    }

    private static double compareToRealValue( double prediction, double score ) {
        return Math.abs(prediction - score);
    }
}
