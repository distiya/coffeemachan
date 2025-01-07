import os
import xml.etree.ElementTree as ET

def extract_versions_from_pom(pom_file):
    """
    Extract Java, Kotlin, and Spring Boot versions from a given pom.xml file.
    """
    versions = {"java": None, "kotlin": None, "spring_boot": None}
    
    try:
        tree = ET.parse(pom_file)
        root = tree.getroot()

        # Define the namespace (usually the default for Maven POMs)
        namespace = {'ns': 'http://maven.apache.org/POM/4.0.0'}
        
        # Extract the properties tag
        properties = root.find('ns:properties', namespace)
        if properties is not None:
            # Extract specific versions
            versions["java"] = properties.find('ns:java.version', namespace).text if properties.find('ns:java.version', namespace) is not None else None
            versions["kotlin"] = properties.find('ns:kotlin.version', namespace).text if properties.find('ns:kotlin.version', namespace) is not None else None
            versions["spring_boot"] = properties.find('ns:spring-boot.version', namespace).text if properties.find('ns:spring-boot.version', namespace) is not None else None
    except Exception as e:
        print(f"Error parsing {pom_file}: {e}")
    
    return versions

def find_pom_files(root_folder):
    """
    Find all pom.xml files in the root folder and its subdirectories.
    """
    pom_files = []
    for dirpath, _, filenames in os.walk(root_folder):
        if "pom.xml" in filenames:
            pom_files.append(os.path.join(dirpath, "pom.xml"))
    return pom_files

def main():
    # Specify the root folder containing the Maven projects
    root_folder = input("Enter the root folder path: ").strip()

    # Find all pom.xml files in the root folder
    pom_files = find_pom_files(root_folder)

    if not pom_files:
        print("No pom.xml files found in the specified directory.")
        return

    # Extract and display versions from each pom.xml file
    print(f"Found {len(pom_files)} Maven project(s).")
    for pom_file in pom_files:
        print(f"\nProcessing: {pom_file}")
        versions = extract_versions_from_pom(pom_file)
        print(f"  Java Version: {versions['java']}")
        print(f"  Kotlin Version: {versions['kotlin']}")
        print(f"  Spring Boot Version: {versions['spring_boot']}")

if __name__ == "__main__":
    main()

