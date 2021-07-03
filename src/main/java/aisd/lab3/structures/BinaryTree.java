package aisd.lab3.structures;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BinaryTree {
    private static final Logger logger = Logger.getLogger(BinaryTree.class.getName());
    private char element = '\0';
    private BinaryTree rightSubtree = null;
    private BinaryTree leftSubtree = null;

    private static char charAt(String string, int index) {
        if (index < string.length()) {
            return string.charAt(index);
        } else {
            return '\0';
        }
    }

    public boolean createFromString(String string) {
        AtomicInteger index = new AtomicInteger(0);
        boolean correct = createFromString(string, index);
        return correct && index.get() == string.length();
    }

    private boolean createFromString(String string, AtomicInteger index) {
        rightSubtree = null;
        leftSubtree = null;

        if (charAt(string, index.get()) == '/') {
            index.incrementAndGet();
            return true;
        }

        if (charAt(string, index.get()) == '(') {
            index.incrementAndGet();

            if (charAt(string, index.get()) != '(' && charAt(string, index.get()) != ')'
                    && charAt(string, index.get()) != '/' && charAt(string, index.get()) != ' '
                    && charAt(string, index.get()) != '\0') {
                element = charAt(string, index.get());
                index.incrementAndGet();
            } else {
                return false;
            }

            if (charAt(string, index.get()) == ' ') {
                index.incrementAndGet();
            } else if (charAt(string, index.get()) == ')') {
                index.incrementAndGet();
                return true;
            }

            if (charAt(string, index.get()) != '/') {
                leftSubtree = new BinaryTree();

                if (!leftSubtree.createFromString(string, index)) {
                    return false;
                }
            } else {
                index.incrementAndGet();
            }

            if (charAt(string, index.get()) == ' ') {
                index.incrementAndGet();
            } else if (charAt(string, index.get()) == ')') {
                index.incrementAndGet();
                return true;
            }

            if (charAt(string, index.get()) != '/') {
                rightSubtree = new BinaryTree();

                if (!rightSubtree.createFromString(string, index)) {
                    return false;
                }
            } else {
                index.incrementAndGet();
            }

            if (charAt(string, index.get()) == ')') {
                index.incrementAndGet();
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty() {
        return rightSubtree == null && leftSubtree == null && element == '\0';
    }

    public int getMaximumDepth() {
        return getMaximumDepth(0);
    }

    private int getMaximumDepth(int depth) {
        logger.log(Level.FINE, "  ".repeat(depth) + "Calling method getMaximumDepth() for binary tree "
                + getString() + ":");

        int leftSubtreeDepth = 0;
        int rightSubtreeDepth = 0;

        if (leftSubtree != null) {
            logger.log(Level.FINE,"  ".repeat(depth) + "Left binary subtree:");
            leftSubtreeDepth = leftSubtree.getMaximumDepth(depth + 1) + 1;
        }

        if (rightSubtree != null) {
            logger.log(Level.FINE,"  ".repeat(depth) + "Right binary subtree:");
            rightSubtreeDepth = rightSubtree.getMaximumDepth(depth + 1) + 1;
        }

        if (rightSubtreeDepth > leftSubtreeDepth) {
            logger.log(Level.FINE,"  ".repeat(depth) + "Method getMaximumDepth() for binary tree " + getString()
                    + " finished: Maximum depth: " + String.valueOf(rightSubtreeDepth) + "\n");
            return rightSubtreeDepth;
        } else {
            logger.log(Level.FINE,"  ".repeat(depth) + "Method getMaximumDepth() for binary tree " + getString()
                    + " finished: Maximum depth: " + String.valueOf(leftSubtreeDepth) + "\n");
            return leftSubtreeDepth;
        }
    }

    public int getInternalPathLength() {
        return getInternalPathLength(0);
    }

    private int getInternalPathLength(int depth) {
        logger.log(Level.FINE, "  ".repeat(depth) + "Calling method getInternalPathLength() for binary tree "
                + getString() + ":");

        int leftSubtreeLength = 0;
        int rightSubtreeLength = 0;

        if (leftSubtree != null) {
            logger.log(Level.FINE,"  ".repeat(depth) + "Left binary subtree:");
            leftSubtreeLength = leftSubtree.getInternalPathLength(depth + 1);
        }

        if (rightSubtree != null) {
            logger.log(Level.FINE,"  ".repeat(depth) + "Right binary subtree:");
            rightSubtreeLength = rightSubtree.getInternalPathLength(depth + 1);
        }

        logger.log(Level.FINE,"  ".repeat(depth) + "Method getInternalPathLength() for binary tree " + getString()
                + " finished: Internal path length: " + String.valueOf(leftSubtreeLength + rightSubtreeLength
                + depth) + "\n");
        return leftSubtreeLength + rightSubtreeLength + depth;
    }

    public String getString() {
        StringBuilder result = new StringBuilder("(");

        result.append(element);

        if (leftSubtree != null) {
            result.append(leftSubtree.getString());
        } else {
            result.append('/');
        }

        if (rightSubtree != null) {
            result.append(rightSubtree.getString());
        } else {
            result.append('/');
        }

        result.append(')');

        return result.toString();
    }
}
