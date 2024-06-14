package B;

public class Primos {
    // Elements of this class are a pair of integers and their position in a list
    private int primo1;
    private int primo2;
    private int position;

    public Primos(int primo1, int primo2, int position) {
        this.primo1 = primo1;
        this.primo2 = primo2;
        this.position = position;
    }

    public int getPrimo1() {
        return primo1;
    }

    public int getPrimo2() {
        return primo2;
    }

    public int getPosition() {
        return position;
    }

    // Theoretically, I should not need the setters, but just in case
    public void setPrimo1(int primo1) {
        this.primo1 = primo1;
    }

    public void setPrimo2(int primo2) {
        this.primo2 = primo2;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "(" + primo1 + ", " + primo2 + ")";
    }
}
