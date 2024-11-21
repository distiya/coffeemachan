import os
import re
from typing import List, Dict, Optional

def find_kotlin_files(directory: str) -> List[str]:
    """
    Recursively find all Kotlin files in the given directory.
    """
    kotlin_files = []
    for root, _, files in os.walk(directory):
        for file in files:
            if file.endswith(".kt"):
                kotlin_files.append(os.path.join(root, file))
    return kotlin_files

def preprocess_code(file_content: str) -> str:
    """
    Preprocess the Kotlin code to handle multi-line statements.
    Joins lines that are broken with common patterns like method chaining.
    """
    # Remove comments
    file_content = re.sub(r"//.*|/\*.*?\*/", "", file_content, flags=re.DOTALL)

    # Combine lines with trailing dots or incomplete statements
    file_content = re.sub(r"\\\s*\n", "", file_content)  # Remove explicit line continuation
    file_content = re.sub(r"\.\s*\n", ".", file_content)  # Merge dot-chained lines
    file_content = re.sub(r"\(\s*\n", "(", file_content)  # Merge broken parentheses
    file_content = re.sub(r"\)\s*\n", ")", file_content)  # Merge closing parentheses

    return file_content

def extract_api_invocations(file_content: str) -> List[Dict[str, Optional[str]]]:
    """
    Extract API invocations with HTTP method, request path, and query parameters.
    """
    invocations = []

    # Regex patterns for WebClient and RestTemplate invocations
    patterns = [
        # WebClient .method() and .uri()
        r"""
        \.method\s*\(\s*(HttpMethod\.(GET|POST|PUT|DELETE))\s*\)
        .*?\.uri\s*\(\s*["'](?P<url>[^"']+)["']
        (.*?queryParam\(["'](?P<param_name>[^"']+)["']\s*,\s*(?P<param_value>[^)]+)\))?
        """,
        # RestTemplate exchange() or postForObject(), etc.
        r"""
        (restTemplate|RestTemplate)\.(exchange|postForObject|getForObject|put|delete)
        \s*\(\s*["'](?P<url>[^"']+)["']
        """,
        # WebClient .get(), .post(), .uri()
        r"""
        \.(get|post|put|delete)\s*\(\)
        .*?\.uri\s*\(\s*["'](?P<url>[^"']+)["']
        (.*?queryParam\(["'](?P<param_name>[^"']+)["']\s*,\s*(?P<param_value>[^)]+)\))?
        """
    ]

    # Combine all regex patterns into one
    combined_pattern = re.compile("|".join(patterns), re.DOTALL | re.VERBOSE)

    # Find matches
    for match in combined_pattern.finditer(file_content):
        method = match.group(2)  # HTTP method like GET, POST, etc.
        url = match.group("url")  # Base URL
        param_name = match.group("param_name")  # Query parameter name
        param_value = match.group("param_value")  # Query parameter value

        invocation = {
            "method": method,
            "url": url,
            "query_params": {}
        }

        if param_name and param_value:
            invocation["query_params"][param_name] = param_value.strip()

        invocations.append(invocation)

    return invocations

def parse_kotlin_project(directory: str) -> List[Dict[str, Optional[str]]]:
    """
    Parse a Kotlin project to find REST API invocations.
    """
    kotlin_files = find_kotlin_files(directory)
    all_invocations = []

    for kotlin_file in kotlin_files:
        with open(kotlin_file, 'r', encoding='utf-8') as file:
            content = file.read()
            preprocessed_content = preprocess_code(content)
            invocations = extract_api_invocations(preprocessed_content)
            if invocations:
                all_invocations.extend(invocations)

    return all_invocations

if __name__ == "__main__":
    # Directory containing the Kotlin project
    project_directory = "/path/to/kotlin/project"

    # Parse the project and extract API invocations
    api_invocations = parse_kotlin_project(project_directory)

    # Print the extracted information
    for invocation in api_invocations:
        print(f"HTTP Method: {invocation['method']}")
        print(f"Request Path: {invocation['url']}")
        print(f"Query Params: {invocation.get('query_params', {})}")
        print("-" * 40)

