package com.github.tomaszp17.intelijplugin.services;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.openapi.fileTypes.FileTypeManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(Service.Level.PROJECT)
public final class ProjectAnalyzer {

    private final Project project;

    public ProjectAnalyzer(Project project) {
        this.project = project;
    }

    public int countTotalLines() {
        int totalLines = 0;
        for (PsiFile psiFile : getJavaFiles()) {
            totalLines += countLines(psiFile);
        }
        return totalLines;
    }

    public int countTotalComments() {
        int totalComments = 0;
        for (PsiFile psiFile : getJavaFiles()) {
            totalComments += countComments(psiFile);
        }
        return totalComments;
    }

    public int countTotalMethods() {
        int totalMethods = 0;
        for (PsiFile psiFile : getJavaFiles()) {
            totalMethods += countMethods(psiFile);
        }
        return totalMethods;
    }

    private Collection<PsiFile> getJavaFiles() {
        Collection<VirtualFile> files = FileTypeIndex.getFiles(
                FileTypeManager.getInstance().getFileTypeByExtension("java"),
                GlobalSearchScope.projectScope(project)
        );
        PsiManager psiManager = PsiManager.getInstance(project);
        return files.stream().map(psiManager::findFile).toList();
    }

    private int countLines(PsiFile psiFile) {
        String[] lines = psiFile.getText().split("\n");
        return lines.length;
    }

    private int countComments(PsiFile psiFile) {
        final int[] commentCount = {0};
        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof PsiComment) {
                    commentCount[0]++;
                }
                super.visitElement(element);
            }
        });
        return commentCount[0];
    }

    public int countMethods(PsiFile psiFile) {
        String text = psiFile.getText();
        int methodCount = 0;

        Pattern pattern = Pattern.compile("\\b(public|protected|private|static|void|int|String|double)\\b\\s+\\w+\\s*\\(");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            methodCount++;
        }

        return methodCount;
    }

}
