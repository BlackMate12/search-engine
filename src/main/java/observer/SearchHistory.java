package observer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SearchHistory implements SearchObserver {
    private final List<String> history = new ArrayList<>();
    private final LinkedList<String> recentQueries = new LinkedList<>();

    @Override
    public void onSearch(String query) {
        if (query == null || query.trim().isEmpty()) return;
        recentQueries.remove(query);
        recentQueries.addFirst(query);
        if (recentQueries.size() > 5) {
            recentQueries.removeLast();
        }
    }

    public List<String> getRecentQueries() {
        return recentQueries.subList(0, Math.min(5, recentQueries.size()));
    }
}
