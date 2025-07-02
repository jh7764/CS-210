import stdlib.StdOut;

public class Sample {
    // Entry point.
    public static void main(String[] args) {

        //Accepts command-line arguments
        int lo = Integer.parseInt(args[0]);
        int hi = Integer.parseInt(args[1]);
        int k = Integer.parseInt(args[2]);
        String mode = args[3];

        //Creates random q
        if (!mode.equals("+") && !mode.equals("-")){
            throw new IllegalArgumentException("Illegal mode");
        }
        ResizingArrayRandomQueue<Integer> q = new ResizingArrayRandomQueue<>();
        for (int i = lo; i <= hi; i++){
            q.enqueue(i);
        }

        //Sampling with replacement
        if (mode.equals("+")){
            for (int i = 0; i < k; i++){
                StdOut.println(q.sample());
            }
        }

        //Sampling without replacement
        else if (mode.equals("-")) {
            for (int i = 0; i < k; i++){
                StdOut.println(q.dequeue());
            }
        }

    }
}
