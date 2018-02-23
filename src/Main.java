
public class Main {

	public static void main(String[] args) {
		
		Matrix m1 = new Matrix();
		m1.addRow(1., 2., 3.);
		m1.addRow(4., 5., 6.);
		
		Matrix m2 = new Matrix();
		m2.addColumn(7., 8., 9.);
		m2.addColumn(10., 11., 12.);
		
		Matrix dot = m1.dot(m2);
		Matrix dot_old = m1.dot_oldscholl(m2);
		
		System.out.println(dot);
		System.out.println(dot_old);
		
	}
}
