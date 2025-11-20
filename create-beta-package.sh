#!/bin/bash

# EatFair Beta Testing Package Creator
# This script packages everything needed for beta testing

echo "📦 Creating Beta Testing Package..."
echo ""

# Create package directory
PACKAGE_DIR="beta-testing-package"
mkdir -p "$PACKAGE_DIR"
mkdir -p "$PACKAGE_DIR/apks"
mkdir -p "$PACKAGE_DIR/docs"

# Copy APKs
echo "📱 Copying APKs..."
cp app/build/outputs/apk/debug/app-debug.apk "$PACKAGE_DIR/apks/customer-app.apk" 2>/dev/null
cp partner/build/outputs/apk/debug/partner-debug.apk "$PACKAGE_DIR/apks/partner-app.apk" 2>/dev/null
cp orderapp/build/outputs/apk/debug/orderapp-debug.apk "$PACKAGE_DIR/apks/delivery-app.apk" 2>/dev/null

# Copy documentation
echo "📄 Copying documentation..."
cp BETA_TESTER_GUIDE.md "$PACKAGE_DIR/" 2>/dev/null
cp TESTING_GUIDE.md "$PACKAGE_DIR/docs/" 2>/dev/null
cp API_KEYS_STATUS.md "$PACKAGE_DIR/docs/" 2>/dev/null
cp PRODUCTION_READINESS.md "$PACKAGE_DIR/docs/" 2>/dev/null

# Create README
cat > "$PACKAGE_DIR/README.md" << 'EOF'
# EatFair Delivery - Beta Testing Package

Welcome to the EatFair Delivery beta testing program! 🎉

## 📱 Quick Start

1. **Install all three apps** from the `apks/` folder:
   - `customer-app.apk` - Order food
   - `partner-app.apk` - Manage restaurant
   - `delivery-app.apk` - Handle deliveries

2. **Read the guide**: `BETA_TESTER_GUIDE.md`

3. **Start testing**: Follow the test scenarios in the guide

## 📂 Package Contents

```
beta-testing-package/
├── README.md (this file)
├── BETA_TESTER_GUIDE.md (comprehensive testing guide)
├── apks/
│   ├── customer-app.apk
│   ├── partner-app.apk
│   └── delivery-app.apk
└── docs/
    ├── TESTING_GUIDE.md
    ├── API_KEYS_STATUS.md
    └── PRODUCTION_READINESS.md
```

## 🚀 Installation

### Option 1: Direct Install (Easiest)
1. Transfer APK files to your Android device
2. Enable "Install from Unknown Sources" in Settings
3. Tap each APK file to install

### Option 2: ADB Install (If you have ADB)
```bash
adb install customer-app.apk
adb install partner-app.apk
adb install delivery-app.apk
```

## 🧪 Test Scenarios

See `BETA_TESTER_GUIDE.md` for detailed test scenarios.

**Quick Test**:
1. Open Customer App
2. Register/Login
3. Browse restaurants
4. Add items to cart
5. Place order
6. Open Partner App to see the order
7. Update order status
8. Watch it update in Customer App in real-time!

## 📝 Feedback

Please report:
- Bugs and crashes
- UI/UX issues
- Performance problems
- Feature suggestions

**Feedback Form**: [Your feedback form link here]
**Email**: support@eatfair.com

## ⚠️ Known Limitations

- Test mode security (data not fully protected)
- Dummy payment system (no real charges)
- Limited push notifications
- Basic delivery app features
- 2 sample restaurants only

## 🙏 Thank You!

Your feedback is invaluable. Thank you for helping us build EatFair Delivery!

---

**Version**: 1.0.0-beta  
**Build Date**: November 19, 2025  
**Firebase Project**: eatfair-40f09
EOF

# Create installation guide
cat > "$PACKAGE_DIR/INSTALLATION.md" << 'EOF'
# Installation Guide

## For Android Devices

### Step 1: Enable Unknown Sources
1. Go to **Settings** > **Security**
2. Enable **"Install from Unknown Sources"** or **"Allow from this source"**
3. (On Android 8+, you'll be prompted when installing)

### Step 2: Transfer APKs
**Option A: Email**
- Email the APK files to yourself
- Open email on your phone
- Download the attachments

**Option B: USB Transfer**
- Connect phone to computer via USB
- Copy APK files to phone's Download folder
- Disconnect phone

**Option C: Cloud Storage**
- Upload APKs to Google Drive/Dropbox
- Download on your phone

### Step 3: Install Apps
1. Open **File Manager** on your phone
2. Navigate to **Downloads** folder
3. Tap each APK file:
   - `customer-app.apk`
   - `partner-app.apk`
   - `delivery-app.apk`
4. Tap **Install** for each
5. Wait for installation to complete

### Step 4: Open Apps
1. Find the apps in your app drawer:
   - **EatFair** (customer app)
   - **EatFair Partner** (partner app)
   - **EatFair Order** (delivery app)
2. Open each app to verify installation

## Troubleshooting

### "App not installed" error
- Make sure you have enough storage space
- Try uninstalling any previous version
- Restart your phone and try again

### "Installation blocked" error
- Check that "Unknown Sources" is enabled
- On some phones, you need to enable it per-app

### App crashes on startup
- Clear app data: Settings > Apps > [App Name] > Clear Data
- Reinstall the app
- Report the crash to us

## System Requirements

- **Android Version**: 7.0 (Nougat) or higher
- **Storage**: At least 100MB free space
- **Internet**: Required for all features
- **Location**: Required for address selection (Customer App)

## Need Help?

Email: support@eatfair.com
EOF

# Create feedback template
cat > "$PACKAGE_DIR/FEEDBACK_TEMPLATE.md" << 'EOF'
# Bug Report Template

**App**: Customer / Partner / Delivery  
**Device**: [e.g., Samsung Galaxy S21]  
**Android Version**: [e.g., Android 13]  
**Date**: [Date of issue]

## Issue Description
[Brief description of the problem]

## Steps to Reproduce
1. 
2. 
3. 

## Expected Behavior
[What should happen]

## Actual Behavior
[What actually happened]

## Screenshots
[Attach screenshots if applicable]

## Additional Notes
[Any other relevant information]

---

# Feature Request Template

**Feature Name**: [Name of the feature]  
**Priority**: Low / Medium / High  
**Category**: UI/UX / Functionality / Performance / Other

## Description
[Detailed description of the feature]

## Use Case
[Why is this feature needed? What problem does it solve?]

## Suggested Implementation
[Optional: How you think it should work]

---

# General Feedback

**What do you like most about the app?**


**What frustrates you the most?**


**What features are missing?**


**Overall rating** (1-5 stars): ⭐⭐⭐⭐⭐

**Would you recommend this app?** Yes / No / Maybe

**Additional comments**:
EOF

# Create version info
cat > "$PACKAGE_DIR/VERSION_INFO.txt" << EOF
EatFair Delivery - Beta Version

Build Information:
------------------
Version: 1.0.0-beta
Build Date: $(date +"%Y-%m-%d %H:%M:%S")
Build Type: Debug
Firebase Project: eatfair-40f09

APK Details:
------------
Customer App: app-debug.apk (~32 MB)
Partner App: partner-debug.apk (~23 MB)
Delivery App: orderapp-debug.apk (~22 MB)

Package Names:
--------------
Customer: com.eatfair.app
Partner: com.eatfair.partner
Delivery: com.eatfair.orderapp

Backend:
--------
Authentication: Firebase Auth (Email/Password)
Database: Cloud Firestore (Test Mode)
Storage: Firebase Cloud Storage
Maps: Google Maps API

Known Issues:
-------------
- Test mode security (acceptable for beta)
- Dummy payment system
- Limited push notifications
- Basic delivery app features
- Limited restaurant data (2 restaurants)

Testing Period:
---------------
Start: $(date +"%Y-%m-%d")
Duration: 2-4 weeks
Expected Testers: 10-50 users

Support:
--------
Email: support@eatfair.com
Feedback Form: [To be added]
EOF

# Create a summary
echo ""
echo "✅ Package created successfully!"
echo ""
echo "📦 Package location: $PACKAGE_DIR/"
echo ""
echo "📂 Contents:"
ls -lh "$PACKAGE_DIR/apks/" 2>/dev/null | grep -v total | awk '{print "   - " $9 " (" $5 ")"}'
echo ""
echo "📄 Documentation:"
ls -1 "$PACKAGE_DIR/"*.md 2>/dev/null | xargs -I {} basename {} | awk '{print "   - " $0}'
echo ""
echo "🎯 Next steps:"
echo "   1. Review the package contents"
echo "   2. Add your feedback form link to README.md"
echo "   3. Zip the package: zip -r beta-testing-package.zip beta-testing-package/"
echo "   4. Share with testers via email/cloud storage"
echo ""
echo "📧 Sample email template saved in: $PACKAGE_DIR/docs/EMAIL_TEMPLATE.txt"
echo ""

# Create email template
mkdir -p "$PACKAGE_DIR/docs"
cat > "$PACKAGE_DIR/docs/EMAIL_TEMPLATE.txt" << 'EOF'
Subject: EatFair Delivery - Beta Testing Invitation 🚀

Hi [Tester Name],

Thank you for joining our EatFair Delivery beta testing program!

WHAT IS EATFAIR DELIVERY?
A complete food delivery ecosystem with three interconnected apps:
• Customer App - Browse restaurants and order food
• Partner App - Manage restaurant orders and menu
• Delivery App - Handle delivery tasks

WHAT YOU'LL BE TESTING:
✓ User registration and authentication
✓ Restaurant browsing and search
✓ Order placement and tracking
✓ Real-time order updates
✓ Partner order management
✓ Delivery coordination

INSTALLATION:
1. Download the attached ZIP file
2. Extract the three APK files
3. Install all three on your Android device
4. Follow the BETA_TESTER_GUIDE.md for detailed instructions

TESTING PERIOD:
• Duration: 2-4 weeks
• Start Date: [Date]
• Expected Time: 2-3 hours total

FEEDBACK:
Please report bugs, issues, and suggestions via:
📝 Feedback Form: [Your form link]
📧 Email: support@eatfair.com

WHAT'S IN IT FOR YOU:
🎁 Early access to the platform
💰 Exclusive launch discounts
🌟 Recognition as a founding tester
📣 Direct influence on features

IMPORTANT NOTES:
⚠️ This is a BETA version with test data
⚠️ No real payments will be processed
⚠️ Data may be reset during testing
⚠️ Some features are still in development

SUPPORT:
If you have any questions or encounter issues:
• Email: support@eatfair.com
• Response time: Within 24 hours

Thank you for helping us build something amazing!

Best regards,
The EatFair Team

---
Attached:
- beta-testing-package.zip (contains APKs and documentation)
EOF

echo "✨ Beta testing package is ready!"
echo ""
echo "📦 To share with testers:"
echo "   zip -r beta-testing-package.zip beta-testing-package/"
echo ""
