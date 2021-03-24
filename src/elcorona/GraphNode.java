/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elcorona;

/**
 *
 * @author Forge-15 i7
 */
public class GraphNode <T,E> {
    
    T data;
    E edge;
    GraphNode nextVertice;
    boolean mark;

    public GraphNode(T data, GraphNode nextVertice) {
        this.data = data;
        this.nextVertice = nextVertice;
        this.edge = null;
        this.mark = false;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public E getEdge() {
        return edge;
    }

    public void setEdge(E edge) {
        this.edge = edge;
    }

    public GraphNode getNextVertice() {
        return nextVertice;
    }

    public void setNextVertice(GraphNode nextVertice) {
        this.nextVertice = nextVertice;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    @Override
    public String toString() {
        return "  Person " + data + " --> ";
    }
}
