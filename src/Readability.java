import java.util.ArrayList;

public class Readability {
    public static void main(String[] args) {

        ArrayList<DocInfo> texts = TextLib.readAllTextsFile("data/Texts/allfeatures-ose-final.csv");
        double averageError = compareToRealValues( texts );
        System.out.println("Average deviation: " + averageError);

    }

    private static double compareToRealValues ( ArrayList<DocInfo> docInfos ) {
        double error = 0;

        for ( DocInfo info : docInfos ) {
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

    private static double FKReadability( ArrayList<String> sentences ) {
        // TODO:  Break each sentence into words.
        // TODO:  Force to lower-case and strip out all punctuation for doing syllable counts.

        int totalWords = 0, totalSyllables = 0;

        for (String sentence : sentences) {
//            System.out.println(sentence.length() + ": " + sentence);

//            String strippedSentence = stripPunctuation( sentence );
//            System.out.println(strippedSentence);

            totalSyllables += getNumSyllablesInSentence( sentence );
//            System.out.println("Syllables: " + totalSyllables);

            totalWords += getNumWords( sentence );
//            System.out.println("Num words: " + totalWords);
        }

        return 206.835 - 1.015*( (double)totalWords / sentences.size() ) - 84.6*( (double)totalSyllables/totalWords );
    }

    private static int getNumWords(String sentence) {
        String[] words = sentence.split(" ");
        int numWords = 0;

        for ( String word : words ) {
            if ( !(word.equals(""))) {
                numWords++;
            }
        }

        return numWords;
    }

    private static int getNumSyllablesInSentence(String sentence) {
        int totalSyllables = 0;
        String[] words = sentence.split(" ");

        for ( String word : words ) {
            totalSyllables += getSyllablesFor( word );
        }

        return totalSyllables;
    }

    private static String stripPunctuation(String sentence) {
        String strippedSentence = "";
        String notPunctuation = "abcdefghijklmnopqrstuvwxyz1234567890 ";

        for (int i = 0; i < sentence.length(); i++) {
            String letter = sentence.substring(i, i + 1);
            letter = letter.toLowerCase();

            if ( notPunctuation.contains(letter) ) {
                strippedSentence += letter;
            }
        }

        return strippedSentence;
    }

    private static void testSyllableMethod() {
        ArrayList<Word> words = TextLib.readSyllablesFile("data/syllables.txt");

        double right = 0;
        for (Word w : words) {
            String word = w.getWord();
            int prediction = getSyllablesFor(word);

            if (prediction == w.getSyllables()) right++;
        }

        System.out.println("You got " + (right/words.size()) + " right");
    }

    private static int getSyllablesFor(String word) {
        int syllables = 0;

        if (word.length() < 2) {
            return 0;
        } else if (word.length() == 2) {
            return 1;
        }

        String[] letters = new String[word.length()];

        for (int i = 0; i < letters.length - 1; i++) {
            String letter = word.substring(i, i + 1);

            if ( hasSyllableBreak(letter, word, i) ) {
                syllables++;
            }
        }

        String lastLetter = word.substring(word.length() - 1);
        if ( "aiouy".contains(lastLetter)) {
            syllables++;
        }

//        if ( lastLetter.equals("e") && !"aeiouy".contains(word.substring(word.length() - 2, word.length() - 1))) {
//            syllables--;
//        }
        return syllables;
    }

    private static boolean hasSyllableBreak(String letter, String word, int i ) {
        String vowels = "aeiouy";
        String nextLetter = word.substring(i + 1, i + 2);
        String previousLetter = "";

        if (i > 0) {
            previousLetter = word.substring(i - 1, i);
        }

        /*
        vowel-vowel exceptions
        ao      ca•ca•o
        eo      e•on
        ia      li•ar               -tial, anti•alcohol is an exception to the exception
        io      i•on                -tion
        iu      lithi•um
        ua      ac•tu•al•ly
        ue      in•flu•en•tial      -que, -nue, -gue
        uo      du•o                -quo
        iy      ter•i•ya•ki
         */

        if (letter.equals("a") && nextLetter.equals("o")) return true;
        if (letter.equals("e") && nextLetter.equals("o")) return true;
        if (i > 0) {
            if (letter.equals("i") && nextLetter.equals("a") && !previousLetter.equals("t")) return true;
            if (letter.equals("i") && nextLetter.equals("o") && !previousLetter.equals("t")) return true;
            if (letter.equals("i") && nextLetter.equals("u") && !previousLetter.equals("q")) return true;
            if (letter.equals("u") && nextLetter.equals("e") && (!previousLetter.equals("q"))) return true;
            if (letter.equals("u") && nextLetter.equals("o") && !previousLetter.equals("q")) return true;
        }
        if (letter.equals("i") && nextLetter.equals("y")) return true;

        if (vowels.contains(letter)) {
            if (!vowels.contains( word.substring(i + 1, i + 2) )) {     // checks for consonant
                return true;
            }
        }

        return false;
    }
}
