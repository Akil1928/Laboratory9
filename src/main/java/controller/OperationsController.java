
package controller;

import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.Random;

public class OperationsController {

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnContains;

    @FXML
    private Button btnNodeHeight;

    @FXML
    private Button btnRandomize;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnTreeHeight;

    @FXML
    private Pane treePane;

    private BTree bTree;

    @FXML
    public void initialize() {
        bTree = new BTree();

        // Configurar acciones de los botones
        btnRandomize.setOnAction(event -> randomizeTree());
        btnRemove.setOnAction(event -> removeRandomNode());
        btnContains.setOnAction(event -> checkContains());
        btnTreeHeight.setOnAction(event -> showTreeHeight());
        btnNodeHeight.setOnAction(event -> showNodeHeight());
        btnAdd.setOnAction(event -> addNode());
    }

    /**
     * Crea un árbol binario aleatorio y lo muestra en el treePane
     */
    private void randomizeTree() {
        // Limpiar el árbol y el panel
        bTree.clear();
        treePane.getChildren().clear();

        // Generar un número aleatorio de nodos (entre 5 y 15)
        int numNodes = (int)(Math.random() * 11) + 5;

        // Agregar nodos aleatorios al árbol
        for (int i = 0; i < numNodes; i++) {
            int value = (int)(Math.random() * 100);
            bTree.add(value);
        }

        // Dibujar el árbol
        drawTree();

        showAlert("Árbol creado", "Se ha creado un árbol binario aleatorio con " + numNodes + " nodos.");
    }

    /**
     * Remueve un nodo aleatorio del árbol
     */
    private void removeRandomNode() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "No hay nodos para eliminar. Por favor, primero crea un árbol.");
                return;
            }

            // Obtener un nodo aleatorio del árbol
            String inOrderStr = bTree.inOrder();
            String[] nodes = inOrderStr.split(" ");

            if (nodes.length == 0) {
                showAlert("Error", "No se pudieron obtener nodos del árbol.");
                return;
            }

            // Seleccionar un nodo aleatorio
            Random random = new Random();
            int randomIndex = random.nextInt(nodes.length);
            String nodeToRemove = nodes[randomIndex].trim();

            // Eliminar el nodo
            int valueToRemove = Integer.parseInt(nodeToRemove);
            bTree.remove(valueToRemove);

            // Redibujar el árbol
            drawTree();

            showAlert("Nodo Eliminado", "Se ha eliminado el nodo con valor " + valueToRemove + " del árbol.");

        } catch (TreeException e) {
            showAlert("Error", "Error al eliminar nodo: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Error", "Error al convertir valor del nodo: " + e.getMessage());
        }
    }

    /**
     * Verifica si un valor está contenido en el árbol
     */
    private void checkContains() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "El árbol está vacío. Por favor, primero crea un árbol.");
                return;
            }

            // Solicitar valor a buscar
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Buscar Valor");
            dialog.setHeaderText("Verificar si un valor está en el árbol");
            dialog.setContentText("Ingrese el valor a buscar:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int valueToSearch = Integer.parseInt(result.get());
                    boolean contains = bTree.contains(valueToSearch);

                    if (contains) {
                        showAlert("Resultado", "El valor " + valueToSearch + " SÍ está en el árbol.");

                        // Resaltar el nodo si está presente
                        highlightNode(valueToSearch);
                    } else {
                        showAlert("Resultado", "El valor " + valueToSearch + " NO está en el árbol.");
                    }

                } catch (NumberFormatException e) {
                    showAlert("Error", "Por favor, ingrese un valor numérico válido.");
                }
            }

        } catch (TreeException e) {
            showAlert("Error", "Error al verificar contenido: " + e.getMessage());
        }
    }

    /**
     * Muestra la altura del árbol
     */
    private void showTreeHeight() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "El árbol está vacío. Por favor, primero crea un árbol.");
                return;
            }

            // Calcular altura del árbol
            int height = calculateTreeHeight();

            showAlert("Altura del Árbol", "La altura del árbol es: " + height);

        } catch (Exception e) {
            showAlert("Error", "Error al calcular altura del árbol: " + e.getMessage());
        }
    }

    /**
     * Muestra la altura de un nodo específico
     */
    private void showNodeHeight() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "El árbol está vacío. Por favor, primero crea un árbol.");
                return;
            }

            // Solicitar valor del nodo
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Altura de Nodo");
            dialog.setHeaderText("Calcular altura de un nodo específico");
            dialog.setContentText("Ingrese el valor del nodo:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                try {
                    int nodeValue = Integer.parseInt(result.get());

                    // Verificar si el nodo existe
                    if (!bTree.contains(nodeValue)) {
                        showAlert("Error", "El valor " + nodeValue + " no existe en el árbol.");
                        return;
                    }

                    // Calcular altura del nodo (distancia desde la raíz)
                    int nodeHeight = calculateNodeHeight(nodeValue);

                    showAlert("Altura del Nodo", "La altura del nodo " + nodeValue + " es: " + nodeHeight);

                    // Resaltar el nodo
                    highlightNode(nodeValue);

                } catch (NumberFormatException e) {
                    showAlert("Error", "Por favor, ingrese un valor numérico válido.");
                }
            }

        } catch (TreeException e) {
            showAlert("Error", "Error al calcular altura del nodo: " + e.getMessage());
        }
    }

    /**
     * Agrega un nuevo nodo al árbol
     */
    private void addNode() {
        // Solicitar valor a agregar
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Nodo");
        dialog.setHeaderText("Agregar un nuevo nodo al árbol");
        dialog.setContentText("Ingrese el valor del nuevo nodo:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int newValue = Integer.parseInt(result.get());

                // Verificar si el valor ya existe
                try {
                    if (!bTree.isEmpty() && bTree.contains(newValue)) {
                        showAlert("Valor Duplicado", "El valor " + newValue + " ya existe en el árbol.");
                        return;
                    }
                } catch (TreeException e) {
                    // Si hay un error al verificar, continuamos con la adición
                }

                // Agregar el nuevo valor
                bTree.add(newValue);

                // Redibujar el árbol
                drawTree();

                showAlert("Nodo Agregado", "Se ha agregado el nodo con valor " + newValue + " al árbol.");

                // Resaltar el nuevo nodo
                highlightNode(newValue);

            } catch (NumberFormatException e) {
                showAlert("Error", "Por favor, ingrese un valor numérico válido.");
            }
        }
    }

    /**
     * Calcula la altura del árbol
     */
    private int calculateTreeHeight() {
        try {
            if (bTree.isEmpty()) {
                return -1;
            }

            // Calcular altura basado en los niveles del árbol
            return findMaxLevel();

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Encuentra el nivel máximo en el árbol
     */
    private int findMaxLevel() {
        try {
            String preOrderStr = bTree.preOrder();
            String[] nodes = preOrderStr.split(" ");
            int maxLevel = 0;

            for (String nodeStr : nodes) {
                // Extraer la ruta entre paréntesis
                if (nodeStr.contains("(") && nodeStr.contains(")")) {
                    String path = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));
                    int level = path.split("/").length - 1;
                    maxLevel = Math.max(maxLevel, level);
                }
            }

            return maxLevel;
        } catch (TreeException e) {
            return 0;
        }
    }

    /**
     * Calcula la altura (nivel) de un nodo específico
     */
    private int calculateNodeHeight(int nodeValue) throws TreeException {
        String preOrderStr = bTree.preOrder();
        String[] nodes = preOrderStr.split(" ");

        for (String nodeStr : nodes) {
            if (nodeStr.contains("(") && nodeStr.contains(")")) {
                String valueStr = nodeStr.substring(0, nodeStr.indexOf("("));
                if (Integer.parseInt(valueStr) == nodeValue) {
                    String path = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));
                    return path.split("/").length - 1;
                }
            }
        }

        return -1; // Nodo no encontrado
    }

    /**
     * Resalta un nodo específico en el árbol
     */
    private void highlightNode(int nodeValue) {
        try {
            // Redibuja el árbol
            drawTree();

            // Busca el nodo con el valor especificado
            String preOrderStr = bTree.preOrder();
            String[] nodes = preOrderStr.split(" ");

            for (String nodeStr : nodes) {
                if (nodeStr.contains("(") && nodeStr.contains(")")) {
                    String valueStr = nodeStr.substring(0, nodeStr.indexOf("("));
                    if (Integer.parseInt(valueStr) == nodeValue) {
                        String path = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));

                        // Calcula la posición del nodo
                        double[] position = calculateNodePosition(path);

                        // Dibuja un círculo resaltado
                        Circle highlightCircle = new Circle(position[0], position[1], 23, Color.TRANSPARENT);
                        highlightCircle.setStroke(Color.RED);
                        highlightCircle.setStrokeWidth(3);
                        treePane.getChildren().add(highlightCircle);

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calcula la posición de un nodo basado en su ruta
     */
    private double[] calculateNodePosition(String path) {
        double x = treePane.getWidth() / 2;
        double y = 50;
        double hGap = treePane.getWidth() / 4;

        String[] parts = path.split("/");
        for (int i = 1; i < parts.length; i++) {
            hGap /= 2;
            if ("left".equals(parts[i])) {
                x -= hGap;
            } else if ("right".equals(parts[i])) {
                x += hGap;
            }
            y += 70;
        }

        return new double[]{x, y};
    }

    /**
     * Dibuja el árbol binario en el panel
     */
    private void drawTree() {
        // Limpiar el panel
        treePane.getChildren().clear();

        try {
            if (!bTree.isEmpty()) {
                // Dibuja el árbol recursivamente
                drawNode(getRootNode(), treePane.getWidth() / 2, 50, treePane.getWidth() / 4);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al dibujar el árbol: " + e.getMessage());
        }
    }

    /**
     * Obtiene el nodo raíz del árbol
     */
    private BTreeNode getRootNode() {
        try {
            if (bTree.isEmpty()) {
                return null;
            }

            // Utilizamos el recorrido preOrder para obtener información sobre la estructura
            String preOrderStr = bTree.preOrder();
            String[] nodes = preOrderStr.split(" ");

            if (nodes.length > 0) {
                String rootStr = nodes[0];
                Object data = extractData(rootStr);
                String path = extractPath(rootStr);

                return new BTreeNode(data, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Extrae el valor de datos de la cadena de nodo
     */
    private Object extractData(String nodeStr) {
        if (nodeStr.contains("(")) {
            return Integer.parseInt(nodeStr.substring(0, nodeStr.indexOf("(")));
        }
        return Integer.parseInt(nodeStr);
    }

    /**
     * Extrae la ruta de la cadena de nodo
     */
    private String extractPath(String nodeStr) {
        if (nodeStr.contains("(") && nodeStr.contains(")")) {
            return nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));
        }
        return "root";
    }

    /**
     * Dibuja un nodo y sus hijos recursivamente
     */
    private void drawNode(BTreeNode node, double x, double y, double hGap) {
        if (node == null) return;

        // Dibujar el círculo del nodo
        Circle circle = new Circle(x, y, 20, Color.LIGHTBLUE);
        circle.setStroke(Color.BLUE);
        treePane.getChildren().add(circle);

        // Dibujar el texto del valor
        Text text = new Text(x - 10, y + 5, node.data.toString());
        text.setFont(Font.font(14));
        treePane.getChildren().add(text);

        // Procesar hijos basados en la ruta del nodo
        String path = node.path;

        // Buscar hijos en el preOrder
        try {
            String preOrderStr = bTree.preOrder();
            String[] nodes = preOrderStr.split(" ");

            BTreeNode leftChild = null;
            BTreeNode rightChild = null;

            // Buscar nodos hijo
            for (String nodeStr : nodes) {
                if (nodeStr.contains("(") && nodeStr.contains(")")) {
                    String nodePath = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));

                    if (nodePath.equals(path + "/left")) {
                        Object data = extractData(nodeStr);
                        leftChild = new BTreeNode(data, nodePath);
                    } else if (nodePath.equals(path + "/right")) {
                        Object data = extractData(nodeStr);
                        rightChild = new BTreeNode(data, nodePath);
                    }
                }
            }

            // Calcular posiciones para los hijos
            double nextY = y + 70;

            // Dibujar hijo izquierdo
            if (leftChild != null) {
                double leftX = x - hGap;
                Line line = new Line(x, y + 20, leftX, nextY - 20);
                line.setStroke(Color.BLACK);
                treePane.getChildren().add(line);

                drawNode(leftChild, leftX, nextY, hGap / 2);
            }

            // Dibujar hijo derecho
            if (rightChild != null) {
                double rightX = x + hGap;
                Line line = new Line(x, y + 20, rightX, nextY - 20);
                line.setStroke(Color.BLACK);
                treePane.getChildren().add(line);

                drawNode(rightChild, rightX, nextY, hGap / 2);
            }

        } catch (TreeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta con el mensaje especificado
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}