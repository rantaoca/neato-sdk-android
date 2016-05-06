[![Build Status](https://magnum.travis-ci.com/NeatoRobotics/neato-sdk-android.svg?token=Y4dRpXzLka6jmXX14Lfr&branch=master)](https://magnum.travis-ci.com/NeatoRobotics/neato-sdk-android)

#Neato SDK - Android

This is the official Android SDK for the Neato API services.
Importing the Neato SDK in your projects you can easily implement applications that communicate with Neato robots. We did the hard work for you so you can focus on implementing your apps!

To boost your development you can also check the *sample application*.

## Setup  
If you are using Gradle add this dependency to your build.gradle file:

``` groovy
compile 'com.neatorobotics.sdk.android:sdk:1.0.0'
```

It is required this permission to be added in your AndroidManifest.xml file:
``` xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage
The Neato SDK has 3 main roles:
1. Handling OAuth authentications.
2. Simplifying users info interactions.
3. Managing communication with Robots.

These tasks are handled by different classes; You’ll mainly work with 3 of them: `NeatoAuthentication`, `NeatoRobot` and `NeatoUser`

### Authentication
The Neato SDK leverages on OAuth 2 to perform user authentication. The `NeatoAuthentication` class gives you all the needed means to easily perform a login through your apps. Let’s go through the steps needed to setup an app and perform  the authentication.

#### 1. Creating a Schema URL
During the registration of your app on Neato Servers you have defined a `Redirect URI`. This is the URL where we redirect a user that completes a login with your app Client ID. You application must be able to handle this Redirect URI thanks to a dedicated `Schema URL`. This is tipically done declaring an Activity in your AndroidManifest.xml that can handle requests coming from this URI. For example, your login activity can be declared like this:

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
In your sign in activity you obtain the instance of the `NeatoAuthentication` class like this:

```java
NeatoAuthentication neatoAuth = NeatoAuthentication.getInstance(this);
```

#### 3. Showing the Login page
You can now start the authentication flow invoking the `openLoginInBrowser` method:

```java
String REDIRECT_URI = "marco-app://neato";
String CLIENT_ID = "your_secret_client_id";
NeatoOAuth2Scope[] scopes = new NeatoOAuth2Scope[]{NeatoOAuth2Scope.CONTROL_ROBOTS};

//we start the auth flow here
//later we'll receive the result in the onNewIntent activity method
neatoAuth.openLoginInBrowser(this,CLIENT_ID,REDIRECT_URI,scopes);
```
The user will be presented with a login page (on Chrome or another external browser) and when it completes the login it will be redirect to your App thanks to the `URL Schema` previously defined.

#### 4. Handling the Redirect URI
When the user finishes the login he is redirected to the previously configured login activity and the method onNewIntent is invoked. Here you can grab the OAuth access token is the login succeded otherwise you can show an error message.

```java
@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    Uri uri = intent.getData();
    if (uri != null) {
        NeatoAuthenticationResponse response = 
        neatoAuth.getOAuth2AuthResponseFromUri(uri);

        switch (response.getType()) {
            case TOKEN:
	            //the token is automatically saved for you by the
	            //NeatoAuthentication class, no need to save it!
                //Yay! we can now play with our robots!!
                break;
            case ERROR:
                //show auth error message
                break;
            default:
                //nothing to do here
        }
    }
}
```

#### 5. How to check if the user is already logged in
Sometimes you need to check if the user is already logged in, for example to skip directly to your robots page instead of pass throught the login page. To check simply do this:

```java
//here we're checking the access token
NeatoAuthenticationneatoAuth = NeatoAuthentication.getInstance(this);
if(neatoAuth.isAuthenticated()) {
    openRobotsActivity();
}else {
    //need to sign in first
    openLoginActivity();
}
```


### Working with Users


### Communicating with Robots
Now that you have the robots for an authenticated user it’s time to communicate with them.
In the previous call you've seen how easy is to retrieve `NeatoRobot` instances for your current user. Those instances are ready to receive messages from your app (if the robots are online obviously).

#### The robot status


#### Sending commands to a Robot
An online robot is ready to receives your commands like `start cleaning`:










