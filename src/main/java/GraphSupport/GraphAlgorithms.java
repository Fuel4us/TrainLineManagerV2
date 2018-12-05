/*
* A collection of graph algorithms.
 */
package GraphSupport;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author DEI-ESINF
 */
public class GraphAlgorithms {

    /**
     * Performs breadth-first search of a Graph starting in a Vertex
     *
     * @param g Graph instance
     * @param vert information of the Vertex that will be the source of the
     * search
     * @return qbfs a queue with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert)) {
            return null;
        }

        int index = g.getKey(vert);

        LinkedList<V> resultQueue = new LinkedList<V>();
        boolean[] knownVertices = new boolean[g.numVertices()];

        LinkedList<V> auxQueue = new LinkedList<V>();

        resultQueue.add(vert);
        auxQueue.add(vert);

        knownVertices[index] = true;

        while (!auxQueue.isEmpty()) {
            V auxV = auxQueue.remove();
            for (V vAdj : g.adjVertices(auxV)) {
                if (vAdj != null) {
                    int dest = g.getKey(vAdj);
                    if (knownVertices[dest] == false) {
                        knownVertices[dest] = true;
                        resultQueue.add(vAdj);
                        auxQueue.add(vAdj);
                    }
                }
            }
        }
        return resultQueue;
    }

    /**
     * Performs depth-first search starting in a Vertex
     *
     * @param g Graph instance
     * @param vOrig Vertex of graph g that will be the source of the search
     * @param visited set of discovered vertices
     * @param qdfs queue with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        visited[g.getKey(vOrig)] = true;
        for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {
            if (visited[g.getKey(edge.getVDest())] == false) {
                qdfs.add(edge.getVDest());
                DepthFirstSearch(g, edge.getVDest(), visited, qdfs);
            }
        }
    }

    /**
     * @param <V>
     * @param <E>
     * @param g Graph instance
     * @param vert
     * @return qdfs a queue with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (g.validVertex(vert)) {
            boolean[] visited = new boolean[g.numVertices()];
            LinkedList<V> qdfs = new LinkedList<>();
            qdfs.add(vert);
            DepthFirstSearch(g, vert, visited, qdfs);
            return qdfs;
        }
        return null;
    }

    /**
     * Returns all paths from vOrig to vDest
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param vDest Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path stack with vertices of the current path (the path is in
     * reverse order)
     * @param paths ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited, LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        visited[g.getKey(vOrig)] = true;
        path.push(vOrig);
        for (V vAdj : g.adjVertices(vOrig)) {
            if (vAdj == vDest) {
                path.push(vDest);
                paths.add(revPath(path));
                path.pop();
            } else {
                if (!visited[g.getKey(vAdj)]) {
                    allPaths(g, vAdj, vDest, visited, path, paths);
                }
            }
        }
        visited[g.getKey(vOrig)] = false;
        path.pop();

    }

    /**
     * @param g Graph instance
     * @return paths ArrayList with all paths from voInf to vdInf
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        ArrayList<LinkedList<V>> paths = new ArrayList<>();
        if (g != null && vOrig != null && vDest != null) {
            LinkedList<V> path = new LinkedList<>();
            paths.clear();
            allPaths(g, vOrig, vDest, new boolean[g.numVertices()], path, paths);
        }
        return paths;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with nonnegative edge weights This implementation
     * uses Dijkstra's algorithm
     *
     * @param g Graph instance
     * @param vOrig Vertex that will be the source of the path
     * @param visited set of discovered vertices
     * @param dist minimum distances
     */
    protected static <V, E> void shortestPathLength(Graph<V, E> g, V vOrig, V[] vertices, boolean[] visited, int[] pathKeys, double[] dist) {
        int vkey = g.getKey(vOrig);
        dist[vkey] = 0;

        while (vkey != -1) {
            vOrig = vertices[vkey];
            visited[vkey] = true;

            for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {
                V vAdj = g.opposite(vOrig, edge);
                int vkeyAdj = g.getKey(vAdj);

                if (!visited[vkeyAdj] && dist[vkeyAdj] > dist[vkey] + edge.getWeight()) {
                    dist[vkeyAdj] = dist[vkey] + edge.getWeight();
                    pathKeys[vkeyAdj] = vkey;
                }
            }
            double minDist = Double.MAX_VALUE;
            vkey = -1;

            for (int i = 0; i < g.numVertices(); i++) {

                if (!visited[i] && dist[i] < minDist) {
                    minDist = dist[i];
                    vkey = i;
                }

            }
        }
    }

    private static int getVertMinDist(double[] minDist, boolean[] knownVertices) {
        double min = Integer.MAX_VALUE;
        int vertIdx = -1;
        for (int i = 0; i < knownVertices.length; i++) {
            if (!knownVertices[i] && minDist[i] < min) {
                min = minDist[i];
                vertIdx = i;
            }
        }
        return vertIdx;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf The path
     * is constructed from the end to the beginning
     *
     * @param g Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path stack with the minimum path (correct order)
     */
    protected static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest, V[] verts, int[] pathKeys, LinkedList<V> path) {
        if (!vOrig.equals(vDest)) {
            path.push(vDest);
            int vKey = g.getKey(vDest);
            int prevVKey = pathKeys[vKey];
            vDest = verts[prevVKey];
            getPath(g, vOrig, vDest, verts, pathKeys, path);
        } else {
            path.push(vOrig);
        }
    }

    //shortest-path between vOrig and vDest
    public static <V, E> double shortestPath(Graph<V, E> g, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) {
            return 0;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts]; //default value: false
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.allkeyVerts();
        shortPath.clear();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);
        double lengthPath = dist[g.getKey(vDest)];

        if (lengthPath != Double.MAX_VALUE) {
            getPath(g, vOrig, vDest, vertices, pathKeys, shortPath);
            return lengthPath;
        }

        return 0;
    }

    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig, ArrayList<LinkedList<V>> paths, ArrayList<Double> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        int nverts = g.numVertices();
        boolean[] visited = new boolean[nverts];
        int[] pathKeys = new int[nverts];
        double[] dist = new double[nverts];
        V[] vertices = g.allkeyVerts();

        for (int i = 0; i < nverts; i++) {
            dist[i] = Double.MAX_VALUE;
            pathKeys[i] = -1;
        }

        shortestPathLength(g, vOrig, vertices, visited, pathKeys, dist);

        dists.clear();
        paths.clear();
        for (int i = 0; i < nverts; i++) {
            paths.add(null);
            dists.add(null);
        }
        for (int i = 0; i < nverts; i++) {
            LinkedList<V> shortPath = new LinkedList<>();
            if (dist[i] != Double.MAX_VALUE) {
                getPath(g, vOrig, vertices[i], vertices, pathKeys, shortPath);
            }
            paths.set(i, shortPath);
            dists.set(i, dist[i]);
        }
        return true;
    }

    /**
     * Reverses the path
     *
     * @param path stack with path
     */
    private static <V, E> LinkedList<V> revPath(LinkedList<V> path) {

        LinkedList<V> pathcopy = new LinkedList<>(path);
        LinkedList<V> pathrev = new LinkedList<>();

        while (!pathcopy.isEmpty()) {
            pathrev.push(pathcopy.pop());
        }

        return pathrev;
    }

    public static <V, E> double shortestPathUnweighted(Graph<V, E> graph, V vOrig, V vDest, LinkedList<V> shortPath) {
        if (!graph.validVertex(vOrig) || !graph.validVertex(vDest)) {
            return 0;
        }

        V[] vertices = (V[]) new Object[graph.numVertices()];
        int[] pathKeys = new int[graph.numVertices()];
        double[] dist = new double[graph.numVertices()];
        boolean[] visited = new boolean[graph.numVertices()];

        for (int i = 0; i < graph.numVertices(); i++) {
            visited[i] = false;
            vertices[i] = null;
            dist[i] = Double.MAX_VALUE;
        }

        for (V v : graph.vertices()) {
            vertices[graph.getKey(v)] = v;
        }

        shortestPathUnweighted(graph, vOrig, vertices, visited, pathKeys, dist);
        shortPath.clear();

        if (!visited[graph.getKey(vDest)]) {
            return 0;
        }

        getPath(graph, vOrig, vDest, vertices, pathKeys, shortPath);
        return dist[graph.getKey(vDest)];
    }

    private static <V, E> void shortestPathUnweighted(Graph<V, E> g, V vOrig, V[] o, boolean[] visited, int[] pathKeys, double[] dist) {
        int oKey = g.getKey(vOrig);
        dist[oKey] = 0;

        while (oKey != -1) {
            visited[g.getKey(vOrig)] = true;
            for (Edge<V, E> e : g.outgoingEdges(vOrig)) {
                V vert = e.getVDest() != vOrig ? e.getVDest() : e.getVOrig();
                if (!visited[g.getKey(vert)] && dist[g.getKey(vert)] > dist[g.getKey(vOrig)]+1) {
                    dist[g.getKey(vert)] = dist[g.getKey(vOrig)]+1;
                    pathKeys[g.getKey(vert)] = g.getKey(vOrig);
                }
            }
            Double min = Double.MAX_VALUE;
            oKey = -1;
            for (int i = 0; i < g.numVertices(); i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    oKey = i;
                }
            }
            for (V v : g.vertices()) {
                if (g.getKey(v) == oKey) {
                    vOrig = v;
                    break;
                }
            }

        }
    }
}
