# Android GPS Compass

This is just a proof-of-concept, showing how to subscribe to ´Google Location´ API and display the current direction of a car, bicycle, etc.

Notice I'm not calling the google service using credentials. I´m mocking the service with the android emulator.

### Considerations

It's 2020, and [there's a new alternative](https://developer.android.com/training/permissions/requesting?hl=es-419) to request and handle permissions. It's in alpha state, but that's what I used instead of writing a _switch_ statement inside ´onRequestPermissionsResults´.

Also, in a real app you would want to create a LocationService and make a more clear separation of concerns. The Activity would handle client permissions, and draw on screen, while your LocationService would deal with google.

## Demo

![Demo](https://github.com/libasoles/android-gps-compass/blob/master/demo.gif)
