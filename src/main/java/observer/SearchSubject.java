package observer;

import java.util.ArrayList;
import java.util.List;

public class SearchSubject {
    private final List<SearchObserver> observers = new ArrayList<>();

    public void addObserver(SearchObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(String query) {
        for (SearchObserver observer : observers) {
            observer.onSearch(query);
        }
    }
}
