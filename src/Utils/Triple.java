package Utils;

public class Triple<R, S, T> {

    private R _1;
    private S _2;
    private T _3;

    public Triple(R _1, S _2, T _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    public R _1() {
        return _1;
    }

    public S _2() {
        return this._2;
    }

    public T _3() {
        return this._3;
    }

    @Override
    public String toString() {
        return String.format("( %s, %s, %s )", _1,_2,_3);
//        return "( " + _1.toString() + ", " + _2.toString() + ", " + _3.toString() + " )";
    }

}
