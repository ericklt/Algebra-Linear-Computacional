package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Vector extends ArrayList<Double> {
	
	private static final long serialVersionUID = 1L;
	
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
		return new Vector(this.stream().map(x -> x*d).collect(Collectors.toList()));
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
		IntStream.range(0, n).forEach(i -> builder.append(this.get(i) + "\t"));
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return toFixedSizeString(this.size());
	}
	
	@Override
	public boolean equals(Object o) {
		Vector other = (Vector)(o);
		return IntStream.range(0, Math.max(this.size(), other.size())).allMatch(i -> this.get(i).equals(other.get(i)));
	}

}
