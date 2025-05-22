package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class BTreeTest {

    @Test
    void test() {
        BTree bTree = new BTree();
        for (int i = 0; i < 20; i++) {
            bTree.add(util.Utility.random(50));
        }
        System.out.println(bTree);
        try {
            System.out.println("BTree size: " + bTree.size());
            for (int i = 0; i < 20; i++) {
                int value = util.Utility.random(50);
                System.out.println(
                        bTree.contains(value)
                                ? "The value [" + value + "] exist in the binary tree"
                                : "The value [" + value + "] does not in the binary tree"

                );
                if(bTree.contains(value)){
                    bTree.remove(value);
                    System.out.println("The value [" + value + "] has been removed");
                }
            }
            System.out.println(bTree);
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
    }

}