import pyodbc

# pip install pyodbc
# Define the OLAP server connection details
server = 'your-server-name'  # Replace with your server name
database = 'YourDatabaseName'  # Replace with your database name
username = 'your-username'  # Replace with your username
password = 'your-password'  # Replace with your password

# Connection string to the OLAP server
connection_string = (
    f'DRIVER={{ODBC Driver 17 for SQL Server}};'
    f'SERVER={server};'
    f'DATABASE={database};'
    f'UID={username};'
    f'PWD={password};'
    f'Trusted_Connection=no;'
    f'Provider=MSOLAP;'
)

# Connect to the OLAP server
conn = pyodbc.connect(connection_string)

# Create a cursor object
cursor = conn.cursor()

# Define the MDX query
mdx_query = """
    SELECT
        {[Measures].[Sales Amount], [Measures].[Order Quantity]} ON COLUMNS,
        [Date].[Calendar].[Calendar Year].MEMBERS ON ROWS
    FROM [Adventure Works]
"""

# Execute the MDX query
cursor.execute(mdx_query)

# Fetch and print the results
for row in cursor.fetchall():
    print(row)

# Close the connection
cursor.close()
conn.close()

