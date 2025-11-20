#!/bin/bash

# EatFair Firestore Population Script
# This script provides instructions for populating Firestore manually

echo "🔥 EatFair Firestore Population Guide"
echo "======================================"
echo ""
echo "Since automated population requires a service account key,"
echo "here's the quickest way to populate your Firestore:"
echo ""
echo "📋 OPTION 1: Use Firebase Console (Recommended for Quick Start)"
echo "----------------------------------------------------------------"
echo ""
echo "1. Go to: https://console.firebase.google.com/project/eatfair-40f09/firestore"
echo ""
echo "2. Click 'Start collection'"
echo ""
echo "3. Collection ID: 'restaurants'"
echo ""
echo "4. Add Document ID: '12'"
echo "   Copy and paste this JSON in the 'Add field' section:"
echo ""
cat << 'EOF'
{
  "id": 12,
  "name": "Natraj Restaurant",
  "rating": 4.5,
  "reviews": "1.2K+",
  "distance": "1.0 km",
  "deliveryTime": "20-25 mins",
  "cuisineType": "Indian",
  "isPureVeg": false,
  "isOpen": true,
  "tags": ["Featured", "Indian"],
  "imageUrl": "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
  "address": "22205 El Paseo #A, Rancho Santa Margarita, CA 92688",
  "latitude": 33.6402,
  "longitude": -117.6033
}
EOF
echo ""
echo "5. Add another document with ID: '2'"
echo "   Copy and paste this JSON:"
echo ""
cat << 'EOF'
{
  "id": 2,
  "name": "Desi Laddu House",
  "rating": 4.2,
  "reviews": "5.2K+",
  "distance": "3.5 km",
  "deliveryTime": "30-35 mins",
  "cuisineType": "Sweets, Indian",
  "isPureVeg": true,
  "isOpen": true,
  "tags": ["Popular", "Pure Veg"],
  "imageUrl": "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1",
  "address": "14, Chandni Chowk Market, Delhi",
  "latitude": 28.6508,
  "longitude": 77.2300
}
EOF
echo ""
echo "✅ That's it! Your apps will now show real data from Firestore."
echo ""
echo "📝 Note: Menu items and categories will still use fallback data"
echo "    (which is fine for beta testing)"
echo ""
echo "================================================================"
echo ""
echo "📋 OPTION 2: Use Firebase CLI (For Advanced Users)"
echo "----------------------------------------------------------------"
echo ""
echo "If you have Node.js installed:"
echo ""
echo "1. cd scripts/"
echo "2. Download service account key from Firebase Console"
echo "3. Save as 'serviceAccountKey.json'"
echo "4. npm install"
echo "5. npm run populate"
echo ""
echo "================================================================"
echo ""
echo "🚀 After populating, test the apps:"
echo ""
echo "   adb install -r app/build/outputs/apk/debug/app-debug.apk"
echo "   # Open the app and you should see the restaurants!"
echo ""
echo "================================================================"
