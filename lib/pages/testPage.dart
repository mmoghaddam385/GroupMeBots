import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';

class RandomWordsState extends State<RandomWords> {
  final _suggestions = generateWordPairs().take(30).toList();
  final _biggerFont = const TextStyle(fontSize: 18.0);

  @override
  Widget build(BuildContext context) {
    return _buildSuggestions();
  }

  Widget _buildSuggestions() {
    return ListView.builder(
      
      itemBuilder: (context, i) {

          final index = i; // Get the actual current index
          if (index < _suggestions.length) {
            return _buildRow(_suggestions[index]);
          }
        }
    );
    
  }

  Widget _buildRow(WordPair pair) {
    return InkWell(
      
      onTap: ()  { },
      child: Container(
        padding: const EdgeInsets.all(12.0),
        child: Column( 
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(pair.asPascalCase, style: _biggerFont, textAlign: TextAlign.right),
            Divider()
            ]
        )
      )
    );
  }
}

class RandomWords extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => new RandomWordsState();
}
