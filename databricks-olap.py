# Install the required library
%pip install olap.xmla

# Import the necessary modules
from olap.xmla import XMLAClient
import pandas as pd

# Connect to the OLAP server
client = XMLAClient(
    url='http://<OLAP_SERVER_URL>/olap/msmdpump.dll',
    username='<YOUR_USERNAME>',
    password='<YOUR_PASSWORD>',
    provider='MSOLAP'
)

# Define the MDX query
mdx_query = """
SELECT
    {[Measures].[Sales Amount]} ON COLUMNS,
    {[Product].[Category].Children} ON ROWS
FROM
    [Sales]
"""

# Execute the MDX query and process the results
with client.open() as conn:
    with conn.begin() as cur:
        result = cur.execute(mdx_query)
        
        # Convert the results to a list of rows
        rows = []
        for row in result.getraw():
            rows.append(row)
        
        # Convert the rows to a Pandas DataFrame
        df = pd.DataFrame(rows)
        
        # Display the DataFrame in Databricks
        display(df)

