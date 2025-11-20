# Firestore Population Guide

## 🎯 Quick Start

This guide will help you populate your Firebase Firestore database with sample data for testing.

---

## 📋 Prerequisites

1. **Node.js** installed (v14 or higher)
2. **Firebase project** created (`eatfair-40f09`)
3. **Service Account Key** from Firebase Console

---

## 🔑 Step 1: Download Service Account Key

1. Go to [Firebase Console](https://console.firebase.google.com/project/eatfair-40f09/settings/serviceaccounts/adminsdk)
2. Click **"Project Settings"** (gear icon)
3. Go to **"Service Accounts"** tab
4. Click **"Generate New Private Key"**
5. Save the downloaded JSON file as `serviceAccountKey.json` in the `scripts/` directory

**⚠️ IMPORTANT**: Never commit this file to Git! It's already in `.gitignore`.

---

## 🚀 Step 2: Install Dependencies

```bash
cd scripts
npm install
```

This will install the Firebase Admin SDK.

---

## 📊 Step 3: Run the Population Script

```bash
npm run populate
```

Or directly:

```bash
node populate-firestore.js
```

---

## ✅ What Gets Populated

### 1. **Restaurants Collection** (`restaurants/`)
- Natraj Restaurant (ID: 12)
- Desi Laddu House (ID: 2)

### 2. **Menu Items** (Subcollection: `restaurants/{id}/menu/`)
- **Natraj Restaurant**: 5 items
  - Garlic Naan
  - Chicken Tikka Masala
  - Saffron Rice
  - Mixed Green Salad
  - Mango Corn Soup
- **Desi Laddu House**: 1 item
  - Kaju Barfi

### 3. **Categories** (Subcollection: `restaurants/{id}/categories/`)
- **Natraj Restaurant**: 4 categories
  - Featured Items
  - Tandoori Specialties
  - Rice
  - Appetizers
- **Desi Laddu House**: 1 category
  - Barfi

### 4. **Promotions** (`promotions/`)
- 3 carousel items for home screen

### 5. **Orders** (`orders/`)
- 2 sample orders for testing

---

## 📁 Firestore Structure

```
eatfair-40f09 (Firestore Database)
│
├── restaurants/
│   ├── 12/
│   │   ├── (restaurant data)
│   │   ├── menu/
│   │   │   ├── 87/ (Garlic Naan)
│   │   │   ├── 88/ (Chicken Tikka Masala)
│   │   │   └── ...
│   │   └── categories/
│   │       ├── 100/ (Featured Items)
│   │       ├── 101/ (Tandoori Specialties)
│   │       └── ...
│   └── 2/
│       ├── (restaurant data)
│       ├── menu/
│       │   └── 1/ (Kaju Barfi)
│       └── categories/
│           └── 1/ (Barfi)
│
├── promotions/
│   ├── 1/ (Flavors in Karbooz)
│   ├── 2/ (Asian Delights)
│   └── 3/ (Italian Classics)
│
└── orders/
    ├── EF1001/
    └── EF1002/
```

---

## 🔍 Verify Data in Firebase Console

After running the script:

1. Go to [Firestore Database](https://console.firebase.google.com/project/eatfair-40f09/firestore)
2. You should see:
   - `restaurants` collection with 2 documents
   - Each restaurant has `menu` and `categories` subcollections
   - `promotions` collection with 3 documents
   - `orders` collection with 2 documents

---

## 🧪 Test the App

After populating Firestore:

1. **Rebuild the apps** (to ensure fresh data):
   ```bash
   cd ..
   ./gradlew :app:assembleDebug :partner:assembleDebug :orderapp:assembleDebug
   ```

2. **Install and test**:
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```

3. **Expected behavior**:
   - Home screen shows 2 restaurants
   - Restaurant details show menu items
   - Partner app shows orders
   - All data is now live from Firestore!

---

## 🔄 Re-running the Script

The script uses `.set()` which will **overwrite** existing data. You can run it multiple times safely.

To **clear** all data first:
1. Go to Firestore Console
2. Delete collections manually
3. Re-run the script

---

## 🛠️ Troubleshooting

### Error: "Cannot find module 'firebase-admin'"
```bash
cd scripts
npm install
```

### Error: "ENOENT: no such file or directory, open 'serviceAccountKey.json'"
- Make sure you downloaded the service account key
- Place it in the `scripts/` directory
- Rename it to exactly `serviceAccountKey.json`

### Error: "Permission denied"
- Check that your service account has Firestore permissions
- Go to IAM & Admin in Google Cloud Console
- Ensure the service account has "Cloud Datastore User" role

---

## 🎉 Success!

Once the script completes, your Firestore database will be populated with sample data, and your apps will fetch real data from the cloud instead of using hardcoded fallbacks!

---

## 📝 Next Steps

1. **Add more restaurants**: Edit `populate-firestore.js` and add more entries
2. **Add more menu items**: Expand the `menuItems` object
3. **Customize promotions**: Update the `carouselItems` array
4. **Test real-time updates**: Make changes in Firestore Console and watch the apps update live!
