package controller;

import domain.BTree;
import domain.BTreeNode;
import domain.TreeException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class GraphicController {

    @FXML
    private Button btnLevels;

    @FXML
    private Button btnRandomize;

    @FXML
    private Button btnTourInfo;

    @FXML
    private Pane treePane;

    private BTree bTree;
    private Map<Integer, Line> levelLines;
    private boolean showLevels = false;

    @FXML
    public void initialize() {
        bTree = new BTree();
        levelLines = new HashMap<>();

        btnRandomize.setOnAction(event -> randomizeTree());
        btnTourInfo.setOnAction(event -> showTourInfo());
        btnLevels.setOnAction(event -> toggleLevels());
    }

    /**
     * Crea un árbol binario aleatorio y lo muestra en el treePane
     */
    private void randomizeTree() {
        // Limpiar el árbol y el panel
        bTree.clear();
        treePane.getChildren().clear();
        levelLines.clear();

        // Generar un número aleatorio de nodos (entre 5 y 15)
        int numNodes = (int)(Math.random() * 11) + 5;

        // Agregar nodos aleatorios al árbol
        for (int i = 0; i < numNodes; i++) {
            int value = (int)(Math.random() * 100);
            bTree.add(value);
        }

        // Dibujar el árbol
        drawTree();

        // Si los niveles estaban activos, volver a mostrarlos
        if (showLevels) {
            drawLevelLines();
        }
    }

    /**
     * Muestra información sobre los recorridos del árbol en una ventana de alerta
     */
    private void showTourInfo() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "Por favor, primero crea un árbol utilizando el botón 'Randomize'.");
                return;
            }

            StringBuilder info = new StringBuilder();
            info.append("Pre-Order Tour: ").append(bTree.preOrder()).append("\n\n");
            info.append("In-Order Tour: ").append(bTree.inOrder()).append("\n\n");
            info.append("Post-Order Tour: ").append(bTree.postOrder());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información de Recorridos");
            alert.setHeaderText("Recorridos del Árbol Binario");

            TextArea textArea = new TextArea(info.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefHeight(200);

            alert.getDialogPane().setContent(textArea);
            alert.showAndWait();

        } catch (TreeException e) {
            showAlert("Error", "Error al obtener información de recorridos: " + e.getMessage());
        }
    }

    /**
     * Alterna la visualización de las líneas de nivel en el árbol
     */
    private void toggleLevels() {
        try {
            if (bTree.isEmpty()) {
                showAlert("Árbol Vacío", "Por favor, primero crea un árbol utilizando el botón 'Randomize'.");
                return;
            }

            showLevels = !showLevels;

            if (showLevels) {
                drawLevelLines();
                btnLevels.setText("Ocultar Niveles");
            } else {
                // Eliminar líneas de nivel
                for (Line line : levelLines.values()) {
                    treePane.getChildren().remove(line);
                }
                levelLines.clear();
                btnLevels.setText("Mostrar Niveles");
            }
        } catch (Exception e) {
            showAlert("Error", "Error al mostrar/ocultar niveles: " + e.getMessage());
        }
    }

    /**
     * Dibuja las líneas horizontales para cada nivel del árbol
     */
    private void drawLevelLines() {
        // Limpiar líneas anteriores
        for (Line line : levelLines.values()) {
            treePane.getChildren().remove(line);
        }
        levelLines.clear();

        // Obtener altura del árbol
        int height;
        try {
            height = calculateTreeHeight(bTree);

            // Calcular espaciado vertical
            double paneHeight = treePane.getHeight();
            double levelHeight = paneHeight / (height + 1);

            // Dibujar línea para cada nivel
            for (int i = 0; i <= height; i++) {
                double y = levelHeight * (i + 1);
                Line line = new Line(10, y, treePane.getWidth() - 10, y);
                line.setStroke(Color.GRAY);
                line.setStrokeWidth(1);
                line.getStrokeDashArray().addAll(5.0, 5.0);

                treePane.getChildren().add(line);
                levelLines.put(i, line);

                // Agregar etiqueta de nivel
                Text levelText = new Text("Nivel " + i);
                levelText.setX(15);
                levelText.setY(y - 5);
                levelText.setFill(Color.DARKBLUE);
                treePane.getChildren().add(levelText);
            }
        } catch (Exception e) {
            showAlert("Error", "Error al calcular la altura del árbol: " + e.getMessage());
        }
    }

    /**
     * Calcula la altura del árbol binario
     */
    private int calculateTreeHeight(BTree tree) {
        try {
            if (tree.isEmpty()) {
                return -1;
            }
            // Si el método height() estuviera implementado en BTree, usaríamos:
            // return tree.height();

            // Como alternativa, podemos calcular la altura basándonos en los niveles visibles
            return findMaxLevel(tree);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Encuentra el nivel máximo en el árbol basado en las rutas de los nodos
     */
    private int findMaxLevel(BTree tree) {
        try {
            String preOrderStr = tree.preOrder();
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
     * Este método es una aproximación ya que no tenemos acceso directo al nodo raíz
     */
    private BTreeNode getRootNode() {
        // Esta es una solución aproximada ya que no tenemos acceso directo a la raíz
        // Idealmente, se modificaría la clase BTree para exponer la raíz o añadir un método para ello
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