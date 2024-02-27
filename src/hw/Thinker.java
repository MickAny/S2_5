package hw;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

class Thinker extends Thread {
    private String name;
    private int leftFork;
    private int rightFork;
    private int mealCounter;
    private Random random;
    private CountDownLatch cdl;
    private Waiter waiter;

    public Thinker(String name, Waiter waiter, int leftFork, int rightFork, CountDownLatch cdl) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.cdl = cdl;
        this.waiter = waiter;
        mealCounter = 0;
        random = new Random();
    }

    @Override
    public void run() {
        while (mealCounter < 3){
            try{
                thinking();
                eating();
            }catch (InterruptedException e){
                e.fillInStackTrace();
            }
        }
        System.out.println(name + " наелся");
        cdl.countDown();
    }

    private void eating() throws InterruptedException{
        if(waiter.tryGetForks(leftFork, rightFork)){
            System.out.printf("%s ест, используя вилки(%d, %d)\n".formatted(name, leftFork, rightFork));
            sleep(random.nextLong(3000, 6000));
            waiter.putForks(leftFork, rightFork);
            System.out.printf("%s поел и вернул вилки на место(%d, %d)\n".formatted(name, leftFork, rightFork));
            mealCounter++;
        }
    }
    private void thinking() throws InterruptedException{
        sleep(random.nextLong(100, 2000));
    }
}
