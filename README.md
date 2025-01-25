# UTAC
### UTAC, now with multiplatform support!

## How to build:
- Android and Desktop
  - Open in android studio
  - Rename key.properties.example to key.properties and fill in values
  - Build like normal
- iOS (Requires a Mac with macOS 14.5 or later)
  - Perform above steps, then:
  - Open iosApp/ in XCode at least once
  - Go to iosApp/ in a terminal and run:
  - `xcodebuild CODE_SIGNING_REQUIRED=no ENTITLEMENTS_REQUIRED=no CODE_SIGN_IDENTITY="" build`