package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Vector extends ArrayList<Double> {
	
	private static final long serialVersionUID = 1L;

	public Vector(int size) {
		this.set(size-1, 0.);
	}
	
	public Vector(List<Double> vec) {
		this.clear();
		this.addAll(vec);
	}
	
	public Vector(Double ...values) {
		this(Arrays.asList( values ));
	}
	
	public Double get(int index) {
		if (index >= this.size()) return 0.;
		return super.get(index);
	}
	
	public Double set(int index, Double element) {
		while (index >= this.size()) this.add(0.);
		return super.set(index, element);
	}
	
	public double sumAll() {
		return this.stream().reduce(0., (x, y) -> x + y);
	}

	public Stream<Double> reverseStream() {
		return IntStream.range(0, this.size()).mapToObj(i -> this.get(this.size() - i -1));
	}

	public void swap(int i1, int i2) {
		Double aux = this.get(i1);
		this.set(i1, this.get(i2));
		this.set(i2, aux);
	}
	
	private Vector zippedOperation(Vector v, BinaryOperator<Double> binaryOperator) {
		List<Double> newVec = IntStream.range(0, Math.max(this.size(), v.size()))
				.mapToDouble(i -> binaryOperator.apply(this.get(i), v.get(i)))
				.boxed()
				.collect(Collectors.toList());
		
		return new Vector(newVec);
	}
	
	public Vector elementwiseDot(Vector v) {
		return zippedOperation(v, (x1, x2) -> x1 * x2);
	}
	
	public Vector dot(double d) {
		return this.stream().map(x -> x * d).collect(Collectors.toCollection(Vector::new));
	}
	
	public double dot(Vector v) {
		return this.elementwiseDot(v).sumAll();
	}
	
	public Vector sum(Vector v) {
		return zippedOperation(v, (x1, x2) -> x1 + x2);
	}
	
	public String toFixedSizeString(int n) {
		StringBuilder builder = new StringBuilder();
		builder.append("[\t");
		IntStream.range(0, n).forEach(i -> builder.append(this.get(i)).append("\t"));
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return toFixedSizeString(this.size());
	}

	public void show() {
		this.show(this.size());
	}

	public void show(int n) {
		System.out.println(this.toFixedSizeString(n));
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector)) return false;
		Vector other = (Vector)(o);
		return IntStream.range(0, Math.max(this.size(), other.size())).allMatch(i -> this.get(i).equals(other.get(i)));
	}

	public static Vector base(int size, int onePosition) {
		Vector v = new Vector(size);
		v.set(onePosition, 1.);
		return v;
	}

}
