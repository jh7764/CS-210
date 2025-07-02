import dsa.LinkedStack;
import stdlib.StdIn;
import stdlib.StdOut;

public class Sort {
    // Entry point.
    public static void main(String[] args) {
        LinkedDeque<String> d = new LinkedDeque<>();
        while (!StdIn.isEmpty()) {
            String w = StdIn.readString();
            boolean value = false;

            //Adds w to the front of the d if it is less than the first word in d
            if (!d.isEmpty() && less(w, d.peekFirst())) {
                d.addFirst(w);
                value = true;
            }

            //Adds w to the back of d if it is greater than the last word in d
            if (!d.isEmpty() && less(d.peekLast(), w)) {
                d.addLast(w);
                value = true;
            }

            //Adds the words that less that w in front of d
            else if (!value) {
                LinkedStack<String> s = new LinkedStack<>();
                while (!d.isEmpty() && less(d.peekFirst(), w)) {
                    s.push(d.removeFirst());
                }
                d.addFirst(w);
                while (!s.isEmpty()) {
                    d.addFirst(s.pop());
                }
            }
        }

        //Prints words from d to standard output
        while (!d.isEmpty()) {
            StdOut.println(d.removeFirst());
        }
    }

    // Returns true if v is less than w according to their lexicographic order, and false otherwise.
    private static boolean less(String v, String w){
            return v.compareTo(w) < 0;
    }
}



