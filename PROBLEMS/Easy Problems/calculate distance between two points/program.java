import java.util.*;
import java.lang.*;

// pythagoras
// theorem
// distance=(x2−x1)2+(y2−y1)2
class program {
    static double distance(int x1, int y1, int x2, int y2) {
        return Math.round((Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))) * 100000.0) / 100000.0;
    }

    public static void main(String[] args) {
        System.out.println(distance(30, 4, 4, 3));
    }
}