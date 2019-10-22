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
            String textName = "data/Texts/AllTexts/" + info.getTextName();
            String phrase = TextLib.readFileAsString(textName);

            ArrayList<String> sentences = TextLib.splitIntoSentences(phrase);
            double prediction = FKReadability( sentences ) - 7;

            System.out.println("Name: " + info.getTextName());
            System.out.println("Calculated readability: " + prediction);
            System.out.println("Real readability: " + info.getFleschScore());
            System.out.println();

            error += compareToRealValue( prediction, info.getFleschScore() );
        }

        return error / docInfos.size();
    }

    private static double compareToRealValue( double prediction, double score ) {
        return Math.abs(prediction - score);
    }
}
