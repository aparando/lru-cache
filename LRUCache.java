package com.aliparandoosh.myapplication;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private static class Node<K, V> {
        private Node(K key, V value, Node<K, V> previous, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.previous = previous;
            this.next = next;
        }

        K key;
        V value;
        Node<K, V> previous;
        Node<K, V> next;
    }

    private Map<K, Node<K, V>> cache;
    private int maxSize;
    private int currentSize;
    private Node<K, V> LRU;
    private Node<K, V> MRU;

    public LRUCache(int maxSize) {
        LRU = new Node<>(null, null, null, null);
        MRU = LRU;
        cache = new HashMap<>();
        this.maxSize = maxSize;
        currentSize = 0;
    }

    public V get(K key) {
        Node<K, V> temp = cache.get(key);
        if (temp == null) {
            return null;
        }
        if (temp.key == MRU.key) {
            return temp.value;
        }
        Node<K, V> nextNode = temp.next;
        Node<K, V> previousNode = temp.previous;
        if (temp.key == LRU.key) {
            LRU = nextNode;
            LRU.previous = null;
            MRU.next = temp;
            temp.previous = MRU;
            MRU = temp;
            MRU.next = null;
            return temp.value;
        }
        nextNode.previous = previousNode;
        previousNode.next = nextNode;
        MRU.next = temp;
        temp.previous = MRU;
        MRU = temp;
        MRU.next = null;
        return temp.value;
    }

    public void put(K key, V value) {
        if (cache.containsKey(key)) {
            Node<K, V> node = cache.get(key);
            if (node != null) {
                node.value = value;
                if (key == MRU.key) {
                    cache.put(key, node);
                    return;
                }
                if (key == LRU.key) {
                    LRU = node.next;
                    LRU.previous = null;
                    MRU.next = node;
                    node.previous = MRU;
                    MRU = node;
                    MRU.next = null;
                    return;
                }
                Node<K, V> previousNode = node.previous;
                Node<K, V> nextNode = node.next;
                nextNode.previous = previousNode;
                previousNode.next = nextNode;
                MRU.next = node;
                node.previous = MRU;
                MRU = node;
                MRU.next = null;
                return;
            }
        }
        Node<K, V> node = new Node<>(key, value, MRU, null);
        cache.put(key, node);
        MRU.next = node;
        MRU = node;
        MRU.next = null;
        if (currentSize == maxSize) {
            cache.remove(LRU.key);
            LRU = LRU.next;
            LRU.previous = null;
        } else {
            if (currentSize == 0) {
                LRU = node;
            }
            currentSize++;
        }

    }
}

