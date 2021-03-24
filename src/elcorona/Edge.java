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
public class Edge <T extends Comparable<T>>{
    
    GraphNode Edgevertice;
    Edge nextEdge;

    public Edge(GraphNode Edgevertice, Edge nextEdge) {
        this.Edgevertice = Edgevertice;
        this.nextEdge = nextEdge;
    }

    public GraphNode getEdgevertice() {
        return Edgevertice;
    }

    public void setEdgevertice(GraphNode Edgevertice) {
        this.Edgevertice = Edgevertice;
    }

    public Edge getNextEdge() {
        return nextEdge;
    }

    public void setNextEdge(Edge nextEdge) {
        this.nextEdge = nextEdge;
    }

    @Override
    public String toString() {
        return "   ->   " + Edgevertice.data;
    }
    
}

