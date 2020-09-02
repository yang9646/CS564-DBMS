
/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.util.ArrayList;
import java.util.List;

/**
 * B+Tree Structure Key - StudentId Leaf Node should contain [ key,recordId ]
 */
class BTree {

	/**
	 * Pointer to the root node.
	 */
	private BTreeNode root;
	/**
	 * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
	 **/
	private int t;

	BTree(int t) {
		this.root = null;
		this.t = t;
	}

	/**
	 * TODO: Implement this function to search in the B+Tree. Return recordID for
	 * the given StudentID. Otherwise, print out a message that the given studentId
	 * has not been found in the table and return -1.
	 */
	long search(long studentId) {

		if (root != null) {

			BTreeNode leafNode = findLeafNode(root, studentId);
//			long min = 0;
//			long max = 0;
//			
//			// Repeat until leaf node is reached
//			while (current.leaf == false) {
//
//				// Go through all pointers in the current node
//				for (int i = 0; i < current.keys.length + 1; i++) {
//
//					// Pointer before the first key
//					if (i == 0) {
//						max = current.keys[i];
//						if (studentId < max) {
//							current = current.children[0];
//							continue;
//						}
//					}
//					// Pointer between the first key and the last key
//					else if (0 < i && i != current.keys.length) {
//						min = max;
//						max = current.keys[i];
//						if (min <= studentId && studentId < max) {
//							current = current.children[i];
//							continue;
//						}
//					}
//					// Pointer after the last key
//					else {
//						current = current.children[i];
//						continue;
//					}
//				}
//			}

			for (int i = 0; i < leafNode.keys.length; i++) {
				if (leafNode.keys[i] == studentId) {
					return leafNode.values[i];
				}
			}
		}

		System.out.println("Given studentId is not found in this B+ Tree");
		return -1;
	}

	private BTreeNode findLeafNode(BTreeNode current, long studentId) {
		if (current.leaf == true) {
			return current;
		}

		long min = 0;
		long max = 0;
		for (int i = 0; i < current.keys.length + 1; i++) {

			// Pointer before the first key
			if (i == 0) {
				max = current.keys[i];
				if (studentId < max) {
					current = current.children[0];
					break;
				}
			}
			// Pointer between the first key and the last key
			else if (0 < i && i != current.keys.length) {
				min = max;
				max = current.keys[i];
				if (min <= studentId && studentId < max) {
					current = current.children[i];
					break;
				}
			}
			// Pointer after the last key
			else {
				current = current.children[i];
				break;
			}
		}
		return findLeafNode(current, studentId);
	}

	/**
	 * TODO: Implement this function to insert in the B+Tree. Also, insert in
	 * student.csv after inserting in B+Tree.
	 */
	BTree insert(Student student) {
		// Case when no tree is initialized
		if (root == null) {
			root = new BTreeNode(t, true);
			root.keys[0] = student.studentId;
			root.values[0] = student.recordId;
		}
		// Case when B Tree exists
		else {
			// Find the appropriate leaf node
			BTreeNode leafNode = findLeafNode(root, student.studentId);
			int count = 0;
			// Count the # of keys stored in the node
			for (int i = 0; i < leafNode.keys.length; i++) {
				if (leafNode.keys[i] != 0) {
					count++;
				} else {
					break;
				}
			}
			// Case when the node has space
			if (count < 2 * t - 1) {
				long key = 0;
				long value = 0;
				for (int i = 0; i < count; i++) {
					if (student.studentId < leafNode.keys[i]) {
						key = leafNode.keys[i];
						value = leafNode.values[i];
					}
				}
			}
		}
		return this;
	}

	boolean delete(long studentId) {
		/**
		 * TODO: Implement this function to delete in the B+Tree. Also, delete in
		 * student.csv after deleting in B+Tree, if it exists. Return true if the
		 * student is deleted successfully otherwise, return false.
		 */
		return true;
	}

	List<Long> print() {

		List<Long> listOfRecordID = new ArrayList<>();

		/**
		 * TODO: Implement this function to print the B+Tree. Return a list of recordIDs
		 * from left to right of leaf nodes.
		 *
		 */
		return listOfRecordID;
	}
}
