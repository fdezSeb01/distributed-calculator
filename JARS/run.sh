#!/bin/bash

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 <number of times to run mom.jar> <number of times to run client.jar>"
  exit 1
fi

num_mom_runs=$1
num_client_runs=$2

for ((i=1; i<=num_mom_runs; i++)); do
  echo "Running mom.jar #$i"
  gnome-terminal -- java -jar mom.jar
done

for ((i=1; i<=num_client_runs; i++)); do
  echo "Running client.jar #$i"
  gnome-terminal -- java -jar client.jar
done

