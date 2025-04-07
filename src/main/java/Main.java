import search.SearchAPI;
import crawler.Crawler;
import db.DBHandler;
import indexer.Indexer;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();
        Indexer indexer = new Indexer(dbHandler);
        Crawler crawler = new Crawler(indexer);
        SearchAPI api = new SearchAPI(dbHandler);

        File rootDirectory = new File("D:/schkool/YEAR 2");
        crawler.crawlDirectory(rootDirectory);

        Scanner scanner = new Scanner(System.in);
        int flag = 1;
        while(flag == 1)
        {
            System.out.print("Search for: ");
            String query = scanner.nextLine();
            api.search(query);
            if(query.equals("exit"))
                flag = 0;
        }
    }
}
