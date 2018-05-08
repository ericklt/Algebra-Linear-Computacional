package model;

public class OldSchoolMatrix extends Matrix {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Matrix dot(Matrix m) {
		Matrix result = new Matrix();
		for (int i = 0; i < shape[0]; i++) {
			for (int j = 0; j < m.shape()[1]; j++) {
				Complex sum = new Complex();
				for (int k = 0; k < shape[1]; k++) {
					sum = sum.sum(this.get(i, k).mul(m.get(k, j)));
				}
				result.set(i, j, sum);
			}
		}
		return result;
	}
	
	@Override
	public Vector dot(Vector v) {
		Vector result = new Vector();
		for (int i = 0; i < shape[0]; i++) {
			Complex sum = new Complex();
			for (int j = 0; j < shape[1]; j++)
				sum = sum.sum( this.get(i, j).mul(v.get(j)) );
			result.set(i, sum);
		}
		return result;
	}
	
	@Override
	public Vector dotByLinearCombination(Vector v) {
		Vector result = new Vector();
		for (int j = 0; j < shape[1]; j++) {
			for (int i = 0; i < shape[0]; i++)
				result.set(i, result.get(i).sum(this.get(i, j).mul(v.get(j))));
		}
		return result;
	}

}
