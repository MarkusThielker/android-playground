# Android Playground

## Build the project

Since the app is programmed using the Java JDK 16, this or a higher version must be installed and set as Gradle JDK. <br>
To build the app, open the project in the latest Android Studio version and run the default run configuration.


## \[WIP] Lobby - Network service discovery (NSD)

A simple lobby system implemented using Android's network service discovery. The client can search
for registered services in it's current network or register it's own service.

Clients searching for services can join them. All clients inside a lobby can see each other in a
client list. The host of the service handles the network communication as a PubSub keeping all
clients in sync.
