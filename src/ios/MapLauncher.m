/*
 * MapLauncher Plugin for Phonegap
 *
 * Copyright (c) 2015 Ralph Dittrich (http://github.com/kizetsu)
 * Copyright (c) 2015 netfutura Deutschland GmbH (http://www.netfutura.eu)
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *  
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *  
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 #import "MapLauncher.h"

 /**
  * Implementation of the Interface
  */
  @implementation MapLauncher

  - (void) openMap:(CDVInvokeUrlCommand*)command;
  {
  	CDVPluginResult* pluginResult = nil;

  	NSString *location = [command.arguments objectAtIndex:0];

  	if(![location isKindOfClass:[NSString]]) {
  		CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR massageAsString:@"Invalid arguments"];
  		[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
  		return;
  	}

  	UIApplication* app = [UIApplication sharedApplication];

  	NSString *locationRequest = nil;
  	NSString *protocol = nil;
  	NSString *callbackParams = nil;

  	NSURL *testURL = [NSURL URLWithString:@"comgooglemaps://"];
    BOOL googleMapsSupported = [app canOpenURL:testURL];

    if(![urlScheme isEqual:[NSNull null]]){
        protocol = @"comgooglemaps-x-callback://?";
        callbackParams = [NSString stringWithFormat:@"&x-success=%@://?resume=true&x-source=%@", urlScheme, backButtonText];

    }else{
        protocol = @"comgooglemaps://?";
    }

    locationRequest = [NSString stringWithFormat:@"%@center=%@&zoom=15&views=streetview"];

    NSString *safeString = [locationRequest stringByAddingPercentEscapeUsingEncoding:NSUTF8StringEncoding];
    NSURL *locationURL = [NSURL URLWithString:safeString];
    [app openURL:locationURL];

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
  }

  @end