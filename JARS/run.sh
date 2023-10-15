#!/bin/bash
echo -n > ports.config
# Default values
num_mom_runs=0
num_client_runs=0

# Parse command line options
while getopts "m:c:" opt; do
  case $opt in
    m)
      num_mom_runs=$OPTARG
      ;;
    c)
      num_client_runs=$OPTARG
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      exit 1
      ;;
  esac
done

# Ensure both options were provided
if [ "$num_mom_runs" -eq 0 ] || [ "$num_client_runs" -eq 0 ]; then
  echo "Usage: $0 -m <num_mom_runs> -c <num_client_runs>"
  exit 1
fi

# Run "java -jar mom.jar"
for ((i=1; i<=num_mom_runs; i++)); do
  echo "Running mom.jar #$i"
  gnome-terminal -- java -jar mom.jar
  sleep 1
done

# Run "java -jar client.jar"
for ((i=1; i<=num_client_runs; i++)); do
  echo "Running client.jar #$i"
  gnome-terminal -- java -jar cliente.jar
done

