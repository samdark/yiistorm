package com.yiistorm;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import com.yiistorm.ReferenceProviders.ControllerReferenceProvider;
import com.yiistorm.ReferenceProviders.ModelReferenceProvider;
import com.yiistorm.helpers.YiiHelper;
import org.jetbrains.annotations.NotNull;

public class YiiPsiReferenceProvider extends PsiReferenceProvider {

    public static final PsiReferenceProvider[] EMPTY_ARRAY = new PsiReferenceProvider[0];
    public static String projectPath;
    public static Project project;

    public YiiPsiReferenceProvider() {
    }

    /**
     * Return reference or empty array
     *
     * @param element PsiElement
     * @param context ProcessingContext
     * @return
     */
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull final ProcessingContext context) {
        project = element.getProject();
        if (element.getClass().getName().endsWith("StringLiteralExpressionImpl")) {

            try {
                PsiFile file = element.getContainingFile();
                String path = file.getVirtualFile().getPath();
                projectPath = project.getBasePath().replace("\\", "/");
                int ProviderType = YiiHelper.getYiiObjectType(path,element);
                switch (ProviderType) {
                    case YiiHelper.YII_TYPE_CONTROLLER:
                        return ControllerReferenceProvider.getReference(path, element);
                    case YiiHelper.YII_TYPE_MODEL:
                        return ModelReferenceProvider.getReference(path, element);
                }
            } catch (Exception e) {
                System.err.println("error" + e.getMessage());
            }
        }
        return PsiReference.EMPTY_ARRAY;
    }


}