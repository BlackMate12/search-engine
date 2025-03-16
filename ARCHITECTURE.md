# Local File Search Engine Project
## Overview
This document describes the architectural view of a local file search engine, based on Simon Brown's C4 model.
## Level 1: System Context
This level is a top-level view of the entire system, focusing on users and the software systems at play.

![image](https://github.com/user-attachments/assets/d18fd25e-250b-4eee-aaaa-6cfd50774f9f)

Elements of System Context:
- User: Inputs search queries
- Search Engine: Takes input and extracts search results from the Database; crawls local files to keep Database up to date
- Local Files: Source for files that are searched
- Database: Contains indexed information about local files
## Level 2: Containers
This level focuses on the containers that form the elements of the System Conext. These containers are deployable units that store data or execute code, such as web or mobile apps, databases, etc.

![image](https://github.com/user-attachments/assets/90a08e5d-7687-423f-a9f9-e155e19933cf)

Containers of the Search Engine:
- `UI`: User Interface through which the User inputs search queries
- `Search API`: Takes user input and parses it, sends it to DB Connection, gets results back from it and formats then to be displayed back to the UI
- `Crawler`: Crawls local files, compares with information in the Database and sends changes to Indexer
- `Indexer`: Cleans data received by Crawler, sends it to DB Connection to update the Database
- `DB Connection`: Communicates directly with the Database through CRUD operations, receives parameters for SELECT queries from Search API, and for INSERT/UPDATE queries from Indexer
## Level 3: Components
This level focuses on the components of the previously established containers, which are the major building blocks of code, their responsibilities and implementation details.

### `UI`
Responsibilities:
- Facilitate searching through local files, based on contents, metadata, etc.
- Display results as the User types

Components:
- Graphical Component: Graphical part of the UI that the User inetracts with, provides a search bar and an area for search results
- Functional Component: Takes query inputted into the search bar and sends it to the Search API

![image](https://github.com/user-attachments/assets/3fe1038c-f03b-4bd8-a508-82531a10caa3)

### `Search API`
Responsibilities:
- Make the connection between the UI and the Database
- Take input from UI and prepare it to send to DB Connection
- Take results from DB Connection and prepare them to be displayed in the UI

Components:
- Input Processing Component: Takes input from UI and processes it into parameters for SELECT query
- Output Processing Component: Takes results from SELECT query and processes it into output for the UI

![image](https://github.com/user-attachments/assets/988f6afc-adca-4eea-807d-370befbebed5)

### `Crawler`
Responsibilities:
- Crawl local files
- Filter out unwanted data
- Compare to data in Database, detect modifications and send them to Indexer

Components:
- File Reading Component: Reads local files, readies data for comparison
- Data Comparison Component: Compares read files to data inside the Database, identifies differences
- Data Sender Component: Sends to Indexer only files/data that is identified as having been modified or new

![image](https://github.com/user-attachments/assets/33199b15-a540-4cb9-a135-7bd0a06ef7bf)

### `Indexer`
Responsibilities:
- Clean up data received from Crawler
- Index data to send to DB Connection for insertion into the Database

Components:
- Input Processing Component: Takes input from Crawler and processes it into parameters for INSERT/UPDATE query

![image](https://github.com/user-attachments/assets/2a9e03c1-167a-476f-a465-47877a30716c)

### `DB Connection`
Responsibilities:
- Communicate directly with the Database
- Generate SELECT queries using parameters obtained from Search API
- Send results of SELECT operation to Search API
- Generate INSERT/UPDATE queries using parameters obtained from Indexer

Components:
- Connection Generator Component: Establishes connection with DBMS
- Query Generator Component: Takes input from Search API or Indexer and uses it as parameters in generating SELECT and INSERT/UPDATE queries, respectively
- Database Communication Component: Sends generated queries to Database
- Result Sender Component: Retrieves results of SELECT query, sends them to Search API

![image](https://github.com/user-attachments/assets/4f10ee58-6292-4019-b1e5-12268074844b)
