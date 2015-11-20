# react-native-android-geolocation-polyfill
Polyfill for react-native geolocation (while the official one is not provided).

Currently only the `getCurrentPosition` method is implemented. Anyone willing to contribute the rest of the methods is wellcome.


Steps to add this to your react-native android project:
* `npm install https://github.com/andreracz/react-native-android-geolocation-polyfill.git`
* add to your `settings.gradle`:
```
include ':com.github.andreracz.react.geolocation'
project(':com.github.andreracz.react.geolocation').projectDir = new File(settingsDir, '../node_modules/react-native-android-geolocation-polyfill/android')
```
* add to your `app/build.gradle`:
```
dependencies {
    ...
    compile project(':com.github.andreracz.react.geolocation')
}
```
* add to your `MainActivity.java`:
	* `import com.facebook.react.CompositeReactPackage;`
	* `import com.github.andreracz.react.geolocation.ReactGeolocationPackage;`
	* in `onCreate`:
	```
	mReactInstanceManager = ReactInstanceManager.builder()
		...
		.addPackage(new ReactGeolocationPackage(this))
		...
	```


* in your `index.android.js`:
    * `require('react-native-android-geolocation-polyfill')`
    * This will change the `navigator.geolocation` object with the polyfill