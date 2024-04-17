public class Main {

    public static void main(String[] args) {
        try{
            Thread t1 = new Hebra("h1", 1);
            Thread t2 = new Hebra("h2", 2);
            Thread t3 = new Hebra("h3", 3);

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();
        }catch (InterruptedException e){
            System.out.println(e);
        }
    }

}

class Hebra extends Thread{
    //Attributes
    int number;

    public Hebra(String name, int number){
        super(name);
        this.number = number;
    }

    public void run() {
        System.out.println(this.getName() + " : " + number);
    }
}