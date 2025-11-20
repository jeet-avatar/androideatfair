const admin = require('firebase-admin');

// Initialize Firebase Admin SDK
// You'll need to download your service account key from Firebase Console
// Project Settings > Service Accounts > Generate New Private Key
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// Sample data from RestaurantRepo.kt
const restaurants = [
    {
        id: 12,
        name: "Natraj Restaurant",
        rating: 4.5,
        reviews: "1.2K+",
        distance: "1.0 km",
        deliveryTime: "20-25 mins",
        cuisineType: "Indian",
        isPureVeg: false,
        isOpen: true,
        tags: ["Featured", "Indian"],
        imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
        address: "22205 El Paseo #A, Rancho Santa Margarita, CA 92688",
        latitude: 33.6402,
        longitude: -117.6033
    },
    {
        id: 2,
        name: "Desi Laddu House",
        rating: 4.2,
        reviews: "5.2K+",
        distance: "3.5 km",
        deliveryTime: "30-35 mins",
        cuisineType: "Sweets, Indian",
        isPureVeg: true,
        isOpen: true,
        tags: ["Popular", "Pure Veg"],
        imageUrl: "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1",
        address: "14, Chandni Chowk Market, Delhi",
        latitude: 28.6508,
        longitude: 77.2300
    }
];

const menuItems = {
    12: [ // Natraj Restaurant
        {
            id: 87,
            name: "Garlic Naan",
            price: 5.00,
            description: "Leavened white bread topped with garlic & cilantro baked to order in the tandoor...",
            isVeg: true,
            isHighlyOrdered: true,
            isCustomizable: true,
            categoryId: 100,
            imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            restaurantId: 12,
            isAvailable: true
        },
        {
            id: 88,
            name: "Chicken Tikka Masala",
            price: 24.00,
            description: "Everybody's Favorite! Tandoori roasted breast of chicken in a velvety sauce of...",
            isVeg: false,
            isHighlyOrdered: true,
            isCustomizable: false,
            categoryId: 101,
            imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            restaurantId: 12,
            isAvailable: true
        },
        {
            id: 89,
            name: "Saffron Rice",
            price: 5.00,
            description: "",
            isVeg: true,
            isHighlyOrdered: false,
            isCustomizable: false,
            categoryId: 102,
            imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            restaurantId: 12,
            isAvailable: true
        },
        {
            id: 90,
            name: "Mixed Green Salad",
            price: 8.00,
            description: "",
            isVeg: true,
            isHighlyOrdered: false,
            isCustomizable: true,
            categoryId: 103,
            imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            restaurantId: 12,
            isAvailable: true
        },
        {
            id: 91,
            name: "Mango Corn Soup",
            price: -1,
            description: "",
            isVeg: true,
            isHighlyOrdered: false,
            isCustomizable: false,
            categoryId: 103,
            imageUrl: "https://s3-media1.fl.yelpcdn.com/bphoto/A5Ky-6s0kl9i9c3v3p_OIA/o.jpg",
            restaurantId: 12,
            isAvailable: true
        }
    ],
    2: [ // Desi Laddu House
        {
            id: 1,
            name: "Kaju Barfi",
            price: 42.0,
            description: "Indulge in the heavenly sweetness of kaju barfi...",
            isVeg: true,
            isHighlyOrdered: true,
            isCustomizable: true,
            categoryId: 1,
            imageUrl: "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Kaju_katli_sweet.jpg/1200px-Kaju_katli_sweet.jpg",
            restaurantId: 2,
            isAvailable: true
        }
    ]
};

const categories = {
    12: [ // Natraj Restaurant
        { id: 100, name: "Featured Items", icon: "⭐", isHighlighted: true },
        { id: 101, name: "Tandoori Specialties", icon: "🔥", isHighlighted: true },
        { id: 102, name: "Rice", icon: "🍚", isHighlighted: false },
        { id: 103, name: "Appetizers", icon: "🥗", isHighlighted: false }
    ],
    2: [ // Desi Laddu House
        { id: 1, name: "Barfi", icon: "🔶", isHighlighted: true }
    ]
};

const carouselItems = [
    {
        id: 1,
        title: "Flavors in Karbooz",
        subtitle: "Spice Up Your Day",
        description: "Taste Mexico!\nCraving Bold Mexican Flavors?",
        backgroundColor: "#FFB6D9",
        imageUrl: "",
        isActive: true
    },
    {
        id: 2,
        title: "Asian Delights",
        subtitle: "Experience The East",
        description: "Authentic Asian\nCuisine Awaits!",
        backgroundColor: "#B6E5FF",
        imageUrl: "",
        isActive: true
    },
    {
        id: 3,
        title: "Italian Classics",
        subtitle: "Taste of Italy",
        description: "Fresh Pasta &\nPizza Daily!",
        backgroundColor: "#FFE5B6",
        imageUrl: "",
        isActive: true
    }
];

async function populateFirestore() {
    try {
        console.log('🚀 Starting Firestore population...\n');

        // 1. Populate Restaurants
        console.log('📍 Adding restaurants...');
        for (const restaurant of restaurants) {
            await db.collection('restaurants').doc(restaurant.id.toString()).set(restaurant);
            console.log(`   ✅ Added: ${restaurant.name}`);
        }

        // 2. Populate Menu Items (subcollection)
        console.log('\n🍽️  Adding menu items...');
        for (const [restaurantId, items] of Object.entries(menuItems)) {
            for (const item of items) {
                await db.collection('restaurants')
                    .doc(restaurantId)
                    .collection('menu')
                    .doc(item.id.toString())
                    .set(item);
                console.log(`   ✅ Added: ${item.name} (Restaurant ${restaurantId})`);
            }
        }

        // 3. Populate Categories (subcollection)
        console.log('\n📂 Adding categories...');
        for (const [restaurantId, cats] of Object.entries(categories)) {
            for (const category of cats) {
                await db.collection('restaurants')
                    .doc(restaurantId)
                    .collection('categories')
                    .doc(category.id.toString())
                    .set(category);
                console.log(`   ✅ Added: ${category.name} (Restaurant ${restaurantId})`);
            }
        }

        // 4. Populate Carousel/Promotions
        console.log('\n🎠 Adding carousel items...');
        for (const item of carouselItems) {
            await db.collection('promotions').doc(item.id.toString()).set(item);
            console.log(`   ✅ Added: ${item.title}`);
        }

        // 5. Create sample orders (optional)
        console.log('\n📦 Adding sample orders...');
        const sampleOrders = [
            {
                orderId: "EF1001",
                restaurantId: 12,
                status: "ORDER_PLACED",
                deliveryPartner: {
                    name: "Jithesh Manoharan",
                    phone: "+1-949-123-4567"
                },
                estimatedTime: "25 mins",
                pickupLocation: {
                    address: "22205 El Paseo",
                    lat: 33.6402,
                    lng: -117.6033
                },
                deliveryInstructions: "Leave at door",
                isDelayed: false,
                createdAt: admin.firestore.FieldValue.serverTimestamp()
            },
            {
                orderId: "EF1002",
                restaurantId: 12,
                status: "PREPARING",
                deliveryPartner: {
                    name: "Sarah Jones",
                    phone: "+1-555-0102"
                },
                estimatedTime: "15 mins",
                pickupLocation: {
                    address: "22205 El Paseo",
                    lat: 33.6402,
                    lng: -117.6033
                },
                deliveryInstructions: "",
                isDelayed: true,
                createdAt: admin.firestore.FieldValue.serverTimestamp()
            }
        ];

        for (const order of sampleOrders) {
            await db.collection('orders').doc(order.orderId).set(order);
            console.log(`   ✅ Added: Order ${order.orderId}`);
        }

        console.log('\n✨ Firestore population complete!');
        console.log('\n📊 Summary:');
        console.log(`   - Restaurants: ${restaurants.length}`);
        console.log(`   - Menu Items: ${Object.values(menuItems).flat().length}`);
        console.log(`   - Categories: ${Object.values(categories).flat().length}`);
        console.log(`   - Promotions: ${carouselItems.length}`);
        console.log(`   - Orders: ${sampleOrders.length}`);

        process.exit(0);
    } catch (error) {
        console.error('❌ Error populating Firestore:', error);
        process.exit(1);
    }
}

populateFirestore();
