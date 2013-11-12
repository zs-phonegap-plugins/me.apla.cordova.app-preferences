/**
 * This module contains basic cross-platform request. platform's request override common one
 * @constructor
 */
var platform = {};
try {
	platform = require ('./platform');
} catch (e) {
	
}

function AppPreferences() {

}

AppPreferences.prototype.prepareKey = platform.prepareKey || function (mode, dict, key, value) {
	var argList = [].slice.apply(arguments);
	argList.shift();
	if ((mode == 'get') || (mode == 'set')) {
		argList.unshift (void 0);
	}
	var args = {
		key: argList[1]
	};
	if (argList[0] !== void 0)
		args.dict = argList[2];
		
	if (mode == 'set')
		args.value = argList[3];
		
	return args;
}

/**
 * Get a preference value
 *
 * @param {Function} successCallback The function to call when the value is available
 * @param {Function} errorCallback The function to call when value is unavailable
 * @param {String} dict Dictionary for key (Required)
 * @param {String} key Key
 */
AppPreferences.prototype.fetch = platform.fetch || function (
	successCallback, errorCallback, dict, key
	) {

		var args = this.prepareKey ('get', dict, key);

		if (!args.key) {
			errorCallback ();
			return;
		}

		console.log('PREFERENCE GET');

		_successCallback = function (_value) {
			var value;
			try {
				value = JSON.parse (_value);
			} catch (e) {
				value = _value;
			}
			successCallback (value);
		}

		var execStatus = cordova.exec (
			_successCallback, errorCallback,
			"AppPreferences", "fetch", [args]
		);
};

/**
 * Set a preference value
 *
 * @param {Function} successCallback The function to call when the value is set successfully
 * @param {Function} errorCallback The function to call when value is not set
 * @param {String} dict Dictionary for key (Required)
 * @param {String} key Key
 * @param {String} value Value
 */
AppPreferences.prototype.store = platform.store || function (
	successCallback, errorCallback, dict, key, value
	) {

		var args = this.prepareKey ('set', dict, key, value);

		if (!args.key || !args.value) {
			errorCallback ();
			return;
		}
		//commented to remove double-quotes which was getting added with JSON.stringify to value
		//args.value = JSON.stringify (args.value);

		var execStatus = cordova.exec (
			successCallback, errorCallback,
			"AppPreferences", "store", [args]
		);
};


module.exports = new AppPreferences();
