import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class HashTable {

    public class Iterator {
        private Node nextNode;
        private int nextIndex;

        public Iterator() {
            nextNode = null;
            nextIndex = 0;
            findNext();
        }

        private void findNext() {
            // If we are currently in a chain, move to the next node in that chain
            if (nextNode != null && nextNode.getNext() != null) {
                nextNode = nextNode.getNext();
                return;
            }

            // Otherwise, scan the table for the next non-empty bucket
            while (nextIndex < table.length) {
                if (table[nextIndex] != null) {
                    nextNode = table[nextIndex];
                    nextIndex++; // Advance index so next search starts at the following bucket
                    return;
                }
                nextIndex++;
            }
            nextNode = null;
        }

        public Optional<Node> next() {
            if (nextNode == null) {
                return Optional.empty();
            }
            Node current = nextNode;
            findNext();
            return Optional.of(current);
        }

        public boolean hasNext() {
            return nextNode != null;
        }

        public void printAll() {
            while (hasNext()) {
                next().ifPresent(node -> System.out.println(node.value));
            }
        }
    }

    private Node[] table = new Node[100];
    private int amountUsed = 0;
    private int totalLength = table.length;

    /**
     * Loads this HashTable from a file named "Lookup.dat".
     */
    public void load() {
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            fileReader = new FileReader(f);
            bufferedReader = new BufferedReader(fileReader);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find input file \"Lookup.dat\"");
            return;
        }
        
        try {
            while (true) {
                String key = bufferedReader.readLine();
                if (key == null) return;
                String value = bufferedReader.readLine();
                if (value == null) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                String blankLine = bufferedReader.readLine();
                if (blankLine != null && !"".equals(blankLine)) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                put(key);
            }
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
            }
            catch(IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * Saves this HashTable onto a file named "Lookup.dat".
     */
    public void save() {
        FileOutputStream stream;
        PrintWriter printWriter = null;
        Iterator iterator;
        
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            stream = new FileOutputStream(f);
            printWriter = new PrintWriter(stream);
        }
        catch (Exception e) {
            System.err.println("Cannot use output file \"Lookup.dat\"");
            return;
        }
       
        iterator = keys();
        while (iterator.hasNext()) {
            Node thingy = iterator.next().orElse(null);
            if (thingy != null) {
                String key = (String) thingy.value;
                printWriter.println(key);
                String value = (String)get(key);
                value = removeNewlines(value);
                printWriter.println(value);
                printWriter.println();
            } else {
                break;
            }
        }
        printWriter.close();
    }
        
    /**
     * Replaces all line separator characters (which vary from one platform
     * to the next) with spaces.
     * 
     * @param value The input string, possibly containing line separators.
     * @return The input string with line separators replaced by spaces.
     */
    private String removeNewlines(String value) {
        return value.replaceAll("\r|\n", " ");
    }

    public int calculatePossibleHash(String key) {
        int defaultHash = key.hashCode();
        return Math.abs(defaultHash) % totalLength;
    }   
    
    private void handleHashState() {
        if (amountUsed > (2.0/3.0)*totalLength) {
            int newLength = table.length * 2;
            Node[] newTable = new Node[newLength];
            Node[] tempTable = table;
            table = newTable;
            totalLength = newLength;
            amountUsed = 0; // Will be incremented back in put()

            for (int i=0; i<tempTable.length; i++) {
                if (tempTable[i] != null) {
                    Node currentNode = tempTable[i];
                    while (currentNode != null) {
                        put(currentNode.value);
                        currentNode = currentNode.getNext();
                    }
                }
            }
        }
    }

    public int put(String key) {
        int hashCode = calculatePossibleHash(key);

        if (table[hashCode] == null) {
            table[hashCode] = new Node(key);
            amountUsed++;
            handleHashState(); // Check if we need to resize
            return hashCode;
        } else {
            Node currentNode = table[hashCode];
            // Check if key already exists in this bucket chain
            while (currentNode != null) {
                if (currentNode.value.equals(key)) {
                    return hashCode; // Key already exists
                }
                if (currentNode.getNext() == null) break;
                currentNode = currentNode.getNext();
            }
            // Add new node to the end of the chain
            currentNode.setNext(new Node(key));
            amountUsed++;
            handleHashState();
            return hashCode;
        }
    }

    public String get(String key) {
        int index = calculatePossibleHash(key);

        if (table[index] != null) {
            Node current = table[index];
            while (current != null) {
                if (current.value.equals(key)) {
                    return current.value;
                }
                current = current.getNext();
            }
        }
        return "";
    }

    public String remove(String key){
        int hashCode = calculatePossibleHash(key);

        if (table[hashCode] == null) {
            return "";
        } else {
            Node currentNode = table[hashCode];
            Node prev = null;
            
            while (currentNode != null) {
                if (currentNode.value.equals(key)) {
                    if (prev == null) {
                        table[hashCode] = currentNode.getNext();
                    } else {
                        prev.setNext(currentNode.getNext());
                    }
                    amountUsed--;
                    return key;
                }
                prev = currentNode;
                currentNode = currentNode.getNext();
            }
        }
        return "";
    }

    public Iterator keys() {
        return new Iterator();
    } 

    public void print(){
        Iterator newIterator = new Iterator();
        newIterator.printAll();
    }
}
