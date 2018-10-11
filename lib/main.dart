import 'package:flutter/material.dart';
import 'package:portfolio_tracker/pages/testPage.dart';
import 'package:portfolio_tracker/pages/coodinatorLayoutTest.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(title: 'Welcome to Flutter', home: Scaffold(
        appBar: AppBar(
          title: Text('Welcome to Flutter'),
        ),
        body: Center(
          child: RandomWords(), // ... this highlighted text
        ),
      ),
    );
  }
}

