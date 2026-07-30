package org.springgen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class GenerateDomain {

    private void createService(Path domainPath, String packageName, String className)
            throws IOException {
        String content = """
                package %s;
                
                import org.springframework.stereotype.Service;
                
                @Service
                public class %sService {
                }
                """.formatted(packageName, className);

        Files.writeString(
                domainPath.resolve(className + "Service.java"),
                content
        );
    }

    private void createController(Path domainPath, String packageName, String className)
            throws IOException {
        String content = """
                package %s;
                
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class %sController {
                }
                """.formatted(packageName, className);

        Files.writeString(
                domainPath.resolve(className + "Controller.java"),
                content
        );
    }

    private void createEntity(Path domainPath, String packageName, String className)
            throws IOException {
        String content = """
                package %s;
                
                import jakarta.persistence.Entity;
                import jakarta.persistence.Id;
                
                @Entity
                public class %s {
                
                    @Id
                    private Long id;
                }
                """.formatted(packageName, className);

        Files.writeString(
                domainPath.resolve(className + ".java"),
                content
        );
    }

    private void createRepository(
            Path domainPath,
            String packageName,
            String className
    ) throws IOException {
        String content = """
                package %s;
                
                import org.springframework.data.jpa.repository.JpaRepository;
                
                public interface %sRepository extends JpaRepository<%s, Long> {
                }
                """.formatted(
                packageName,
                className,
                className
        );

        Files.writeString(
                domainPath.resolve(className + "Repository.java"),
                content
        );
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        return Character.toUpperCase(name.charAt(0))
                + name.substring(1);
    }

    private String resolvePackageName(String domainName) {
        String currentPackage = this.getClass().getPackageName();
        if (currentPackage != null && !currentPackage.isBlank()) {
            return currentPackage + "." + domainName.toLowerCase(Locale.ROOT);
        }

        return "org.example." + domainName.toLowerCase(Locale.ROOT);
    }

    public void generateDomain(String domainName) {
        String className = capitalize(domainName);

        Path domainPath = Path.of(domainName);
        String packageName = resolvePackageName(domainName);

        try {
            Files.createDirectories(domainPath);

            createEntity(domainPath, packageName, className);
            createRepository(domainPath, packageName, className);
            createService(domainPath, packageName, className);
            createController(domainPath, packageName, className);

        } catch (IOException e) {
            System.err.println("Failed to generate domain: " + domainName);
            e.printStackTrace();
        }
    }
}