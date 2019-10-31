import java.util.ArrayList;

public class ReviewTester {
    public static ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
    public static ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

    public static void main(String[] args) {
        ArrayList<Document> amazonReviews = TextLib.readAmazonReviewFile("data/updated_reviews.csv");

//        ArrayList<Document> reviews = TextLib.readSampleReviewFile("data/sample_reviews.csv");
        double averageError = compareToRealValues(amazonReviews);
        System.out.println("Average Error: " + averageError);

//        ArrayList<Document> airlineReviews = TextLib.readAirlineReviewFile("data/airline_reviews.csv");
//        double averageAirlineError = compareToRealValues(airlineReviews);
//        System.out.println("Average Error: " + averageAirlineError);

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

        if (ratio < 0.4) {
            rating = 1;
        } else if (ratio >= 0.4 && ratio < 0.8) {
            rating = 2;
        } else if (ratio >= 0.8 && ratio < 1.2) {
            rating = 3;
        } else if (ratio >= 1.2 && ratio <= 1.5) {
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