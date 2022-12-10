import { NativeModules, Platform, Alert } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-truecaller-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const TruecallerSdk = NativeModules.TruecallerSdk
  ? NativeModules.TruecallerSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function authenticate(): Promise<any> {
  return TruecallerSdk.authenticate().then(data => {
    if(data?.error === "ERROR_APP_NOT_INSTALLED"){
      Alert.alert(
        "App not installed",
        "You need to install the Trucaller app and login to the app to use this authentication process",
        [
          {
            text: "Cancel",
            onPress: () => console.log("Cancel Pressed"),
            style: "cancel"
          },
          { text: "OK", onPress: () => console.log("OK Pressed") }
        ]
      );
      return false;
    } else {
      return data;
    }
    
  });
}

export function SDKClear(): void {
  return TruecallerSdk.SDKClear();
}
