#!/bin/bash

# Simple Firestore Population Script using REST API
# This adds sample restaurant data to your Firestore database

PROJECT_ID="eatfair-app"
API_KEY="AIzaSyAepfv31h7GlkJE3oWT-hfrfkQZQAAq13M"

echo "🔥 Populating Firestore with sample data..."
echo "Project: $PROJECT_ID"
echo ""

# Restaurant 1: Natraj Restaurant
echo "Adding Natraj Restaurant..."
curl -X POST \
  "https://firestore.googleapis.com/v1/projects/$PROJECT_ID/databases/(default)/documents/restaurants?documentId=12&key=$API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": {
      "id": {"integerValue": "12"},
      "name": {"stringValue": "Natraj Restaurant"},
      "rating": {"doubleValue": 4.5},
      "reviews": {"stringValue": "1.2K+"},
      "distance": {"stringValue": "1.0 km"},
      "deliveryTime": {"stringValue": "20-25 mins"},
      "cuisineType": {"stringValue": "Indian"},
      "isPureVeg": {"booleanValue": false},
      "isOpen": {"booleanValue": true},
      "tags": {"arrayValue": {"values": [{"stringValue": "Featured"}, {"stringValue": "Indian"}]}},
      "imageUrl": {"stringValue": "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg"},
      "address": {"stringValue": "22205 El Paseo #A, Rancho Santa Margarita, CA 92688"},
      "latitude": {"doubleValue": 33.6402},
      "longitude": {"doubleValue": -117.6033}
    }
  }'

echo ""
echo "✅ Natraj Restaurant added"
echo ""

# Restaurant 2: Desi Laddu House
echo "Adding Desi Laddu House..."
curl -X POST \
  "https://firestore.googleapis.com/v1/projects/$PROJECT_ID/databases/(default)/documents/restaurants?documentId=2&key=$API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": {
      "id": {"integerValue": "2"},
      "name": {"stringValue": "Desi Laddu House"},
      "rating": {"doubleValue": 4.2},
      "reviews": {"stringValue": "5.2K+"},
      "distance": {"stringValue": "3.5 km"},
      "deliveryTime": {"stringValue": "30-35 mins"},
      "cuisineType": {"stringValue": "Sweets, Indian"},
      "isPureVeg": {"booleanValue": true},
      "isOpen": {"booleanValue": true},
      "tags": {"arrayValue": {"values": [{"stringValue": "Popular"}, {"stringValue": "Pure Veg"}]}},
      "imageUrl": {"stringValue": "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1"},
      "address": {"stringValue": "14, Chandni Chowk Market, Delhi"},
      "latitude": {"doubleValue": 28.6508},
      "longitude": {"doubleValue": 77.2300}
    }
  }'

echo ""
echo "✅ Desi Laddu House added"
echo ""

# Restaurant 3: Pizza Palace
echo "Adding Pizza Palace..."
curl -X POST \
  "https://firestore.googleapis.com/v1/projects/$PROJECT_ID/databases/(default)/documents/restaurants?documentId=3&key=$API_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "fields": {
      "id": {"integerValue": "3"},
      "name": {"stringValue": "Pizza Palace"},
      "rating": {"doubleValue": 4.7},
      "reviews": {"stringValue": "3.5K+"},
      "distance": {"stringValue": "2.2 km"},
      "deliveryTime": {"stringValue": "25-30 mins"},
      "cuisineType": {"stringValue": "Italian, Pizza"},
      "isPureVeg": {"booleanValue": false},
      "isOpen": {"booleanValue": true},
      "tags": {"arrayValue": {"values": [{"stringValue": "Fast Food"}, {"stringValue": "Italian"}]}},
      "imageUrl": {"stringValue": "https://images.unsplash.com/photo-1513104890138-7c749659a591"},
      "address": {"stringValue": "456 Main Street, Irvine, CA 92614"},
      "latitude": {"doubleValue": 33.6846},
      "longitude": {"doubleValue": -117.8265}
    }
  }'

echo ""
echo "✅ Pizza Palace added"
echo ""

echo "🎉 Firestore population complete!"
echo ""
echo "You can verify the data at:"
echo "https://console.firebase.google.com/project/$PROJECT_ID/firestore/data"
echo ""
