//import java.lang.reflect.Array;
//import java.util.ArrayList;
//
//public class WordBucket {
//    private static ArrayList<Word> words = new ArrayList<>();
//
//    public WordBucket() {
//
//    }
//
////    public boolean containsWord(String item) {
////        for ( Word word : words ) {
////            if ( word.getWord().equals(item) ) {
////                return true;
////            }
////        }
////        return false;
////    }
//
//    public void add(String item) {
//        boolean containsWord = false;
//
//        for ( Word word : words ) {
//            if ( word.getWord().equals(item) ) {
//                containsWord = true;
//                word.setFrequency( word.getFrequency() + 1);
//            }
//        }
//
//        if (!containsWord) words.add(new Word(item, 1));
//    }
//
//    public static void add(String item, int count) {
//        boolean containsWord = false;
//
//        for ( Word word : words ) {
//            if ( word.getWord().equals(item) ) {
//                containsWord = true;
//                word.setFrequency( word.getFrequency() + count );
//            }
//        }
//
//        if (!containsWord) words.add(new Word(item, count));
//    }
//
//    public int getCountOf(String target) {
//        for ( Word word : words ) {
//            if ( word.getWord().equals(target) ) {
//                return word.getFrequency();
//            }
//        }
//
//        return -1;
//    }
//
//    public int getSize() {
//        return words.size();
//    }
//
//    public String getMostFreq() {
//        int largest = 0;
//        String mostFrequent = "";
//
//        for (String word : words) {
//            int count = getCountOf(word);
//            if (count > largest) {
//                largest = count;
//                mostFrequent = word;
//            }
//        }
//        return mostFrequent;
//    }
//
//    public ArrayList<String> getTopMostFreq(int n) {
//        ArrayList<String> mostFreq = new ArrayList<>();
//
//        // TODO: create a copy of the words arraylist
//
//        for (int i = 0; i < n; i++) {
//
//            int largest = 0;
//            String mostFrequent = "";
//
//            for (String word : words) {
//                int count = getCountOf(word);
//                if (count > largest) {
//                    largest = count;
//                    mostFrequent = word;
//                }
//            }
//
//            mostFreq.add(mostFrequent);
//
//            for (String word : words) {
//                if (word.equals(mostFrequent)) {
//                    words.remove(word);
//                }
//            }
//        }
//
//        return mostFreq;
//    }
//
//}
