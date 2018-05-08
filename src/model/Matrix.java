package model;
import solvers.GaussianEliminationSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Matrix extends ArrayList<Vector> {
	
	private static final long serialVersionUID = 1L;

	protected int[] shape = new int[] {0, 0};
	
	public Matrix(List<Vector> matrix) {
		matrix.forEach(this::addRow);
	}
	
	public Matrix(Vector ...vectors) {
		this(Arrays.asList(vectors));
	}

	public Matrix copy() {
		Matrix m = new Matrix();
		this.forEach(row -> m.addRow(row.copy()));
		return m;
	}
	
	public int[] shape() {
		return shape;
	}
	
	public Complex get(int i, int j) {
		return this.get(i).get(j);
	}
	
	public Complex set(int i, int j, Complex value) {
		while(shape[0] <= i) addRow(new Vector());
		while(shape[1] <= j) addColumn(new Vector());
		return this.get(i).set(j, value);
	}
	
	public Vector set(int i, Vector v) {
		while(shape[0] <= i) addRow(new Vector());
		if (v.size() > shape[1]) shape[1] = v.size();
		return super.set(i, v);
	}
	
	public Vector get(int i) {
		if (i >= shape[0]) return new Vector();
		return super.get(i);
	}
	
	public Vector getColumn(int i) {
		return new Vector(IntStream.range(0, shape[0]).mapToObj(j -> this.get(j, i)).collect(Collectors.toList()));
	}
	
	public void addRow(Vector v) {
		this.add(v);
		if (v.size() > shape[1]) shape[1] = v.size();
		shape[0]++;
	}
	
	public void addRow(double ...values) {
		this.addRow(new Vector(values));
	}
	
	public void addColumn(Vector v) {
		while(shape[0] < v.size()) addRow(new Vector());
		IntStream.range(0, v.size()).forEach(i -> this.get(i).set(shape[1], v.get(i)));
		shape[1]++;
	}
	
	public void addColumn(double ...values) {
		this.addColumn(new Vector(values));
	}

	public void setColumn(int j, Vector v) {
		IntStream.range(0, this.shape[0]).forEach(i -> this.set(i, j, v.get(i)));
	}

	public void swapRows(int l1, int l2) {
		Vector aux = this.get(l1);
		this.set(l1, this.get(l2));
		this.set(l2, aux);
	}

	public void swapColumns(int c1, int c2) {
		Vector aux = this.getColumn(c1);
		this.setColumn(c1, this.getColumn(c2));
		this.setColumn(c2, aux);
	}

	public Stream<Vector> reverseStream() {
		return IntStream.range(0, this.shape[0]).mapToObj(i -> this.get(this.shape[0] - i -1));
	}

	private IntStream reverseRange(int start, int end) {
		return IntStream.range(start, end).map(i -> end - i + start - 1);
	}

	public Matrix subRows(int start, int end) {
		Matrix result = new Matrix();
		IntStream.range(start, end).forEach(i -> result.addRow(get(i)));
		return result;
	}

	public Matrix subCols(int start, int end) {
		return this.T().subRows(start, end).T();
	}
	
	public Matrix T() {
		Matrix transposed = new Matrix();
		this.forEach(transposed::addColumn);
		return transposed;
	}

	public Matrix sum(Matrix m) {
		Matrix result = new Matrix();
		IntStream.range(0, shape[0]).mapToObj(i -> this.get(i).sum(m.get(i))).forEach(result::addRow);
		return result;
	}

	public Matrix subtract(Matrix m) {
		Matrix result = new Matrix();
		IntStream.range(0, shape[0]).mapToObj(i -> this.get(i).subtract(m.get(i))).forEach(result::addRow);
		return result;
	}

	public Matrix dot(double d) {
		Matrix result = new Matrix();
		this.stream().map(v -> v.dot(d)).forEach(result::addRow);
		return result;
	}
	
	public Vector dot(Vector v) {
		return this.stream().map(row -> row.dot(v)).collect(Collectors.toCollection(Vector::new));
	}
	
	public Vector dotByLinearCombination(Vector v) {
		Matrix mT = this.T();
		return IntStream.range(0, v.size())
				.mapToObj(i -> mT.get(i).dot(v.get(i)))
				.reduce(Vector::sum)
				.get();
	}
	
	public Matrix dot(Matrix m) {
		Matrix result = new Matrix();
		this.stream().map(m.T()::dot).forEach(result::addRow);
		return result;
	}

	public Complex determinant() {
		if (this.shape[0] != this.shape[1]) {
			System.err.println("It is not a square matrix!");
			return null;
		}
		Matrix copy = this.copy();
		int finalMultiplier = 1;
		Complex result = new Complex(1);
		for (int k = 0; k < copy.size(); k++) {
			boolean swapped = GaussianEliminationSolver.partialPivoting(copy, k);
			finalMultiplier *= (swapped)? -1 : 1;

			for (int i = k+1; i < copy.size(); i++) {
				if (copy.get(k, k).equals(new Complex(0))) {
					return new Complex();
				}
				Complex multiplier = copy.get(i, k).div(copy.get(k, k)).mul(-1);
				copy.set(i, copy.get(i).sum(copy.get(k).dot(multiplier)));
			}
			result = result.mul(copy.get(k, k));
		}
		return result.mul(finalMultiplier);
	}

	public Vector getDiagonalAsVector() {
		return new Vector(IntStream.range(0, shape[0]).mapToObj(i -> this.get(i, i)).collect(Collectors.toList()));
	}

	public double errorBelowDiagonal() {
		return Math.sqrt(
				IntStream.range(0, shape[1])
						.mapToObj(j -> IntStream.range(j+1, shape[0])
								.mapToObj(i -> this.get(i, j))
								.mapToDouble(Complex::mulByConjugate)
								.sum())
						.mapToDouble(x -> x*x)
						.sum()
		);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[\n");
		this.forEach(row -> builder.append("\t").append(row.toFixedSizeString(shape[1])).append("\n"));
		builder.append("]");
		return builder.toString();
	}

	public void show() {
		System.out.println(this);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Matrix)) return false;
		Matrix other = (Matrix)(o);
		return IntStream.range(0, Math.max(this.size(), other.size())).allMatch(i -> this.get(i).equals(other.get(i)));
	}

	public static Matrix identity(int size) {
		Matrix m = new Matrix();
		IntStream.range(0, size).mapToObj(i -> Vector.base(size, i)).forEach(m::addRow);
		return m;
	}

}
