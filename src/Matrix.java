import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Matrix extends ArrayList<Vector> {
	
	private static final long serialVersionUID = 1L;

	private int[] shape = new int[] {0, 0};
	
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
	
	public Vector getRow(int i) {
		return this.get(i);
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
	
	public Matrix T() {
		Matrix transposed = new Matrix();
		this.forEach(row -> transposed.addColumn(row));
		return transposed;
	}
	
	public Vector dot(Vector v) {
		return new Vector(this.stream().mapToDouble(row -> row.dot(v)).boxed().collect(Collectors.toList()));
	}
	
	public Matrix dot(Matrix m) {
		Matrix mT = m.T();
		return new Matrix(this.stream().map(row -> mT.dot(row)).collect(Collectors.toList()));
	}
	
	public Matrix dot_oldscholl(Matrix m) {
		Matrix result = new Matrix();
		for (int i = 0; i < shape[0]; i++) {
			for (int j = 0; j < m.shape()[1]; j++) {
				double sum = 0;
				for (int k = 0; k < shape[1]; k++) {
					sum += this.get(i, k) * m.get(k, j);
				}
				result.set(i, j, sum);
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		this.forEach(row -> builder.append(row.toFixedSizeString(shape[1]) + "\n"));
		return builder.toString();
	}

}
