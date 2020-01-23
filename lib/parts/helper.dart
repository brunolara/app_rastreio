import 'dart:convert';

import 'package:shared_preferences/shared_preferences.dart';

String parseDate({str, formatDateTime = false}){
  var aux = str.split('T');
  var data = aux[0].split('-');
  String data_parsed = '';
  if(formatDateTime) return aux[0].toString();
  else data_parsed = data[2] +'/'+ data[1] +'/'+ abreviateYear(data[0]);
  return data_parsed +' '+aux[1];
}

  
String abreviateYear(String year){
  int aux = int.parse(year);

  if(aux > 2000) return (aux % 1000).toString();
  return year;
}

readHistory() async {
  final prefs = await SharedPreferences.getInstance();
  final key = 'history_key';
  final value = prefs.getString(key) != null ? json.decode(prefs.getString(key)) : [];
  return value;
}

saveHistory(value) async {
  final prefs = await SharedPreferences.getInstance();
  final key = 'history_key';
  String aux = json.encode(value);
  prefs.setString(key, aux);
  return value;
}

removeFromHistory(code) async{
  final prefs = await SharedPreferences.getInstance();
  final key = 'history_key';
  var value = prefs.getString(key) != null ? json.decode(prefs.getString(key)) : [];
  print(value[0]['code']);
  value.removeWhere((el)=> el['code'].toString() == code.toString());
  print(value.length);
  saveHistory(value);
}

existsOnHistory(code,value){
  var t = value.length;
  print(value[0]);
  for(int i=0; i < t; i++){
    if(value[i]['code'] == code) return true;
  }
  return false;
}