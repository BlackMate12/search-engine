import indexReport.CsvReportFormatter;
import search.SearchAPI;
import crawler.Crawler;
import db.DBHandler;
import indexer.Indexer;
import indexReport.*;
import observer.*;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String reportType = args.length > 0 ? args[0] : "console";

        IndexReportFormatter formatter = switch (reportType.toLowerCase()) {
            case "csv" -> new CsvReportFormatter("index_report.csv");
            default -> new ConsoleReportFormatter();
        };

        SearchSubject searchSubject = new SearchSubject();
        SearchHistory searchHistory = new SearchHistory();
        searchSubject.addObserver(searchHistory);

        DBHandler dbHandler = new DBHandler(searchSubject);
        Indexer indexer = new Indexer(dbHandler, formatter);

        Crawler crawler = new Crawler(indexer);
        SearchAPI api = new SearchAPI(dbHandler);

        File rootDirectory = new File("D:/schkool/YEAR 2");
        crawler.crawlDirectory(rootDirectory);

        Scanner scanner = new Scanner(System.in);
        int flag = 1;
        while(flag == 1)
        {
            List<String> suggestions = searchHistory.getRecentQueries();
            if (!suggestions.isEmpty()) {
                System.out.println("Recent searches:");
                for (String q : suggestions) {
                    System.out.println("- " + q);
                }
            }
            System.out.print("Search for: ");
            String query = scanner.nextLine();
            api.search(query);
            if(query.equals("exit"))
                flag = 0;
        }
    }
}
