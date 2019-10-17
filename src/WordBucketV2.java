import java.util.ArrayList;

public class WordBucketV2 {
    private static ArrayList<Word> data = new ArrayList<>();

    public WordBucketV2() {

    }

    public void add(String item) {
        add(item, 1);
    }

    public void add(String item, int count) {
        int i = 0;
        Word word = data.get(i);

        while ( !word.getWord().equals(item) && i < data.size() - 1) {
            i++;
            word = data.get(i);
        }

        if ( word.getWord().equals(item) ) {
            word.setFrequency( count );
            sort(i, word.getFrequency());
        } else {
            data.add(new Word(item, 1));
        }
    }

    private void sort(int i, int frequency) {
        while ( frequency > data.get(i - 1).getFrequency() && i > 0) {
            Word word = data.remove( i );
            data.add(i - 1, word);
        }
    }

    public int getCountOf(String target) {
        for ( Word word : data) {
            if ( word.getWord().equals(target) ) {
                return word.getFrequency();
            }
        }

        return -1;
    }

    public int getSize() {
        return data.size();
    }

    public Word getMostFreq() {
        return data.get(0);
    }

    public ArrayList<Word> getTopMostFreq(int n) {
        ArrayList<Word> mostFreq = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            mostFreq.add( data.get(i) );
        }

        return mostFreq;
    }

}
