# Alternative: Populate Firestore via Firebase Console

Since service account key download is restricted, you can populate Firestore directly through the Firebase Console UI.

## 🎯 Quick Manual Population

### Step 1: Navigate to Firestore
1. Go to [Firestore Database](https://console.firebase.google.com/project/eatfair-40f09/firestore/databases/-default-/data)
2. Click "Start collection"

---

## 📊 Collections to Create

### 1. **Restaurants Collection**

#### Document ID: `12`
```json
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
```

**Then add subcollections:**

**Subcollection: `menu`**

Document ID: `87`
```json
{
  "id": 87,
  "name": "Garlic Naan",
  "price": 5.00,
  "description": "Leavened white bread topped with garlic & cilantro baked to order in the tandoor...",
  "isVeg": true,
  "isHighlyOrdered": true,
  "isCustomizable": true,
  "categoryId": 100,
  "imageUrl": "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
  "restaurantId": 12,
  "isAvailable": true
}
```

Document ID: `88`
```json
{
  "id": 88,
  "name": "Chicken Tikka Masala",
  "price": 24.00,
  "description": "Everybody's Favorite! Tandoori roasted breast of chicken in a velvety sauce of...",
  "isVeg": false,
  "isHighlyOrdered": true,
  "isCustomizable": false,
  "categoryId": 101,
  "imageUrl": "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
  "restaurantId": 12,
  "isAvailable": true
}
```

**Subcollection: `categories`**

Document ID: `100`
```json
{
  "id": 100,
  "name": "Featured Items",
  "icon": "⭐",
  "isHighlighted": true
}
```

Document ID: `101`
```json
{
  "id": 101,
  "name": "Tandoori Specialties",
  "icon": "🔥",
  "isHighlighted": true
}
```

---

#### Document ID: `2`
```json
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
```

**Subcollection: `menu`**

Document ID: `1`
```json
{
  "id": 1,
  "name": "Kaju Barfi",
  "price": 42.0,
  "description": "Indulge in the heavenly sweetness of kaju barfi...",
  "isVeg": true,
  "isHighlyOrdered": true,
  "isCustomizable": true,
  "categoryId": 1,
  "imageUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Kaju_katli_sweet.jpg/1200px-Kaju_katli_sweet.jpg",
  "restaurantId": 2,
  "isAvailable": true
}
```

---

### 2. **Promotions Collection**

Document ID: `1`
```json
{
  "id": 1,
  "title": "Flavors in Karbooz",
  "subtitle": "Spice Up Your Day",
  "description": "Taste Mexico!\nCraving Bold Mexican Flavors?",
  "backgroundColor": "#FFB6D9",
  "imageUrl": "",
  "isActive": true
}
```

Document ID: `2`
```json
{
  "id": 2,
  "title": "Asian Delights",
  "subtitle": "Experience The East",
  "description": "Authentic Asian\nCuisine Awaits!",
  "backgroundColor": "#B6E5FF",
  "imageUrl": "",
  "isActive": true
}
```

---

### 3. **Orders Collection**

Document ID: `EF1001`
```json
{
  "orderId": "EF1001",
  "restaurantId": 12,
  "status": "ORDER_PLACED",
  "deliveryPartner": {
    "name": "Jithesh Manoharan",
    "phone": "+1-949-123-4567"
  },
  "estimatedTime": "25 mins",
  "pickupLocation": {
    "address": "22205 El Paseo",
    "lat": 33.6402,
    "lng": -117.6033
  },
  "deliveryInstructions": "Leave at door",
  "isDelayed": false
}
```

---

## ⚡ Faster Alternative: Import JSON

1. Save each collection's data to a JSON file
2. Use Firebase CLI to import:

```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Import data
firebase firestore:import ./firestore-data --project eatfair-40f09
```

---

## 🎬 Video Tutorial

I've created the population script in `scripts/populate-firestore.js`. Once you can download the service account key:

1. Download key from Firebase Console
2. Save as `scripts/serviceAccountKey.json`
3. Run:
   ```bash
   cd scripts
   npm install
   npm run populate
   ```

This will automatically populate everything!
