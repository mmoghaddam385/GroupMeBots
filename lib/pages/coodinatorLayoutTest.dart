
// import 'package:flutter/material.dart';
// import 'package:portfolio_tracker/pages/testPage.dart';

// class CoordinatorLayoutState extends State<CoordinatorLayout> {
  
//   @override
//   Widget build(BuildContext context) {
//     return new CustomScrollView(
//   slivers: <Widget>[
//     const SliverAppBar(
//       pinned: true,
//       expandedHeight: 250.0,
//       flexibleSpace: const FlexibleSpaceBar(
//         title: const Text('Demo'),
//       ),
//     ),
//     new RandomWords(),
//     new SliverFixedExtentList(
//       itemExtent: 50.0,
//       delegate: new SliverChildBuilderDelegate(
//         (BuildContext context, int index) {
//           return new Container(
//             alignment: Alignment.center,
//             color: Colors.lightBlue[100 * (index % 9)],
//             child: new Text('list item $index'),
//           );
//         },
//       ),
//     ),
//   ],
// );
//   }

// }

// class CoordinatorLayout extends StatefulWidget {
//   @override
//   State<StatefulWidget> createState() => new CoordinatorLayoutState();

// }