SQL Query Builder
=================
SQL Query Builder is a library that enables building SQL queries in a simple and error-free way in Java.

Code Sample
-----------
First of all, it is needed to init the library to let it know user, password, and database name:

	SQLConnectionFactory.init(DB_USER, DB_PASSWORD, DB_NAME);

Then, we can create an INSERT QUERY:

	SQLInsert insert = new SQLInsert();		
	insert.setTable("USERS");
	insert.addValue("id", 1);
	insert.addValue("name", "Joe");

Finally, we create a connection and execute the query:

	SQLConnection connection = SQLConnectionFactory.create();
	connection.executeQuery(insert.createQuery());
	connection.close();

That's all!