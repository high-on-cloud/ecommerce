#!/bin/bash
echo "Staring killing of processes"
input="save_pid.txt"
while IFS= read -r line
do
  echo "$line"
  kill -9 $line
done < "$input"

echo "COMPLETED killing of processes"
