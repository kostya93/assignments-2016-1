package ru.spbau.mit;

import java.io.*;

/**
 * Created by kostya on 12.03.2016.
 */
public class StringSetImpl implements StringSet, StreamSerializable{

    private static final int NUM_OF_SYM = 'z' - 'A' + 1;
    private Node root = new Node();

    @Override
    public boolean add(String element) {
        if (contains(element)) {
            return false;
        }

        Node currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            currentNode.size++;
            int ind = getIndex(element, i);
            if (currentNode.children[ind] == null) {
                currentNode.children[ind] = new Node();
            }
            currentNode = currentNode.children[ind];
        }

        currentNode.size++;
        currentNode.endOfString = true;

        return true;
    }

    @Override
    public boolean contains(String element) {
        if (element == null) {
            return false;
        }

        Node currentNode = goDown(root, element);

        return currentNode != null && currentNode.endOfString;
    }

    @Override
    public boolean remove(String element) {
        if (!contains(element)) {
            return false;
        }

        Node currentNode = root;

        for (int i = 0; i < element.length(); i++) {
            currentNode.size--;
            int ind = getIndex(element, i);
            if (currentNode.children[ind].size == 1) {
                currentNode.children[ind] = null;
                return true;
            }
            currentNode = currentNode.children[ind];
        }
        currentNode.endOfString = false;
        currentNode.size--;
        return true;
    }

    @Override
    public int size() {
        return root.size;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        Node currentNode = goDown(root, prefix);
        if (currentNode == null) {
            return 0;
        }
        return currentNode.size;
    }

    @Override
    public void serialize(OutputStream out) {
        root.serialize(new DataOutputStream(out));
    }

    @Override
    public void deserialize(InputStream in) {
        root.deserialize(new DataInputStream(in));
    }

    private Node goDown(Node currentNode, String str) {
        for (int i = 0; i < str.length(); i++) {
            if (currentNode.children[getIndex(str, i)] == null) {
                return null;
            }
            currentNode = currentNode.children[getIndex(str, i)];
        }
        return currentNode;
    }

    private static int getIndex(String str, int i) {
        return str.charAt(i) - 'A';
    }

    private static final class Node{

        private int size;
        private boolean endOfString;
        private Node[] children = new Node[NUM_OF_SYM];

        private void serialize(DataOutputStream out) {
            try {
                out.writeBoolean(endOfString);
                for (Node aChildren : children) {
                    if (aChildren != null) {
                        out.writeBoolean(true);
                        aChildren.serialize(out);
                    } else {
                        out.writeBoolean(false);
                    }
                }
            } catch (IOException e) {
                throw new SerializationException();
            }

        }

        private void deserialize(DataInputStream in) {
            try {
                endOfString = in.readBoolean();
                if (endOfString) {
                    size = 1;
                }
                for (int i = 0; i < children.length; i++) {
                    if (in.readBoolean()) {
                        children[i] = new Node();
                        children[i].deserialize(in);
                        size += children[i].size;
                    }
                }
            } catch (IOException e) {
                throw new SerializationException();
            }
        }
    }
}
