import 'package:flutter/material.dart';
import '../parts/base.dart';
import '../parts/helper.dart';

class EmbarqueDetail extends StatelessWidget {
  final Map data;
  EmbarqueDetail(this.data);

  @override
  Widget build(BuildContext context) {
    return Container(
      child: Scaffold(
        appBar: getAppBar(),
        body: Padding(
          padding: EdgeInsets.only(top: 20, left: 15, right: 15, bottom: 25),
          child: Column(
            children: <Widget>[
              Padding(
                padding: EdgeInsets.only(bottom: 25),
                child: Row(
                  children: <Widget>[
                    Expanded(
                      child: Text(
                        data['mensagem'],
                        textAlign: TextAlign.center,
                        style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
                      ),
                    ),
                  ],
                ),
              ),
              Expanded(
                child:  ListView(
                children: <Widget>[
                    ListTile(
                      title: Text('Data'),
                      subtitle: Text(parseDate(str: data['dataMensagem']))
                    ),
                    ListTile(
                      title: Text('Placa'),
                      subtitle: Text(data['placaConjunto'] != null ? data['placaConjunto']: 'Não informado')
                    ),
                     ListTile(
                      title: Text('Nome Motorista'),
                      subtitle: Text(data['nomeMotorista'] != null ? data['nomeMotorista']: 'Não informado')
                    ),
                     ListTile(
                      title: Text('Telefone'),
                      subtitle: Text(data['noCelular'] != null ? data['noCelular']: 'Não informado')
                    ),
                     ListTile(
                      title: Text('Valor das diárias'),
                      subtitle: Text(data['valorDiarias'] != null ? data['valorDiarias']: 'Não informado')
                    ),
                     ListTile(
                      title: Text('Total de diárias'),
                      subtitle: Text(data['qtdDiarias'] != null ? data['qtdDiarias']: 'Não informado')
                    ),
                     ListTile(
                      title: Text('Observação'),
                      subtitle: Text(data['observacao'] != null ? data['observacao']: 'Não informado')
                    ),
                  ],
                ),
              )
             
            ],
          ),
        ),
      ),
    );
  }
}