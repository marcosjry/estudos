package br.com.gubee.interview.adapters;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class DependencyRulesTests {

    private JavaClasses javaClasses;

    @BeforeEach
    void setUp() {
        javaClasses = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("br.com.gubee.interview");
    }

    @Test
    void domainShouldNotDependOnOtherLayers() {
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("br.com.gubee.interview.domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage(
                        "br.com.gubee.interview.application..",
                        "br.com.gubee.interview.adapters..");

        rule.check(javaClasses);
    }

    @Test
    void applicationShouldOnlyDependOnDomain() {
        ArchRule rule = classes()
                .that()
                .resideInAPackage("br.com.gubee.interview.application..")
                .should()
                .onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "br.com.gubee.interview.application..",
                        "br.com.gubee.interview.domain..",
                        "java..",
                        "jakarta..",
                        "org.springframework.stereotype.."
                );

        rule.check(javaClasses);
    }
}
