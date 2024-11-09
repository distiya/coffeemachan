import os
import re
import pandas as pd

# Define the root path of your Kotlin project
project_root = "path/to/your/kotlin/project"

# Define search patterns
rest_api_pattern = r"@(GetMapping|PostMapping|PutMapping|DeleteMapping|RequestMapping)\(.*\"(.*?)\".*\)"
http_client_pattern = r"(RestTemplate|WebClient|OkHttp).*?\.(get|post|put|delete|exchange)\(.*\"(.*?)\".*\)"
kafka_producer_pattern = r"KafkaTemplate.*\.send\(\"(.*?)\""
kafka_consumer_pattern = r"@KafkaListener\s*\(\s*topics\s*=\s*\{?\s*\"(.*?)\""

# Initialize lists to store results
exposed_apis = []
consumed_apis = []
kafka_published_topics = []
kafka_consumed_topics = []

# Function to scan Kotlin files for exposed REST APIs
def get_exposed_apis(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        matches = re.findall(rest_api_pattern, content)
        for match in matches:
            annotation, path = match
            exposed_apis.append({
                "File": file_path,
                "Annotation": annotation,
                "Path": path
            })

# Function to scan Kotlin files for HTTP client calls (consumed APIs)
def get_consumed_apis(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        matches = re.findall(http_client_pattern, content)
        for match in matches:
            client, method, url = match
            consumed_apis.append({
                "File": file_path,
                "Client": client,
                "Method": method,
                "URL": url
            })

# Function to scan for Kafka topics being published to
def get_kafka_published_topics(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        matches = re.findall(kafka_producer_pattern, content)
        for topic in matches:
            kafka_published_topics.append({
                "File": file_path,
                "Topic": topic
            })

# Function to scan for Kafka topics being consumed
def get_kafka_consumed_topics(file_path):
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()
        matches = re.findall(kafka_consumer_pattern, content)
        for topic in matches:
            kafka_consumed_topics.append({
                "File": file_path,
                "Topic": topic
            })

# Iterate through each Kotlin file in the project
for root, dirs, files in os.walk(project_root):
    for file in files:
        if file.endswith(".kt"):
            file_path = os.path.join(root, file)
            get_exposed_apis(file_path)
            get_consumed_apis(file_path)
            get_kafka_published_topics(file_path)
            get_kafka_consumed_topics(file_path)

# Convert results to DataFrames for better formatting (optional)
exposed_apis_df = pd.DataFrame(exposed_apis)
consumed_apis_df = pd.DataFrame(consumed_apis)
kafka_published_topics_df = pd.DataFrame(kafka_published_topics)
kafka_consumed_topics_df = pd.DataFrame(kafka_consumed_topics)

# Display results
print("Exposed REST APIs:")
print(exposed_apis_df if not exposed_apis_df.empty else "No exposed APIs found.")

print("\nConsumed REST APIs:")
print(consumed_apis_df if not consumed_apis_df.empty else "No consumed APIs found.")

print("\nKafka Published Topics:")
print(kafka_published_topics_df if not kafka_published_topics_df.empty else "No Kafka published topics found.")

print("\nKafka Consumed Topics:")
print(kafka_consumed_topics_df if not kafka_consumed_topics_df.empty else "No Kafka consumed topics found.")

