package model;
import Utils.Pair;

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
	
	public Double get(int i, int j) {
		return this.get(i).get(j);
	}
	
	public Double set(int i, int j, Double value) {
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
		return new Vector(IntStream.range(0, shape[0]).mapToDouble(j -> this.get(j, i)).boxed().collect(Collectors.toList()));
	}
	
	public void addRow(Vector v) {
		this.add(v);
		if (v.size() > shape[1]) shape[1] = v.size();
		shape[0]++;
	}
	
	public void addRow(Double ...values) {
		this.addRow(new Vector(values));
	}
	
	public void addColumn(Vector v) {
		while(shape[0] < v.size()) addRow(new Vector());
		IntStream.range(0, v.size()).forEach(i -> this.get(i).set(shape[1], v.get(i)));
		shape[1]++;
	}
	
	public void addColumn(Double ...values) {
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
		return this.stream().mapToDouble(row -> row.dot(v)).boxed().collect(Collectors.toCollection(Vector::new));
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
	
	public Vector solveBySubstitution(Vector b, Matrix colPermutation) {
		return colPermutation.dot(solveBySubstitution(b));
	}

	public Vector solveBySubstitution(Vector b) {
		Vector result = new Vector(b.size());
		for (int i = 0; i < b.size(); i++)
			result.set(i, (b.get(i) - this.get(i).dot(result)) / this.get(i, i));
		return result;
	}

	public Vector solveByRetroSubstitution(Vector b, Matrix colPermutation) {
		return colPermutation.dot(solveByRetroSubstitution(b));
	}
	
	public Vector solveByRetroSubstitution(Vector b) {
		Vector result = new Vector(b.size());
		for (int i = b.size()-1; i >=0; i--)
			result.set(i, (b.get(i) - this.get(i).dot(result)) / this.get(i, i));
		return result;
	}

	public boolean partialPivoting(int k) {
		return this.partialPivoting(k, new Vector());
	}

	private boolean partialPivoting(int k, Vector b) {
		int greatestRow = -1;
		Double greatest = this.get(k, k);
		for (int i = k+1; i < this.shape[0]; i++) {
			if (this.get(i, k) > greatest) {
				greatestRow = i;
				greatest = this.get(i, k);
			}
		}
		if (greatestRow == -1) return false;

		this.swapRows(k, greatestRow);
		b.swap(k, greatestRow);
		return true;
	}

	private boolean totalPivoting(int k, Vector b, Matrix columnPermutation) {
		int greatestRow = -1;
		int greatestCol = -1;
		Double greatest = this.get(k, k);
		for (int i = k; i < this.shape[0]; i++) {
			for (int j = k; j < this.shape[1]; j++) {
				if (this.get(i, j) > greatest) {
					greatestRow = i;
					greatestCol = j;
					greatest = this.get(i, j);
				}
			}
		}
		if (greatestRow == -1) return false;

		this.swapRows(k, greatestRow);
		b.swap(k, greatestRow);
		this.swapColumns(k, greatestCol);
		columnPermutation.swapRows(k, greatestCol);
		return true;
	}

	public Vector solveByGaussianElimination(Vector b, PivotingMode mode) {
		if (this.shape[0] != this.shape[1]) {
			System.err.println("It is not a square matrix!");
			return null;
		}
		Matrix copy = this.copy();
		b = b.copy();
		Matrix colPermutation = Matrix.identity(copy.shape()[0]);
		for (int k = 0; k < copy.size()-1; k++) {
			if (mode == PivotingMode.PARTIAL)
				copy.partialPivoting(k, b);
			else if (mode == PivotingMode.TOTAL)
				copy.totalPivoting(k, b, colPermutation);

			for (int i = k+1; i < copy.size(); i++) {
				if (copy.get(k, k) == 0) {
					System.err.println("Some pivot is 0, aborting gauss!");
					return null;
				}
				Double multiplier = - copy.get(i, k) / copy.get(k, k);
				copy.set(i, copy.get(i).sum(copy.get(k).dot(multiplier)));
				b.set(i, b.get(i) + b.get(k)*multiplier);
			}
		}
		return copy.solveByRetroSubstitution(b, colPermutation);
	}

	public Double determinant() {
		if (this.shape[0] != this.shape[1]) {
			System.err.println("It is not a square matrix!");
			return null;
		}
		Matrix copy = this.copy();
		int finalMultiplier = 1;
		Double result = 1.;
		for (int k = 0; k < copy.size(); k++) {
			boolean swapped = copy.partialPivoting(k);
			finalMultiplier *= (swapped)? -1 : 1;

			for (int i = k+1; i < copy.size(); i++) {
				if (copy.get(k, k) == 0) {
					return 0.;
				}
				Double multiplier = - copy.get(i, k) / copy.get(k, k);
				copy.set(i, copy.get(i).sum(copy.get(k).dot(multiplier)));
			}
			result *= copy.get(k, k);
		}
		return result * finalMultiplier;
	}

	public Matrix choleskyDecomposition() {
		if (this.shape[0] != this.shape[1]) {
			System.err.println("It is not a square matrix!");
			return null;
		} else if (!this.equals(this.T())) {
			System.err.println("It is not a symmetric matrix!");
			return null;
		}
		Matrix result = new Matrix();
		for (int i = 0; i < this.shape[0]; i++) {
			for (int j = 0; j <= i; j++) {
				Double term = 0.;
				for (int k = 0; k < j; k++)
					term += result.get(i, k) * result.get(j, k);
				term = this.get(i, j) - term;

				if (i == j) {
					if (term <= 0) {
						System.err.println("Matrix is not positive definite");
						return null;
					} else {
						result.set(i, j, Math.sqrt(term));
					}
				} else {
					result.set(i, j, term / result.get(j, j));
				}
			}
		}
		return result;
	}

	public Vector solveByCholesky(Vector b) {
		Matrix cho = this.choleskyDecomposition();
		return cho.T().solveByRetroSubstitution(cho.solveBySubstitution(b));
	}

	public Pair<Matrix> LUDecomposition() {
		if (this.shape[0] != this.shape[1]) {
			System.err.println("It is not a square matrix!");
			return null;
		}
		Matrix L = Matrix.identity(this.size());
		Matrix U = this.copy();

		for (int k = 0; k < U.size()-1; k++) {
			for (int i = k+1; i < U.size(); i++) {
				if (U.get(k, k) == 0) {
					System.err.println("Some pivot is 0, aborting LU!");
					return null;
				}
				Double multiplier = - U.get(i, k) / U.get(k, k);
				U.set(i, U.get(i).sum(U.get(k).dot(multiplier)));
				L.set(i, k, -multiplier);
			}
		}
		return new Pair<>(L, U);
	}

	public Vector solveByLU(Vector b) {
		Pair<Matrix> LU = this.LUDecomposition();
		return LU._2().solveByRetroSubstitution(LU._1().solveBySubstitution(b));
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
