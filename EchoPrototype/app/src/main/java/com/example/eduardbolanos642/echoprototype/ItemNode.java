package com.example.eduardbolanos642.echoprototype;

public class ItemNode {
    private ItemNode next;
    private ItemNode previous;
    //DATA GOES HERE
    public ItemNode(int data, ItemNode nextNode, ItemNode previousNode){
        next = nextNode;
        previous = previousNode;
    }
    public void setNext(ItemNode node){
        next = node;
    }
    public ItemNode getNext(){
        return next;
    }
    public void setPrevious(ItemNode node){
        previous = node;
    }
    public ItemNode getPrevious(){
        return previous;
    }
    public void setData(int data){
    }
    public int getData(){
        return 0;
    }
}
