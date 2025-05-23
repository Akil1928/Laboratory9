package controller;

import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class TourController {

    @FXML
    private Button btnInOrder;

    @FXML
    private Button btnPostOrder;

    @FXML
    private Button btnPreOrder;

    @FXML
    private Button btnRandomize;

    @FXML
    private Label lblSubTitle;

    @FXML
    private Label lblTourTitle;

    @FXML
    private Pane treePane;

    private BTree bTree;
    private SequentialTransition animation;
    private List<BTreeNode> nodesList;
    private static final int ANIMATION_DURATION = 1000; // milliseconds

    @FXML
    public void initialize() {
        bTree = new BTree();
        nodesList = new ArrayList<>();

        // Configurar acciones de los botones
        btnRandomize.setOnAction(event -> randomizeTree());
        btnPreOrder.setOnAction(event -> animatePreOrderTour());
        btnInOrder.setOnAction(event -> animateInOrderTour());
        btnPostOrder.setOnAction(event -> animatePostOrderTour());

        // Inicializar con un subtítulo para PreOrder
        lblSubTitle.setText("Pre Order Transversal Tour (Nodo-Izquierdo-Derecho)");
    }

    /**
     * Crea un árbol binario aleatorio y lo muestra en el treePane
     */
    private void randomizeTree() {
        // Detener cualquier animación en curso
        if (animation != null && animation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
            animation.stop();
        }

        // Limpiar el árbol y el panel
        bTree.clear();
        treePane.getChildren().clear();
        nodesList.clear();

        // Generar un número aleatorio de nodos (entre 5 y 10 para mejor visualización)
        int numNodes = (int)(Math.random() * 6) + 5;

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
     * Anima el recorrido PreOrder (Nodo-Izquierdo-Derecho)
     */
    private void animatePreOrderTour() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "Por favor, primero crea un árbol utilizando el botón 'Randomize'.");
                return;
            }

            // Detener cualquier animación en curso
            if (animation != null && animation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                animation.stop();
            }

            // Actualizar el subtítulo
            lblSubTitle.setText("Pre Order Transversal Tour (Nodo-Izquierdo-Derecho)");

            // Redibujar el árbol para eliminar cualquier resaltado anterior
            drawTree();

            // Preparar la animación
            animation = new SequentialTransition();

            // Obtener la secuencia de nodos en PreOrder
            String preOrderStr = bTree.preOrder();
            String[] nodes = preOrderStr.split(" ");

            // Crear animación para cada nodo en la secuencia
            for (String nodeStr : nodes) {
                if (nodeStr.trim().isEmpty()) continue;

                // Extraer el valor y la ruta del nodo
                String valueStr = nodeStr;
                String path = "root";

                if (nodeStr.contains("(") && nodeStr.contains(")")) {
                    valueStr = nodeStr.substring(0, nodeStr.indexOf("("));
                    path = nodeStr.substring(nodeStr.indexOf("(") + 1, nodeStr.indexOf(")"));
                }

                int nodeValue = Integer.parseInt(valueStr);

                // Crear una pausa para este nodo
                PauseTransition pause = new PauseTransition(Duration.millis(ANIMATION_DURATION));
                int finalNodeValue = nodeValue;
                String finalPath = path;

                pause.setOnFinished(event -> highlightNode(finalNodeValue, finalPath, Color.RED));
                animation.getChildren().add(pause);
            }

            // Iniciar la animación
            animation.play();

        } catch (TreeException e) {
            showAlert("Error", "Error al animar recorrido PreOrder: " + e.getMessage());
        }
    }

    /**
     * Anima el recorrido InOrder (Izquierdo-Nodo-Derecho)
     */
    private void animateInOrderTour() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "Por favor, primero crea un árbol utilizando el botón 'Randomize'.");
                return;
            }

            // Detener cualquier animación en curso
            if (animation != null && animation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                animation.stop();
            }

            // Actualizar el subtítulo
            lblSubTitle.setText("In Order Transversal Tour (Izquierdo-Nodo-Derecho)");

            // Redibujar el árbol para eliminar cualquier resaltado anterior
            drawTree();

            // Preparar la animación
            animation = new SequentialTransition();

            // Obtener la secuencia de nodos en InOrder
            String inOrderStr = bTree.inOrder();
            String[] nodes = inOrderStr.split(" ");

            // Mapear los valores de InOrder a sus correspondientes nodos en PreOrder
            String preOrderStr = bTree.preOrder();
            String[] preOrderNodes = preOrderStr.split(" ");

            for (String nodeStr : nodes) {
                if (nodeStr.trim().isEmpty()) continue;

                // Extraer el valor del nodo InOrder
                int nodeValue = Integer.parseInt(nodeStr.trim());

                // Buscar la ruta de este nodo en el PreOrder
                String path = "root";
                for (String preOrderNode : preOrderNodes) {
                    if (preOrderNode.contains("(") && preOrderNode.contains(")")) {
                        String valueStr = preOrderNode.substring(0, preOrderNode.indexOf("("));
                        if (Integer.parseInt(valueStr) == nodeValue) {
                            path = preOrderNode.substring(preOrderNode.indexOf("(") + 1, preOrderNode.indexOf(")"));
                            break;
                        }
                    }
                }

                // Crear una pausa para este nodo
                PauseTransition pause = new PauseTransition(Duration.millis(ANIMATION_DURATION));
                String finalPath = path;

                pause.setOnFinished(event -> highlightNode(nodeValue, finalPath, Color.GREEN));
                animation.getChildren().add(pause);
            }

            // Iniciar la animación
            animation.play();

        } catch (TreeException e) {
            showAlert("Error", "Error al animar recorrido InOrder: " + e.getMessage());
        }
    }

    /**
     * Anima el recorrido PostOrder (Izquierdo-Derecho-Nodo)
     */
    private void animatePostOrderTour() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "Por favor, primero crea un árbol utilizando el botón 'Randomize'.");
                return;
            }

            // Detener cualquier animación en curso
            if (animation != null && animation.getStatus() == javafx.animation.Animation.Status.RUNNING) {
                animation.stop();
            }

            // Actualizar el subtítulo
            lblSubTitle.setText("Post Order Transversal Tour (Izquierdo-Derecho-Nodo)");

            // Redibujar el árbol para eliminar cualquier resaltado anterior
            drawTree();

            // Preparar la animación
            animation = new SequentialTransition();

            // Obtener la secuencia de nodos en PostOrder
            String postOrderStr = bTree.postOrder();
            String[] nodes = postOrderStr.split(" ");

            // Mapear los valores de PostOrder a sus correspondientes nodos en PreOrder
            String preOrderStr = bTree.preOrder();
            String[] preOrderNodes = preOrderStr.split(" ");

            for (String nodeStr : nodes) {
                if (nodeStr.trim().isEmpty()) continue;

                // Extraer el valor del nodo PostOrder
                int nodeValue = Integer.parseInt(nodeStr.trim());

                // Buscar la ruta de este nodo en el PreOrder
                String path = "root";
                for (String preOrderNode : preOrderNodes) {
                    if (preOrderNode.contains("(") && preOrderNode.contains(")")) {
                        String valueStr = preOrderNode.substring(0, preOrderNode.indexOf("("));
                        if (Integer.parseInt(valueStr) == nodeValue) {
                            path = preOrderNode.substring(preOrderNode.indexOf("(") + 1, preOrderNode.indexOf(")"));
                            break;
                        }
                    }
                }

                // Crear una pausa para este nodo
                PauseTransition pause = new PauseTransition(Duration.millis(ANIMATION_DURATION));
                String finalPath = path;

                pause.setOnFinished(event -> highlightNode(nodeValue, finalPath, Color.BLUE));
                animation.getChildren().add(pause);
            }

            // Iniciar la animación
            animation.play();

        } catch (TreeException e) {
            showAlert("Error", "Error al animar recorrido PostOrder: " + e.getMessage());
        }
    }

    /**
     * Resalta un nodo específico en el árbol con el color especificado
     */
    private void highlightNode(int nodeValue, String path, Color color) {
        try {
            // Calcula la posición del nodo
            double[] position = calculateNodePosition(path);

            // Agrega un contador de secuencia al nodo
            int sequenceNumber = countHighlightedNodes() + 1;
            Text sequenceText = new Text(position[0] + 25, position[1] - 15, String.valueOf(sequenceNumber));
            sequenceText.setFill(color);
            sequenceText.setFont(Font.font("Arial", 14));
            treePane.getChildren().add(sequenceText);

            // Dibuja un círculo resaltado
            Circle highlightCircle = new Circle(position[0], position[1], 23, Color.TRANSPARENT);
            highlightCircle.setStroke(color);
            highlightCircle.setStrokeWidth(3);
            treePane.getChildren().add(highlightCircle);

            // Resalta el valor del nodo
            Text valueText = new Text(position[0] - 8, position[1] + 30, String.valueOf(nodeValue));
            valueText.setFill(color);
            valueText.setFont(Font.font("Arial", 12));
            treePane.getChildren().add(valueText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cuenta el número de nodos resaltados actualmente
     */
    private int countHighlightedNodes() {
        int count = 0;
        for (javafx.scene.Node node : treePane.getChildren()) {
            if (node instanceof Circle && ((Circle) node).getStroke() != Color.BLUE) {
                count++;
            }
        }
        return count / 2; // Dividir por 2 porque cada nodo tiene dos círculos (uno para el nodo y otro para el resaltado)
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