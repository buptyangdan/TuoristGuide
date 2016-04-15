# TuoristGuide

## How to run the application?

To run and test it locally, run following command after you clone the repo:

```bash
# install dependencies
# start server
```

## Package dependencies

* Support Library 

```build.gradle
  compile 'com.squareup.picasso:picasso:2.5.2'
  compile 'com.android.support:appcompat-v7:23.2.0'
  compile 'com.facebook.android:facebook-android-sdk:4.+'
  compile 'com.android.support:design:23.2.0'
  compile 'de.hdodenhof:circleimageview:1.3.0'
  compile 'com.android.support:multidex:1.0.0'
  compile 'com.yelp.clientlib:yelp-android:1.0.0'
  compile 'com.android.volley:volley:1.0.0'
  compile 'com.android.support:multidex:1.0.0'
  compile 'com.google.android.gms:play-services:8.4.0'
```
* Support jars


[https://github.com/47deg/android-wunderground-client/downloads](https://github.com/47deg/android-wunderground-client/downloads)

[https://github.com/47deg/appsly-android-rest/downloads](https://github.com/47deg/appsly-android-rest/downloads)


* Yelp API

[https://www.yelp.com/developers/](https://www.yelp.com/developers/)

```java

YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
YelpAPI yelpAPI = apiFactory.createAPI();

```

* Weather Underground Weather API

[https://www.wunderground.com/weather/api/d/docs?d=index/](https://www.wunderground.com/weather/api/d/docs?d=index/)

```java

 GeoPoint center = mapView.getMapCenter();
    WundergroundApiProvider.getClient().query(new ContextAwareAPIDelegate<WundergroundResponse>(MainActivity.this, WundergroundResponse.class, RequestCache.LoadPolicy.NEVER) {
        @Override
        public void onResults(WundergroundResponse wundergroundResponse) {
            Toast.makeText(MyActivity.this, wundergroundResponse.getCurrentObservation().getWeather(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onError(Throwable e) {
            Toast.makeText(MyActivity.this, "fail", Toast.LENGTH_LONG).show();
        }
    }, "Your API Key", Query.latLng(center.getLatitudeE6() / 1E6, center.getLongitudeE6() / 1E6), Feature.conditions);

```


## Run Server
* Install Activator
```bash
# brew install typesafe-activator
# cd /xxx/ToursitGuide-server
# activator run
```



