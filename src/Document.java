import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Document {
    private String text;
    private ArrayList<String> sentences;
    private ArrayList<String> words;
    private boolean isCurrent = true;
    private int rating = 0;
    private static WordBucketV2 commonWords = new WordBucketV2();

    public Document( String text, int rating ) {
        this.text = text;
        this.rating = rating;
        this.sentences = splitIntoSentences( this.text );
        this.words = splitIntoWords( this.text );

        commonWords = loadWordFrequenciesDoc();
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String newText ) {
        this.text = newText;
        isCurrent = false;
    }

    public int getWordCount() {
        updateWords();

        int wordCount = 0;
        for ( String word : words ) {
            if ( !(word.equals(""))) {
                wordCount++;
            }
        }

        return wordCount;
    }

    public int getSentenceCount() {
        updateSentences();
        return sentences.size();
    }

    public double getAverageWordsPerSentence() {
        updateWords();
        updateSentences();
        return (double) words.size()/getSentenceCount();
    }

    public double getAverageCharactersPerWord() {
        updateWords();

        int charCount = 0;
        for ( String word : words ) {
            charCount += word.length();
        }

        return (double) charCount/words.size();
    }

    public int getSyllableCount() {
        updateWords();

        int syllables = 0;
        for ( String word : words ) {
            syllables += getSyllablesFor(word);
        }
        return syllables;
    }

    public int getVocabularySize() {
        updateWords();
        ArrayList<String> vocabulary = new ArrayList<>();

        for ( String word : words ) {
            if ( !vocabulary.contains(word) ) {
                vocabulary.add(word);
            }
        }

        return vocabulary.size();
    }

    public double getFKReadability() {
        updateWords();

        int totalWords = getWordCount();
        int totalSyllables = getSyllableCount();

        return 206.835 - 1.015*( (double)totalWords / sentences.size() ) - 84.6*( (double)totalSyllables/totalWords );
    }

    public ArrayList<Integer> getOccurencesOf(String target) {
        updateWords();
        ArrayList<Integer> locations = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (word.equals(target)) {
                locations.add(i);
            }
        }

        return locations;
    }

    public int getOccurenceCountOf(String target) {
         return getOccurencesOf(target).size();
    }

    public int countReoccurencesOf(String target1, String target2) {
        updateSentences();
        int count = 0;
        for ( String sentence : sentences ) {
            if ( sentence.contains(target1) && sentence.contains(target2) ) {
                count++;
            }
        }

        return count;
    }

    private void updateWords() {
        if ( !isCurrent ) {
            words = splitIntoWords( this.text );
            isCurrent = true;
        }
    }

    private void updateSentences() {
        if ( !isCurrent ) {
            sentences = splitIntoSentences( this.text );
            isCurrent = true;
        }
    }

    // static helper methods
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

    public static String getTextFrom(String filename) {
        Scanner scanner;
        StringBuilder output = new StringBuilder();

        try {
            scanner = new Scanner(new FileInputStream(filename), "UTF-8");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output.append(line.trim()+"\n");
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + filename);
        }

        return output.toString();
    }

    public WordBucketV2 loadWordFrequenciesDoc() {
        Scanner scanner;
        WordBucketV2 wordBucket = new WordBucketV2();

        try {
            scanner = new Scanner(new FileReader("data/commonWords.txt"));
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String word = "";
                int frequency = 0;

                String[] data = line.split(" ");
                String numbers = "0123456789";

                for ( String charSequence : data ) {
                    if ( !charSequence.contains(numbers) ) word = charSequence;
                    if ( charSequence.contains(numbers) ) frequency = Integer.parseInt(charSequence);
                }

                wordBucket.add(word, frequency);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found " + "data/commonWords.txt");
        }

        return wordBucket;
    }

    public ArrayList<String> splitIntoSentences( String text ) {
        ArrayList<String> output = new ArrayList<>();

        Locale locale = Locale.US;
        BreakIterator breakIterator = BreakIterator.getSentenceInstance(locale);
        breakIterator.setText(text);

        int prevIndex = 0;
        int boundaryIndex = breakIterator.first();
        while(boundaryIndex != BreakIterator.DONE) {
            String sentence = text.substring(prevIndex, boundaryIndex).trim();
            if (sentence.length()>0)
                output.add(sentence);
            prevIndex = boundaryIndex;
            boundaryIndex = breakIterator.next();
        }

        String sentence = text.substring(prevIndex).trim();
        if (sentence.length()>0)
            output.add(sentence);

        return output;
    }

    public ArrayList<String> splitIntoWords( String text ) {
        ArrayList<String> words = convertToArrayList( text.split(" ") );
        return words;
    }

    private static ArrayList<String> convertToArrayList( String[] words ) {
        ArrayList<String> arrayList = new ArrayList<>();
        for ( String word : words ) {
            String word1 = stripPunctuation(word).trim();
            arrayList.add( word1 );
        }
        return arrayList;
    }

    private static int getSyllablesFor( String word ) {
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

    private static boolean hasSyllableBreak( String letter, String word, int i ) {
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
