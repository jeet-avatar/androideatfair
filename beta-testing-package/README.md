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
