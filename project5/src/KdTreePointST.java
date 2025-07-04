import dsa.LinkedQueue;
import dsa.MaxPQ;
import dsa.Point2D;
import dsa.RectHV;
import stdlib.StdIn;
import stdlib.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    Node root;
    int n;

    // Constructs an empty symbol table.
    public KdTreePointST() {
        root = null;
    }

    // Returns true if this symbol table is empty, and false otherwise.
    public boolean isEmpty() {
        return root == null;
    }

    // Returns the number of key-value pairs in this symbol table.
    public int size() {
        return n;
    }

    // Inserts the given point and value into this symbol table.
    public void put(Point2D p, Value value) {
        if (p == null){
            throw new NullPointerException("p is null");
        }
        if (value == null){
            throw new NullPointerException("value is null");
        }
        double PosINF = Double.POSITIVE_INFINITY;
        double NegINF = Double.NEGATIVE_INFINITY;
        root = put(root, p, value, new RectHV(NegINF, NegINF, PosINF, PosINF), true);
    }

    // Returns the value associated with the given point in this symbol table, or null.
    public Value get(Point2D p) {
        if ( p == null){
            throw new NullPointerException("p is null");
        }
        return get(root, p, true);
    }

    // Returns true if this symbol table contains the given point, and false otherwise.
    public boolean contains(Point2D p) {
        if (p == null){
            throw new NullPointerException("p is null");
        }
        return get(p) != null;
    }

    // Returns all the points in this symbol table.
    public Iterable<Point2D> points() {
        LinkedQueue<Point2D> points = new LinkedQueue<>();
        LinkedQueue<Node> traversal = new LinkedQueue<>();
        traversal.enqueue(root);
        while (!traversal.isEmpty()){
            Node temp = traversal.dequeue();
            if (temp != null){
                points.enqueue(temp.p);
                traversal.enqueue(temp.lb);
                traversal.enqueue(temp.rt);
            }
        }
        return points;
    }

    // Returns all the points in this symbol table that are inside the given rectangle.
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null){
            throw new NullPointerException("rect is null");
        }
        LinkedQueue<Point2D> q = new LinkedQueue<Point2D>();
        range(root, rect, q);
        return q;
    }

    // Returns the point in this symbol table that is different from and closest to the given point,
    // or null.
    public Point2D nearest(Point2D p) {
        if (p == null){
            throw new NullPointerException("p is null");
        }
        return nearest(root, p, new Point2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), true);
    }

    // Returns up to k points from this symbol table that are different from and closest to the
    // given point.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        if (p == null){
            throw new NullPointerException("p is null");
        }
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, pq, true);
        return pq;
    }

    // Note: In the helper methods that have lr as a parameter, its value specifies how to
    // compare the point p with the point x.p. If true, the points are compared by their
    // x-coordinates; otherwise, the points are compared by their y-coordinates. If the
    // comparison of the coordinates (x or y) is true, the recursive call is made on x.lb;
    // otherwise, the call is made on x.rt.

    // Inserts the given point and value into the KdTree x having rect as its axis-aligned
    // rectangle, and returns a reference to the modified tree.
    private Node put(Node x, Point2D p, Value value, RectHV rect, boolean lr)  {
        if (x == null){
            n++;
            return new Node(p, value, rect);
        }
        if (p.equals(x.p)){
            return x;
        }
        double cmp = lr ? p.x() - x.p.x() : p.y() - x.p.y();
        if (lr == true && cmp < 0) {
            x.lb = put(x.lb, p, value, new RectHV(rect.xMin(), rect.yMin(), x.p.x(), rect.yMax()), !lr);
        }
        else if (lr == true && cmp >= 0){
            x.rt = put(x.rt, p, value, new RectHV(x.p.x(), rect.yMin(), rect.xMax(), rect.yMax()), !lr);
        }
        if (lr == false && cmp < 0){
            x.lb = put(x.lb, p, value, new RectHV(rect.xMin(), rect.yMin(), rect.xMax(), x.p.y()), !lr);
        }
        else if (lr == false && cmp >= 0){
            x.rt = put(x.rt, p, value, new RectHV(rect.xMin(), x.p.y(), rect.xMax(), rect.yMax()), !lr);
        }
        return x;
    }

    // Returns the value associated with the given point in the KdTree x, or null.
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) {
            return null;
        }
        if (p.equals(x.p)){
            return x.value;
        }
        double cmp = lr ? p.x() - x.p.x() : p.y() - x.p.y();
        if (cmp < 0){
            return get(x.lb, p, !lr);
        }
        else if (cmp > 0){
            return get(x.rt, p, !lr);
        }
        return x.value;

    }

    // Collects in the given queue all the points in the KdTree x that are inside rect.
    private void range(Node x, RectHV rect, LinkedQueue<Point2D> q) {
        if (x == null){
            return;
        }
        if (rect.contains(x.p)){
            q.enqueue(x.p);
        }
        if (!x.rect.intersects(rect)){
            return;
        }
        range(x.lb, rect, q);
        range(x.rt, rect, q);
    }

    // Returns the point in the KdTree x that is closest to p, or null; nearest is the closest
    // point discovered so far.
    private Point2D nearest(Node x, Point2D p, Point2D nearest, boolean lr) {
        if (x == null || nearest.distanceSquaredTo(p) < x.rect.distanceSquaredTo(p) ){
            return nearest;
        }
        double xdis = x.p.distanceSquaredTo(p);
        double neardis = nearest.distanceSquaredTo(p);
        if (!p.equals(x.p) && xdis < neardis){
            nearest = x.p;
        }
        double cmp = lr ? p.x() - x.p.x() : p.y() - x.p.y();
        Node first = (cmp < 0) ? x.lb : x.rt;
        Node second = (cmp < 0) ? x.rt : x.lb;

        nearest = nearest(first, p, nearest, !lr);
        nearest = nearest(second, p, nearest, !lr);

        return nearest;
    }

    // Collects in the given max-PQ up to k points from the KdTree x that are different from and
    // closest to p.
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq, boolean lr) {
        if (x == null || pq.size() > k && pq.max().distanceSquaredTo(p) < x.rect.distanceSquaredTo(p)){
            return;
        }
        if (!p.equals(x.p)) {
            pq.insert(x.p);
            if (pq.size() > k) {
                pq.delMax();
            }
        }
        double cmp = lr ? p.x() - x.p.x() : p.y() - x.p.y();
        if (cmp < 0) {
            nearest(x.lb, p, k, pq, !lr);
            nearest(x.rt, p, k, pq, !lr);
        }
        else if (cmp >= 0){
            nearest(x.lb, p, k, pq, !lr);
            nearest(x.rt, p, k, pq, !lr);
        }

    }

    // A representation of node in a KdTree in two dimensions (ie, a 2dTree). Each node stores a
    // 2d point (the key), a value, an axis-aligned rectangle, and references to the left/bottom
    // and right/top subtrees.
    private class Node {
        private Point2D p;   // the point (key)
        private Value value; // the value
        private RectHV rect; // the axis-aligned rectangle
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Constructs a node given the point (key), the associated value, and the
        // corresponding axis-aligned rectangle.
        Node(Point2D p, Value value, RectHV rect) {
            this.p = p;
            this.value = value;
            this.rect = rect;
        }
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        int k = Integer.parseInt(args[2]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(-1, -1, 1, 1);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.printf("st.contains(%s)? %s\n", query, st.contains(query));
        StdOut.printf("st.range(%s):\n", rect);
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.printf("st.nearest(%s) = %s\n", query, st.nearest(query));
        StdOut.printf("st.nearest(%s, %d):\n", query, k);
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
