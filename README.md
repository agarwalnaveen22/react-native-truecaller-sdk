# react-native-truecaller-sdk

This plugin is a bootstrap to use Truecaller SDK

## Installation

```sh
npm install react-native-truecaller-sdk
```

## Android Setup

Create a new string to add partner key
```sh
<string name="partner_key"><Your Partner Key></string>
```

Add partner key meta-data in AndroidManifest.xml
```sh
<meta-data android:name="com.truecaller.android.sdk.PartnerKey" android:value="@string/partner_key" />
```

Open Android build.gradle file and add jcenter() support in allProjects -> repositories
```sh
allprojects {
    repositories {
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url("$rootDir/../node_modules/react-native/android")
        }
        maven {
            // Android JSC is installed from npm
            url("$rootDir/../node_modules/jsc-android/dist")
        }
        mavenCentral {
            // We don't want to fetch react-native from Maven Central as there are
            // older versions over there.
            content {
                excludeGroup "com.facebook.react"
            }
        }
        google()
        maven { url 'https://www.jitpack.io' }
        jcenter() <--- Add This
    }
}
```

## Usage

```js
import { authenticate } from 'react-native-truecaller-sdk';

// ...

const result = await authenticate());
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

