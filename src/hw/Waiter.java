package hw;

import java.util.concurrent.CountDownLatch;

public class Waiter extends Thread{
    private final int THINKERS_COUNT = 5;
    private Fork[] forks;
    private Thinker[] thinkers;
    private CountDownLatch cdl;

    public Waiter() {
        forks = new Fork[THINKERS_COUNT];
        thinkers = new Thinker[THINKERS_COUNT];
        cdl = new CountDownLatch(THINKERS_COUNT);
        init();
    }

    @Override
    public void run() {
        System.out.println("Выполнение программы");
        try{
            thinkingProcess();
            cdl.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Все условия программы выполнены");
    }

    public synchronized boolean tryGetForks(int leftFork, int rightFork){
        if(!forks[leftFork].isUsing() && !forks[rightFork].isUsing()){
            forks[leftFork].setUsing(true);
            forks[rightFork].setUsing(true);
            return true;
        }
        return false;
    }
    public void putForks(int leftFork, int rightFork){
        forks[leftFork].setUsing(false);
        forks[rightFork].setUsing(false);
    }
    private void init(){
        for (int i = 0; i < THINKERS_COUNT; i++) {
            forks[i] = new Fork();
        }

        for (int i = 0; i < THINKERS_COUNT; i++) {
            thinkers[i] = new Thinker("Thinker " + (i+1),this,
                    i, (i+1) % THINKERS_COUNT, cdl);
        }
    }
    private void thinkingProcess(){
        for (Thinker thinker: thinkers) {
            thinker.start();
        }
    }
}
