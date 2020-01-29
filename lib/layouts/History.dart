// import 'dart:convert';

import 'package:Transpnet/layouts/Rastreio.dart';
import 'package:Transpnet/parts/base.dart';
import 'package:flutter/material.dart';
// import 'package:path_provider/path_provider.dart';
import '../parts/helper.dart';

class History extends StatefulWidget {
  @override
  _HistoryState createState() => _HistoryState();
}

class _HistoryState extends State<History> {
  List history = [];

   @override
  void initState() {
    super.initState();
    readHistory().then((data){
      setState(() {
        history = data;
      });
    });
  }


  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: getAppBar(),
      body: ListView.builder(
                  padding: EdgeInsets.only(top: 10.0),
                  itemCount: history.length,
                  itemBuilder: buildItem
            ),
    );
  }

  Widget buildItem(BuildContext context, int index){
    return Dismissible(
      key: Key(DateTime.now().millisecondsSinceEpoch.toString()),
      background: Container(
        color: Colors.red,
        child: Align(
          alignment: Alignment(-0.9, 0.0),
          child: Icon(Icons.delete, color: Colors.white,),
        ),
      ),
      direction: DismissDirection.startToEnd,
      child: ListTile(
        leading: Icon(Icons.history),
        title: Text(history[index]['crt']),
        subtitle: Text(history[index]['code']),
        onTap: (){
          Navigator.pushReplacement(context,
            MaterialPageRoute(builder: (context) => Rastreio(str: history[index]['code'],))
          );
        },
      ),
      onDismissed: (direction){
        removeFromHistory(history[index]['code']);
      },
    );
  }
}