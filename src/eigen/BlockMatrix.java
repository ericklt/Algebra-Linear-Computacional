package eigen;

import model.Matrix;
import model.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BlockMatrix {

    private static final Double eps = 1e-6;
    private int[] shape = {0, 0};
    private List<List<Matrix>> blockMatrix;
    private List<Boolean> blockSequence;

    public BlockMatrix() {
        blockMatrix = new ArrayList<>();
        blockSequence = new ArrayList<>();
    }

    private static List<Boolean> blockSequence(Matrix m) {
        List<Boolean> seq = new ArrayList<>();
        if (m.shape()[0] != m.shape()[1])
            return null;
        for (int i = 0; i < m.shape()[0]; i++) {
            if (i < m.shape()[0]-1 && m.get(i+1, i).size() > eps) {
                seq.add(true);
                i++;
            } else
                seq.add(false);
        }
        return seq;
    }

    public static BlockMatrix blockify(Matrix m) {
        List<Boolean> blockSeq = blockSequence(m);
        if (blockSeq == null)
            return null;
        BlockMatrix blockMatrix = new BlockMatrix();
        blockMatrix.setBlockSequence(blockSeq);
        int i = 0;
        for (Boolean bL : blockSeq) {
            int j = 0;
            List<Matrix> blockLine = new ArrayList<>();
            for (Boolean bC : blockSeq) {
                int lineSpan = bL ? 2 : 1;
                int colSpan = bC ? 2 : 1;
                blockLine.add(m.subMatrix(i, i+lineSpan, j, j+colSpan));
                if (bC) j++;
                j++;
            }
            if (bL) i++;
            i++;
            blockMatrix.addLine(blockLine);
        }
        return blockMatrix;
    }

    public BlockMatrix blockifyVector(Vector v) {
        BlockMatrix matrix = new BlockMatrix();
        matrix.setBlockSequence(this.blockSequence);
        int i = 0;
        for (Boolean b : blockSequence) {
            Matrix toAdd = b ?
                    Matrix.parse(String.format(Locale.ENGLISH, "[[%f], [%f]]", v.get(i).getReal(), v.get(i+1).getReal())):
                    Matrix.parse(String.format(Locale.ENGLISH, "[[%f]]", v.get(i).getReal()));
            matrix.addLine(Collections.singletonList(toAdd));
            if (b) i++;
            i++;
        }
        return matrix;
    }

    public void setBlockSequence(List<Boolean> blockSequence) {
        this.blockSequence = blockSequence;
    }

    public int[] shape() {
        return shape;
    }

    public List<Boolean> getBlockSequence() {
        return blockSequence;
    }

    public List<Matrix> getLine(int i) {
        return blockMatrix.get(i);
    }

    public void addLine(List<Matrix> line) {
        blockMatrix.add(line);
        shape[0]++;
        shape[1] = Math.max(shape[1], line.size());
    }

    public Matrix get(int i, int j) {
        return this.blockMatrix.get(i).get(j);
    }

    public void show() {
        System.out.println(this);
    }

    @Override
    public String toString() {
        return blockMatrix.stream()
                .map(line -> line.stream()
                        .map(Matrix::toString)
                        .reduce((x, y) -> x+"\t"+y)
                        .get())
                .reduce((x, y) -> x+"\n"+y)
                .get();
    }
}
