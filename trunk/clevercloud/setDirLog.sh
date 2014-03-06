#!/bin/bash
dir=LOGS/
 
if [ -d $dir ]; 
  then
    echo "L albero LOGS esiste, lo azzero!"
    rm -r LOGS/
  else
    echo "L albero LOGS non esiste, lo creo!"
fi


mkdir LOGS

cd LOGS
mkdir ClusterManager Common HostManager

cd ClusterManager
mkdir DatabaseManager  Dispatcher  Info  StorageManager  VirtualizationManager

cd ..
cd Common
mkdir Prova Logging

cd ..
cd HostManager
mkdir DispatcherAgentHm  HyperVisor    Info     NetworkManager HostCoordinator    ImageManager  Monitor  ServiceManager
