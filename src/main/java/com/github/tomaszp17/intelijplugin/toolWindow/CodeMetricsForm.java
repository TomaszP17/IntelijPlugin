package com.github.tomaszp17.intelijplugin.toolWindow;

import com.github.tomaszp17.intelijplugin.services.ProjectAnalyzer;
import com.intellij.openapi.project.Project;

import javax.swing.*;

public class CodeMetricsForm {
    private JPanel panel1;
    private JLabel upperLabel;
    private JPanel centerPanel;
    private JLabel allLinesLabel;
    private JLabel commentsLinesLabel;
    private JLabel methodsNumberLabel;
    private JLabel codeLinesLabel;
    private JLabel allLinesResultLabel;
    private JLabel commentsLinesResultLabel;
    private JLabel methodsNumberResultLabel;
    private JLabel codeLinesResultLabel;
    private JButton analizeButton;

    private final ProjectAnalyzer analyzer;

    public CodeMetricsForm(Project project) {
        this.analyzer = new ProjectAnalyzer(project);
        analizeButton.addActionListener(e -> analyzeMetrics());
    }

    public JPanel getPanel1() {
        return panel1;
    }

    private void analyzeMetrics() {
        int totalLines = analyzer.countTotalLines();
        int commentLines = analyzer.countTotalComments();
        int methodsCount = analyzer.countTotalMethods();
        int codeLines = totalLines - commentLines;

        allLinesResultLabel.setText(String.valueOf(totalLines));
        commentsLinesResultLabel.setText(String.valueOf(commentLines));
        methodsNumberResultLabel.setText(String.valueOf(methodsCount));
        codeLinesResultLabel.setText(String.valueOf(codeLines));
    }
}
