
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNTruecallerSdkSpec.h"

@interface TruecallerSdk : NSObject <NativeTruecallerSdkSpec>
#else
#import <React/RCTBridgeModule.h>

@interface TruecallerSdk : NSObject <RCTBridgeModule>
#endif

@end
