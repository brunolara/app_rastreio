import 'package:flutter/material.dart';
import 'Rastreio.dart';
import '../parts/base.dart';

class Options{
  const Options({this.titulo, this.icone, this.dest, this.iconImage, this.redirType = 1});
  final String titulo;
  final String dest;
  final Image iconImage;
  final IconData icone;
  final int redirType; //push ou replace
}


class Home extends StatefulWidget {
  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
    final List<Options> lista =  <Options>[
       Options(titulo: "Rastreio de carga", icone: Icons.local_shipping, dest: '/Rastreio', redirType: 1),
      
    ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: getDrawer(context),
      appBar: getAppBar(),
      body: Container(
        padding: EdgeInsets.only(top:30, left: 15, right: 15),
        child: GridView.count(
          crossAxisCount: 2,
          children: List.generate(lista.length, (index){
            return Center(
              child: OpcaoCard(option: lista[index]),
            );
          }),
        ),
      )
    );
  }
}

class OpcaoCard extends StatelessWidget {
  
  const OpcaoCard({Key key, this.option}): super(key: key);
  final Options option;
  
  @override
  Widget build(BuildContext context) {
    final TextStyle textStyle = Theme.of(context).textTheme.display1;
    return InkWell(
      child: Card(
            color: Colors.white,
            child: Center(
              child: Column(
                  mainAxisSize: MainAxisSize.min,
                  crossAxisAlignment: CrossAxisAlignment.center,
                    children: <Widget>[
                  Icon(option.icone, size:60.0, color: Colors.blueAccent),
                  Text(option.titulo, style: TextStyle(color: Colors.grey, fontSize: 30,), textAlign: TextAlign.center,),
            ]
          )
        )
      ),
      onTap: (){
        option.redirType == 1 ? Navigator.pushNamed(context, option.dest) : Navigator.pushReplacementNamed(context, option.dest);
      },
    );
  }
}