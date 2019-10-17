import java.util.ArrayList;
import java.util.Arrays;

public class DocumentTester {
    public static void main(String[] args) {
        Document document = new Document("And you really will have to make it through that violent, metaphysical, symbolic storm. No matter how metaphysical or symbolic it might be, make no mistake about it: it will cut through flesh like a thousand razor blades. People will bleed there, and you will bleed too. Hot, red blood. You’ll catch that blood in your hands, your own blood and the blood of others.");
//        System.out.println(Document.splitIntoSentences().toString());
        System.out.println(document.getWordCount());

        String[] words = splitIntoWords("And you really will have to make it through that violent, metaphysical, symbolic storm. No matter how metaphysical or symbolic it might be, make no mistake about it: it will cut through flesh like a thousand razor blades. People will bleed there, and you will bleed too. Hot, red blood. You’ll catch that blood in your hands, your own blood and the blood of others.");
        System.out.println(Arrays.toString(words));
    }

    private static String[] splitIntoWords(String text) {
        String[] words = text.split(" ");
        for (int i = 0; i < words.length; i++) {
            words[i] = stripPunctuation(words[i]);
            words[i] = words[i].trim();
        }
        return words;
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

}
