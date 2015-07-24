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
package de.netfutura.phonegap.plugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class MapLauncher extends CordovaPlugin {

	private static final String LOG_TAG = "MapLauncher";
	private static final String NO_GOOGLE_NAVIGATOR = "No Activity found to handle Intent";

	@Override
	public boolean execute(String action, JSONArray args,
		CallbackContext callbackContext) throws JSONException {
		boolean result;

		if ("openMap".equals(action)){
			result = this.openMap(args, callbackContext);
		}else {
			Log.e(LOG_TAG, "Invalid action");
			result = false;
		}
		
		if(result == true){
			callbackContext.success();
		}
		return result;
	}

	private boolean openMap(JSONArray args, CallbackContext callbackContext){
		boolean result;
		try {
			String location;
			
			String dType = args.getString(0);
			if(dType.equals("pos")){
				JSONArray pos = args.getJSONArray(1);

				String dLat = pos.getString(0);
	        	String dLon = pos.getString(1);
				if (dLat == null || dLat.length() == 0 || dLon == null || dLon.length() == 0) {
					Log.e(LOG_TAG, "Expected two non-empty string arguments for location lat/lon." );
					return false;
	            }
				location = dLat + "," + dLon;
			}else{
				String dName = args.getString(1);
				if (dName == null || dName.length() == 0) {
					Log.e(LOG_TAG, "Expected non-empty string argument for location name." );
		        	return false;
		        }
				location = dName;
			}
			
			
			result = this.doOpen(location, callbackContext);

		}catch( JSONException e ) {
			Log.e(LOG_TAG, "Exception occurred: ".concat(e.getMessage()));
        	result = false;
		}
        return result;
    }
	
	private boolean doOpen(String location, CallbackContext callbackContext){
		boolean result;
		try {
			String logMsg = "Navigating to "+location;
			String url;

			url = String.format(Locale.ENGLISH, "geo:%f",location);

			// if(start != null){
			// 	logMsg += " from " + start;
			// 	url = "http://maps.google.com/maps?daddr=" + destination + "&saddr=" + start;
			// }else{
			// 	logMsg += " from current location";
			// 	url = "google.navigation:q=" + destination;
			// }
			Log.d(LOG_TAG, logMsg);
			
	        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        // if(start != null){
	        // 	intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
	        // }
	        this.cordova.getActivity().startActivity(intent);
	        result = true;
		}catch( Exception e ) {
			String msg = e.getMessage();
			if(msg.contains(NO_GOOGLE_NAVIGATOR)){
				msg = "Google Navigator app is not installed on this device";
			}
			Log.e(LOG_TAG, "Exception occurred: ".concat(msg));
			callbackContext.error(msg);
        	result = false;
		}
        return result;
	}

}