import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HashTable {

// Methods you have to supply
// 
//
    private class Iterator {


        Node currentNode;
        int currentIndex = 0;
        


        public Iterator() {

            if (table.length>0) {
                currentNode = table[0];
            } else {
                currentNode = null;
            }
            // We don't really need to do anything here.
        }

        public Node next() {

            if (currentNode != null) {
                currentNode = currentNode.getNext();
                return currentNode;
            } else {
                // increase the index and set new node;

                if (table[currentIndex+1] != null) {
                    currentNode = table[currentIndex+1];
                    return currentNode;
                } else {
                    if (table.length > 0) {
                        currentNode = table[0];
                    }
                    return null;
                    // return null;
                }
                
            }
        }

        
        public void printAll() {
            Node newNext = next();

            while (newNext != null) {
                System.out.println(newNext.value);
            }
        }


        public boolean hasNext() {

            if (currentNode != null) {
                Node nextNode = currentNode.getNext();

                if (nextNode != null || table[currentIndex+1] != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

            
        }
        






    }

    // We can start by this by default
    private Node[] table = new Node[100];

    private int amountUsed = 0;
    Iterator publicIterator = new Iterator();
    // private int[] allT/rees = new int[table.length];
    private int totalLength = table.length;



	/**
	 * Loads this HashTable from a file named "Lookup.dat".
	 */
    public void load() {
        FileReader fileReader;
        BufferedReader bufferedReader = null;
        
        // Open the file for reading
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            fileReader = new FileReader(f);
            bufferedReader = new BufferedReader(fileReader);
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot find input file \"Lookup.dat\"");
        }
        
        // Read the file contents and save in the HashTable
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
                if (!"".equals(blankLine)) {
                    System.out.println("Error in input file");
                    System.exit(1);
                }
                put(key);
            }
        }
        catch (IOException e) {
            e.printStackTrace(System.out);
        }
        
        // Close the file when we're done
        try {
            bufferedReader.close( );
        }
        catch(IOException e) {
            e.printStackTrace(System.out);
        }
    }

	// /**
	//  * Saves this HashTable onto a file named "Lookup.dat".
	//  */
	public void save() {
        FileOutputStream stream;
        PrintWriter printWriter = null;
        Iterator iterator;
        
        // Open the file for writing
        try {
            File f = new File(System.getProperty("user.home"), "Lookup.dat");
            stream = new FileOutputStream(f);
            printWriter = new PrintWriter(stream);
        }
        catch (Exception e) {
            System.err.println("Cannot use output file \"Lookup.dat\"");
        }
       
        // Write the contents of this HashTable to the file
        iterator = keys();
        while (iterator.hasNext()) {
            String key = (String)iterator.next().value;
            printWriter.println(key);
            String value = (String)get(key);
            value = removeNewlines(value);
            printWriter.println(value);
            printWriter.println();
        }
       
        // Close the file when we're done
        printWriter.close( );
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

        int newThing = defaultHash % totalLength;

        

        return newThing;
    }   
    
    // public static boolean isPrimitiveArray(Object obj) {
    //     if (obj == null) {
    //         return false;
    //     }
    //     Class<?> clazz = obj.getClass();
    //     return clazz.isArray() && clazz.getComponentType().isPrimitive();
    // }

    private void handleHashState() {

        // This will automatically double and rebalance this hashtable
        if (amountUsed > (2./3.)*totalLength) {

            Node[] newTable = new Node[table.length*2];
            totalLength = table.length*2;
            Node[] tempTable = table;
            table = newTable;

            for (int i=0; i<tempTable.length; i++) {


                if (tempTable[i] != null) {
                    Node currentNode = tempTable[i];

                    put(currentNode.value);

                    while (currentNode.getNext() != null) {
                        put(currentNode.value);
                        currentNode = currentNode.getNext();
                    }


                    
                    // This means there's an elemant


                }
                


            }


            // We need to rebalance the tree
        } else {
            System.out.println("Already balanced properly");
        }


    }



    public int put(String key) {
        int hashCode = calculatePossibleHash(key);

        // I think zero means that it is empty
        if (table[hashCode] == null) {
            // We can just place it there, no collision;
            table[hashCode] = new Node(key);
            // allTrees[hashCode] = key.hashCode();
            amountUsed++;
            return hashCode;
        } else {

            Node currentNode = table[hashCode];

            while (currentNode.getNext() != null) {
                currentNode = currentNode.getNext();
            }

            currentNode.setNext(new Node(key));


            // Im not sure if we should be adding since, in theory, it is still inside an index. 
            amountUsed++;

            handleHashState();
            return hashCode;

            // This will chain things appropiately.


            // This is chaining properly done.




            // This means there was a collsion

            // Okay, we need to start chaining in here;

            



            // Okay, we first need to check if the amount of total collisions is higher than the base amount


            // if (amountUsed > (2/3)*table.length) {

            //     // We need to double the array here


            // }





        }


        

        // Now, we can check the state of this hash table


        // System.out.println("Put here: " + hashCode);

 
        
 
    }

 public String get(String key) {

    int index = key.hashCode() % totalLength;

    if (table[index] != null) {
        // We can just return the first one since we rly dont care.
        return table[index].value;
    } else {
        return "";
    }
 }

 public String remove(String key){
    // The same as put but inverse;
    int hashCode = calculatePossibleHash(key);

    if (table[hashCode] == null) {
            // We can just place it there, no collision;
        // then this thing cannot exist
            // return hashCode;
            return "";
    } else {

        Node currentNode = table[hashCode];
        Node prev = currentNode;
        if (currentNode.value.equals(key)) {
            // We need to remove this node from the linked list;
            table[hashCode] = currentNode.getNext();
            // allTrees[hashCode]
            amountUsed--;
            return key;
        }
        while (currentNode.getNext() != null && !currentNode.value.equals(key)) {
            prev = currentNode;
            currentNode = currentNode.getNext();
        }

        if (currentNode != null) {
            prev.next = currentNode.getNext();
            amountUsed--;
        } 
        }




    return key;
	}

  public Iterator keys() {
    publicIterator.next();
    return publicIterator;
  } 

 public void print(){
    Iterator newIterator = new Iterator();
    newIterator.printAll();
	}


}



// publi
