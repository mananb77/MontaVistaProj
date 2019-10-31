import java.util.ArrayList;

public class ReviewTester {
    public static ArrayList<String> positiveWords = TextLib.saveWordsIntoList("data/positivewords.csv");
    public static ArrayList<String> negativeWords = TextLib.saveWordsIntoList("data/negativewords.csv");

    public static void main(String[] args) {
//        ArrayList<Document> amazonReviews = TextLib.readAmazonReviewFile("data/updated_reviews.csv");
//
////        ArrayList<Document> reviews = TextLib.readSampleReviewFile("data/sample_reviews.csv");
//        double averageError = compareToRealValues(amazonReviews);
//        System.out.println("Average Error: " + averageError);

        ArrayList<Document> airlineReviews = TextLib.readAirlineReviewFile("data/updated_airline_reviews.csv");
        double averageAirlineError = compareToRealValues(airlineReviews);
        System.out.println("Average Error: " + averageAirlineError);

    }

    private static double compareToRealValues(ArrayList<Document> reviews) {
        double error = 0;
        double correct = 0;
        double incorrect = 0;

        for (Document review : reviews) {
            double prediction = calculateRating(review);
            System.out.println(prediction + " " + review.getRating());
            System.out.println();
            error += compareAirlineValue(prediction, review.getRating());

            if (prediction == review.getRating()){
                correct++;
            } else{
                incorrect++;
            }
        }

        System.out.println("correct: " + correct);
        System.out.println("incorrect: " + incorrect);
        System.out.println( 100* (correct/(correct+incorrect)) + "%");

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
        System.out.println("ratio " + ratio);

        if (ratio < 0.4) {
            rating = 1;
        } else if (ratio >= 0.4 && ratio < 0.8) {
            rating = 2;
        } else if (ratio >= 0.8 && ratio < 1.2) {
            rating = 3;
        } else if (ratio >= 1.2 && ratio <= 1.6) {
            rating = 4;
        } else {
            rating = 5;
        }

        return rating;   // average the rating of all sentences to give final rating
    }

    private static double compareToRealValue(double prediction, double rating) {
        return Math.abs(prediction - rating);
    }

    private static double compareAirlineValue ( double prediction, double rating ) {
        int rating1 = 0;
        int rating2 = 0;
        if ( rating == 5 ) {
            rating1 = 4;
            rating2 = 5;
            return Math.min( Math.abs(rating1 - prediction), Math.abs(rating2 - prediction));
        } else if ( rating == 3 ) {
            return Math.abs( rating - prediction );
        } else if ( rating == 1) {
            rating1 = 1;
            rating2 = 2;
            return Math.min( Math.abs(rating1 - prediction), Math.abs(rating2 - prediction));
        }

        return 0;
    }
}