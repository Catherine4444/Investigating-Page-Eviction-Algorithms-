// Writte by Catherine Li 
import java.util.Scanner;
import java.util.LinkedList;

public class Replacement {
    private static int firstInFirstOut(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * 
         * Using the frames memory object, process the pageReferences using the FIFO paging algorithm, returning the number of page faults.
         */
         LinkedList<Integer> FIFO = new LinkedList<Integer>();
         for(int i = 0 ; i < pageReferences.length; i++){
            int page = pageReferences[i];
            
            if(!frames.contains(page)){
               if(pageFaults < frames.size()){
                  frames.put(pageFaults, page);
                  FIFO.push(page);
               }
               else{
                  int replacement = FIFO.removeLast();
                  frames.replace(replacement, page);
                  FIFO.push(page);
               }
               pageFaults++;
               System.out.println(page+": "+frames.toString());
            }
            else{
               System.out.println(page+": -");
            }
         }
        return pageFaults;
    }

    private static int optimal(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * Implements the benchmark of what the minimum page eviction would look like
         * returns the number of page faults.
         */
         for(int i = 0 ; i < pageReferences.length; i++){
            int page = pageReferences[i];
            if(!frames.contains(page)){
               if(pageFaults < frames.size()){
                  frames.put(pageFaults, page);
               }
               else{
                  int replacement = findOptimal(i, frames, pageReferences);
                  frames.replace(replacement, page);
                  
               }
               pageFaults++;
               System.out.println(page+": "+frames.toString());
            }
            else{
               System.out.println(page+": -");

            }
         }

        return pageFaults;
    }
    
    private static int findOptimal(int currentPosition, final Memory frames, final Integer[] pageReferences){
      int max = 0; 
      int replacement = frames.get(0);
      
      for (int f =0; f < frames.size(); f++){
         int page = frames.get(f);
         boolean found = false;
         int dist  = 0; 
         
         for(int pg = currentPosition; pg < pageReferences.length; pg++){
            dist++;
            if (frames.get(f) == pageReferences[pg]){
               found = true;
               if(dist > max){
                  replacement = frames.get(f);
                  max = dist;
               }
               break;
            }
         }
         if(!found){
            max = 10000;
            replacement = frames.get(f);
            break;
         }
      }
      return replacement;
    }
    
 

    private static int leastRecentlyUsed(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * returns the number of page faults.
         */
         LinkedList<Integer> LRU = new LinkedList<Integer>();
         
         for(int i = 0 ; i < pageReferences.length; i++){
            Integer page = pageReferences[i];
            if(!frames.contains(page)){
               if(pageFaults < frames.size()){
                  frames.put(pageFaults, page);
                  LRU.push(page);
               }
               else{
                  int replacement = LRU.removeLast();
                  frames.replace(replacement, page);
                  LRU.push(page);
                  
               }
               pageFaults++;
               System.out.println(page+": "+frames.toString());
            }
            else{
               int index = LRU.indexOf(page);
               int item = LRU.remove(index);
               System.out.println(page+": -");
               LRU.push(item);
            }
         }

         return pageFaults;
    }

    private static int clock(final Memory frames, final Integer[] pageReferences) {
        int pageFaults = 0;
        /**
         * Implementation of the 2nd chance clock algorithm
         * returns the number of page faults. 
         */
         int clocksize = frames.size();
         int[] refs = new int[clocksize];
         int clockhand = 0;
         
         for (int i = 0 ; i < pageReferences.length ; i++){
            int page = pageReferences[i];
            if(!frames.contains(page)){
               if(pageFaults< clocksize){
                  frames.put(pageFaults, page);
                  refs[pageFaults] =1;
               }
               else{
                  
                  boolean found = false;
                  while(!found){
                     if(refs[clockhand] == 0){
                        found = true;
                        frames.put(clockhand, page);
                        refs[clockhand] = 1;
                     
                     }
                     else{
                        refs[clockhand] = 0;
                     }
                     clockhand = (clockhand +1)%clocksize;
                  }
                  
                  
               }
               System.out.println(page+": "+frames.toString());
               pageFaults++;
            }
            else{
               int index = frames.indexOf(page);
               refs[index]= 1;
               System.out.println(page+": -");
            }
         
         }
         return pageFaults;
    }




    public static void main(final String[] args) {
        final Scanner stdIn = new Scanner(System.in);

        System.out.println("Enter the physical memory size (number of frames):");
        final int numFrames = stdIn.nextInt();
        stdIn.nextLine();

        System.out.println("Enter the string of page references:");
        final String referenceString = stdIn.nextLine();

        System.out.printf("Page faults: %d.\n", optimal(new Memory(numFrames), toArray(referenceString)));
    }

    private static Integer[] toArray(final String referenceString) {
        final Integer[] result = new Integer[referenceString.length()];
        
        for(int i=0; i < referenceString.length(); i++) {
            result[i] = Character.digit(referenceString.charAt(i), 10);
        }
        return result;
    }
}

