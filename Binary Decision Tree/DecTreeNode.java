/**
 * Possible class for internal organization of a decision tree. Included to show standardized output
 * method, print().
 *
 * Do not modify.
 *
 */
public class DecTreeNode {
	//If leaf, label to return.
	public int classLabel;		// 0 or 1
	//Attribute split label.
	public int attribute;
	//Threshold that attributes are split on.
	public int threshold;
	//Left child. Can directly access and update. <= threshold.
	public DecTreeNode left = null;
	//Right child. Can directly access and update. > threshold.
	public DecTreeNode right = null;

	DecTreeNode(int classLabel, int attribute, int threshold) {
		this.classLabel = classLabel;
		this.attribute = attribute;
		this.threshold = threshold;
	}

	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
}
