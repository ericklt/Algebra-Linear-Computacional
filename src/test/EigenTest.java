package test;

import Utils.Pair;
import eigen.*;
import model.Complex;
import model.Matrix;
import model.PivotingMode;
import model.Vector;
import org.junit.jupiter.api.BeforeAll;
import solvers.GaussianEliminationSolver;
import solvers.LUSolver;

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
        EigenSearcher searcher = new PotencyMethod(start, eps);
        Pair<Complex, Vector> eigens = searcher.findEigen(A);
        assertEquals(new Complex(6), eigens._1(), "Potency");
        eigens._2().show();
    }

    @org.junit.jupiter.api.Test
    void inversePotencyTest() {
        EigenSearcher searcher = new InversePotencyMethod(start, eps);
        Pair<Complex, Vector> eigens = searcher.findEigen(A);
        assertEquals(new Complex(3), eigens._1(), "Inverse");
        eigens._2().show();
    }

    @org.junit.jupiter.api.Test
    void dislocatedPotencyTest() {
        EigenSearcher searcher = new DislocatedPotencyMethod(start, eps, -4.5);
        Pair<Complex, Vector> eigens = searcher.findEigen(A);
        assertEquals(new Complex(-5), eigens._1(), "Dislocated");
        eigens._2().show();
    }

    @org.junit.jupiter.api.Test
    void peresTest() {
        Matrix p = new Matrix();
        p.addRow(1, 2, 3);
        p.addRow(4, 5, 6);
        p.addRow(7, 8, 10);
        EigenSearcher searcher1 = new DislocatedPotencyMethod(start, eps, 11);
        EigenSearcher searcher2 = new DislocatedPotencyMethod(start, eps, 0.3);
        EigenSearcher searcher3 = new DislocatedPotencyMethod(start, eps, -5);
        Pair<Complex, Vector> eigens1 = searcher1.findEigen(p);
        Pair<Complex, Vector> eigens2 = searcher2.findEigen(p);
        Pair<Complex, Vector> eigens3 = searcher3.findEigen(p);
        System.out.println(eigens1);
        System.out.println(eigens2);
        System.out.println(eigens3);
    }

    @org.junit.jupiter.api.Test
    void transformationTest() {
        Matrix big = Matrix.parse("[[5, -3, 2, -5, 1], [8, 10, 4, 3, 2], [-4, -5, 1, 0, 10], [2, -2, 0, -6, -1], [1, 2, 3, 4, 5]]");
        System.out.println(TransformationMethod.findAllEigens(big));
    }

    @org.junit.jupiter.api.Test
    void transformationTestPeres() {
        Matrix big = Matrix.parse("[[1, 1, 3 ,2, 5], [6, 7, 8, 9, 10], [2, 3, 3, 4, 5], [1, 3, 1, 1, 1], [2, 1, 1, 3, 5]]");
        System.out.println(TransformationMethod.findAllEigens(big));
    }

    @org.junit.jupiter.api.Test
    void peresTestWithTransformation() {
        Matrix p = Matrix.parse("[[1, 2, 3], [4, 5, 6], [7, 8, 10]]");
        System.out.println(TransformationMethod.findAllEigens(p));
    }

    @org.junit.jupiter.api.Test
    void complexEigensTransformationTest() {
        Matrix p = Matrix.parse("[[-2, -2, -9], [-1, 1, -3], [1, 1, 4]]");
        System.out.println(TransformationMethod.findAllEigens(p));
    }

}
