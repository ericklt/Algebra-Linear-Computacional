package model;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
	
	private Random r = new Random();
	
	public Generator(long seed) {
		r = new Random(seed);
	}
	
	public Vector generateRandomVector(int n, double maxAbsValue) {
		return new Vector(r.doubles(n).mapToObj(d -> new Complex(d*maxAbsValue)).collect(Collectors.toList()));
	}
	
	public void randomize(Matrix matrix, int M, int N, double maxAbsValue) {
		IntStream.range(0, M).forEach(i -> matrix.set(i, generateRandomVector(N, maxAbsValue)));
	}
	
	public Matrix generateRandomMatrix(int M, int N, double maxAbsValue) {
		Matrix newMatrix = new Matrix();
		randomize(newMatrix, M, N, maxAbsValue);
		return newMatrix;
	}

}
