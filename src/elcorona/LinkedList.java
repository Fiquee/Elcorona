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
public class LinkedList<T extends Comparable<T>>{
    
    int size = 0;
    ListNode head;
    ListNode tail;

    public LinkedList() {
        
    }
    
    public boolean isEmpty(){
        return size==0;
    }
    
    public int size(){
        return size;
    }
    public void add(T elem) {
        addLast(elem);
    }
    
    public void clear(){
        while(head != null){
            removenode(head);
        }
    }

    public void addLast(T elem) {
        ListNode newnode = new ListNode(elem, null);
        if (isEmpty()) {
            head = tail = newnode;
            size++;
        } else {
            tail.next = newnode;
            tail = tail.next;
            size++;
        }
    }

    public void addFirst(T elem) {
        if (isEmpty()) {
            ListNode newnode = new ListNode(elem, null);
            head = tail = newnode;
            size++;
        } else {
            ListNode newnode = new ListNode(elem, head);
            head = newnode;
            size++;
        }
    }
    
    public T get(int index) {
        if (index >= size() || index < 0 || head == null) {
            return null;
        } else {
            int count = 0;
            ListNode currentNode = head;
            while (currentNode.next != null && count != index) {
                currentNode = currentNode.next;
                count++;
            }
            return (T) currentNode.getData();
        }
    }
    
    public void remove(T elem){
        if(!isEmpty()){
            ListNode curr = head;
            while(curr != null){
                if(elem.compareTo((T)curr.data)==0){
                    removenode(curr);
                    break;
                }
            }
        }
    }
    
    public void remove(int index) {
        if (index > 0 && index < size()) {
            ListNode curr = new ListNode(null, head);
            for (int i = 0; i <= index; i++) {
                curr = curr.next;
            }
            removenode(curr);
        }
    }

    public void removenode(ListNode node) {
        ListNode curr = head;
        if (node == head) {
            head = node.next;
            size--;
        } else {
            while (curr.next != node) {
                curr = curr.next;
            }
            curr.next = curr.next.next;
            size--;
        }
    }
    
    public String showList() {
        String str = "";
        if (isEmpty()) {
            System.out.println("The linked list is empty");
        } else {
            ListNode current = head;
            while (current != null) {
                str = str + current.data + " :: ";
                current = current.next;
            }
        }
        return str;
    }
    
    public boolean contains(T elem){
        if(!isEmpty()){
            ListNode curr = head;
            while(curr != null){
                if(((T)curr.data).compareTo(elem) == 0){
                    return true;
                }
                curr = curr.next;
            }
        }
        return false;
    }
    
    
}
