import java.util.*;

public class NetworkApplication {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Graph> kamBarkhordar = new ArrayList<>();
    static ArrayList<Graph> porBarkhordar = new ArrayList<>();
    static ArrayList<Graph> graphs = new ArrayList<>();
    static Graph mainGraph = new Graph("Main");

    public static void main(String[] args) {

        getGraphs();
        buildMST();
        initialMatrices();
        setShortestPathes();
        setShortestPathesForMain();
        launch();

    }

    private static void setShortestPathesForMain() {
        for (String vertex1 : mainGraph.getVertices().keySet()) {
            for (String vertex2 : mainGraph.getVertices().keySet()) {
                mainGraph.mainDijkstra(vertex1, vertex2);
            }
        }
    }

    private static void initialMatrices() {
        for (Graph g : graphs) {
            g.initialMatrix();
        }
        mainGraph.initialMatrix();
    }

    private static void setShortestPathes() {
        for (Graph g : graphs) {
            for (String vertexKey : g.getVertices().keySet()) {
                g.dijkstra(g.getName() + "1", vertexKey);
            }
        }
    }

    private static void getGraphs() {

        System.out.println("Enter the num of graphs");
        int numOfGraphs = sc.nextInt();

        for (int i = 1; i <= numOfGraphs; i++) {

            System.out.println("Enter the " + i + " graph name:");
            String name = sc.next();

            Graph graph = new Graph(name);
            System.out.println("Enter num of vertices:");

            int num_of_vertices = sc.nextInt();

            for (int j = 1; j <= num_of_vertices; j++) {
                if (j == 1) {
                    graph.addMainVertex(name + j);
                } else
                    graph.addVertex(name + j);
            }

            System.out.println("Enter num of edges:");
            int num_of_edges = sc.nextInt();

            for (int j = 0; j < num_of_edges; j++) {

                System.out.print("Enter src:");
                String first = sc.next();
                System.out.print("Enter dest:");
                String dest = sc.next();
                System.out.print("Enter weight:");
                int weight = sc.nextInt();

                if (graph.getVertices().containsKey(first) &&
                        graph.getVertices().containsKey(dest)) {

                    graph.addEdge(first, dest, weight);
                } else {
                    System.out.println("one or two of the vertices are missing");
                    j--;
                }
            }

            System.out.println("1.kam barkhordar\n2.barkhordar");
            int option = sc.nextInt();

            switch (option) {

                case 1 -> kamBarkhordar.add(graph);

                case 2 -> porBarkhordar.add(graph);
            }

            graphs.add(graph);
        }

        for (Graph graph : graphs) {
            mainGraph.addVertex(graph.getName() + 1);
        }

        System.out.print("Enter the num of main graph edges:");

        int num_of_main_graph_edges = sc.nextInt();

        for (int i = 0; i < num_of_main_graph_edges; i++) {

            System.out.print("Enter src:");
            String first = sc.next();
            System.out.print("Enter dest:");
            String dest = sc.next();
            System.out.print("Enter weight:");
            int weight = sc.nextInt();

            if (mainGraph.getVertices().containsKey(first) &&
                    mainGraph.getVertices().containsKey(dest)) {

                mainGraph.addEdge(first, dest, weight);
            } else {
                System.out.println("one or two of the vertices are missing");
                i--;
            }
        }
    }

    private static void launch() {

        Scanner sc = new Scanner(System.in);

        String src;
        String dst;

        while (true) {

            System.out.println("Enter source vertex for sending data:");
            src = sc.next();
            System.out.println("Enter destination vertex for sending data:");
            dst = sc.next();

            findShortestPath(src, dst);
        }
    }

    private static void findShortestPath(String src, String dest) {

        if (src.equals(dest)) {
            System.out.println("They are same");
            return;
        }

        Vertex startVertex = null;
        Vertex destVertex = null;

        label:
        for (Graph g : graphs) {
            for (String v : g.getVertices().keySet()) {

                if (v.equals(src)) {
                    startVertex = g.getVertices().get(v);
                }
                if (v.equals(dest)) {
                    destVertex = g.getVertices().get(v);
                }

                if (startVertex != null && destVertex != null) break label;
            }
        }

        if (startVertex == null || destVertex == null) {
            System.out.println("One or two vertices are missing");
            return;
        }

        List<Vertex> path = new ArrayList<>();
        if (src.charAt(0) != dest.charAt(0)) {

            for (Graph g : graphs) {
                if (g.getName().charAt(0) == src.charAt(0)) {
                    path.addAll(g.getShortest_paths()[Integer.parseInt(String.valueOf(src.charAt(1))) - 1][0]);
                    break;
                }
            }

            int size1 = path.size();

            path.addAll(mainGraph.getShortest_paths()[src.charAt(0)-65]
            [dest.charAt(0)-65]);

            int size2 = path.size();

            path.remove(size1);
            path.remove(size2 - 2);

            for (Graph g : graphs) {
                if (g.getName().charAt(0) == dest.charAt(0)) {
                    path.addAll(g.getShortest_paths()[0][Integer.parseInt(String.valueOf(dest.charAt(1))) - 1]);
                    break;
                }
            }

            System.out.print("Shortest path between " + src + " and " + dest + " is:\n");
            for (Vertex v : path) {
                System.out.print(v.getKey() + " ");
            }
            System.out.println();
        } else {

            for (Graph g : graphs) {
                if (g.getName().charAt(0) == src.charAt(0)) {

                    System.out.print("Shortest path between " + src + " and " + dest + " is:\n");

                    path.addAll(g.dijkstra(src, src.charAt(0) + "1"));
                    int boz = path.size();
                    path.remove(boz - 1);
                    path.addAll(g.dijkstra(src.charAt(0) + "1", dest));
                    for (Vertex v : path) {
                        System.out.print(v.getKey() + " ");
                    }
                    System.out.println();
                    break;
                }
            }
        }
    }

    private static void buildMST() {

        for (Graph g : kamBarkhordar) {

            Set<Edge> mstEdges = g.primMST();

            g.removeEdges();

            for (Edge e : mstEdges) {
                g.addEdge(e);
            }
        }
    }

}

class Vertex {
    private String key;
    private Map<Vertex, Integer> neighbors;

    public Vertex(String key) {
        this.key = key;
        this.neighbors = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public void addNeighbor(Vertex neighbor, int weight) {
        neighbors.put(neighbor, weight);
    }

    public Map<Vertex, Integer> getNeighbors() {
        return neighbors;
    }
}


class Graph {

    public List<Vertex>[][] getShortest_paths() {
        return shortest_paths;
    }

    private List<Vertex>[][] shortest_paths;
    private String name;
    private Vertex mainVertex;
    private Map<String, Vertex> vertices;

    public Map<String, Vertex> getVertices() {
        return vertices;
    }

    public Graph(String name) {

        this.name = name;
        this.vertices = new HashMap<>();
    }

    public void removeEdges() {
        for (Vertex vertex : vertices.values()) {
            vertex.getNeighbors().clear();
        }
    }

    public void addVertex(String key) {
        vertices.put(key, new Vertex(key));
    }

    public void addMainVertex(String key) {
        Vertex v = new Vertex(key);
        vertices.put(key, v);
        mainVertex = v;
    }

    public void addEdge(String key1, String key2, int weight) {
        Vertex v1 = vertices.get(key1);
        Vertex v2 = vertices.get(key2);
        if (v1 != null && v2 != null) {
            v1.addNeighbor(v2, weight);
            v2.addNeighbor(v1, weight);
        } else {
            System.out.println("One or two vertices are missing");
        }
    }

    public void addEdge(Edge e) {

        Vertex v1 = e.getSrc();
        Vertex v2 = e.getDest();
        int weight = e.getWeight();

        if (v1 != null && v2 != null) {
            v1.addNeighbor(v2, weight);
            v2.addNeighbor(v1, weight);
        } else {
            System.out.println("One or two vertices are missing");
        }
    }

    public List<Vertex> dijkstra(String startKey, String endKey) {


        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Map.Entry<Vertex, Integer>> priorityQueue = new PriorityQueue<>(Map.Entry.comparingByValue());

        Vertex startVertex = vertices.get(startKey);
        distances.put(startVertex, 0);
        priorityQueue.add(new AbstractMap.SimpleEntry<>(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            Map.Entry<Vertex, Integer> entry = priorityQueue.poll();
            Vertex current = entry.getKey();
            int currentDistance = entry.getValue();

            if (current.getKey().equals(endKey)) {
                List<Vertex> path = new ArrayList<>();
                for (Vertex at = current; at != null; at = previous.get(at)) {
                    path.add(at);
                }


                if (!Objects.equals(this.name, "Main")) {
                    shortest_paths[Integer.parseInt(String.valueOf(endKey.charAt(1))) - 1]
                            [Integer.parseInt(String.valueOf(startKey.charAt(1))) - 1] = new ArrayList<>(path);
                }

                Collections.reverse(path);

                if (!Objects.equals(this.name, "Main")) {
                    shortest_paths[Integer.parseInt(String.valueOf(startKey.charAt(1))) - 1]
                            [Integer.parseInt(String.valueOf(endKey.charAt(1))) - 1] = new ArrayList<>(path);
                }

                return path;
            }

            for (Map.Entry<Vertex, Integer> neighborEntry : current.getNeighbors().entrySet()) {
                Vertex neighbor = neighborEntry.getKey();
                int newDist = currentDistance + neighborEntry.getValue();

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    public Set<Edge> primMST() {
        Set<Edge> mst = new HashSet<>();
        PriorityQueue<Edge> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        Vertex startVertex = vertices.values().iterator().next();
        Set<Vertex> visited = new HashSet<>();
        visited.add(startVertex);

        for (Map.Entry<Vertex, Integer> entry : startVertex.getNeighbors().entrySet()) {
            priorityQueue.add(new Edge(startVertex, entry.getKey(), entry.getValue()));
        }

        while (!priorityQueue.isEmpty()) {
            Edge edge = priorityQueue.poll();
            if (visited.contains(edge.getDest())) {
                continue;
            }
            mst.add(edge);
            Vertex newVertex = edge.getDest();
            visited.add(newVertex);

            for (Map.Entry<Vertex, Integer> entry : newVertex.getNeighbors().entrySet()) {
                if (!visited.contains(entry.getKey())) {
                    priorityQueue.add(new Edge(newVertex, entry.getKey(), entry.getValue()));
                }
            }
        }
        return mst;
    }

    public Vertex getMainVertex() {
        return mainVertex;
    }

    public String getName() {
        return name;
    }

    public void initialMatrix() {
        int len = vertices.size();
        shortest_paths = new ArrayList[len][len];
    }

    public List<Vertex> mainDijkstra(String startKey, String endKey) {


        Map<Vertex, Integer> distances = new HashMap<>();
        Map<Vertex, Vertex> previous = new HashMap<>();
        PriorityQueue<Map.Entry<Vertex, Integer>> priorityQueue = new PriorityQueue<>(Map.Entry.comparingByValue());

        Vertex startVertex = vertices.get(startKey);
        distances.put(startVertex, 0);
        priorityQueue.add(new AbstractMap.SimpleEntry<>(startVertex, 0));

        while (!priorityQueue.isEmpty()) {
            Map.Entry<Vertex, Integer> entry = priorityQueue.poll();
            Vertex current = entry.getKey();
            int currentDistance = entry.getValue();

            if (current.getKey().equals(endKey)) {
                List<Vertex> path = new ArrayList<>();
                for (Vertex at = current; at != null; at = previous.get(at)) {
                    path.add(at);
                }


                if (Objects.equals(this.name, "Main")) {
                    int index1 = (int)endKey.charAt(0) - 65;
                    int index2 = (int)startKey.charAt(0) - 65;
                    shortest_paths[index1]
                            [index2] = new ArrayList<>(path);
                }

                Collections.reverse(path);

                if (Objects.equals(this.name, "Main")) {
                    int index1 = (int)startKey.charAt(0) - 65;
                    int index2 = (int)endKey.charAt(0) - 65;
                    shortest_paths[index1]
                            [index2] = new ArrayList<>(path);
                }

                return path;
            }

            for (Map.Entry<Vertex, Integer> neighborEntry : current.getNeighbors().entrySet()) {
                Vertex neighbor = neighborEntry.getKey();
                int newDist = currentDistance + neighborEntry.getValue();

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    priorityQueue.add(new AbstractMap.SimpleEntry<>(neighbor, newDist));
                }
            }
        }
        return Collections.emptyList(); // No path found
    }
}

class Edge {
    private Vertex src;
    private Vertex dest;
    private int weight;

    public Edge(Vertex src, Vertex dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Vertex getSrc() {
        return src;
    }

    public Vertex getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }
}
