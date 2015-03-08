/**
 * 
 */
package querqy.model;

/**
 * @author René Kriegler, @renekrie
 *
 */
public interface CloneableNode<P extends Node> extends Node {
   Node clone(P newParent);
   Node clone(P newParent, boolean generated);
}
