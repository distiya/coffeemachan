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
        r"""
        \.method\s*\(\s*(HttpMethod\.(GET|POST|PUT|DELETE))\s*\)
        .*?\.uri\s*\(\s*["'](?P<url>[^"']+)["']
        (?:.*?queryParam\(["'](?P<param_name>[^"']+)["']\s*,\s*(?P<param_value>[^)]+)\))*
        """,
        r"""
        (restTemplate|RestTemplate)\.(exchange|postForObject|getForObject|put|delete)
        \s*\(\s*["'](?P<url>[^"']+)["']
        """,
        r"""
        \.(get|post|put|delete)\s*\(\)
        .*?\.uri\s*\(\s*["'](?P<url>[^"']+)["']
        (?:.*?queryParam\(["'](?P<param_name>[^"']+)["']\s*,\s*(?P<param_value>[^)]+)\))*
        """
    ]

    combined_pattern = re.compile("|".join(patterns), re.DOTALL | re.VERBOSE)

    for match in combined_pattern.finditer(file_content):
        method = match.group(2)  # HTTP method like GET, POST, etc.
        url = match.group("url")  # Base URL
        query_params_raw = re.findall(r'queryParam\(["\']([^"\']+)["\']\s*,\s*([^,)]+)\)', match.group(0))

        # Convert query params to a dictionary
        query_params = {key: value.strip() for key, value in query_params_raw}

        invocation = {
            "method": method,
            "url": url,
            "query_params": query_params
        }

        invocations.append(invocation)

    return invocations

def extract_kafka_topics(file_content: str) -> Dict[str, List[str]]:
    """
    Extract consumed and published Kafka topics.
    """
    kafka_data = {"consumed_topics": [], "published_topics": []}

    # Pattern for KafkaListener (consumers)
    kafka_listener_pattern = r"@KafkaListener\s*\(.*?topics\s*=\s*[{]?['\"](.*?)['\"]"
    consumed_topics = re.findall(kafka_listener_pattern, file_content, re.DOTALL)
    kafka_data["consumed_topics"].extend(consumed_topics)

    # Pattern for KafkaTemplate (producers)
    kafka_producer_pattern = r"kafkaTemplate\.send\s*\(\s*['\"](.*?)['\"]"
    published_topics = re.findall(kafka_producer_pattern, file_content, re.DOTALL)
    kafka_data["published_topics"].extend(published_topics)

    return kafka_data

def parse_kotlin_project(directory: str) -> Dict[str, List[Dict[str, Optional[str]]]]:
    """
    Parse a Kotlin project to find REST API invocations and Kafka topics.
    """
    kotlin_files = find_kotlin_files(directory)
    all_api_invocations = []
    all_kafka_topics = {"consumed_topics": [], "published_topics": []}

    for kotlin_file in kotlin_files:
        with open(kotlin_file, 'r', encoding='utf-8') as file:
            content = file.read()
            preprocessed_content = preprocess_code(content)

            # Extract REST API invocations
            api_invocations = extract_api_invocations(preprocessed_content)
            if api_invocations:
                all_api_invocations.extend(api_invocations)

            # Extract Kafka topics
            kafka_topics = extract_kafka_topics(preprocessed_content)
            all_kafka_topics["consumed_topics"].extend(kafka_topics["consumed_topics"])
            all_kafka_topics["published_topics"].extend(kafka_topics["published_topics"])

    # Remove duplicates from Kafka topics
    all_kafka_topics["consumed_topics"] = list(set(all_kafka_topics["consumed_topics"]))
    all_kafka_topics["published_topics"] = list(set(all_kafka_topics["published_topics"]))

    return {"api_invocations": all_api_invocations, "kafka_topics": all_kafka_topics}

if __name__ == "__main__":
    # Directory containing the Kotlin project
    project_directory = "/path/to/kotlin/project"

    # Parse the project to extract API invocations and Kafka topics
    result = parse_kotlin_project(project_directory)

    # Print REST API invocations
    print("REST API Invocations:")
    for invocation in result["api_invocations"]:
        print(f"HTTP Method: {invocation['method']}")
        print(f"Request Path: {invocation['url']}")
        print(f"Query Params: {invocation.get('query_params', {})}")
        print("-" * 40)

    # Print Kafka topics
    print("\nKafka Topics:")
    print("Consumed Topics:", result["kafka_topics"]["consumed_topics"])
    print("Published Topics:", result["kafka_topics"]["published_topics"])

