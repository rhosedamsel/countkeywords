import java.io.*;
import java.util.*;

public class CountKeywords {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CountKeywords <Java_file_name>");
            return;
        }

        String filename = args[0];
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " does not exist");
            return;
        }

        try {
            int keywordCount = countKeywords(file);
            System.out.println("The number of keywords in " + filename + " is " + keywordCount);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static int countKeywords(File file) throws IOException {
        String[] keywords = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
            "const", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
            "for", "if", "goto", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
            "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
            "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "true",
            "false", "null"};

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        int count = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean inStringOrComment = false;

            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+"); // Split line into words by spaces
                for (String word : words) {
                    if (!inStringOrComment) {
                        if (word.startsWith("//")) {
                            break; // Ignore the rest of the line if it's a comment
                        } else if (word.startsWith("/*")) {
                            inStringOrComment = true; // Start of paragraph comment
                            if (word.endsWith("*/")) {
                                inStringOrComment = false; // End of paragraph comment on the same line
                            }
                        } else if (word.startsWith("\"")) {
                            if (!word.endsWith("\"")) {
                                inStringOrComment = true; // Start of string
                            }
                        }

                        if (word.endsWith("*/")) {
                            inStringOrComment = false; // End of paragraph comment
                        }

                        if (!inStringOrComment && keywordSet.contains(word)) {
                            count++;
                        }
                    } else {
                        if (word.endsWith("*/")) {
                            inStringOrComment = false; // End of paragraph comment
                        }
                    }
                }
            }
        }

        return count;
    }
}
