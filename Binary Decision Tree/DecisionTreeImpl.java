import java.util.List;
import java.util.ArrayList;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// public int tempTotal;
	public int attribute;
	public int threshold;
	public double maxGain;
	public double hyEntro;
	// 2D array of doubles with columns corresponding to attributes and rows corresponding to thresholds.
	// public List<List<Double>> gain;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(trainDataSet, 0);

		// this.tempTotal = 0;
		this.attribute = 0;
		this.threshold = 1;
		// this.gain = new ArrayList<List<Double>>();
		this.maxGain = 0;
		this.hyEntro = 0;
	}


	// recursive calls on left and right children
	private DecTreeNode buildTree(List<List<Integer>> dataSet, int depth) {
		int z = 0;		// # zero
		int o = 0;		// # one
		int label = 1;

		if (dataSet.size() == 0) {
				DecTreeNode newNode = new DecTreeNode(1, 0, 0);
				return newNode;
		}

		for (int i = 0; i < dataSet.size(); i++) {		// get label
			if (dataSet.get(i).get(dataSet.get(i).size() - 1) == 1) {
					o++;
			}
			else {
					z++;
			}
		}

		// set label
		if (o >= z) {
			label = 1;
		}
		else {
			label = 0;
		}


		if (z == 0 || o == 0) {		// check if all labels are same
				DecTreeNode newNode = new DecTreeNode(label, 0, 0);
				return newNode;
		}

		// check if depth is maxDepth
		if (depth == this.maxDepth) {
				DecTreeNode newNode = new DecTreeNode(label, 0, 0);
				return newNode;
		}

		// System.out.println("maxPerLeaf: " + this.maxPerLeaf);
		// check if maxPerLeaf
		if (dataSet.size() <= this.maxPerLeaf) {
				DecTreeNode newNode = new DecTreeNode(label, 0, 0);
				return newNode;
		}

		// this.hyEntro = calcHY(dataSet);
		this.maxGain = -1;
		// this.threshold = -1;
		// this.attribute = -1;
		for (int i = 0; i < dataSet.get(0).size() - 1; i++) {
				for (int j = 1; j < 10; j++) {
						calcInfoGain(j, i, dataSet);
				}
		}

		if (this.maxGain == 0) {
				DecTreeNode newNode = new DecTreeNode(label, 0, 0);
				return newNode;
		}

		List<List<Integer>> leftN = new ArrayList<List<Integer>>();
		List<List<Integer>> rightN = new ArrayList<List<Integer>>();
		List<Integer> person = new ArrayList<Integer>();

		for (int i = 0; i < dataSet.size(); i++) {
				person = dataSet.get(i);
				if (person.get(this.attribute) <= this.threshold) {
						leftN.add(person);
				}
				else {
						rightN.add(person);
				}
		}

		DecTreeNode newNode = new DecTreeNode(label, this.attribute, this.threshold);
		newNode.left = buildTree(leftN, depth+1);
		newNode.right = buildTree(rightN, depth+1);

		return newNode;
	}

	public int classify(List<Integer> person) {
		// Note that the last element of the array is the label.
		DecTreeNode node = root;
		while(!node.isLeaf()) {
			if (person.get(node.attribute) <= node.threshold) {
					node = node.left;
			}
			else {
					node = node.right;
			}
		}
		return node.classLabel;
	}

	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}




	// calculate the conditional entropy and use this and HY to calc info gain
 	public void calcInfoGain(int thresh, int attr, List<List<Integer>> people) {
		// double tempProb = 0;
		// double entro = 0;
		double right1 = 0;
		double right0 = 0;
		double left1 = 0;
		double left0 = 0;
		double total = 0;
		double infoGain = 0;
		// double entroHY = this.hyEntro;
		double entroHY = calcHY(people);
		List<Integer> person = new ArrayList<Integer>();

		for (int p = 0; p < people.size(); p++) {
				person = people.get(p);
				if (person.get(attr) <= thresh) {
						if (person.get(person.size() - 1) == 0) {
								left0++;
						}
						else {
								left1++;
						}
				}
				else {
						if (person.get(person.size() - 1) == 0) {
								right0++;
						}
						else {
								right1++;
						}
				}
			}


				total = left0 + left1 + right0 + right1;
				double rRatio = (right1 + right0) / total;
				double lRatio = (left1 + left0) / total;
				double r1Ratio = right1 / (right1 + right0);
				double r0Ratio = right0 / (right1 + right0);
				double l1Ratio = left1 / (left1 + left0);
				double l0Ratio = left0 / (left1 + left0);

				double leftEntro = 0;
				double rightEntro = 0;
				if ((right1 + right0) == 0 || (left1 + left0) == 0) {
						infoGain = 0;
				} else {
						if (r0Ratio == 0 || r1Ratio == 0) {
								rightEntro = 0;
						}
						else {
								rightEntro = rRatio * (r0Ratio * (Math.log(r0Ratio) / Math.log(2))
															 + r1Ratio * (Math.log(r1Ratio) / Math.log(2)));
						}
						if (l0Ratio  == 0 || l1Ratio == 0) {
								leftEntro = 0;
						}
						else {
								leftEntro = lRatio * (l0Ratio * (Math.log(l0Ratio) / Math.log(2))
														 	 	 + l1Ratio * (Math.log(l1Ratio) / Math.log(2)));
						}

						infoGain = entroHY + leftEntro + rightEntro;
				}
				if (infoGain > this.maxGain) {
						this.maxGain = infoGain;
						this.threshold = thresh;
						this.attribute = attr;
				}

	}




	public double calcHY(List<List<Integer>> people) {
		// first find number of one and zero values
		double one = 0;
		double zero = 0;
		double total = 0;
		List<Integer> person = new ArrayList<Integer>();

		for (int p = 0; p < people.size(); p++) {
			person = people.get(p);

			if (person.get(person.size() - 1) == 1) {
				one++;
			}
			else {
				zero++;
			}
		}
		total = zero + one;

		// next, calculate the ratios and the entropy H(Y) for each ratio
		double ratioA = one / total;
		double ratioB = zero / total;
		double entropyHY = 0;
		if (ratioA == 0) {
				return 0;
		}
		else if (ratioB == 0) {
				return 0;
		}
		else {
				entropyHY = (-1) * ((ratioA * (Math.log(ratioA) / Math.log(2)))
																	 + ratioB * (Math.log(ratioB) / Math.log(2)));
		}

		return entropyHY;
	}





	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}

	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
