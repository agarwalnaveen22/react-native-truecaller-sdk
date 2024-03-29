package com.truecallersdk;

import android.app.Activity;
import android.graphics.Color;
import android.widget.Toast;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.module.annotations.ReactModule;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueException;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;
import com.truecaller.android.sdk.TruecallerSdkScope;
import com.truecaller.android.sdk.clients.VerificationCallback;
import com.truecaller.android.sdk.clients.VerificationDataBundle;
import static com.truecaller.android.sdk.clients.VerificationDataBundle.KEY_OTP;

@ReactModule(name = TruecallerSdkModule.NAME)
public class TruecallerSdkModule extends ReactContextBaseJavaModule {
  public static final String NAME = "TruecallerSdk";
  private Promise promise = null;

  public TruecallerSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    TruecallerSdkScope trueScope = new TruecallerSdkScope.Builder(reactContext, sdkCallback)
      .consentMode(TruecallerSdkScope.CONSENT_MODE_BOTTOMSHEET)
      .buttonColor(Color.parseColor("#0000FF"))
      .buttonTextColor(Color.parseColor("#FFFFFF"))
      .loginTextPrefix(TruecallerSdkScope.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
      .loginTextSuffix(TruecallerSdkScope.LOGIN_TEXT_SUFFIX_PLEASE_VERIFY_MOBILE_NO)
      .ctaTextPrefix(TruecallerSdkScope.CTA_TEXT_PREFIX_USE)
      .buttonShapeOptions(TruecallerSdkScope.BUTTON_SHAPE_ROUNDED)
      .privacyPolicyUrl("<YOUR-PRIVACY-POLICY-URL>")
      .termsOfServiceUrl("<YOUR-TERMS-OF-SERVICE-URL>")
      .footerType(TruecallerSdkScope.FOOTER_TYPE_NONE)
      .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_LOG_IN)
      .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITHOUT_OTP)
      .build();
    TruecallerSDK.init(trueScope);
    reactContext.addActivityEventListener(mActivityEventListener);  
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  private final ITrueCallback sdkCallback = new ITrueCallback() {
    @Override
    public void onSuccessProfileShared(@NonNull final TrueProfile trueProfile) {

  if (promise != null) {
      WritableMap map = Arguments.createMap();
      map.putBoolean("successful", true);
      map.putString("firstName", trueProfile.firstName);
      map.putString("lastName", trueProfile.lastName);
      map.putString("phoneNumber", trueProfile.phoneNumber);
      map.putString("gender", trueProfile.gender);
      map.putString("street", trueProfile.street);
      map.putString("city", trueProfile.city);
      map.putString("zipcode", trueProfile.zipcode);
      map.putString("countryCode", trueProfile.countryCode);
      map.putString("facebookId", trueProfile.facebookId);
      map.putString("twitterId", trueProfile.twitterId);
      map.putString("email", trueProfile.email);
      map.putString("url", trueProfile.url);
      map.putString("avatarUrl", trueProfile.avatarUrl);
      map.putBoolean("isVerified", trueProfile.isTrueName);
      map.putBoolean("isAmbassador", trueProfile.isAmbassador);
      map.putString("companyName", trueProfile.companyName);
      map.putString("jobTitle", trueProfile.jobTitle);
      map.putString("payload", trueProfile.payload);
      map.putString("signature", trueProfile.signature);
      map.putString("signatureAlgorithm", trueProfile.signatureAlgorithm);
      map.putString("requestNonce", trueProfile.requestNonce);
      map.putBoolean("isBusiness",trueProfile.isBusiness);
      promise.resolve(map);
    }
  }

  @Override
  public void onFailureProfileShared(@NonNull final TrueError trueError) {
    Log.d("TruecallerSDKModule", Integer.toString(trueError.getErrorType()));
    if (promise != null) {
      String errorReason = null;
      switch (trueError.getErrorType()) {
        case TrueError.ERROR_TYPE_INTERNAL:
          errorReason = "ERROR_TYPE_INTERNAL";
          break;
        case TrueError.ERROR_TYPE_NETWORK:
          errorReason = "ERROR_TYPE_NETWORK";
          break;
        case TrueError.ERROR_TYPE_USER_DENIED:
          errorReason = "ERROR_TYPE_USER_DENIED";
          break;
        case TrueError.ERROR_PROFILE_NOT_FOUND:
          errorReason = "ERROR_TYPE_UNAUTHORIZED_PARTNER";
          break;
        case TrueError.ERROR_TYPE_UNAUTHORIZED_USER:
          errorReason = "ERROR_TYPE_UNAUTHORIZED_USER";
          break;
        case TrueError.ERROR_TYPE_TRUECALLER_CLOSED_UNEXPECTEDLY:
          errorReason = "ERROR_TYPE_TRUECALLER_CLOSED_UNEXPECTEDLY";
          break;
        case TrueError.ERROR_TYPE_TRUESDK_TOO_OLD:
          errorReason = "ERROR_TYPE_TRUESDK_TOO_OLD";
          break;
        case TrueError.ERROR_TYPE_POSSIBLE_REQ_CODE_COLLISION:
          errorReason = "ERROR_TYPE_POSSIBLE_REQ_CODE_COLLISION";
          break;
        case TrueError.ERROR_TYPE_RESPONSE_SIGNATURE_MISMATCH:
          errorReason = "ERROR_TYPE_RESPONSE_SIGNATURE_MISSMATCH";
          break;
        case TrueError.ERROR_TYPE_REQUEST_NONCE_MISMATCH:
          errorReason = "ERROR_TYPE_REQUEST_NONCE_MISSMATCH";
          break;
        case TrueError.ERROR_TYPE_INVALID_ACCOUNT_STATE:
          errorReason = "ERROR_TYPE_INVALID_ACCOUNT_STATE";
          break;
        case TrueError.ERROR_TYPE_TC_NOT_INSTALLED:
          errorReason = "ERROR_TYPE_TC_NOT_INSTALLED";
          break;
        case TrueError.ERROR_TYPE_ACTIVITY_NOT_FOUND:
          errorReason = "ERROR_TYPE_ACTIVITY_NOT_FOUND";
          break;
      }
      WritableMap map = Arguments.createMap();
      map.putString("error", errorReason != null ? errorReason : "ERROR_TYPE_NULL");
      promise.resolve(map);
    }
  }

  @Override
  public void onVerificationRequired(TrueError trueError) {
  //The statement below can be ignored incase of one-tap flow integration
    TruecallerSDK.getInstance().requestVerification("IN", "PHONE-NUMBER-STRING", apiCallback,(FragmentActivity) getCurrentActivity());
    }
  };


  //Callback below can be ignored incase of one-tap only integration 

  final VerificationCallback apiCallback = new VerificationCallback() {

  @Override
  public void onRequestSuccess(int requestCode, @Nullable VerificationDataBundle extras) {
  if (requestCode == VerificationCallback.TYPE_MISSED_CALL_INITIATED) {

  //Retrieving the TTL for missedcall 
      if(extras != null){
          extras.getString(VerificationDataBundle.KEY_TTL); 
      }
  }
  if (requestCode == VerificationCallback.TYPE_MISSED_CALL_RECEIVED) {
                
      TrueProfile profile = new TrueProfile.Builder("USER-FIRST-NAME","USER-LAST-NAME").build();
      TruecallerSDK.getInstance().verifyMissedCall(profile, apiCallback);
    }
  if (requestCode == VerificationCallback.TYPE_OTP_INITIATED) {

  //Retrieving the TTL for otp 
      if(extras != null){
          extras.getString(VerificationDataBundle.KEY_TTL); 
      }  
    }
  if (requestCode == VerificationCallback.TYPE_OTP_RECEIVED) {
      TrueProfile profile = new TrueProfile.Builder("USER-FIRST-NAME","USER-LAST-NAME").build();
      TruecallerSDK.getInstance().verifyOtp(profile, "OTP-ENTERED-BY-THE-USER", apiCallback);
    }
    if (requestCode == VerificationCallback.TYPE_VERIFICATION_COMPLETE) {
  }
    if (requestCode == VerificationCallback.TYPE_PROFILE_VERIFIED_BEFORE) {
    }
  }

  @Override
  public void onRequestFailure(final int requestCode, @NonNull final TrueException e) {
      promise.reject(String.valueOf(e));
      }  
  };

  @ReactMethod
  public void authenticate(Promise promise) {
      try {
          this.promise = promise;
            if (TruecallerSDK.getInstance() != null) {
              if(TruecallerSDK.getInstance().isUsable()){
                  TruecallerSDK.getInstance().getUserProfile((FragmentActivity) getCurrentActivity());
              } else {
                WritableMap map = Arguments.createMap();
                    map.putString("error", "ERROR_APP_NOT_INSTALLED");
                    this.promise.resolve(map);
              }
                  
            } else {
                    WritableMap map = Arguments.createMap();
                    map.putString("error", "ERROR_TYPE_NOT_SUPPORTED");
                    this.promise.resolve(map);
            }
        } catch (Exception e) {
                this.promise.reject(e);
            }
        }
       
  @ReactMethod 
  public void SDKClear(){
    TruecallerSDK.clear();
  }

 private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(activity, requestCode, resultCode, intent);
    
    if (requestCode == TruecallerSDK.SHARE_PROFILE_REQUEST_CODE) {
         TruecallerSDK.getInstance().onActivityResultObtained((FragmentActivity)activity, requestCode, resultCode, intent);
        }
      }
  };   
}
