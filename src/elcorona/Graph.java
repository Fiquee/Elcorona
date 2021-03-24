/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcorona;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Forge-15 i7
 */
public class Graph<T extends Comparable<T>> {

    GraphNode head;

    public Graph() {
        head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        int count = 0;
        if (!isEmpty()) {
            GraphNode curr = head;
            while (curr != null) {
                count++;
                curr = curr.nextVertice;
            }
        }
        return count;
    }

    public void clear() {
        head = null;
    }

    public String showGraph() {
        String str = "";
        if (!isEmpty()) {
            GraphNode curr = head;
            while (curr != null) {
                str = str + curr.toString() + "\n\n";
//                System.out.println(curr.toString());
                Edge edgeNode = (Edge) curr.edge;
                while (edgeNode != null) {
                    str = str + edgeNode.toString();
//                    System.out.print(edgeNode.toString());
                    edgeNode = edgeNode.nextEdge;
                }
                str = str + "\n\n";
                curr = curr.nextVertice;
            }
        } else {
            System.out.println("Graph is Empty");
        }
        return str;
    }

    public GraphNode getVertice(T vertice) {
        if (isEmpty()) {
            return null;
        } else {
            GraphNode curr = head;
            while (curr != null) {
                if (vertice.compareTo((T) curr.data) == 0) {
                    return curr;
                }
                curr = curr.nextVertice;
            }
            return null;
        }
    }

    public void mark(T vertice) {
        if (getVertice(vertice) == null) {
            System.out.println(vertice + " is not exist in the graph");
        } else {
            GraphNode node = getVertice(vertice);
            node.mark = true;
        }
    }

    public boolean isMarked(T vertice) {
        if (getVertice(vertice) == null) {
            return false;
        } else {
            return getVertice(vertice).mark;
        }
    }

    public void addVertice(T elem) {
        GraphNode newnode = new GraphNode(elem, null);
        if (isEmpty()) {
            head = newnode;
        } else {
            GraphNode curr = head;
            while (curr.nextVertice != null) {
                curr = curr.nextVertice;
            }
            curr.nextVertice = newnode;
        }
    }

    public boolean addEdge(T from, T to) {
        if (getVertice(from) == null || getVertice(to) == null) {
            return false;
        } else {
            GraphNode node = getVertice(from);
            GraphNode edgeNode = getVertice(to);
            Edge edge = (Edge) node.edge;
            if (edge == null) {
                node.edge = new Edge(edgeNode, null);
                return true;
            } else {
                while (edge.nextEdge != null) {
                    edge = edge.nextEdge;
                }
                edge.nextEdge = new Edge(edgeNode, null);
                return true;
            }
        }
    }
    
    public boolean Edgecontains(Edge edge,T elem){
        if(edge != null){
            while(edge != null){
                if(((T)edge.Edgevertice.data).compareTo(elem) == 0){
                    return true;
                }
                edge = edge.nextEdge;
            }
        }
        return false;
    }

    public int edgeCount(Edge elem) {
        int count = 0;
        if (elem != null) {
            Edge curr = elem;
            while (curr != null) {
                count++;
                curr = curr.nextEdge;
            }
        }
        return count;
    }

    public boolean isEdge(T from, T to) {
        if (getVertice(from) == null || getVertice(to) == null) {
            return false;
        } else {
            GraphNode target = getVertice(to);
            Edge edgeNode = (Edge) getVertice(from).edge;
            while (edgeNode != null) {
                if (edgeNode.Edgevertice == target) {
                    return true;
                }
                edgeNode = edgeNode.nextEdge;
            }
            return false;
        }
    }

    public boolean removeEdge(T from, T to) {
        if (!isEdge(from, to)) {
            return false;
        } else {
            GraphNode target = getVertice(to);
            Edge edgeNode = (Edge) getVertice(from).edge;
            if (edgeNode.Edgevertice == target) {
                getVertice(from).edge = edgeNode.nextEdge;
                return true;
            } else {
                while (edgeNode != null && edgeNode.nextEdge != null) {
                    if (edgeNode.nextEdge.Edgevertice == target) {
                        edgeNode.nextEdge = edgeNode.nextEdge.nextEdge;
                        return true;
                    }
                    edgeNode = edgeNode.nextEdge;
                }
                return false;
            }
        }
    }

    public ArrayList getEdge(T vertice) {
        ArrayList edgelist = new ArrayList();
        Edge edgeNode = (Edge) getVertice(vertice).edge;
        while (edgeNode != null) {
            edgelist.add(edgeNode.Edgevertice.data);
            edgeNode = edgeNode.nextEdge;
        }
        return edgelist;
    }

    public boolean DFS(T start, T to) {
        Stack<T> nodeStack = new Stack();
        Stack<T> pathStack = new Stack();
        ArrayList<T> adjascent = new ArrayList();
        if (getVertice(start) == null || getVertice(to) == null) {
            return false;
        } else {
            nodeStack.push(start);
            while (true) {
                T current = nodeStack.peek();
                mark(current);
                if (current.compareTo(to) == 0) {
                    while (!nodeStack.isEmpty()) {
                        T pathnode = nodeStack.pop();
                        if (isMarked(pathnode)) {
                            if (!pathStack.contains(pathnode)) {
                                pathStack.push(pathnode);
                            }
                        }
                    }
                    break;
                } else {
                    adjascent = getEdge(current);
                    for (int i = 0; i < adjascent.size(); i++) {
                        if (getVertice(adjascent.get(i)).mark == true) {
                            adjascent.remove(i);
                        }
                    }
                }
                if (adjascent.size() == 0) {
                    while (true) {
                        if (nodeStack.isEmpty()) {
                            System.out.println("No solution");
                            return false;
                        }
                        nodeStack.pop();
                        if (nodeStack.isEmpty()) {
                            System.out.println("No solution");
                            return false;
                        }
                        if (!isMarked(nodeStack.peek())) {
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < adjascent.size(); i++) {
                        nodeStack.push(adjascent.get(i));
                    }
                }
            }
        }
        while (!pathStack.isEmpty()) {
            System.out.print(pathStack.pop() + " --> ");
        }
        System.out.println();
        return true;
    }
}
