package Utils;

public class Pair<T> {

    private T _1, _2;

    public Pair(T _1, T _2) {
        this._1 = _1;
        this._2 = _2;
    }

    public T _1() {
        return _1;
    }

    public T _2() {
        return this._2;
    }

}
