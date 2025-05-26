# 🚀 mobile-sdk-wrapper-nativescript-okhttp

**mobile-sdk-wrapper-nativescript-okhttp** is a wrapper around the [**Android DataDome SDK**](https://docs.datadome.co/docs/sdk-android) for NativeScript projects.

## Project structure
The project is organized as follows:
```bash
.
├── ns-datadome-android/    # Android module - Kotlin wrapper for the Android DataDome SDK
├── sample-app/             # Example NativeScript app demonstrating how to integrate the module
├── README.md               # Project documentation
```

## Prerequisite - Enable Kotlin support
Note: This wrapper is written in Kotlin, so Kotlin support must be enabled in your NativeScript project.
Please follow the steps below to enable it. You can also refer to the [official NativeScript documentation](https://docs.nativescript.org/guide/native-code/android#configuring-kotlin) for more details.

1. Set useKotlin=true in `App_Resources/Android/gradle.properties`  (create the file if it doesn't exist).
```gradle
useKotlin=true
```

2. Configure the version of Kotlin to use in the application in App_Resources/Android/before-plugins.gradle (create the file if it doesn't exist).
```gradle
project.ext {
  kotlinVersion = "1.9.10"
}
```

## Installation
You have two options to integrate the wrapper into your NativeScript project, depending on your project organization and preference: 

### 🅰️ Option A: Use the Full Module (ns-datadome-android)
1. Clone or download the `ns-datadome-android` module, then place in inside your NativeScript project's root folder.
```bash
your-nativescript-app/
├── app/
├── ns-datadome-android/    ← Place the module here
├── ...
```
2. Add the module as a dependency to your app. 

- In your `App_Resources/Android/settings.gradle` (create the file if doesn't exist): 
```gradle
// Adjust the path below according to your project structure.
// The path '../../../ns-datadome-android/wrapper' assumes that the module is located in the root folder of the NativeScript project.
include ':wrapper'
project(':wrapper').projectDir = new File('../../../ns-datadome-android/wrapper')
```

- In your `App_Resources/Android/app.gradle`, add the following dependencies:
```gradle
implementation project(":wrapper")
implementation 'co.datadome.sdk:sdk:1.15.3'
```
Make sure to replace 1.15.3 with the latest version of the DataDome SDK if necessary.

### 🅱️ Option B: Copy the Kotlin file directly
If you prefer not to include the entire module, you can copy the Kotlin wrapper file manually into your project.

- Download the Kotlin wrapper file from the following path in this repository:
[DatadomeWrapper.kt](https://github.com/DataDome/mobile-sdk-wrapper-nativescript-okhttp/blob/feat/INT-5072-add-wrapper/ns-datadome-android/wrapper/src/main/java/co/datadome/ns/wrapper/DataDomeSDKWrapper.kt)

- Paste it into your NativeScript app's App_Resources/Android source folder:
```bash
your-nativescript-app/
└── App_Resources/
    └── Android/
        └── src/
            └── main/
                └── java/
                    └── co/
                        └── datadome/
                            └── ns/
                                └── wrapper/
                                    └── DatadomeWrapper.kt
```


## Usage
In your TypeScript file, follow these steps to initialize and use the SDK:
- Declare the Kotlin namespace:
```TypeScript
declare const co: any;
```

- Initialize the SDK:
```TypeScript
this.dataDomeSDKWrapper = new co.datadome.ns.wrapper.DataDomeSDKWrapper(
  androidApplication,
  'YOUR_API_KEY',
  'YOU_APP_VERSION',
  true // if you want to enable logs
);
```
- Define the completion handler:
```TypeScript
const completionHandler = new co.datadome.ns.wrapper.DataDomeSDKWrapper.CompletionHandler({
  onSuccess(response: string) {
    console.log('✅ Request succeeded:', response);
    this.message = response;
  },
  onError(error: string) {
    console.error('❌ Request failed:', error);
  }
});
```

- Make a request:
```TypeScript
const headers = new java.util.HashMap();
headers.put('User-Agent', 'BLOCKUA');
headers.put('Accept', 'application/json');

const body = JSON.stringify({
  techno: 'Nativescript',
  role: 'wrapper'
});

this.dataDomeSDKWrapper.makeRequest(
  'post',
  'YOUR_ENDPOINT',
  headers,
  body,
  completionHandler
);
```


