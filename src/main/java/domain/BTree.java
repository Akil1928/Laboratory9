package domain;

public class BTree implements Tree {
    private BTreeNode root; // se refiere a la raíz del árbol

    @Override
    public int size() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return size(root);
    }

    private int size(BTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return bynarySearch(root, element);
    }

    private boolean bynarySearch(BTreeNode node, Object element) {
        if (node == null) {
            return false;
        }
        if (util.Utility.compare(node.data, element) == 0) {
            return true;
        }
        return bynarySearch(node.left, element) || bynarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        root = add(root, element);
    }

    private BTreeNode add(BTreeNode node, Object element) {
        if (node == null) {
            node = new BTreeNode(element);
        } else {
            // Si no es nulo, nos posicionamos recursivamente
            // en la posición correcta según el criterio
            if (util.Utility.compare(element, node.data) < 0) {
                node.left = add(node.left, element);
            } else if (util.Utility.compare(element, node.data) > 0) {
                node.right = add(node.right, element);
            }
        }
        return node;
    }

    private BTreeNode add(BTreeNode node, Object element, String path) {
        if (node == null) {
            node = new BTreeNode(element);
        } else if (path.charAt(0) == 'L') {
            node.left = add(node.left, element, path.substring(1));
        } else if (path.charAt(0) == 'R') {
            node.right = add(node.right, element, path.substring(1));
        }
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) {
        if (node != null) {
            if (util.Utility.compare(element, node.data) < 0) {
                node.left = remove(node.left, element);
            } else if (util.Utility.compare(element, node.data) > 0) {
                node.right = remove(node.right, element);
            } else if (util.Utility.compare(element, node.data) == 0) { // Encontró el elemento a eliminar
                // Caso 1: El nodo es una hoja (no tiene hijos)
                if (node.left == null && node.right == null) {
                    return null;
                }
                // Caso 2: El nodo tiene un hijo
                else if (node.left != null && node.right == null) {
                    return node.left; // Sustituye el nodo por su hijo izquierdo
                } else if (node.left == null && node.right != null) {
                    return node.right; // Sustituye el nodo por su hijo derecho
                }
                // Caso 3: El nodo tiene dos hijos
                else if (node.left != null && node.right != null) {
                    Object value = getLeaf(node.left);
                    node.data = value;
                    node.left = removeLeaf(node.left, value);
                }
            }
        }
        return node;
    }

    private BTreeNode removeLeaf(BTreeNode node, Object value) {
        if (node == null) {
            return null;
        }
        if (node.left == null && node.right == null && util.Utility.compare(node.data, value) == 0) {
            return null;
        }
        node.left = removeLeaf(node.left, value);
        node.right = removeLeaf(node.right, value);
        return node;
    }

    private Object getLeaf(BTreeNode node) {
        if (node == null) {
            return null;
        }
        if (node.left == null && node.right == null) {
            return node.data;
        }
        Object leftLeaf = getLeaf(node.left);
        if (leftLeaf != null) {
            return leftLeaf;
        }
        return getLeaf(node.right);
    }

    @Override
    public int height() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return height(root);
    }

    private int height(BTreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    @Override
    public int height(Object element) throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        if (!contains(element)) {
            throw new TreeException("The element doesn't exist in Binary Tree");
        }
        return height(root, element, 0);
    }

    @Override
    public int height(BTreeNode node, Object element, int level) throws TreeException {
        if (node == null) {
            return 0;
        }
        if (util.Utility.compare(node.data, element) == 0) {
            return heightFromNode(node);
        }
        int leftHeight = height(node.left, element, level + 1);
        int rightHeight = height(node.right, element, level + 1);
        return Math.max(leftHeight, rightHeight);
    }

    // Método auxiliar para calcular la altura desde un nodo específico
    private int heightFromNode(BTreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = heightFromNode(node.left);
        int rightHeight = heightFromNode(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return min(root);
    }

    private Object min(BTreeNode node) {
        if (node.left == null) {
            return node.data;
        }
        return min(node.left);
    }

    @Override
    public Object max() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return max(root);
    }

    private Object max(BTreeNode node) {
        if (node.right == null) {
            return node.data;
        }
        return max(node.right);
    }

    @Override
    public String preOrder() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return "PreOrder Transversal: " + preOrder(root);
    }

    private String preOrder(BTreeNode node) {
        if (node == null) {
            return "";
        }
        return node.data + " " + preOrder(node.left) + preOrder(node.right);
    }

    @Override
    public String inOrder() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return "InOrder Transversal: " + inOrder(root);
    }

    private String inOrder(BTreeNode node) {
        if (node == null) {
            return "";
        }
        return inOrder(node.left) + node.data + " " + inOrder(node.right);
    }

    @Override
    public String postOrder() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("Binary Tree is empty");
        }
        return "PostOrder Transversal: " + postOrder(root);
    }

    private String postOrder(BTreeNode node) {
        if (node == null) {
            return "";
        }
        return postOrder(node.left) + postOrder(node.right) + node.data + " ";
    }

    @Override
    public String toString() {
        if (isEmpty()) return "The Binary Tree is empty";
        StringBuilder s = new StringBuilder();
        s.append("\nRoot: ").append(root.data).append("\n");
        try {
            s.append("PreOrder: ").append(preOrder()).append("\n");
            s.append("InOrder: ").append(inOrder()).append("\n");
            s.append("PostOrder: ").append(postOrder()).append("\n");
        } catch (TreeException ex) {
            System.out.println(ex.getMessage());
        }
        return s.toString();
    }

    public String printLeaves() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("El árbol está vacío");
        }
        StringBuilder result = new StringBuilder("Binary tree leaves");
        printLeaves(root, result);
        return result.toString();
    }

    private void printLeaves(BTreeNode node, StringBuilder result) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            result.append("\n").append(node.data);
        }
        printLeaves(node.left, result);
        printLeaves(node.right, result);
    }

    // MÉTODOS REIMPLEMENTADOS:

    public String printNodes1Child() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("El árbol está vacío");
        }
        StringBuilder result = new StringBuilder("Binary tree - nodes 1 child");
        boolean foundNodes = printNodes1Child(root, result);

        // Retorna el resultado sin modificación adicional si no se encontraron nodos
        if (!foundNodes) {
            return result.toString();
        }

        return result.toString();
    }

    private boolean printNodes1Child(BTreeNode node, StringBuilder result) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        // Verificar si el nodo tiene exactamente un hijo
        if ((node.left != null && node.right == null) || (node.left == null && node.right != null)) {
            result.append("\n");

            if (node.left != null) {
                result.append("Node: ").append(node.data).append(", left son: ").append(node.left.data);
            } else {
                result.append("Node: ").append(node.data).append(", right son: ").append(node.right.data);
            }

            found = true;
        }

        // Continuar recorrido en subárboles
        boolean foundLeft = printNodes1Child(node.left, result);
        boolean foundRight = printNodes1Child(node.right, result);

        return found || foundLeft || foundRight;
    }

    public String printNodes2Children() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("El árbol está vacío");
        }
        StringBuilder result = new StringBuilder("Binary tree - nodes 2 children");
        boolean foundNodes = printNodes2Children(root, result);

        // Retorna el resultado sin modificación adicional si no se encontraron nodos
        if (!foundNodes) {
            return result.toString();
        }

        return result.toString();
    }

    private boolean printNodes2Children(BTreeNode node, StringBuilder result) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        // Verificar si el nodo tiene exactamente dos hijos
        if (node.left != null && node.right != null) {
            result.append("\n");
            result.append("Node: ").append(node.data)
                    .append(", left son: ").append(node.left.data)
                    .append(", right son: ").append(node.right.data);
            found = true;
        }

        // Continuar recorrido en subárboles
        boolean foundLeft = printNodes2Children(node.left, result);
        boolean foundRight = printNodes2Children(node.right, result);

        return found || foundLeft || foundRight;
    }

    public String printNodesWithChildren() throws TreeException {
        if (isEmpty()) {
            throw new TreeException("El árbol está vacío");
        }
        StringBuilder result = new StringBuilder("Binary tree - nodes with children");
        boolean foundNodes = printNodesWithChildren(root, result);

        // Retorna el resultado sin modificación adicional si no se encontraron nodos
        if (!foundNodes) {
            return result.toString();
        }

        return result.toString();
    }

    private boolean printNodesWithChildren(BTreeNode node, StringBuilder result) {
        if (node == null) {
            return false;
        }

        boolean found = false;

        // Verificar si el nodo tiene al menos un hijo
        if (node.left != null || node.right != null) {
            result.append("\n");

            if (node.left != null && node.right != null) {
                result.append("Node: ").append(node.data)
                        .append(", children: ").append(node.left.data)
                        .append(", ").append(node.right.data);
            } else if (node.left != null) {
                result.append("Node: ").append(node.data)
                        .append(", left son: ").append(node.left.data);
            } else {
                result.append("Node: ").append(node.data)
                        .append(", right son: ").append(node.right.data);
            }

            found = true;
        }

        // Continuar recorrido en subárboles
        boolean foundLeft = printNodesWithChildren(node.left, result);
        boolean foundRight = printNodesWithChildren(node.right, result);

        return found || foundLeft || foundRight;
    }
}