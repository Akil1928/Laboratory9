package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BTreeMethodsTest {

    private BTree emptyTree;
    private BTree singleNodeTree;
    private BTree smallTree;
    private BTree balancedTree;
    private BTree unbalancedTree;

    @BeforeEach
    void setUp() {
        emptyTree = new BTree();

        singleNodeTree = new BTree();
        singleNodeTree.add(10);

        smallTree = new BTree();
        smallTree.add(20);
        smallTree.add(10);
        smallTree.add(30);

        balancedTree = new BTree();
        balancedTree.add(50);
        balancedTree.add(30);
        balancedTree.add(70);
        balancedTree.add(20);
        balancedTree.add(40);
        balancedTree.add(60);
        balancedTree.add(80);

        unbalancedTree = new BTree();
        unbalancedTree.add(10);
        unbalancedTree.add(20);
        unbalancedTree.add(30);
        unbalancedTree.add(40);
        unbalancedTree.add(15);
    }

    @Test
    void testEmptyTreeThrowsException() {
        TreeException ex1 = assertThrows(TreeException.class, () -> emptyTree.printNodes1Child());
        assertEquals("El árbol está vacío", ex1.getMessage());

        TreeException ex2 = assertThrows(TreeException.class, () -> emptyTree.printNodes2Children());
        assertEquals("El árbol está vacío", ex2.getMessage());

        TreeException ex3 = assertThrows(TreeException.class, () -> emptyTree.printNodesWithChildren());
        assertEquals("El árbol está vacío", ex3.getMessage());
    }

    @Test
    void testPrintNodes1Child() throws TreeException {
        assertEquals("Binary tree - nodes 1 child", singleNodeTree.printNodes1Child());
        assertEquals("Binary tree - nodes 1 child", smallTree.printNodes1Child());
        assertEquals("Binary tree - nodes 1 child", balancedTree.printNodes1Child());

        String result = unbalancedTree.printNodes1Child();
        assertTrue(result.contains("Node: 10, right son: 20"));
        assertTrue(result.contains("Node: 30, right son: 40"));
    }

    @Test
    void testPrintNodes2Children() throws TreeException {
        assertEquals("Binary tree - nodes 2 children", singleNodeTree.printNodes2Children());

        String result2 = smallTree.printNodes2Children();
        assertTrue(result2.contains("Node: 20, left son: 10, right son: 30"));

        String result3 = balancedTree.printNodes2Children();
        assertTrue(result3.contains("Node: 50, left son: 30, right son: 70"));
        assertTrue(result3.contains("Node: 30, left son: 20, right son: 40"));
        assertTrue(result3.contains("Node: 70, left son: 60, right son: 80"));

        String result4 = unbalancedTree.printNodes2Children();
        assertTrue(result4.contains("Node: 20, left son: 15, right son: 30"));
    }

    @Test
    void testPrintNodesWithChildren() throws TreeException {
        assertEquals("Binary tree - nodes with children", singleNodeTree.printNodesWithChildren());

        String result2 = smallTree.printNodesWithChildren();
        assertTrue(result2.contains("Node: 20, children: 10, 30"));

        String result3 = balancedTree.printNodesWithChildren();
        assertTrue(result3.contains("Node: 50, children: 30, 70"));

        String result4 = unbalancedTree.printNodesWithChildren();
        assertTrue(result4.contains("Node: 10, right son: 20"));
        assertTrue(result4.contains("Node: 20, children: 15, 30"));
    }

    @Test
    void demonstrateAllTreeOperations() throws TreeException {

        demonstrateTree("ÁRBOL BALANCEADO", balancedTree);
        demonstrateTree("ÁRBOL NO BALANCEADO", unbalancedTree);
        demonstrateTree("ÁRBOL PEQUEÑO (3 nodos)", smallTree);
        demonstrateTree("ÁRBOL DE UN SOLO NODO", singleNodeTree);

    }

    private void demonstrateTree(String title, BTree tree) throws TreeException {
        System.out.println("=== " + title + " ===\n");
        System.out.println("→ Estructura del árbol:");
        System.out.println(tree.toString() + "\n");

        System.out.println("→ Nodos con 1 hijo:");
        System.out.println(tree.printNodes1Child() + "\n");

        System.out.println("→ Nodos con 2 hijos:");
        System.out.println(tree.printNodes2Children() + "\n");

        System.out.println("→ Nodos con al menos un hijo:");
        System.out.println(tree.printNodesWithChildren());

        System.out.println("\n" + "=".repeat(52) + "\n");
    }
}
