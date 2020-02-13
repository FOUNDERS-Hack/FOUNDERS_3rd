According to Android Developers Official Site:

Caution: Although these (SQLite) APIs are powerful, they are fairly low-level and require a great deal of time and effort to use:

- There is no compile-time verification of raw SQL queries. As your data graph changes, you need to update the affected SQL queries manually. This process can be time consuming and error prone.
- You need to use lots of boilerplate code to convert between SQL queries and data objects.

For these reasons, we highly recommended using the **Room** Persistence Library as an abstraction layer for accessing information in your app's SQLite databases.
https://developer.android.com/training/data-storage/sqlite.html

Room provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite. We highly recommend using Room instead of SQLite.
https://developer.android.com/training/data-storage/room