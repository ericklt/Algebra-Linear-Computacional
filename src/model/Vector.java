package model;
import Utils.MyMath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Vector extends ArrayList<Complex> {
	
	private static final long serialVersionUID = 1L;

	public Vector(int size) {
		this.set(size-1, new Complex());
	}
	
	public Vector(List<Complex> vec) {
		this.clear();
		this.addAll(vec);
	}
	
	public Vector(double ...values) {
		this(Arrays.stream( values ).mapToObj(Complex::new).collect(Collectors.toList()));
	}

	public Vector copy() {
		return this.stream().collect(Collectors.toCollection(Vector::new));
	}
	
	public Complex get(int index) {
		if (index >= this.size()) return new Complex(0);
		return super.get(index);
	}

	public Vector subVector(int iStart, int iEnd) {
		return IntStream.range(iStart, iEnd).mapToObj(this::get).collect(Collectors.toCollection(Vector::new));
	}

	public Complex maxAbs() {
		return this.stream().max(Complex::compareTo).get();
	}

	public Complex set(int index, Complex element) {
		while (index >= this.size()) this.add(new Complex());
		return super.set(index, element);
	}

	public Complex sumAll() {
		return this.stream().reduce(new Complex(), Complex::sum);
	}

	public Stream<Complex> reverseStream() {
		return IntStream.range(0, this.size()).mapToObj(i -> this.get(this.size() - i -1));
	}

	public void swap(int i1, int i2) {
		Complex aux = this.get(i1);
		this.set(i1, this.get(i2));
		this.set(i2, aux);
	}
	
	private Vector zippedOperation(Vector v, BinaryOperator<Complex> binaryOperator) {
		List<Complex> newVec = IntStream.range(0, Math.max(this.size(), v.size()))
				.mapToObj(i -> binaryOperator.apply(this.get(i), v.get(i)))
				.collect(Collectors.toList());

		return new Vector(newVec);
	}

	public double norm() {
		return Math.sqrt(this.stream().mapToDouble(Complex::mulByConjugate).sum());
	}

	public double distanceTo(Vector v) {
		return (v.subtract(this)).norm();
	}

	public Vector normalized() {
		return this.dot(1 / this.norm());
	}
	
	public Vector elementWiseDot(Vector v) {
		return zippedOperation(v, Complex::mul);
	}

	public Vector dot(Complex c) {
		return this.stream().map(x -> x.mul(c)).collect(Collectors.toCollection(Vector::new));
	}

	public Vector dot(double d) {
		return this.dot(new Complex(d));
	}
	
	public Complex dot(Vector v) {
		return this.elementWiseDot(v).sumAll();
	}
	
	public Vector sum(Vector v) {
		return zippedOperation(v, Complex::sum);
	}

	public Vector subtract(Vector v) {
		return zippedOperation(v, Complex::sub);
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
		return this.distanceTo(other) < 0.0001;
//		return IntStream.range(0, Math.max(this.size(), other.size())).allMatch(i -> this.get(i).distanceTo(other.get(i)) < 0.0001);
	}

	public static Vector base(int size, int onePosition) {
		Vector v = new Vector(size);
		v.set(onePosition, new Complex(1));
		return v;
	}

}
