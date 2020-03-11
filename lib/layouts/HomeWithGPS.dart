import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:geolocator/geolocator.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:permission_handler/permission_handler.dart';
import 'dart:io' show Platform;




class HomeWithGPS extends StatefulWidget {
  @override
  _HomeWithGPSState createState() => _HomeWithGPSState();
}

class _HomeWithGPSState extends State<HomeWithGPS> {
  bool serviceStatus = false;
  @override
  void initState() {
    super.initState();
   
  }

  void startServiceInPlatform() async{
    if(Platform.isAndroid){
      var methodChannel = MethodChannel("com.lara.bru.rastreio.messages");
      String data = await methodChannel.invokeMethod("startService");
      debugPrint(data);
    }
  }
  void stopServiceInPlatform() async{
    if(Platform.isAndroid){
      var methodChannel = MethodChannel("com.lara.bru.rastreio.messages");
      String data = await methodChannel.invokeMethod("stopService");
      debugPrint(data);
    }
  }


  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.white,
      child: Center(
        child:RaisedButton(
            child: Text(serviceStatus ? "Para":"Rodar"),
            onPressed: (){
            
              setState(() {
                if(serviceStatus == false) {
                  setState(() {
                    serviceStatus = true;
                    startServiceInPlatform();    
                  });
                
                }
                else {
                  setState(() {
                    serviceStatus = false;
                    stopServiceInPlatform();    
                  });
                
                }
              });
            },
          ),
        )
    );
  }
}