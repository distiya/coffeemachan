from airflow import DAG
from airflow.providers.cncf.kubernetes.operators.kubernetes_pod import KubernetesPodOperator
from airflow.utils.dates import days_ago

# Default arguments for the DAG
default_args = {
    'owner': 'airflow',
    'depends_on_past': False,
    'start_date': days_ago(1),
    'email_on_failure': False,
    'email_on_retry': False,
    'retries': 1,
}

# Define the DAG
dag = DAG(
    'run_dotnet_k8s',
    default_args=default_args,
    description='Run a .NET app in Kubernetes using Docker image',
    schedule_interval=None,  # Set your schedule if needed
)

# KubernetesPodOperator to execute the .NET application
dotnet_task = KubernetesPodOperator(
    namespace='default',  # Kubernetes namespace
    image='your_docker_image',  # Replace with your Docker image
    name='run-dotnet-app',
    task_id='dotnet_task',
    get_logs=True,
    dag=dag,
    in_cluster=True,  # Whether to use the local Kubernetes cluster
    is_delete_operator_pod=True,  # Clean up the pod after execution
)

# The task is now part of the DAG
dotnet_task
