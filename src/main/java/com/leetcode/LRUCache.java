package com.leetcode;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * 定义一个双链表和 Map实现LRU
 *
 *
 * @Author HaosionChiang
 * @Date 2023/4/13
 **/
public class LRUCache {

    int cap;
    Node head;
    Node tail;
    Map<Integer,Node> map;


    /**
     * 初始化
     * @param capacity
     */
    public LRUCache(int capacity) {
        this.cap=capacity;
        map= new HashMap<>();
        head=new Node(-1,-1);
        tail=new Node(-1,-1);
        head.next=tail;
        tail.pre=head;
    }

    public int get(int key) {
        if (!map.containsKey(key)){
            return -1;
        }
        //获取当前节点, 然后放在队头
        Node node = map.get(key);

        removeNode(node);
        addFront(node);
        return node.val;
    }

    public void put(int key, int value) {
        //如果LRU满了
        if (map.size()>this.cap){
            //删除队尾的
            removeTail();
            addFront(new Node(value,key));
            return;
        }
        //么有满
        if (map.containsKey(key)){
            //当前已有
            Node node = map.get(key);
            removeNode(node);
            addFront(node);
        }
        else{
            //没有直接放队头
            addFront(new Node(value,key));
        }
    }

    public void removeTail(){
        Node pp=tail.next;
        tail.next=null;
        pp.pre=null;
        pp.next=null;

    }
    public void removeNode(Node node){
        node.pre.next=node.next;
        node.next.pre=node.pre;
    }

    public void addFront(Node node){
        Node temp=head.next;
        head.next=node;
        node.pre=head;
        node.next=temp;
        temp.pre=node;
    }

    static class Node{
        int val;
        int key;
        Node pre;
        Node next;
        public Node(int _val,int _key){
            this.val= _val;
            this.key=_key;
            pre=null;
            next=null;
        }
    }
}
