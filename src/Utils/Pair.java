package Utils;

public class Pair<T, S> {

    private T _1;
    private S _2;

    public Pair(T _1, S _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public T _1() {
        return _1;
    }

    public S _2() {
        return this._2;
    }

    @Override
    public String toString() {
        return "( " + _1.toString() + ", " + _2.toString() + " )";
    }
}
