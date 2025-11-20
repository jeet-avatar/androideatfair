#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}🔍 Scanning for google-services.json files in ~/Downloads...${NC}"

# Function to process a file
process_file() {
    local file="$1"
    if [[ ! -f "$file" ]]; then
        return
    fi

    # Extract package name using grep/sed (simple parsing)
    local package_name=$(grep -o '"package_name": *"[^"]*"' "$file" | cut -d'"' -f4)

    if [[ -z "$package_name" ]]; then
        echo -e "${RED}⚠️  Could not find package name in $file${NC}"
        return
    fi

    echo -e "Found config for: ${GREEN}$package_name${NC} in $(basename "$file")"

    local dest_dir=""
    case "$package_name" in
        "com.eatfair.app")
            dest_dir="app"
            ;;
        "com.eatfair.partner")
            dest_dir="partner"
            ;;
        "com.eatfair.orderapp")
            dest_dir="orderapp"
            ;;
        *)
            echo -e "${RED}❌ Unknown package: $package_name. Skipping.${NC}"
            return
            ;;
    esac

    if [[ -n "$dest_dir" ]]; then
        cp "$file" "$dest_dir/google-services.json"
        echo -e "✅ Installed to ${GREEN}$dest_dir/google-services.json${NC}"
    fi
}

# Check Downloads folder
found_files=0
for file in ~/Downloads/google-services*.json; do
    if [[ -f "$file" ]]; then
        process_file "$file"
        found_files=$((found_files + 1))
    fi
done

if [[ $found_files -eq 0 ]]; then
    echo -e "${RED}❌ No google-services.json files found in ~/Downloads.${NC}"
    echo "Please download them from the Firebase Console first."
else
    echo -e "\n${GREEN}🎉 Setup complete! You can now build the apps.${NC}"
fi
