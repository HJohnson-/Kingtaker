package ai;

/**
 * Created by Rob on 10/11/2014.
 */
public class Pair<T,U> {

    private T obj1;
    private U obj2;

    public Pair(T obj1, U obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public T getObj1() {
        return obj1;
    }

    public U getObj2() {
        return obj2;
    }

}
