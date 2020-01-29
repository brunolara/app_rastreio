import 'dart:isolate';

import 'package:android_alarm_manager/android_alarm_manager.dart';
import 'package:flutter/material.dart';
import 'layouts/HomeWithGPS.dart';
import 'layouts/Home.dart';
import 'layouts/Rastreio.dart';

void main() async{
  runApp(MaterialApp(
    home: HomeWithGPS(),
    routes: <String, WidgetBuilder> {
      '/Home': (context) => Home(),
      '/Rastreio': (context) => Rastreio()
    }
  ));

}

