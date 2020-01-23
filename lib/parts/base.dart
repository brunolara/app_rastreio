import 'package:flutter/material.dart';

Widget getDrawer(context){
  return Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: <Widget>[
            DrawerHeader(
              decoration: BoxDecoration(
                color: Colors.blueAccent
              ),
              child: Text(
                "Ferramentas de carga",
                style: TextStyle(fontSize: 24, color: Colors.white),
                ),
            ),
            ListTile(
              leading: Icon(Icons.home),
              title: Text('Pagina Inicial'),
              onTap: (){
                Navigator.pushReplacementNamed(context, '/');
              
              },
            ),
          ],
        ),
      );
}

Widget getAppBar({action: null}){
  return AppBar(
        backgroundColor: Colors.blueAccent,
        title: Image(image: AssetImage("assets/img/logoTranspNet.png"), height: 50,),
        centerTitle: true,
        actions: action,
      );
}
