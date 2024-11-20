import os
import re

def extract_bean_declarations(file_path, target_beans):
    """
    Extracts entire bean declaration functions from a Kotlin file containing the target beans.
    
    Args:
        file_path (str): Path to the Kotlin file.
        target_beans (list): List of bean names to search for.
    
    Returns:
        list: List of matched bean declaration snippets.
    """
    with open(file_path, 'r', encoding='utf-8') as file:
        content = file.read()

    # Create a regex to match any of the target beans
    bean_pattern = rf"@Bean\s+fun\s+\w+\s*\(.*?\)\s*:\s*\w+\s*\{{.*?({'|'.join(map(re.escape, target_beans))}).*?\}}"

    # Match multiline functions (non-greedy)
    matches = re.findall(bean_pattern, content, re.DOTALL)
    return matches

def analyze_kotlin_project_for_beans(project_dir, target_beans):
    """
    Analyzes a Kotlin Spring Boot project for specific bean declaration functions.

    Args:
        project_dir (str): Path to the Kotlin project directory.
        target_beans (list): List of bean names to search for.

    Returns:
        dict: Dictionary with bean names as keys and list of declarations as values.
    """
    bean_declarations = {bean: [] for bean in target_beans}

    for root, _, files in os.walk(project_dir):
        for file in files:
            if file.endswith(".kt"):
                file_path = os.path.join(root, file)
                matches = extract_bean_declarations(file_path, target_beans)
                for bean in target_beans:
                    bean_declarations[bean].extend(
                        [match for match in matches if bean in match]
                    )

    return bean_declarations

def main():
    # Specify the Kotlin project directory
    kotlin_project_dir = input("Enter the path to your Kotlin Spring Boot project: ").strip()

    if not os.path.exists(kotlin_project_dir):
        print("Invalid directory. Please check the path and try again.")
        return

    # Beans to search for
    target_beans = [
        "RestTemplate",
        "WebClient",
        "KafkaTemplate",
        "RestClient",
        "WebClientBuilder",
        "RestClientBuilder"
    ]

    print(f"Extracting bean declarations for: {', '.join(target_beans)}...")
    result = analyze_kotlin_project_for_beans(kotlin_project_dir, target_beans)

    # Print the results
    for bean, declarations in result.items():
        if declarations:
            print(f"\nExtracted Bean Declarations for '{bean}':")
            for declaration in declarations:
                print("====================================")
                print(declaration)
                print("====================================")
        else:
            print(f"\nNo bean declarations found for '{bean}'.")

if __name__ == "__main__":
    main()

