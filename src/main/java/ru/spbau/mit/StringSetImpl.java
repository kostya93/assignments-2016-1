package ru.spbau.mit;

import java.util.ArrayList;

/**
 * Created by kostya on 2/21/16.
 */
public class StringSetImpl implements StringSet{

    private static int NUM_OF_SYM = 'z' - 'A' + 1;
    private Node root = new Node();

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            if (currentNode.childs[element.charAt(i) - 'A'] == null) {
                currentNode.childs[element.charAt(i) - 'A'] = new Node();
                currentNode.childs[element.charAt(i) - 'A'].parent = currentNode;
            }
            currentNode = currentNode.childs[element.charAt(i) - 'A'];
        }

        currentNode.endOfString = true;

        incSize(currentNode);

        return true;
    }

    @Override
    public boolean contains(String element) {

        if (element == null) {
            return false;
        }

        Node currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            if (currentNode.childs[element.charAt(i) - 'A'] == null) {
                return false;
            }
            else {
                currentNode = currentNode.childs[element.charAt(i) - 'A'];
            }
        }
        if (currentNode.endOfString) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Node currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            currentNode = currentNode.childs[element.charAt(i) - 'A'];
        }

        currentNode.endOfString = false;
        decSize(currentNode);

        return true;
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node currentNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (currentNode.childs[prefix.charAt(i) - 'A'] == null) {
                return 0;
            }
            else {
                currentNode = currentNode.childs[prefix.charAt(i) - 'A'];
            }
        }
        return currentNode.size;
    }

    private void incSize(Node currentNode) {
        while (currentNode != root) {
            currentNode.size++;
            currentNode = currentNode.parent;
        }
        currentNode.size++;
    }
    private void decSize(Node currentNode) {
        while (currentNode != root) {
            currentNode.size--;
            if (currentNode.size < 0) {
                currentNode.size = 0;
            }
            currentNode = currentNode.parent;
        }
        currentNode.size--;
        if (currentNode.size < 0) {
            currentNode.size = 0;
        }
    }

    private static class Node {
        private Node parent;
        private int size;
        private boolean endOfString;
        private Node[] childs = new Node[NUM_OF_SYM];

        private Node() {
            size = 0;
        }
    }
}
