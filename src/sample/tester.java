package sample;

public class tester {
    private static tester t = new tester();
    int a;
    private tester(){}

    public static tester getInstance(){
        return t;
    }

    public void setA(){
        a = 3;
    }

    public int getA(){
        return a;
    }

}
