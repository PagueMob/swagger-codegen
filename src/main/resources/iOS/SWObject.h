#import <Foundation/Foundation.h>

@interface SWObject : NSObject

- (id) initWithValues: (NSDictionary*)dict;

- (NSDictionary*) toDictionary;

@end
