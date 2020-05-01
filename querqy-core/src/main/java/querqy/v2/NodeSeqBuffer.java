package querqy.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * Two use cases of NodeSeq need to be covered:
 *      1. NodeSeqBuilder: Build a NodeSeq to build new query, add variants or for tests: Append and wire new nodes
 *          - Nodes must be fresh
 *          - ArrayList of nodes useful for iteration / addAll?
 *      2. NodeSeqBuffer: keeps an ArrayList of nodes, remembers offsets of last modification?
 *          - e. g. ArrayList + offset
 *          - clear() -> offset = 0
 */
public class NodeSeqBuffer {
    private final List<Node> nodeBuffer;


    private int offset;

    public NodeSeqBuffer() {
        this.nodeBuffer = new ArrayList<>();
        this.offset = 0;
    }

    public NodeSeqBuffer add(Node node) {

        int size = this.nodeBuffer.size();

        if (this.offset == size) {
            this.nodeBuffer.add(this.offset++, node);

        } else if (this.offset < size){
            this.nodeBuffer.set(this.offset++, node);

        } else {
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds", this.offset));
        }
        return this;
    }

    public NodeSeqBuffer set(int index, Node node) {
        offset = index;
        return add(node);
    }

    public NodeSeqBuffer addAll(Node... nodes) {
        for (Node node : nodes) {
            add(node);
        }
        return this;
    }

    public NodeSeqBuffer setAll(int index, Node... nodes) {
        offset = index;
        return addAll(nodes);
    }

    public NodeSeqBuffer clear() {
        this.offset = 0;
        return this;
    }

    public NodeSeq createNodeSeqFromBuffer() {
        return new NodeSeq(new ArrayList<>(nodeBuffer.subList(0, this.offset)));
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public Node getLast() {
        return offset > 0 ? nodeBuffer.get(offset - 1) : null;
    }

    public List<Node> getNodes() {
        return nodeBuffer.subList(0, offset);
    }

    @Override
    public String toString() {
        return "Nodes" + nodeBuffer.subList(0, offset);
    }
}
