import stdlib.In;
import stdlib.StdIn;
import stdlib.StdOut;

public class Spell {
    // Entry point.
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] lines = in.readAllLines();
        in.close();

        // Create an ArrayST<String, String> object called st.
        ArrayST<String, String> st = new ArrayST<>();

        // For each line in lines, split it into two tokens using "," as delimiter; insert into
        // st the key-value pair (token 1, token 2).
        for (String line : lines){
            String[] token = line.split(",");
            st.put(token[0], token[1]);
        }


        // Read from standard input one line at a time; increment a line number counter; split
        // the line into words using "\\b" as the delimiter; for each word in words, if it
        // exists in st, write the (misspelled) word, its line number, and corresponding value
        // (correct spelling) from st.
        int count = 0;
        while (!StdIn.isEmpty()) {
            String line = StdIn.readLine();
            count++;
            String[] words = line.split("\\b");
            for (String word : words) {
                if (st.contains(word)) {
                    StdOut.println(word + ":" + count + " -> " + st.get(word));
                }
            }
        }
    }
}
