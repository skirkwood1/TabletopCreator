package UI;

import Observers.Observer;

import java.util.ArrayList;

public interface Observable {
    ArrayList<Observer> observers = new ArrayList<>();

    default void addObserver(Observer observer){
        this.observers.add(observer);
    }

    default void updateObservers(){
        for(Observer observer: observers){
            observer.update();
        }
    }
}