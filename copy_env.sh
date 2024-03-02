#!/bin/bash

# Get the current directory
source=$(pwd)

# List of destination directories
destinations=("customer" "product" "organization" "api-gateway" "admin" "auth" "logo" "mail" "purchase" "review")

# Loop through each destination directory and copy the .env file
for dest in "${destinations[@]}"; do
    echo "Copying .env file to $dest directory..."
    cp "$source/.env" "$source/$dest/"
    if [ $? -ne 0 ]; then
        echo "Failed to copy .env file to $dest directory."
    else
        echo ".env file copied successfully to $dest directory."
    fi
done
