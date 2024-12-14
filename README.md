
# NetworkPathFinder - Algorithm Design Project

## Overview
This project is a Java-based implementation for finding shortest paths in network graphs, developed as part of an algorithm design course at the University of Isfahan. It includes functionalities for graph creation, edge addition, and finding both shortest paths and minimum spanning trees (MSTs).

## Features
- **Graph Creation**: Create graphs with multiple vertices and edges.
- **Shortest Path Calculation**: Use Dijkstra's algorithm to find the shortest path between vertices in the graph.
- **Minimum Spanning Tree (MST)**: Implement Prim’s algorithm to find the MST of a graph.
- **Main Graph**: A special graph that connects all other graphs and computes paths through them.

## Usage
1. **Graph Creation**:
   - Add multiple graphs by specifying the number of vertices and edges.
   - Assign weights to edges between vertices.

2. **Find Shortest Path**:
   - The system allows you to input a source and destination vertex to compute the shortest path.

3. **MST Calculation**:
   - For each graph, compute the MST using Prim’s algorithm.

4. **Interactive Mode**:
   - Once the graphs and paths are defined, the user can interactively query for shortest paths between any pair of vertices.

## Getting Started
To get started with this project, clone the repository and run the program in your Java IDE (e.g., IntelliJ IDEA, Eclipse).

```bash
git clone https://github.com/yourusername/NetworkPathFinder.git
cd NetworkPathFinder
```

### Requirements
- Java Development Kit (JDK) 8 or higher.
- Java IDE (e.g., IntelliJ IDEA, Eclipse).

## Example
- Input graph with vertices and edges.
- Calculate and display shortest paths using Dijkstra’s algorithm.

## License
This project is licensed under the MIT License.
