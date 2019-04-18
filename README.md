
[![Build Status](https://travis-ci.org/NeatoRobotics/neato-sdk-android.svg?branch=master)](https://travis-ci.org/NeatoRobotics/neato-sdk-android)[
![Release](https://jitpack.io/v/NeatoRobotics/neato-sdk-android.svg)
](https://jitpack.io/#NeatoRobotics/neato-sdk-android)

# Neato SDK - Android

This is the [Neato Developer Network's](http://developers.neatorobotics.com) official Android SDK (Beta release).

The Neato Android SDK enables Android apps to easily communicate with Neato connected robots and use its various features.
The official Github repository can be found [here](https://github.com/NeatoRobotics/neato-sdk-android).

The SDK has been completely rewritten in **Kotlin** and uses **coroutines** for all async calls. 

> If you're still using Java see below for a proper integration or use an older SDK version.

To boost your development, you can also check the *sample application*.

> This is a beta version. It is subject to change without prior notice.

## Preconditions

 - Create the Neato user account via the Neato portal or from the official Neato App
 - Link the robot to the user account via the official Neato App

## Setup
If you are using Gradle, add this dependency to your app build.gradle file:

``` groovy
implementation 'com.github.neatorobotics:neato-sdk-android:0.11.0'
```

and this repo reference to your project .gradle file:

``` groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

This permission is required to be added in your AndroidManifest.xml file:

``` xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage
The Neato SDK has 3 main roles:
1. Handling OAuth authentications
2. Simplifying users info interactions
3. Managing communication with Robots

These tasks are handled by different classes; You’ll mainly work with 3 of them: `NeatoAuthentication`, `Robot` and `NeatoUser`

>There is no need to initialise the SDK into your Application onCreate() method, because the SDK use an empty *ContentProvider* in order to obtain the application context needed by the SDK.

### Authentication
The Neato SDK leverages on OAuth 2 to perform user authentication. The `NeatoAuthentication` class gives you all the needed means to easily perform a login through your apps. Let’s go through the steps needed to setup an app and perform the authentication.

#### 1. Creating a Schema URL
During the registration of your app on the Neato Developer Portal you have defined a `Redirect URI`. This is the URL where we redirect a user that completes a login with your Neato App Client ID. Your Android app must be able to handle this Redirect URI using a dedicated `Schema URL`. This is typically done declaring an Activity in your AndroidManifest.xml that can handle requests coming from this URI. For example, your login activity can be declared like this:

```xml
<activity
    android:name=".login.LoginActivity"
    android:launchMode="singleInstance">
    <intent-filter>
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />

        <action android:name="android.intent.action.VIEW" />

        <data
            android:host="neato"
            android:scheme="my-neato-app" />
    </intent-filter>
</activity>
```

#### 2. Configuring the NeatoAuthentication class
In your sign in activity you obtain the instance of the `NeatoAuthentication` object like this:

```kotlin
val neatoAuth = NeatoAuthentication
```
or you can user *NeatoAuthentication* directly since it is a singleton *object*.

#### 3. Showing the Login page
You can now start the authentication flow invoking the `openLoginInBrowser` method:

```kotlin
val REDIRECT_URI = "my-neato-app://neato"
val CLIENT_ID = "your_secret_client_id"
val scopes = arrayOf(NeatoOAuth2Scope.CONTROL_ROBOTS,  
                     NeatoOAuth2Scope.PUBLIC_PROFILE,  
                     NeatoOAuth2Scope.MAPS)
// we start the auth flow here
// later we'll receive the result in the onNewIntent activity method
neatoAuth.openLoginInBrowser(this,CLIENT_ID,REDIRECT_URI,scopes)
```
The user will be presented with a login page (on Chrome or another external browser) and when it completes the login it will be redirected to your App thanks to the `URL Schema` previously defined.

#### 4. Handling the Redirect URI
When the user finishes the login he is redirected to the previously configured login activity and the method onNewIntent is invoked. Here you can grab the OAuth access token if the login succeeded, otherwise you can show an error message.

```kotlin

override fun onNewIntent(intent: Intent) {  
    ...
    val uri = intent.data  
    if (uri != null) {  
        val response = NeatoAuthentication.getOAuth2AuthResponseFromUri(uri)  
        when (response.type) {  
            NeatoAuthenticationResponse.Response.TOKEN -> {  
                // the token is automatically saved for you by the
                // NeatoAuthentication class, no need to save it!
                // Yay! we can now play with our robots!!
            }  
            NeatoAuthenticationResponse.Response.ERROR -> {
                // show auth error message
            }
            else -> {
                // nothing to do here
            }  
        }  
    }  
}
```

#### 5. How to check if the user is already logged in
Sometimes you need to check if the user is already logged in, for example to skip directly to your robots page instead of passing through the login page. To check, simply do this:

```kotlin
//here we're checking the access token
if(NeatoAuthentication.isAuthenticated()) {
    openRobotsActivity()
}else {
    //need to sign in first
    openLoginActivity()
}
```

#### 6. Create a custom AccessTokenDatasource
By default the Neato Android SDK use the *DefaultAccessTokenDatasource* to store and load the OAuth access token. This class stores the token into the app shared preferences. Although these preferences are typically known only by the app itself, it is possible that on rooted device someone can read these data. So, if you feel the need to secure the token, you can override the default access token datasource implementing the *AccessTokenDatasource* interface and these methods:

```kotlin
interface AccessTokenDatasource {  
    val isTokenValid: Boolean  
    fun storeToken(token: String, expires: Date)  
    fun loadToken(): String?  
    fun clearToken()  
}
```

Once you have your custom access token datasource you can inject it into the *NeatoAuthentication* object:

``` kotlin
NeatoAuthentication.accessTokenDatasource = MyCustomTokenDatasource()
```

### Working with Users
Once the user is authenticated you can retrieve the `NeatoUser` singleton object:

```kotlin
val user = NeatoUser
```
### A quick note about all async network calls and coroutines

All the SDK methods that do network calls are *suspending function* in order to be used with coroutines. That means you need to invoke these method in this way (I will not include this wrapper code in the subsequent code snippets):
```kotlin
// coroutines  
private var myJob: Job = Job()  
private var uiScope: CoroutineScope = CoroutineScope(Dispatchers.Main + myJob)

uiScope.launch {  
    // use here the suspend functions
}
```

#### Get user robots
To get the user robots list you can do this:

```kotlin
val result = NeatoUser.loadRobots()  
when(result.status) {  
    Resource.Status.SUCCESS -> {  
        //now you have the robot list
        val robots = result.data
        ...
        //request the robots states  
        for (robot in robots) {  
            robot.updateRobotState()  
        }  
   }  
   else -> {  
       // some error occurred
       // use result.code for error handling
   }  
}  
```

#### Get user info
If you want to retrieve the logged user email you can do this:

```kotlin
val result = NeatoUser.getUserInfo()  
when(result.status) {  
    Resource.Status.SUCCESS -> {  
        // name = result.data?.first_name 
    }else -> {  
        // result.code
    }  
}  
```

### Communicating with Robots
Now that you have the robots for an authenticated user, it’s time to communicate with them.
In the previous call you've seen how easy is to retrieve `Robot` instances for your current user. Those instances are ready to receive messages from your app (if the robots are online obviously).

#### The robot status
Before, we saw how to retrieve the robot list from the `User` class. It is best practice to check the robot state before sending commands, otherwise the robot may be in a state that cannot accept the command and return an error code. To update/get the robot state do this:

```kotlin
robot.updateRobotState()
```

#### Sending commands to a Robot
An online robot is ready to receive your commands like `startCleaning`. Some commands require parameters while others don't, see the API doc for details. 

*The SDK helps you avoiding to send unsupported parameters even if you try to do that.*

Pause cleaning doesn't require parameters:

```kotlin
val result = robot.cleaningService?.pauseCleaning(robot)  
when(result?.status) {  
    Resource.Status.SUCCESS ->  // robot paused
    Resource.Status.ERROR ->    // result.code
}  
robot.state = result?.data
```

Start cleaning requires parameters like the cleaning type (clean all house, spot or a floor plan), the cleaning mode (eco or turbo), the navigation mode and, in case of spot cleaning, the spot cleaning parameters (large or small area, 1x or 2x).

```kotlin
val params = CleaningParams(category = CleaningCategory.HOUSE, 
                            mode = CleaningMode.TURBO)  
  
val result = robot.houseCleaningService?.startCleaning(robot, params)
```

#### Working with Robot schedule

To enable or disable all the robot schedule (note that schedule data are not deleted from the robot):

```kotlin
if (robot.state?.isScheduleEnabled == true) {  
    val result = robot.schedulingService?.disableSchedule(robot)  
    when(result?.status) {  
        Resource.Status.SUCCESS -> // disabled
        else -> // error
    } 
} else {  
    val result = robot.schedulingService?.enableSchedule(robot)  
    when(result.status) {  
        Resource.Status.SUCCESS -> // error
        else -> // error
    }  
}
```

To schedule house cleaning *every Wednesday at 15:00 in turbo mode*:

```kotlin
val everyWednesday = ScheduleEvent().apply {  
    mode = CleaningMode.TURBO  
    day = 3//0 is Sunday, 1 Monday and so on  
    startTime = "15:00"  
}  
val robotSchedule = RobotSchedule(true, arrayListOf(everyWednesday))  
val result = robot.schedulingService?.setSchedule(robot, robotSchedule)  
when(result?.status) {  
    Resource.Status.SUCCESS -> // schedule stored
    else -> // error
}
```

#### Getting robot coverage maps

To retrieve the list of robot cleaning coverage maps:

```kotlin
val result = robot.mapService?.getCleaningMaps(robot)  
when(result?.status) {  
    Resource.Status.SUCCESS -> {  
        if (result?.data?.isNotEmpty() == true) {  
            // now you can get a map id and retrieve the map details  
            // to download the map image use the map "url" property 
            // this second call is needed because the map urls expire after a while 

            val maps = result?.data
  
        } else {  
            // no maps available yet... 
        }  
    }  
    null -> // service not supported by this robot model version
    else -> // error
}
```

To retrieve a specific map details:

```kotlin
val result = robot.mapService?.getCleaningMap(robot, mapId)  
when(result.status) {  
    Resource.Status.SUCCESS -> {  
        showMapImage(result.data?.url?:"")  
    }  
    null -> // service not supported by the robot
    else -> // error 
}
```

You can now show the map image, for example using the very convenient Glide library:
```kotlin
private fun showMapImage(url: String) {  
    Glide.with(this).load(url).into(mapImage)  
}
```

#### Checking robot available services

Different robot models and versions have different features. So before sending commands to the robot you should check if that command is available on the robot. Otherwise the robot will respond with an error. You can check the available services on the robot looking into the *RobotState* class:

```kotlin
val services = robot.state.availableServices // hashMap<String, String>
```

In addition there are some utility methods you can use to check if the robot supports the services.

```kotlin
//any version
val supportFindMe = robot.hasService("findMe");
```

```kotlin
//specific service version
val supportManualCleaning = robot.hasService("manualCleaning","basic-1");
```

Moreover you can understand if a robot support a service simply trying to get it, if it returns null the service is not supported:

```kotlin
val service = robot.findMeService // FindMeService? <-- if null it is not supported
```

#### How to pass the Robot object through activities
*Robot* is *Parcelable* so you can easily pass it through different activities.  For example in the first activity, say the robot list, we can click on the robot and pass it to the robot commands activity:

```kotlin
val intent = Intent(context, RobotCommandsActivity::class.java).apply {  
  flags = Intent.FLAG_ACTIVITY_SINGLE_TOP  
  putExtra("ROBOT", robot)  
}  
  
startActivity(intent)
```

And in the onCreate method of the receiving activity:

```kotlin
val extras = intent.extras  
if (extras != null && savedInstanceState == null) {  
    val robot = extras.getParcelable<Robot>("ROBOT")
}
```

In the same way you can save and restore your activities and fragments state when needed.

## Integration with Java code
Kotlin is 100% interoperable with Java, but you cannot use coroutines the same clean way you use them in Kotlin. Please check by yourself how to invoke suspend functions from Java, below you can see a basic example.

```java
BeehiveRepository repository = new BeehiveRepository(Beehive.URL, new BeehiveErrorsProvider());  
  
repository.loadRobots(new Continuation<Resource<List<Robot>>>() {  
    @NotNull  
    @Override  
    public CoroutineContext getContext() {  
        return EmptyCoroutineContext.INSTANCE;  
    }  
  
    @Override  
    public void resumeWith(@NotNull Object o) {  
        // check and use the result  
    }  
});
```
