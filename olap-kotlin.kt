import org.olap4j.*
import org.olap4j.driver.xmla.XmlaOlap4jDriver
import org.olap4j.layout.RectangularCellSetFormatter
import java.sql.DriverManager

<dependencies>
    <dependency>
        <groupId>org.olap4j</groupId>
        <artifactId>olap4j</artifactId>
        <version>1.2.0</version>
    </dependency>
</dependencies>


fun main() {
    // URL of the OLAP server
    val olapServerUrl = "http://your-olap-server/OLAP/msmdpump.dll"  // Replace with your server URL

    // Connection properties
    val props = java.util.Properties().apply {
        setProperty("Catalog", "YourCatalogName")  // Replace with your catalog name
        setProperty("User", "your-username")       // Replace with your username
        setProperty("Password", "your-password")   // Replace with your password
    }

    // Connect to the OLAP server
    val connection: OlapConnection = DriverManager.getConnection("jdbc:xmla:$olapServerUrl", props) as OlapConnection

    // Create an MDX query
    val mdxQuery = """
        SELECT
            {[Measures].[Sales Amount], [Measures].[Order Quantity]} ON COLUMNS,
            [Date].[Calendar].[Calendar Year].MEMBERS ON ROWS
        FROM [Adventure Works]
    """

    // Execute the MDX query
    val statement: OlapStatement = connection.createStatement()
    val cellSet: CellSet = statement.executeOlapQuery(mdxQuery)

    // Display the results
    val formatter = RectangularCellSetFormatter()
    formatter.format(cellSet, System.out)

    // Close the connection
    cellSet.close()
    statement.close()
    connection.close()
}

