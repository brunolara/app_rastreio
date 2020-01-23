import 'package:flutter/material.dart';
import 'layouts/HomeWithGPS.dart';
import 'layouts/Home.dart';
import 'layouts/Rastreio.dart';

void main(){
  runApp(MaterialApp(
    home: Home(),
    routes: <String, WidgetBuilder> {
      '/Home': (context) => Home(),
      '/Rastreio': (context) => Rastreio()
    }
  ));
}


