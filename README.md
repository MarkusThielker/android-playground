# Android Playground

## \[WIP] Lobby - Network service discovery (NSD)

A simple lobby system implemented using Android's network service discovery. The client can search
for registered services in it's current network or register it's own service.

Clients searching for services can join them. All clients inside a lobby can see each other in a
client list. The host of the service handles the network communication as a PubSub keeping all
clients in sync.
