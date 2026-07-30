package org.springgen;

public class SpringGenApplication {
    GenerateDomain generator = new GenerateDomain();

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide a domain name.");
            return;
        }
        SpringGenApplication app = new SpringGenApplication();
        app.generator.generateDomain(args[0]);
    }
}
