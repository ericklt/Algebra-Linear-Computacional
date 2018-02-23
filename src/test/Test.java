package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;

import model.Generator;
import model.Matrix;
import model.OldschoolMatrix;
import model.StopWatch;
import model.Vector;

class Test {
	
	private static Matrix m1, m1Clone, m2, mGen;
	private static Matrix oldM;
	private static Vector v1, v1Clone, v2, vGen;
	private static Generator generator;
	private static StopWatch w1, w2;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
		
		generator = new Generator(1);
		
		mGen = generator.generateRandomMatrix(3, 100000, 10);
		vGen = generator.generateRandomVector(100000, 5);
		
		oldM = new OldschoolMatrix();
		generator.randomize(oldM, 3, 100000, 10);
	}
	
	@Before
	public void setUp() {
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
		assertEquals(7., v1.dot(v2), 0.01, "Vector dot test");
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
	void testOldschoolTimes() {
		w1.start();
		Vector result = oldM.dot(vGen);
		w1.stop();
		System.out.println("Oldscholl result: " + result);
		
		w2.start();
		oldM.dotByLinearCombination(vGen);
		w2.stop();
		
		System.out.println("(OldScholl) Time simple dot: " + w1.getTime());
		System.out.println("(OldScholl) Time linear combination dot: " + w2.getTime());
	}

}
