package domain;

public class BTree implements  Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public int size() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {

        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return bynarySearch(root, element);
    }

    private boolean bynarySearch(BTreeNode node, Object element) {
        if(node==null) return false;
        else if(util.Utility.compare(node.data, element) == 0) return true;
        else return bynarySearch(node.left, element) || bynarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
       //this.root = add(root, element);
        this.root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null)
            node = new BTreeNode(element);
        else{
            int value = util.Utility.random(100);
            if(value%2==0)
                node.left = add(node.left, element);
            else node.right = add(node.right, element);
        }
        return node;
    }

    private BTreeNode add(BTreeNode node, Object element, String path){
        if(node==null)
            node = new BTreeNode(element, path);
        else{
            int value = util.Utility.random(100);
            if(value%2==0)
                node.left = add(node.left, element, path+"/left");
            else node.right = add(node.right, element, path+"/right");
        }
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
if(isEmpty())
    throw new TreeException("Binary Tree is empty");
root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) {
        if(node!=null){
            if(util.Utility.compare(node.data, element) == 0){
                if(node.left==null && node.right==null) return null;
                else if ((node.left!=null && node.right == null)) {
                    //node.left = newPath(node.left, node.path);
                    return node.left;

                } else if (node.left==null&&node.right!=null) {
                   // node.right = newPath(node.right, node.path);
                    return node.right;

                } else if (node.left!=null&&node.right!=null) {
                    //El algoritmo de supresion dice que cuando el nodo a suprimir tiene 2 hijos entonces busque una hoja
                    //del subarbol derecho 
                    Object value =getLeaf(node.right);
                    node.data = value;
                    node.right = removeLeaf(node.right , value);
                    
                }
            }
            node.left = remove(node.left, element);//Llamado recursivo izquierda
            node.right = remove(node.right, element);
        }
        return node;
    }

    private BTreeNode removeLeaf(BTreeNode node, Object value) {
        if(node==null) return null;
        else if(node.left==null&&node.right==null && util.Utility.compare(node.data, value)==0) return null;
        else{
            node.left = removeLeaf(node.left, value);
            node.right = removeLeaf(node.right, value);
        }
        return node;
        //Akil
    }

    private Object getLeaf(BTreeNode node) {
        Object aux;
        if(node==null) return null;
        else if (node.left==null&&node.right==null) return node.data;
           else{
               aux = getLeaf(node.left);
               if(aux==null) aux = getLeaf(node.right);

        }
        return aux;
    }



    @Override
    public int height(Object element) throws TreeException {
        return 0;
    }

    @Override
    public int height() throws TreeException {
        return 0;
    }

    @Override
    public Object min() throws TreeException {
        return null;
    }

    @Override
    public Object max() throws TreeException {
        return null;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            //result = node.data+" ";
            result  = node.data+"("+node.path+")"+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }

    @Override
    public String toString() {
        String result="Binary Tree Content:";
        try {
            result = "PreOrder: "+preOrder();
            result+= "\nInOrder: "+inOrder();
            result+= "\nPostOrder: "+postOrder();

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String printLeaves() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return printLeaves(root);
    }
    private String printLeaves(BTreeNode node){
        if(node==null) return "";
        else{

        }
        return ""; //corregir para el retorno correcto
    }

    public String printNodes1Child() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return printNodes1Child(root);
    }
    private String printNodes1Child(BTreeNode node) {
        if (node == null)
            return "";
        else {

        }
        return "";
    }

    public String printNodes2Children() throws TreeException {
        if(isEmpty())
            throw new TreeException("Binary Tree is empty");
        return printNodes2Children(root);
    }
    private String printNodes2Children(BTreeNode node) {
        if (node == null)
            return "";
        else {

        }
        return ""; //corregir para el retorno correcto
    }
}
