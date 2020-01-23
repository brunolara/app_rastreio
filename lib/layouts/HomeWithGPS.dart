import 'dart:async';

import 'package:flutter/material.dart';

import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:permission_handler/permission_handler.dart';



class HomeWithGPS extends StatefulWidget {
  @override
  _HomeWithGPSState createState() => _HomeWithGPSState();
}

class _HomeWithGPSState extends State<HomeWithGPS> {

  @override
  void initState() {
    super.initState();
    getPos();
  }
  
  void getPos() async{

    PermissionStatus permission = await PermissionHandler()
        .checkPermissionStatus(PermissionGroup.location);
    if (permission == PermissionStatus.denied) {
      await PermissionHandler()
          .requestPermissions([PermissionGroup.locationAlways]);
    }

    GeolocationStatus geolocationStatus  = await Geolocator().checkGeolocationPermissionStatus();

    switch (geolocationStatus) {
      case GeolocationStatus.denied:
        print('denied');
        break;
      case GeolocationStatus.disabled:
      case GeolocationStatus.restricted:
        print('restricted');
        break;
      case GeolocationStatus.unknown:
        print('unknown');
        break;
      case GeolocationStatus.granted:

        var geolocator = Geolocator();
        var locationOptions = LocationOptions(accuracy: LocationAccuracy.high, distanceFilter: 10);

        StreamSubscription<Position> positionStream = geolocator.getPositionStream(locationOptions).listen(
            (Position position) {
                print(position == null ? 'Unknown' : position.latitude.toString() + ', ' + position.longitude.toString());
            });
        await Geolocator()
            .getCurrentPosition(desiredAccuracy: LocationAccuracy.high)
            .then((Position _position) {
          if (_position != null) {
            setState((){
           
              print(LatLng(_position.latitude, _position.longitude,));
            });
          }
        });
        break;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: Center(
        child:RaisedButton(
            child: Text("Rodar")
          ),
        )
    );
  }
}