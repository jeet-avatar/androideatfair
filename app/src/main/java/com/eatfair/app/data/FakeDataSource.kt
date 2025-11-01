package com.eatfair.app.data

import com.eatfair.app.model.home.Category
import com.eatfair.app.model.restaurant.MenuItem
import com.eatfair.app.model.restaurant.Restaurant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeDataSource @Inject constructor() {

    private val allRestaurants = listOf(
        Restaurant(
            id = 2,
            name = "Desi Laddu House",
            rating = 4.2f,
            reviews = "5.2K+",
            distance = "3.5 km",
            deliveryTime = "30-35 mins",
            cuisineType = "Sweets, Indian",
            isPureVeg = true,
            isOpen = true,
            tags = listOf("Popular", "Pure Veg"),
            imageUrl = "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/27/d5/bb/74/lounge.jpg?w=900&h=500&s=1",
            address = "14, Chandni Chowk Market, Delhi",
            latitude = 28.6508,
            longitude = 77.2300
        ),
        Restaurant(
            id = 3,
            name = "The Great American Grill",
            rating = 4.5f,
            reviews = "3.1K",
            distance = "1.2 km",
            deliveryTime = "20-25 mins",
            cuisineType = "Burgers, BBQ",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Quick Bite", "Deals"),
            imageUrl = "https://cdn.excelproperties.ae/media/blog/hero/Best_Restaurants_in_Dubai_With_a_View.webp?width=1280&height=384&format=webp&quality=90",
            address = "A-10 Connaught Place, New Delhi",
            latitude = 28.6322,
            longitude = 77.2185
        ),
        Restaurant(
            id = 4,
            name = "Sushi Zen Master",
            rating = 4.7f,
            reviews = "1.8K+",
            distance = "5.0 km",
            deliveryTime = "40-50 mins",
            cuisineType = "Japanese, Sushi",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("High Rated", "Premium"),
            imageUrl = "https://picsum.photos/seed/103/400/300",
            address = "DLF Cyber Hub, Gurugram",
            latitude = 28.4900,
            longitude = 77.0850
        ),
        Restaurant(
            id = 5,
            name = "Mama Mia Pizzeria",
            rating = 4.3f,
            reviews = "7.9K+",
            distance = "2.1 km",
            deliveryTime = "25-30 mins",
            cuisineType = "Italian, Pizza",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Family Pack", "Pizza"),
            imageUrl = "https://picsum.photos/seed/104/400/300",
            address = "22, Hauz Khas Village, New Delhi",
            latitude = 28.5519,
            longitude = 77.1950
        ),
        Restaurant(
            id = 6,
            name = "El Fuego Taco Bar",
            rating = 4.6f,
            reviews = "2.5K",
            distance = "0.8 km",
            deliveryTime = "15-20 mins",
            cuisineType = "Mexican, Tacos",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Staff Pick", "Spicy"),
            imageUrl = "https://picsum.photos/seed/105/400/300",
            address = "G-1 Rajouri Garden, Delhi",
            latitude = 28.6470,
            longitude = 77.1120
        ),
        Restaurant(
            id = 7,
            name = "The Wok Express",
            rating = 4.4f,
            reviews = "4.1K+",
            distance = "3.2 km",
            deliveryTime = "30-40 mins",
            cuisineType = "Chinese, Thai",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Fast Delivery", "Noodles"),
            imageUrl = "https://picsum.photos/seed/106/400/300",
            address = "Food Court, Ambience Mall, Vasant Kunj",
            latitude = 28.5285,
            longitude = 77.1605
        ),
        Restaurant(
            id = 8,
            name = "Mediterranean Delight",
            rating = 4.7f,
            reviews = "1.1K",
            distance = "4.5 km",
            deliveryTime = "40-55 mins",
            cuisineType = "Greek, Lebanese",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Healthy", "High Rated"),
            imageUrl = "https://picsum.photos/seed/107/400/300",
            address = "M-Block Market, Greater Kailash-I",
            latitude = 28.5440,
            longitude = 77.2405
        ),
        Restaurant(
            id = 9,
            name = "Veggie Patch Cafe",
            rating = 4.1f,
            reviews = "6.3K",
            distance = "1.5 km",
            deliveryTime = "20-30 mins",
            cuisineType = "Sandwiches, Salads",
            isPureVeg = true,
            isOpen = true,
            tags = listOf("Lunch Specials", "Vegan"),
            imageUrl = "https://picsum.photos/seed/108/400/300",
            address = "Near Lodhi Garden Gate No. 1, New Delhi",
            latitude = 28.5880,
            longitude = 77.2285
        ),
        Restaurant(
            id = 10,
            name = "French Kiss Bakery",
            rating = 4.6f,
            reviews = "950",
            distance = "2.8 km",
            deliveryTime = "25-35 mins",
            cuisineType = "French, Patisserie",
            isPureVeg = true,
            isOpen = false,
            tags = listOf("Dessert", "Breakfast"),
            imageUrl = "https://picsum.photos/seed/109/400/300",
            address = "Select Citywalk Mall, Saket",
            latitude = 28.5350,
            longitude = 77.2600
        ),
        Restaurant(
            id = 11,
            name = "Coastal Catch",
            rating = 4.3f,
            reviews = "1.5K+",
            distance = "6.0 km",
            deliveryTime = "50-60 mins",
            cuisineType = "Seafood, Grill",
            isPureVeg = false,
            isOpen = true,
            tags = listOf("Premium", "Weekend Special"),
            imageUrl = "https://picsum.photos/seed/110/400/300",
            address = "Sector 29 Market, Noida",
            latitude = 28.5790,
            longitude = 77.3400
        )
    )

    private val allMenuItems = listOf(
        MenuItem(
            1,
            "Kaju Barfi",
            42.0f,
            "Indulge in the heavenly sweetness of kaju barfi...",
            true,
            true,
            true,
            1,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/Kaju_katli_sweet.jpg/1200px-Kaju_katli_sweet.jpg",
            2
        ),
        MenuItem(
            2,
            "Moti Pak",
            57.0f,
            "Is a sumptuous and traditional Indian sweet made from pure ghee...",
            true,
            true,
            false,
            2,
            "https://cdn.jwplayer.com/v2/media/S1UPw9pH/poster.jpg?width=1920",
            2
        ),
        MenuItem(
            3,
            "Besan Laddoo",
            35.0f,
            "Classic gram flour laddoo, rich and melt-in-your-mouth texture.",
            true,
            false,
            false,
            2,
            "https://picsum.photos/seed/203/200/200",
            2
        ),
        MenuItem(
            4,
            "Gajar Halwa",
            65.0f,
            "Carrot-based sweet pudding, slow-cooked in milk and ghee.",
            true,
            true,
            true,
            3,
            "https://picsum.photos/seed/204/200/200",
            2
        ),
        MenuItem(
            5,
            "Milk Cake",
            45.0f,
            "Granulated sweet confectionery, a popular Indian dessert.",
            true,
            false,
            false,
            1,
            "https://picsum.photos/seed/205/200/200",
            2
        ),
        MenuItem(
            6,
            "Soan Papdi",
            30.0f,
            "Flaky, cube-shaped Indian sweet, often served as dessert.",
            true,
            false,
            false,
            1,
            "https://picsum.photos/seed/206/200/200",
            2
        ),
        MenuItem(
            7,
            "Moong Dal Halwa",
            70.0f,
            "A rich, indulgent halwa made from split yellow lentils.",
            true,
            false,
            true,
            3,
            "https://picsum.photos/seed/207/200/200",
            2
        ),
        MenuItem(
            8,
            "Gulab Jamun (2 pcs)",
            50.0f,
            "Deep-fried milk solids balls soaked in a rose-flavored syrup.",
            true,
            true,
            false,
            3,
            "https://picsum.photos/seed/208/200/200",
            2
        ),
        MenuItem(
            9,
            "Kachori (2 pcs)",
            40.0f,
            "Spicy lentil-filled deep-fried pastry.",
            true,
            true,
            false,
            4,
            "https://picsum.photos/seed/209/200/200",
            2
        ),
        MenuItem(
            10,
            "Samosa (2 pcs)",
            30.0f,
            "Crispy pastry filled with spiced potatoes and peas.",
            true,
            true,
            false,
            4,
            "https://picsum.photos/seed/210/200/200",
            2
        ),

        // === Restaurant ID 3: The Great American Grill ===
        MenuItem(
            11,
            "Classic Beef Burger",
            12.99f,
            "A juicy beef patty, cheddar, lettuce, tomato, house sauce.",
            false,
            true,
            true,
            10,
            "https://picsum.photos/seed/211/200/200",
            3
        ),
        MenuItem(
            12,
            "Smoky BBQ Chicken Sandwich",
            14.50f,
            "Grilled chicken breast, smoky BBQ sauce, coleslaw.",
            false,
            true,
            false,
            10,
            "https://picsum.photos/seed/212/200/200",
            3
        ),
        MenuItem(
            13,
            "Portobello Veggie Burger",
            11.99f,
            "Grilled Portobello mushroom, Swiss cheese, aioli.",
            true,
            false,
            true,
            10,
            "https://picsum.photos/seed/213/200/200",
            3
        ),
        MenuItem(
            14,
            "Loaded Chili Cheese Fries",
            7.99f,
            "Crispy fries topped with beef chili and melted cheese.",
            false,
            true,
            false,
            11,
            "https://picsum.photos/seed/214/200/200",
            3
        ),
        MenuItem(
            15,
            "House Salad",
            8.50f,
            "Mixed greens, cucumber, cherry tomatoes, balsamic vinaigrette.",
            true,
            false,
            true,
            11,
            "https://picsum.photos/seed/215/200/200",
            3
        ),
        MenuItem(
            16,
            "Hot Buffalo Wings (6 pcs)",
            10.99f,
            "Classic wings tossed in fiery Buffalo sauce.",
            false,
            true,
            true,
            12,
            "https://picsum.photos/seed/216/200/200",
            3
        ),
        MenuItem(
            17,
            "Mango Milkshake",
            6.00f,
            "Creamy milkshake made with fresh mango pulp.",
            true,
            false,
            false,
            13,
            "https://picsum.photos/seed/217/200/200",
            3
        ),
        MenuItem(
            18,
            "Sweet Potato Fries",
            5.50f,
            "Side of crispy sweet potato fries.",
            true,
            false,
            false,
            11,
            "https://picsum.photos/seed/218/200/200",
            3
        ),
        MenuItem(
            19,
            "Pulled Pork Sliders (3)",
            13.99f,
            "Slow-cooked pulled pork on mini brioche buns.",
            false,
            false,
            false,
            10,
            "https://picsum.photos/seed/219/200/200",
            3
        ),
        MenuItem(
            20,
            "Garlic Parmesan Wings (6)",
            11.50f,
            "Wings coated in rich garlic and Parmesan sauce.",
            false,
            true,
            true,
            12,
            "https://picsum.photos/seed/220/200/200",
            3
        ),
        MenuItem(
            21,
            "Chocolate Oreo Shake",
            6.50f,
            "Thick chocolate shake blended with Oreo cookies.",
            true,
            true,
            false,
            13,
            "https://picsum.photos/seed/221/200/200",
            3
        ),

        // === Restaurant ID 4: Sushi Zen Master ===
        MenuItem(
            22,
            "California Roll (8 pcs)",
            9.50f,
            "Crab stick, avocado, cucumber, sesame seeds.",
            false,
            true,
            false,
            20,
            "https://images.squarespace-cdn.com/content/v1/5021287084ae954efd31e9f4/1585721783314-R7SO7BPEFD9E6SVGNW7I/California+Roll+4.jpg?format=1000w",
            4
        ),
        MenuItem(
            23,
            "Spicy Tuna Roll (8 pcs)",
            11.00f,
            "Fresh tuna mixed with spicy mayo, chili flakes.",
            false,
            true,
            true,
            20,
            "https://picsum.photos/seed/223/200/200",
            4
        ),
        MenuItem(
            24,
            "Salmon Sashimi (3 pcs)",
            14.00f,
            "Finely sliced raw salmon served with wasabi.",
            false,
            true,
            false,
            21,
            "https://picsum.photos/seed/224/200/200",
            4
        ),
        MenuItem(
            25,
            "Avocado Roll (8 pcs)",
            8.00f,
            "Simple and classic roll with fresh avocado.",
            true,
            false,
            false,
            20,
            "https://picsum.photos/seed/225/200/200",
            4
        ),
        MenuItem(
            26,
            "Edamame",
            5.00f,
            "Steamed soybeans sprinkled with sea salt.",
            true,
            true,
            false,
            22,
            "https://picsum.photos/seed/226/200/200",
            4
        ),
        MenuItem(
            27,
            "Miso Soup",
            4.00f,
            "Traditional Japanese soup with tofu and seaweed.",
            true,
            true,
            false,
            23,
            "https://picsum.photos/seed/227/200/200",
            4
        ),
        MenuItem(
            28,
            "Unagi Nigiri (2 pcs)",
            13.00f,
            "Grilled freshwater eel served over rice.",
            false,
            false,
            false,
            21,
            "https://picsum.photos/seed/228/200/200",
            4
        ),
        MenuItem(
            29,
            "Tempura Shrimp (4 pcs)",
            9.99f,
            "Lightly battered and deep-fried shrimp.",
            false,
            true,
            false,
            22,
            "https://picsum.photos/seed/229/200/200",
            4
        ),
        MenuItem(
            30,
            "Ramen (Pork Broth)",
            17.50f,
            "Rich tonkotsu broth, pork belly, soft-boiled egg, noodles.",
            false,
            false,
            true,
            23,
            "https://picsum.photos/seed/230/200/200",
            4
        ),
        MenuItem(
            31,
            "Dragon Roll (8 pcs)",
            16.00f,
            "Eel and cucumber inside, topped with avocado and sweet sauce.",
            false,
            false,
            false,
            20,
            "https://picsum.photos/seed/231/200/200",
            4
        ),
        MenuItem(
            32,
            "Vegetable Tempura",
            8.50f,
            "Mixed seasonal vegetables, lightly battered and fried.",
            true,
            false,
            false,
            22,
            "https://picsum.photos/seed/232/200/200",
            4
        ),
        MenuItem(
            33,
            "Tuna Nigiri (2 pcs)",
            12.50f,
            "Sliced raw tuna over seasoned rice.",
            false,
            false,
            false,
            21,
            "https://picsum.photos/seed/233/200/200",
            4
        ),

        // === Restaurant ID 5: Mama Mia Pizzeria ===
        MenuItem(
            34,
            "Margherita Pizza (12\")",
            15.00f,
            "Classic tomato sauce, fresh mozzarella, and basil.",
            true,
            true,
            true,
            30,
            "https://picsum.photos/seed/234/200/200",
            5
        ),
        MenuItem(
            35,
            "Pepperoni Pizza (12\")",
            17.50f,
            "Classic tomato sauce, mozzarella, and savory pepperoni slices.",
            false,
            true,
            false,
            30,
            "https://picsum.photos/seed/235/200/200",
            5
        ),
        MenuItem(
            36,
            "Truffle Mushroom Pizza (12\")",
            22.00f,
            "White sauce base, exotic mushrooms, and truffle oil.",
            true,
            false,
            true,
            31,
            "https://picsum.photos/seed/236/200/200",
            5
        ),
        MenuItem(
            37,
            "Spaghetti Carbonara",
            16.50f,
            "Creamy sauce with egg yolk, Parmesan, and cured pork.",
            false,
            true,
            false,
            32,
            "https://picsum.photos/seed/237/200/200",
            5
        ),
        MenuItem(
            38,
            "Veggie Lovers Pizza (12\")",
            18.00f,
            "Topped with bell peppers, onions, olives, and mushrooms.",
            true,
            false,
            true,
            30,
            "https://picsum.photos/seed/238/200/200",
            5
        ),
        MenuItem(
            39,
            "Lasagna",
            18.99f,
            "Layers of pasta, rich meat sauce, and ricotta cheese.",
            false,
            false,
            false,
            32,
            "https://picsum.photos/seed/239/200/200",
            5
        ),
        MenuItem(
            40,
            "Chicken Pesto Pizza (12\")",
            19.50f,
            "Pesto base, mozzarella, grilled chicken, and cherry tomatoes.",
            false,
            false,
            false,
            31,
            "https://picsum.photos/seed/240/200/200",
            5
        ),
        MenuItem(
            41,
            "Tiramisu",
            7.00f,
            "Classic coffee-flavored Italian dessert.",
            true,
            true,
            false,
            33,
            "https://picsum.photos/seed/241/200/200",
            5
        ),
        MenuItem(
            42,
            "Fettuccine Alfredo",
            15.50f,
            "Thick pasta in a rich, buttery, and cheesy sauce.",
            true,
            false,
            true,
            32,
            "https://picsum.photos/seed/242/200/200",
            5
        ),
        MenuItem(
            43,
            "Cannoli (2 pcs)",
            6.50f,
            "Crispy fried pastry filled with sweet ricotta cheese.",
            true,
            false,
            false,
            33,
            "https://picsum.photos/seed/243/200/200",
            5
        ),

        // === Restaurant ID 6: El Fuego Taco Bar ===
        MenuItem(
            44,
            "Al Pastor Tacos (3 pcs)",
            11.50f,
            "Spit-grilled pork, pineapple, onion, and cilantro.",
            false,
            true,
            false,
            40,
            "https://picsum.photos/seed/244/200/200",
            6
        ),
        MenuItem(
            45,
            "Carnitas Tacos (3 pcs)",
            10.99f,
            "Slow-cooked seasoned pork, tender and juicy.",
            false,
            true,
            false,
            40,
            "https://picsum.photos/seed/245/200/200",
            6
        ),
        MenuItem(
            46,
            "Grilled Veggie Tacos (3 pcs)",
            9.99f,
            "Seasonal grilled vegetables, black beans, and cotija cheese.",
            true,
            false,
            true,
            40,
            "https://picsum.photos/seed/246/200/200",
            6
        ),
        MenuItem(
            47,
            "Steak Burrito",
            14.50f,
            "Filled with grilled steak, rice, beans, and salsa.",
            false,
            true,
            true,
            41,
            "https://picsum.photos/seed/247/200/200",
            6
        ),
        MenuItem(
            48,
            "Cheese Quesadilla",
            8.50f,
            "Melted Monterey Jack cheese inside a warm flour tortilla.",
            true,
            false,
            false,
            42,
            "https://picsum.photos/seed/248/200/200",
            6
        ),
        MenuItem(
            49,
            "Guacamole & Chips",
            9.00f,
            "Freshly mashed avocado dip served with crispy tortilla chips.",
            true,
            true,
            false,
            42,
            "https://picsum.photos/seed/249/200/200",
            6
        ),
        MenuItem(
            50,
            "Chicken Fajita Burrito",
            13.99f,
            "Sizzling chicken, peppers, onions, and all the fixings.",
            false,
            false,
            true,
            41,
            "https://picsum.photos/seed/250/200/200",
            6
        ),
        MenuItem(
            51,
            "Fish Tacos (3 pcs)",
            12.50f,
            "Battered fish, crunchy slaw, and chipotle crema.",
            false,
            false,
            false,
            40,
            "https://picsum.photos/seed/251/200/200",
            6
        ),
        MenuItem(
            52,
            "Churros with Chocolate",
            6.00f,
            "Crispy, cinnamon-sugar fried dough served with dipping chocolate.",
            true,
            true,
            false,
            43,
            "https://picsum.photos/seed/252/200/200",
            6
        ),
        MenuItem(
            53,
            "Bean & Rice Burrito",
            11.00f,
            "A hearty mix of refried beans, Mexican rice, and cheese.",
            true,
            false,
            true,
            41,
            "https://picsum.photos/seed/253/200/200",
            6
        ),

        // === Restaurant ID 7: The Wok Express ===
        MenuItem(
            54,
            "Pad Thai (Chicken)",
            15.00f,
            "Stir-fried rice noodles with chicken, peanuts, and lime.",
            false,
            true,
            true,
            50,
            "https://picsum.photos/seed/301/200/200",
            7
        ),
        MenuItem(
            55,
            "Veg Spring Rolls (4 pcs)",
            7.50f,
            "Crispy rolls filled with shredded vegetables.",
            true,
            true,
            false,
            53,
            "https://picsum.photos/seed/302/200/200",
            7
        ),
        MenuItem(
            56,
            "Pork Belly Steamed Buns",
            12.50f,
            "Fluffy buns filled with slow-cooked, glazed pork belly.",
            false,
            false,
            false,
            53,
            "https://picsum.photos/seed/303/200/200",
            7
        ),
        MenuItem(
            57,
            "Spicy Schezwan Noodles",
            14.00f,
            "Wok-tossed noodles in a fiery chili garlic sauce.",
            true,
            false,
            true,
            50,
            "https://picsum.photos/seed/304/200/200",
            7
        ),
        MenuItem(
            58,
            "Pineapple Fried Rice",
            16.50f,
            "Rice tossed with shrimp, chicken, cashews, and pineapple.",
            false,
            false,
            false,
            51,
            "https://picsum.photos/seed/305/200/200",
            7
        ),
        MenuItem(
            59,
            "Green Curry (Tofu)",
            15.50f,
            "Aromatic Thai green curry with coconut milk and vegetables.",
            true,
            true,
            false,
            52,
            "https://picsum.photos/seed/306/200/200",
            7
        ),
        MenuItem(
            60,
            "Chicken Lo Mein",
            13.00f,
            "Soft egg noodles stir-fried with chicken and mixed veggies.",
            false,
            true,
            false,
            50,
            "https://picsum.photos/seed/307/200/200",
            7
        ),

        // === Restaurant ID 8: Mediterranean Delight ===
        MenuItem(
            61,
            "Chicken Shawarma Wrap",
            13.99f,
            "Marinated chicken, garlic sauce, and pickles wrapped in pita.",
            false,
            true,
            true,
            61,
            "https://picsum.photos/seed/308/200/200",
            8
        ),
        MenuItem(
            62,
            "Falafel Plate",
            11.50f,
            "Crispy chickpea fritters served with tahini and salad.",
            true,
            true,
            false,
            62,
            "https://picsum.photos/seed/309/200/200",
            8
        ),
        MenuItem(
            63,
            "Lamb Kebab Skewers (2)",
            18.00f,
            "Grilled spiced lamb cubes served with rice.",
            false,
            false,
            false,
            60,
            "https://picsum.photos/seed/310/200/200",
            8
        ),
        MenuItem(
            64,
            "Hummus & Pita",
            8.50f,
            "Creamy chickpea dip served with warm pita bread.",
            true,
            true,
            false,
            62,
            "https://picsum.photos/seed/311/200/200",
            8
        ),
        MenuItem(
            65,
            "Greek Salad",
            10.99f,
            "Feta cheese, olives, tomato, cucumber, and lemon dressing.",
            true,
            false,
            false,
            62,
            "https://picsum.photos/seed/312/200/200",
            8
        ),
        MenuItem(
            66,
            "Beef Kofta Wrap",
            14.50f,
            "Spiced ground beef patties, onions, and yogurt sauce.",
            false,
            false,
            true,
            61,
            "https://picsum.photos/seed/313/200/200",
            8
        ),

        // === Restaurant ID 9: Veggie Patch Cafe ===
        MenuItem(
            67,
            "Avocado Toast",
            9.50f,
            "Smashed avocado, sea salt, and chili flakes on sourdough.",
            true,
            true,
            true,
            70,
            "https://picsum.photos/seed/314/200/200",
            9
        ),
        MenuItem(
            68,
            "Beyond Meat Sandwich",
            13.00f,
            "Plant-based patty, vegan cheese, lettuce, tomato.",
            true,
            false,
            true,
            70,
            "https://picsum.photos/seed/315/200/200",
            9
        ),
        MenuItem(
            69,
            "Quinoa Power Bowl",
            14.50f,
            "Quinoa, kale, sweet potato, chickpeas, and tahini dressing.",
            true,
            true,
            false,
            71,
            "https://picsum.photos/seed/316/200/200",
            9
        ),
        MenuItem(
            70,
            "Lentil Soup",
            6.00f,
            "Hearty lentil soup, served with crusty bread.",
            true,
            true,
            false,
            72,
            "https://picsum.photos/seed/317/200/200",
            9
        ),
        MenuItem(
            71,
            "Caprese Panini",
            11.99f,
            "Fresh mozzarella, tomato, basil, and balsamic glaze, grilled.",
            true,
            false,
            false,
            70,
            "https://picsum.photos/seed/318/200/200",
            9
        ),
        MenuItem(
            72,
            "Arugula & Berry Salad",
            12.50f,
            "Arugula, seasonal berries, walnuts, and goat cheese.",
            true,
            false,
            false,
            71,
            "https://picsum.photos/seed/319/200/200",
            9
        ),
        MenuItem(
            73,
            "Tomato Basil Soup",
            5.50f,
            "Classic creamy tomato soup with basil notes.",
            true,
            false,
            false,
            72,
            "https://picsum.photos/seed/320/200/200",
            9
        ),

        // === Restaurant ID 10: French Kiss Bakery ===
        MenuItem(
            74,
            "Classic Butter Croissant",
            4.00f,
            "Flaky, buttery, perfectly golden-brown croissant.",
            true,
            true,
            false,
            80,
            "https://picsum.photos/seed/321/200/200",
            10
        ),
        MenuItem(
            75,
            "Almond Croissant",
            5.50f,
            "Filled and topped with sweet almond frangipane.",
            true,
            false,
            false,
            80,
            "https://picsum.photos/seed/322/200/200",
            10
        ),
        MenuItem(
            76,
            "Chocolate Eclair",
            6.00f,
            "Light pastry filled with cream and topped with chocolate icing.",
            true,
            true,
            false,
            81,
            "https://picsum.photos/seed/323/200/200",
            10
        ),
        MenuItem(
            77,
            "Strawberry Tart (slice)",
            7.50f,
            "Sweet pastry crust with custard and fresh strawberries.",
            true,
            false,
            false,
            81,
            "https://picsum.photos/seed/324/200/200",
            10
        ),
        MenuItem(
            78,
            "Plain Baguette",
            3.00f,
            "Crispy on the outside, soft on the inside, perfect for dipping.",
            true,
            false,
            false,
            82,
            "https://picsum.photos/seed/325/200/200",
            10
        ),
        MenuItem(
            79,
            "Pain au Chocolat",
            4.50f,
            "A classic croissant wrapped around dark chocolate.",
            true,
            true,
            false,
            80,
            "https://picsum.photos/seed/326/200/200",
            10
        ),
        MenuItem(
            80,
            "Lemon Meringue Cake (slice)",
            7.99f,
            "Tart lemon filling beneath a toasted meringue.",
            true,
            false,
            false,
            81,
            "https://picsum.photos/seed/327/200/200",
            10
        ),

        // === Restaurant ID 11: Coastal Catch ===
        MenuItem(
            81,
            "Grilled Salmon Fillet",
            25.00f,
            "Perfectly grilled salmon, served with roasted vegetables.",
            false,
            true,
            true,
            90,
            "https://media.istockphoto.com/id/1214416414/photo/barbecued-salmon-fried-potatoes-and-vegetables-on-wooden-background.jpg?s=612x612&w=0&k=20&c=Y8RYbZFcvec-FXMMuoU-qkprC3TUFNiw3Ysoe8Drn6g=",
            11
        ),
        MenuItem(
            82,
            "New England Clam Chowder",
            9.00f,
            "Thick, creamy chowder filled with clams and potatoes.",
            false,
            true,
            false,
            91,
            "https://picsum.photos/seed/329/200/200",
            11
        ),
        MenuItem(
            83,
            "Calamari Rings",
            14.50f,
            "Lightly battered and fried calamari, served with marinara.",
            false,
            false,
            false,
            90,
            "https://picsum.photos/seed/330/200/200",
            11
        ),
        MenuItem(
            84,
            "Shrimp Fish Tacos (2 pcs)",
            16.00f,
            "Grilled shrimp, mango salsa, and cilantro-lime slaw.",
            false,
            false,
            true,
            92,
            "https://picsum.photos/seed/331/200/200",
            11
        ),
        MenuItem(
            85,
            "Classic French Fries",
            5.00f,
            "Crispy, salted classic potato fries.",
            true,
            false,
            false,
            91,
            "https://picsum.photos/seed/332/200/200",
            11
        ),
        MenuItem(
            86,
            "Grilled Seabass",
            28.00f,
            "Seabass seasoned with herbs, grilled to perfection.",
            false,
            false,
            true,
            90,
            "https://picsum.photos/seed/333/200/200",
            11
        )
    )

    private val allCategories = listOf(
        // Desi Laddu House (ID 2)
        Category(1, "Barfi", "🔶", true),
        Category(2, "Laddoo", "🟡", true),
        Category(3, "Halwa", "🍮"),
        Category(4, "Savory Snacks", "🥨"),
        // The Great American Grill (ID 3)
        Category(10, "Signature Burgers", "🍔", true),
        Category(11, "Fries & Sides", "🍟", true),
        Category(12, "BBQ Wings", "🍗", true),
        Category(13, "Shakes", "🥤"),
        // Sushi Zen Master (ID 4)
        Category(20, "Classic Rolls", "🍣", true),
        Category(21, "Sashimi/Nigiri", "🐟", true),
        Category(22, "Appetizers", "🥢"),
        Category(23, "Soup & Noodles", "🍜"),
        // Mama Mia Pizzeria (ID 5)
        Category(30, "Classic Pizzas", "🍕", true),
        Category(31, "Gourmet Pizzas", "👨‍🍳"),
        Category(32, "Pasta", "🍝", true),
        Category(33, "Desserts", "🍰"),
        // El Fuego Taco Bar (ID 6)
        Category(40, "Tacos", "🌮", true),
        Category(41, "Burritos", "🌯", true),
        Category(42, "Sides & Dips", "🌶️"),
        Category(43, "Dessert", "🫔"),
        // The Wok Express (ID 7)
        Category(50, "Noodles", "🍜", true),
        Category(51, "Fried Rice", "🍚", true),
        Category(52, "Thai Curries", "🥥"),
        Category(53, "Dim Sum", "🥟"),
        // Mediterranean Delight (ID 8)
        Category(60, "Kebabs & Skewers", "🔥", true),
        Category(61, "Pitas & Wraps", "🫓", true),
        Category(62, "Salads & Sides", "🥗"),
        // Veggie Patch Cafe (ID 9)
        Category(70, "Gourmet Sandwiches", "🥪", true),
        Category(71, "Fresh Salads", "🥬", true),
        Category(72, "Soups", "🥣"),
        // French Kiss Bakery (ID 10)
        Category(80, "Croissants", "🥐", true),
        Category(81, "Cakes & Tarts", "🎂", true),
        Category(82, "Baguettes", "🥖"),
        // Coastal Catch (ID 11)
        Category(90, "Grilled Seafood", "🦞", true),
        Category(91, "Fries & Chowder", "🥣"),
        Category(92, "Fish Tacos", "🌮")
    )

    // Public functions to access the data.
    fun getRestaurants(): List<Restaurant> = allRestaurants

    fun getRestaurantById(id: Int): Restaurant? = allRestaurants.find { it.id == id }

    fun getMenuItemsForRestaurant(restaurantId: Int): List<MenuItem> {
        return allMenuItems.filter { it.restaurantId == restaurantId }
    }

    fun getCategories(): List<Category> = allCategories

    fun getCategoriesForRestaurant(restaurantId: Int): List<Category> {
        // This is a more complex example. In a real app, restaurants and categories would have a many-to-many relationship.
        // For this fake data, we can filter based on the menu item categories.
        val categoryIdsForRestaurant =
            getMenuItemsForRestaurant(restaurantId).map { it.categoryId }.toSet()
        return allCategories.filter { it.id in categoryIdsForRestaurant }
    }
}
