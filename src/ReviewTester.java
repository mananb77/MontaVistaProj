import java.util.ArrayList;

public class ReviewTester {
    public static ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
    public static ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

    public static void main(String[] args) {
        // ArrayList<Document> reviews = TextLib.readAmazonReviewFile("data/updated_reviews.csv");
        // ArrayList<Document> reviews = TextLib.readSampleReviewFile("data/sample_reviews.csv");
        ArrayList<Document> reviews = TextLib.readAirlineReviewFile("data/airline_reviews.csv");
        double averageError = compareToRealValues(reviews);
        System.out.println("Average Error: " + averageError);
    }

    private static double compareToRealValues(ArrayList<Document> reviews) {
        double error = 0;

        for (Document review : reviews) {
            double prediction = calculateRating(review);
            System.out.println(prediction + " " + review.getRating());
            error += compareToRealValue(prediction, review.getRating());
        }

        return error / reviews.size();
    }

    private static double calculateRating(Document review) {

        ArrayList<String> sentences = review.splitIntoSentences(review.getText());
        double positive = 1;
        double negative = 1;
        double rating = 0;
        double wordCount = 0;

        for (String sentence : sentences) {
            ArrayList<String> words = review.splitIntoWords(review.getText());
            wordCount += words.size();

            for (int i = 0; i < words.size(); i++) {

                if (positiveWords.contains(words.get(i))) {
                    positive++;
//                } else if(strongPositiveWords.contains(word)){
//                    positive += 2;
                } else if (negativeWords.contains(words.get(i))) {
                    negative++;
//                }else if (strongNegativeWords.contains(word)){
//                    negative += 2;
                }
            }
        }

        double ratio = positive / negative;

        if (ratio < 0.5) {
            rating = 1;
        } else if (ratio >= 0.5 && ratio < 0.7) {
            rating = 2;
        } else if (ratio >= 0.7 && ratio < 1.3) {
            rating = 3;
        } else if (ratio >= 1.3 && ratio <= 1.7) {
            rating = 4;
        } else {
            rating = 5;
        }

        return rating;   // average the rating of all sentences to give final rating
    }

    private static double compareToRealValue(double prediction, double rating) {
        return Math.abs(prediction - rating);
    }

}