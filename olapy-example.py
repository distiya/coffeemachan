from olapy.core.connection import XMLAConnection

# pip install olapy
# Connection parameters
url = "http://your-olap-server/olap/msmdpump.dll"
username = "your_username"
password = "your_password"
datasource = "YourDataSource"
catalog = "YourCatalog"

# Create a connection object
conn = XMLAConnection(url, datasource, catalog, username=username, password=password)

# Open the connection
conn.open()

# Define the MDX query
mdx_query = """
SELECT
    [Measures].[Sales Amount] ON COLUMNS,
    [Date].[Calendar].[Calendar Year].MEMBERS ON ROWS
FROM
    [YourCube]
"""

# Execute the MDX query
result = conn.execute_mdx(mdx_query)

# Process and print the result
for row in result:
    print(f"Year: {row[0]}, Sales Amount: {row[1]}")

# Close the connection
conn.close()

