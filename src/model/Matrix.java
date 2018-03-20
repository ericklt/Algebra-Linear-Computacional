package model;
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
	
	public Matrix T() {
		Matrix transposed = new Matrix();
		this.forEach(transposed::addColumn);
		return transposed;
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

	private void partialPivoting(int k, Vector b) {
		int greatestRow = -1;
		Double greatest = this.get(k, k);
		for (int i = k+1; i < this.shape[0]; i++) {
			if (this.get(i, k) > greatest) {
				greatestRow = i;
				greatest = this.get(i, k);
			}
		}
		if (greatestRow != -1) {
			this.swapRows(k, greatestRow);
			b.swap(k, greatestRow);
		}
	}

	private void totalPivoting(int k, Vector b, Matrix columnPermutation) {
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
		if (greatestRow != -1) {
			this.swapRows(k, greatestRow);
			b.swap(k, greatestRow);
			this.swapColumns(k, greatestCol);
			columnPermutation.swapRows(k, greatestCol);
		}
	}

	public Vector solveByGaussianElimination(Vector b, PivotingMode mode) {
		Matrix colPermutation = Matrix.identity(this.shape[0]);
		for (int k = 0; k < this.size()-1; k++) {
			if (mode == PivotingMode.PARTIAL)
				partialPivoting(k, b);
			else if (mode == PivotingMode.TOTAL)
				totalPivoting(k, b, colPermutation);

			for (int i = k+1; i < this.size(); i++) {
				if (this.get(k, k) == 0) {
					System.err.println("Some pivot is 0, aborting gauss!");
					return null;
				}
				Double multiplier = - this.get(i, k) / this.get(k, k);
				this.set(i, this.get(i).sum(this.get(k).dot(multiplier)));
				b.set(i, b.get(i) + b.get(k)*multiplier);
			}
		}
		return solveByRetroSubstitution(b, colPermutation);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		this.forEach(row -> builder.append(row.toFixedSizeString(shape[1])).append("\n"));
		return builder.toString();
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
