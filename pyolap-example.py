from pyolap.cubes import Cubes, Workspace
from pyolap.cubes.models import Store

# pip install pyolap
# Set up the OLAP connection
workspace = Workspace()
workspace.register_default_store(Store("default", "sql", "connection_string"))

# Load your cube model
model = workspace.import_model("your_model.json")

# Define the MDX query
mdx_query = """
SELECT
    [Measures].[Sales Amount] ON COLUMNS,
    [Date].[Calendar].[Calendar Year].MEMBERS ON ROWS
FROM
    [YourCube]
"""

# Execute the MDX query
cube = workspace.cube("YourCube")
result = cube.browser().execute(mdx_query)

# Process and print the result
for row in result:
    print(f"Year: {row[0]}, Sales Amount: {row[1]}")

