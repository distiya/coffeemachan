import re
import yaml


def normalize_spring_properties(file_path):
    """
    Normalizes a Spring Boot application.properties or application.yml file to a flat dictionary
    with property names in a consistent format (lowercase and using dots for hierarchy).

    :param file_path: Path to the application.properties or application.yml file.
    :return: A dictionary of normalized properties.
    """
    normalized_properties = {}

    if file_path.endswith(".properties"):
        # Process application.properties file
        with open(file_path, 'r', encoding='utf-8') as file:
            for line in file:
                line = line.strip()
                # Ignore comments and empty lines
                if not line or line.startswith("#"):
                    continue
                # Match key-value pairs
                match = re.match(r'^([\w.\-]+)\s*=\s*(.*)$', line)
                if match:
                    key = match.group(1).strip()
                    value = match.group(2).strip()
                    # Normalize property name
                    normalized_key = key.lower().replace("-", ".")
                    normalized_properties[normalized_key] = value
    elif file_path.endswith(".yml") or file_path.endswith(".yaml"):
        # Process application.yml file
        with open(file_path, 'r', encoding='utf-8') as file:
            content = yaml.safe_load(file)
            # Recursively flatten the YAML structure
            def flatten_yaml(data, parent_key=""):
                for k, v in data.items():
                    new_key = f"{parent_key}.{k}" if parent_key else k
                    if isinstance(v, dict):
                        # Recurse into nested dictionaries
                        flatten_yaml(v, new_key)
                    else:
                        # Add flattened key-value pair
                        normalized_key = new_key.lower().replace("-", ".")
                        normalized_properties[normalized_key] = str(v)

            flatten_yaml(content)
    else:
        raise ValueError("Unsupported file format. Use a .properties or .yml/.yaml file.")

    return normalized_properties


def save_normalized_properties(normalized_properties, output_file):
    """
    Saves the normalized properties dictionary to a .properties file for easy regex matching.

    :param normalized_properties: Dictionary of normalized properties.
    :param output_file: Path to the output .properties file.
    """
    with open(output_file, 'w', encoding='utf-8') as file:
        for key, value in sorted(normalized_properties.items()):
            file.write(f"{key}={value}\n")
    print(f"Normalized properties saved to {output_file}")


# Example Usage
file_path = "/path/to/application.properties"  # or "/path/to/application.yml"
output_file = "/path/to/normalized.properties"

normalized_props = normalize_spring_properties(file_path)
save_normalized_properties(normalized_props, output_file)

