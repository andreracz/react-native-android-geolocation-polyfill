var { NativeModules } = require('react-native');
var androidGeolocation = NativeModules.GeolocationModule;

// POLYFILL!!!
navigator.geolocation = {
    getCurrentPosition:function(cb, errorCb, params) {
        
        console.log('androidGeolocation');
        console.log(androidGeolocation);
        // INVERTING the PARAMETERS, because that's how react works...
        androidGeolocation.getCurrentPosition(params, errorCb, function(latitude, longitude, accuracy) {
            // assembling the response in the right format
            cb({
                coords: {
                    latitude: latitude,
                    longitude: longitude,
                    accuracy: accuracy
                }
            });
        });
    },
    
    watchPosition: function() {
        throw new Error('Not implemented yet');
    },

    clearWatch: function() {
        throw new Error('Not implemented yet');
    }
}

module.exports = navigator.geolocation;
