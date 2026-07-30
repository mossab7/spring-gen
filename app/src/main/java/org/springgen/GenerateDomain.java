package org.springgen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class GenerateDomain {

    private void createService(Path domainPath, String packageName, String className)
            throws IOException {
        String servicePackageName = packageName + ".service";
        Path servicePath = domainPath.resolve("service");
        Files.createDirectories(servicePath);
        String content = """
                package %s;
                
                import org.springframework.stereotype.Service;
                
                @Service
                public class %sService {
                }
                """.formatted(servicePackageName, className);

        Files.writeString(
                servicePath.resolve(className + "Service.java"),
                content
        );
    }

    private void createController(Path domainPath, String packageName, String className)
            throws IOException {
        String controllerPackageName = packageName + ".controller";
        Path controllerPath = domainPath.resolve("controller");
        Files.createDirectories(controllerPath);
        String content = """
                package %s;
                
                import org.springframework.web.bind.annotation.RestController;
                
                @RestController
                public class %sController {
                }
                """.formatted(controllerPackageName, className);

        Files.writeString(
                controllerPath.resolve(className + "Controller.java"),
                content
        );
    }

    private void createEntity(Path domainPath, String packageName, String className)
            throws IOException {
        String entityPackageName = packageName + ".entity";
        Path entityPath = domainPath.resolve("entity");
        Files.createDirectories(entityPath);
        String content = """
                package %s;
                
                import jakarta.persistence.Entity;
                import jakarta.persistence.Id;
                
                @Entity
                public class %s {
                
                    @Id
                    private Long id;
                }
                """.formatted(entityPackageName, className);

        Files.writeString(
                entityPath.resolve(className + ".java"),
                content
        );
    }

    private void createRepository(
            Path domainPath,
            String packageName,
            String className
    ) throws IOException {
        String repositoryPackageName = packageName + ".repository";
        String EntityPath = packageName + ".entity." + className;
        Path repositoryPath = domainPath.resolve("repository");
        Files.createDirectories(repositoryPath);
        String content = """
                package %s;
                
                import org.springframework.data.jpa.repository.JpaRepository;

                import %s;
                
                public interface %sRepository extends JpaRepository<%s, Long> {
                }
                """.formatted(
                repositoryPackageName,
                EntityPath,
                className,
                className
        );
        Files.writeString(
                repositoryPath.resolve(className + "Repository.java"),
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
            String currentDir = System.getProperty("user.dir");
            String packageName = currentDir.replace("\\", ".").replace("/", ".");
            int srcIndex = packageName.indexOf("src.main.java.");
            if (srcIndex != -1) {
                packageName = packageName.substring(srcIndex + "src.main.java.".length());
                return packageName + "." + domainName.toLowerCase(Locale.ROOT);
            }
            return  "package.name." + domainName.toLowerCase(Locale.ROOT);
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