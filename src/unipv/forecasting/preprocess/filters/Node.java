package unipv.forecasting.preprocess.filters;

import java.util.ArrayList;

import unipv.forecasting.CONFIGURATION;

public class Node {
	private double greatestValue;
	private double smallestValue;
	private int upperIndex;
	private int lowerIndex;
	private ArrayList<Value> data;
	private double splitPoint;
	private Node father;
	private Node leftChild;
	private Node rightChild;
	private Node sibling;

	public Node(Node father, double greatestValue, double smallestValue,
			int upperIndex, int lowerIndex, ArrayList<Value> data) {
		this.father = father;
		this.greatestValue = greatestValue;
		this.smallestValue = smallestValue;
		this.upperIndex = upperIndex;
		this.lowerIndex = lowerIndex;
		this.data = data;
	}

	public void splitData() {
		/**
		 * 2.2 Randomly choose a number between the greatest value and smallest
		 * value as the split point, and use it as the greatest value and
		 * smallest value to create two new nodes.
		 **/
		double r = Math.random();
		splitPoint = r * (greatestValue - smallestValue) + smallestValue;
		int splitIndex = lowerIndex;
		
		/** find the split index for the child node **/
		for (; data.get(splitIndex).getValue() < splitPoint; splitIndex++);
		/** create child node **/
		leftChild = new Node(this, splitPoint, smallestValue, splitIndex - 1,
				lowerIndex, data);
		rightChild = new Node(this, greatestValue, splitPoint, upperIndex,
				splitIndex, data);
		leftChild.setSibling(rightChild);
		rightChild.setSibling(leftChild);
		
		int granularity = data.size() / CONFIGURATION.IFOREST_GRANULARITY;
		if(leftChild.getVolume() < granularity) {
//			System.out.println(splitPoint + "," + lowerIndex + "," + upperIndex + " Left OK " + calculatePL());
			leftChild.updatePL();
		} else {
//			System.out.println(splitPoint + "," + lowerIndex + "," + upperIndex + " Left NO " + calculatePL());
			leftChild.splitData();
		}
		if(rightChild.getVolume() < granularity) {
//			System.out.println(splitPoint + "," + lowerIndex + "," + upperIndex + " Left OK " + calculatePL());
			rightChild.updatePL();
		} else {
//			System.out.println(splitPoint + "," + lowerIndex + "," + upperIndex + " Left NO " + calculatePL());
			rightChild.splitData();
		}
	}

	public int getVolume() {
		return upperIndex - lowerIndex;
	}

	public int getPL() {
		int pl = 1;
		Node child = this;
		while(child.getFather() != null) {
			pl++;
			child = child.getFather();
		}
		return pl;
	}
	
	public void updatePL() {
		int pl = getPL();
		for(int i = lowerIndex; i <= upperIndex; i++) {
			data.get(i).addPL(pl);
		}
	}
	
	public boolean isOutlier(int pl_outlier) {
		if((leftChild == null) && (rightChild == null)) {
			if(getPL() <= pl_outlier) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
//	public double getSP(boolean isLeft) {
//		double sp = 0;
//		if (isLeft) {
//			sp = data.get(sibling.getLowerIndex()).getValue();
//		} else {
//			sp = data.get(sibling.getUpperIndex()).getValue();
//		}
//		return sp; 
//	}
//	
//	public void updateSP(boolean isLeft) {
//		double sp = getSP(isLeft);
//		for(int i = lowerIndex; i <= upperIndex; i++) {
//			data.get(i).addSP(sp);
//		}
//	}

	/**
	 * @return the father
	 */
	public Node getFather() {
		return father;
	}

	/**
	 * @return the splitPoint
	 */
	public double getSplitPoint() {
		return splitPoint;
	}

	/**
	 * @return the sibling
	 */
	public Node getSibling() {
		return sibling;
	}

	/**
	 * @param sibling the sibling to set
	 */
	public void setSibling(Node sibling) {
		this.sibling = sibling;
	}

	/**
	 * @return the leftChild
	 */
	public Node getLeftChild() {
		return leftChild;
	}

	/**
	 * @return the rightChild
	 */
	public Node getRightChild() {
		return rightChild;
	}

	/**
	 * @return the upperIndex
	 */
	public int getUpperIndex() {
		return upperIndex;
	}

	/**
	 * @return the lowerIndex
	 */
	public int getLowerIndex() {
		return lowerIndex;
	}
	
	
}
