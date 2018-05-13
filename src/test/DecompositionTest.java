package test;
import static org.junit.jupiter.api.Assertions.*;

import Utils.Pair;
import Utils.Triple;
import decomposition.SVD;
import model.PivotingMode;
import org.junit.jupiter.api.BeforeAll;

import model.Matrix;
import model.Vector;
import solvers.CholeskySolver;
import solvers.GaussianEliminationSolver;
import solvers.LUSolver;
import solvers.Solver;

class DecompositionTest {

    private static Matrix A;
    private static Vector b;
    private static Solver gaussianSolver;
    private static Solver choleskySolver;
    private static Solver luSolver;

    @BeforeAll
    static void setUpBeforeClass() {
        gaussianSolver = new GaussianEliminationSolver(PivotingMode.NONE);
        choleskySolver = new CholeskySolver();
        luSolver = new LUSolver();
        A = new Matrix();
        A.addRow(20., 10., -4., -10.);
        A.addRow(10., 50., 1., -8.);
        A.addRow(-4., 1., 100., 5.);
        A.addRow(-10., -8., 5., 200.);

        b = new Vector(20., 1., 4., 800.);
    }

    @org.junit.jupiter.api.Test
    void choleskyTest() {
        assertEquals(gaussianSolver.solve(A, b), choleskySolver.solve(A, b), "Solving by Cholesky");
    }

    @org.junit.jupiter.api.Test
    void LUTest() {
        assertEquals(gaussianSolver.solve(A, b), luSolver.solve(A, b), "Solving by LU");
    }

    @org.junit.jupiter.api.Test
    void SVDTest() {
        Triple<Matrix, Matrix, Matrix> svd = SVD.decompose(A);
        System.out.println(svd);
        assertEquals(A, svd._1().dot(svd._2().dot(svd._3())), "SVD Equals");

        Matrix A = new Matrix();
        A.addRow(1, 2);
        A.addRow(-3, 4);
        A.addRow(0, 2);
        A.addRow(5, 6);

        svd = SVD.decompose(A);
        System.out.println(svd);
        assertEquals(A, svd._1().dot(svd._2().dot(svd._3())), "SVD Equals2");

        svd = SVD.decompose(A.T());
        System.out.println(svd);
        assertEquals(A.T(), svd._1().dot(svd._2().dot(svd._3())), "SVD Equals2");
    }

}
