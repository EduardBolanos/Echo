package com.example.echo.echoprototype;

public class ItemNode implements Comparable{
    private ItemNode next;
    private ItemNode previous;
    private Item me;
    public ItemNode(Item data, ItemNode nextNode, ItemNode previousNode){
        next = nextNode;
        previous = previousNode;
        me = data;
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
    public void setData(Item data){
        me = data;
    }
    public Item getData(){
        return me;
    }

    @Override
    public int compareTo(Object o) {
        //some compare about data
        if (((ItemNode)o).getData().getName() == this.getData().getName()){
            return 0; // we found it Tim
        }
        return -1; // not found
        }
}
