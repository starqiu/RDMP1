package ustc.sse.datamining.example;

public class Main {

    public static void doBefore() {
        A a = new A();
        a.doIt();
    }

    public static void doAfter() {
        mA a = new mA();
        a.doIt();
    }

    public static void main(String[] args) {
        doBefore();
        doAfter();
    }
}
