# RadioLocator

RadioLocator is a mobile app enabling visualisation and catalogization of measurements done by a passive radiolocator. It locates the position of a source of radio signal by using the measured values as a data layer of a Google map and making an intersection out of polylines. 

![Alt text](radiolocator.png?raw=true "RadioLocator")

The app serves as a graphical interface for a passive radiolocator. The radiolocator uses a Pseudo-Doppler antenna array to get an angle of an incoming signal. The measurements are made out of a moving car, so the angle changes as the car changes it's position relative to the transmitter. Once "Angle of Arrival" is calculated, it gets sent to the android app as an xml file via java.net sockets. Geo position at the time of a measurement is also stored and sent over. The app then uses an xml pull parser to parse the incoming measurements and store this data in an SQLite database from which it can either visualise or export the whole set. Visualization is made by putting a Marker onto the map and connecting it to a second marker, which position is set at a specified distance in direction of the measured angle. Once at least 3 "Datapoints" are drawn onto the map, the position of the transmitter can be triagulated.

Screenshots:

The position of the located transmitter.
![Alt text](transmitter.png?raw=true "Green circle added for clarity")

Locator activity
![Alt text](locatoractivity.png?raw=true "The visualisation interface")

Polylines are used to get the intersection of angles.
![Alt text](polylines.png?raw=true "Polylines")

Database activity. Also serves to provide direct import and export capabilities.
![Alt text](databaseactivity.png?raw=true "Database view activity")

A detailed view of a measurement consisting out of a set of Datapoints.
![Alt text](detailactivity.png?raw=true "Detail of a Measurement")

The app was made as a bachelor project. Quick presentation of the app can be viewed [here](radiolocator.pdf).

Download the [apk](radiolocator.apk) of the app here. To get data either import [this](measurement.xml) previously meade measurement or try real time radiolocation by pressing the correct button in the locator activity.

