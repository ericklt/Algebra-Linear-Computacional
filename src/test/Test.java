package test;

import static org.junit.jupiter.api.Assertions.*;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;

import Utils.StopWatch;

class Test {
	
	private static Matrix m1, m1Clone, m2, mGen;
	private static Matrix oldM;
	private static Vector v1, v1Clone, v2, vGen;
	private static StopWatch w1, w2;

	@BeforeAll
	static void setUpBeforeClass() {
		w1 = new StopWatch();
		w2 = new StopWatch();

		v1 = new Vector(2., 4., -1., 2., 3.);
		v1Clone = new Vector(2., 4., -1., 2., 3.);
		v2 = new Vector(-1., 2., -5., -2.);
		
		m1 = new Matrix();
		m1.addRow(1., 2., 3.);
		m1.addRow(4., 5., 6.);
		
		m1Clone = new Matrix();
		m1Clone.addRow(1., 2., 3.);
		m1Clone.addRow(4., 5., 6.);

		m2 = new Matrix();
		m2.addColumn(7., 8., 9.);
		m2.addColumn(10., 11., 12.);

		Generator generator = new Generator(1);
		
		mGen = generator.generateRandomMatrix(3, 100000, 10);
		vGen = generator.generateRandomVector(100000, 5);
		
		oldM = new OldSchoolMatrix();
		generator.randomize(oldM, 3, 100000, 10);
	}
	
	@BeforeEach
	void setUp() {
		w1.reset();
		w2.reset();
	}
	
	@org.junit.jupiter.api.Test
	void testEquals() {
		assertEquals(v1, v1Clone, "Vector equals");
		assertEquals(m1, m1Clone, "Matrix equals");
	}

	@org.junit.jupiter.api.Test
	void testVectorDot() {
		assertEquals(new Complex(7), v1.dot(v2), "Vector dot test");
	}

	@org.junit.jupiter.api.Test
	void testMatrixVectorDot() {
		Matrix m = Matrix.parse("[[3, 4], [1, 6]]");
		Vector v = Vector.parse("[7, 2]");
		assertEquals(new Vector(29, 19), m.dot(v), "Matrix-Vector dot test");
	}

	@org.junit.jupiter.api.Test
	void testMatrixMatrixDot() {
		Matrix m1 = Matrix.parse("[[3, 4], [1, 6]]");
		Matrix m2 = Matrix.parse("[[7, 2], [-2, 5]]");
		assertEquals(Matrix.parse("[[13, 26], [-5, 32]]"), m1.dot(m2), "Matrix-Matrix dot test");
	}

	@org.junit.jupiter.api.Test
	void testTimes() {
		w1.start();
		Vector result = mGen.dot(vGen);
		w1.stop();
		System.out.println("Functional result: " + result);
		
		w2.start();
		mGen.dotByLinearCombination(vGen);
		w2.stop();

		System.out.println("(Functional) Time simple dot: " + w1.getTime());
		System.out.println("(Functional) Time linear combination dot: " + w2.getTime());
	}
	
	@org.junit.jupiter.api.Test
	void testOldSchoolTimes() {
		w1.start();
		Vector result = oldM.dot(vGen);
		w1.stop();
		System.out.println("OldSchool result: " + result);
		
		w2.start();
		oldM.dotByLinearCombination(vGen);
		w2.stop();
		
		System.out.println("(OldSchool) Time simple dot: " + w1.getTime());
		System.out.println("(OldSchool) Time linear combination dot: " + w2.getTime());
	}

//	@org.junit.jupiter.api.Test
//	void testDot() {
//		Matrix m1 = new Matrix();
//		m1.addRow(1, 0, 0, 0);
//		m1.addRow(0, 1, 0, 0);
//		m1.addRow(0, 0, -0.6, -0.8);
//		m1.addRow(0, 0, -0.8, 0.6);
//
//		Matrix m2 = new Matrix();
//		m2.addRow(4, -3, 0, 0);
//		m2.addRow(-3, 3.33333, -1.666666, 0);
//		m2.addRow(0, 1, 0.066666, -2.133333);
//		m2.addRow(0, 1.333333, 1.6, 0.4666666666);
//
//		m1.dot(m2).show();
//	}

	@org.junit.jupiter.api.Test
	void testMatrixParse() {
		Matrix m = Matrix.parse("[[1, 2, 3], [2, 3, 4], [5, 6, 1]]");
		m.show();
		m = Matrix.parse("[[1], [2], [3], [-2]]");
		m.show();
	}

}
