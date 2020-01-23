import 'dart:convert';

import 'package:Transpnet/layouts/History.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import 'package:http/http.dart' as http;
import 'EmbarqueDetail.dart';
import '../parts/base.dart';
import '../parts/helper.dart';


class Rastreio extends StatefulWidget {
  final String str;

  const Rastreio({Key key, this.str});

  @override
  _RastreioState createState() => _RastreioState();
}

class _RastreioState extends State<Rastreio> {
  String search = '';
  final TextEditingController inputController = TextEditingController();

  @override
  void initState() {
    super.initState();
    if(widget.str != null){
      search = widget.str;
      inputController.text = widget.str;
    }
  }
  Future<Map> _getApi() async{
    http.Response response;
    if(search != null && search != ''){
      response = await http.get("http://apps.transp.net/rastreiocarga/api/embarque/relatorio?chaveRastreio=$search");
      if(context != null) FocusScope.of(context).requestFocus(FocusNode());
      
      var data = json.decode(response.body);
      // print(data['errors']);
      if(data['errors'] == null){
        var history = await readHistory();
       
        if(history.length == 0) history = [{'code': search, 'crt': data['data']['crt']}];
        else if(!existsOnHistory(search,history)) {
        // else if(true) {
          history.add({'code': search, 'crt': data['data']['crt']});
        }
        // history = [];
        print(history);
        
        saveHistory(history);
      } 
    }

    return json.decode(response.body);
  }
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: getAppBar(
          action: <Widget>[
            IconButton(
              icon: Icon(Icons.history,  color: Colors.white,),
              onPressed: (){
                Navigator.pushReplacement(context,
                   MaterialPageRoute(builder: (context) => History())
                );
              },
            )
          ]
        ),
      // drawer: getDrawer(context),
      body: Column(
        children: <Widget>[
          Padding(
            padding: EdgeInsets.all(10),
            child: TextField(
                controller: inputController,
                decoration: InputDecoration(
                  labelText: "Insira o código de rastreio",
                  suffixIcon: IconButton(
                    icon: Icon(Icons.search),
                    onPressed: (){
                       setState(() {
                        search = inputController.text;
                    
                      });
                    },
                  )
                ),
                onSubmitted: (text){
                  setState(() {
           
                    search = text;
                  
                  });
                },
              ),
          ),
          Expanded(
            child: FutureBuilder(
              future: _getApi(),
              builder: (context,snapshot){
                switch(snapshot.connectionState){
                  case ConnectionState.waiting:
                  case ConnectionState.none:
                    return Container(
                      width: 200,
                      height: 200,
                      alignment: Alignment.center,
                      child: CircularProgressIndicator(
                        valueColor: AlwaysStoppedAnimation<Color>(Colors.blueAccent),
                        strokeWidth: 5,
                      ),
                    );
                  default:
                    if(snapshot.hasError) {
                      return Container();
                    }
                    if(snapshot.data['errors'] != null && snapshot.data['errors']!='') {
                     
                      return Padding(
                        padding: EdgeInsets.only(top: 15),
                        child: Container(
                         padding: EdgeInsets.only(top:15),
                          child: Text(
                            "Chave não encontrada",
                            style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
                            ),
                        ),
                      );
                    }
                    return listBuild(context, snapshot);
                }
              },
            ),
          )
        ],
      )
    );
  }

  int sortListByDate(a,b, {dir='asc'}){
    var aux1 = DateTime.parse(parseDate(str:a['dataMensagem'], formatDateTime:true));
    var aux2 = DateTime.parse(parseDate(str:b['dataMensagem'], formatDateTime:true));
    if(dir =='asc'){
      if(aux1.isAfter(aux2)) return -1;
      if(aux1.isAtSameMomentAs(aux2)) return 0;
      if(aux1.isBefore(aux2)) return 1;
    }
    else{
      if(aux1.isBefore(aux2)) return -1;
      if(aux1.isAtSameMomentAs(aux2)) return 0;
      if(aux1.isAfter(aux2)) return 1;
    }
  }

  Widget listBuild(context, snapshot){
    final List<dynamic> logs = snapshot.data['data'] != null ? snapshot.data['data']['logEmbarques'] : [];

    logs.sort((a,b) {
       return sortListByDate(a, b);
    });

    return RefreshIndicator(
        onRefresh: (){
          setState(() {
            search = inputController.text;
          });
          return null;
        },
        child: SingleChildScrollView(
          padding: EdgeInsets.only(left: 10, right: 10),
          child: Column(
            children: <Widget>[
              Container(
                // alignment: Alignment.bottomLeft,
                child: Column(
                  children: <Widget>[
                    ListView(
                      scrollDirection: Axis.vertical,
                      shrinkWrap: true,
                      children: <Widget>[
                        ListTile(
                          title: Text('CRT'),
                          subtitle: Text(snapshot.data['data']['crt']),
                        ),
                        ListTile(
                          title: Text('Cliente'),
                          subtitle: Text(snapshot.data['data']['cliente']),
                        ),
                      ],
                    )
                  ],
                ),
              ),
              DataTable(
                sortAscending: true,
                sortColumnIndex: 0,
                columns: [
                  DataColumn(
                    label: Text('Data'),
                    numeric: false,
                  
                  ),
                  DataColumn(
                    label: Text('Informação'),
                    numeric: false,
                  ),
                  DataColumn(
                    label: Text(''),
                    numeric: false,
                  ),
                
                ],
                rows: logs.map(
                    (el)=>  DataRow(
                      cells: <DataCell>[
                        DataCell(
                          Text(parseDate(str: el['dataMensagem'])),
                          onTap: (){
                            print(el['dataMensagem']);
                          }
                        ),
                        DataCell(
                          Padding(
                            padding: EdgeInsets.only(top: 10, bottom: 10),
                            child: Text(el['mensagem']),
                          )
                        ),
                        DataCell(
                          Padding(
                            padding: EdgeInsets.only(top: 10, bottom: 10),
                            child: Icon(
                              Icons.find_in_page,
                              color: Colors.blueAccent,
                              size: 30,
                              ),
                          ),
                          onTap: () {
                            Navigator.push(context,
                              MaterialPageRoute(builder: (context) => EmbarqueDetail(el))
                            );
                          },
                        ),
                      
                      ],
                      
                    )
                  ).toList()
              
              ),
            ],
          )
      )
    );
  }
}
