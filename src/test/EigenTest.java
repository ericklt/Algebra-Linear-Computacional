package test;

import Utils.Pair;
import eigen.DislocatedPotencyMethod;
import eigen.EigenSearcher;
import eigen.InversePotencyMethod;
import eigen.PotencyMethod;
import model.Matrix;
import model.PivotingMode;
import model.Vector;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EigenTest {

    private static final double eps = 0.000001;
    private static Matrix A;
    private static Vector start;

    @BeforeAll
    static void setUpBeforeClass() {
        A = new Matrix();
        A.addRow(-2, -4, 2);
        A.addRow(-2, 1, 2);
        A.addRow(4, 2, 5);

        start = new Vector(1, 1, 1);
    }

    @org.junit.jupiter.api.Test
    void potencyTest() {
        EigenSearcher searcher = new PotencyMethod(eps);
        Pair<Double, Vector> eigens = searcher.findEigen(A, start);
        assertEquals(6, eigens._1(), eps, "Potency");
    }

    @org.junit.jupiter.api.Test
    void inversePotencyTest() {
        EigenSearcher searcher = new InversePotencyMethod(eps);
        Pair<Double, Vector> eigens = searcher.findEigen(A, start);
        assertEquals(3, eigens._1(), eps, "Inverse");
    }

    @org.junit.jupiter.api.Test
    void dislocatedPotencyTest() {
        EigenSearcher searcher = new DislocatedPotencyMethod(eps, -4.5);
        Pair<Double, Vector> eigens = searcher.findEigen(A, start);
        assertEquals(-5, eigens._1(), eps, "Dislocated");
    }

    @org.junit.jupiter.api.Test
    void peresTest() {
        Matrix p = new Matrix();
        p.addRow(1, 2, 3);
        p.addRow(4, 5, 6);
        p.addRow(7, 8, 10);
        EigenSearcher searcher1 = new DislocatedPotencyMethod(eps, 11);
        EigenSearcher searcher2 = new DislocatedPotencyMethod(eps, 0.3);
        EigenSearcher searcher3 = new DislocatedPotencyMethod(eps, -5);
        Pair<Double, Vector> eigens1 = searcher1.findEigen(p, start);
        Pair<Double, Vector> eigens2 = searcher2.findEigen(p, start);
        Pair<Double, Vector> eigens3 = searcher3.findEigen(p, start);
        System.out.println(eigens1);
        System.out.println(eigens2);
        System.out.println(eigens3);
    }

}
