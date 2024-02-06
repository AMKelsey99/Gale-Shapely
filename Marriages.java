/* Alana Kelsey
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marriages;

import java.util.Random;

public class Marriages {

    public static void main(String[] args) {
        for (int n = 0; n < 100; n += 1) {
            int numPeople = 10; //save to integer to print last run of the loop
            Population village = Population.newRandom(numPeople);
            Population.Marriage marriage = village.galeShapelyMarriage(village);
            if (marriage.isStable(marriage, village)) {
                System.out.println("Marriage " + n + " is stable.");
            } else {
                throw new IllegalStateException("Marriage "+n+" is not stable!");
            }
        }
        
    }
    
    
    
    static class Population {
        int[][] boysPreferences; // boysPreferences[b][i] is the ith choice of boy b
        int[][] girlsPreferences; // girlsPreferences[g][i] is the ith choice of girl g
        int[] boys;
        int[] girls;
        int listSize = 0;

        static Population newRandom(int n) {
            Population pop = new Population();
            pop.boysPreferences = new int[n][n];
            pop.girlsPreferences = new int[n][n];
            for (int b = 0; b < n; b += 1) {
                for (int i = 0; i < n; i += 1) {
                    pop.boysPreferences[b][i] = i;
                    pop.girlsPreferences[b][i] = i;
                }
                pop.listSize++;
                randomize(pop.boysPreferences[b]);
                randomize(pop.girlsPreferences[b]);
            }
            pop.boys = new int[pop.listSize];
            pop.girls = new int[pop.listSize];
            return pop;
        }

        
        class Marriage {
            int[] wives;

            int husbandOfGirl(int g) {
                for (int b = 0; b < wives.length; b += 1) {
                    if (wives[b]==g) return b;
                }
                throw new IllegalStateException();
            }

            int wifeOfBoy(int b) {
                return wives[b];
            }
            
            boolean isStable(Marriage marriage, Population village) {
                int BindexofCheck = 0;
                int BindexofCurrent = 0;
                int GindexofCheck = 0;
                int GindexofCurrent = 0;
                
                boolean Bprefers[] = new boolean[boysPreferences.length];
                boolean Gprefers[] = new boolean[girlsPreferences.length];
                for (int partner = 0; partner < boysPreferences.length; partner++) {
                for (int i = 0; i < village.boysPreferences.length; i++) {
                    if (village.boysPreferences[partner][i] == partner) { //pref index of the potential partner
                        BindexofCheck = partner;
                        //break;
                        //System.out.println("B Checking: " + BindexofCheck);
                    }
                

                    if (village.boysPreferences[partner][i] == marriage.wives[partner]) { //pref index of the current partner
                        BindexofCurrent = i;
                        //break;
                        //System.out.println("B Current: " + BindexofCurrent);
                    }
                    
                    if (village.girlsPreferences[partner][i] == partner) { //pref index of the potential partner
                        GindexofCheck = i;
                        //break;
                        //System.out.println("G Checking: " + GindexofCheck);
                    }
                    if (village.girlsPreferences[partner][i] == marriage.wives[partner]) { //pref index of the current partner
                        GindexofCurrent = i;
                        //break;
                        //System.out.println("G Current: " + GindexofCurrent);
                    }
                    
                    if (BindexofCheck < BindexofCurrent && BindexofCheck != BindexofCurrent) { //if the index of potential is less than current and != the same, return false
                        Bprefers[i] = true;
                    } else {
                        Bprefers[i] = false;
                    }
                    
                    if (GindexofCheck < GindexofCurrent && GindexofCheck != GindexofCurrent) { //if the index of potential is less than current and != the same, return false
                        Gprefers[i] = true;
                    } else {
                        Gprefers[i] = false;
                    }
                    
                }
                
                //i think you could also just have the function break when there's a false pairing without making a separate for loop to iterate through all afterwards?
                for (int i = 0; i < Gprefers.length; i++) { //check each pairing, if any returned false, the marriage is not stable
                    if (Gprefers[i] == false && Bprefers[i] == false) {
                        return true;
                    } else {
                        return false;
                    }
                }
                }
            return false;
            }
            

        }
        
         //find the index of the next false return
         public int findNextFree(Population village, boolean[] boysEngaged) {
             int b;
             for (b = 0; b < village.boysPreferences.length; b++) {
                    if (boysEngaged[b] == false) { 
                        //System.out.println("Next free: " + b);
                        break; 
                    }
                }
             return b;
         }
         
         
        
        Marriage galeShapelyMarriage(Population village) {

            Marriage marriage = new Marriage(); //initialize marriage/not a static function
            marriage.wives = new int[boysPreferences.length]; //initialize the partners array
            boolean boysEngaged[] = new boolean[boysPreferences.length]; //keep track of who is still not paired
            int free = boysPreferences.length; //all partners start off as free
            // fill the array as "false" so that each partner is free

            for (int i = 0; i < marriage.wives.length; i ++) {
                marriage.wives[i] = -1;
                boysEngaged[i] = false;
                //System.out.println(marriage.wives[i]);
            }
            
            for (int i = 0; i < boysEngaged.length; i ++) {
                boysEngaged[i] = false;
            }
            
           
            while (free > 0) {
                int b;
                b = village.findNextFree(village, boysEngaged);
                //Find the next free boy; init b outside of the loop so we can use it in
                //successive loops
                
                
                //until the end of the array and only when i == a boy that is not engaged
                for (int i = 0; i < boysPreferences.length && boysEngaged[b] == false; i++) {
                
                    // girl at (boy #, ith choice)
                int g = boysPreferences[b][i];
                //System.out.println("preference #: " + g );
                
                if (marriage.wives[g] == -1) {
                    marriage.wives[g] = b;
                    //System.out.println("NO HUSBAND girl #: " + g + " husband: " + marriage.wives[g]);
                    boysEngaged[b] = true;
                    free--;
                } else { //if g is not free
                    int bCurrent = marriage.wives[g];
                    
                    for (int ithChoice = 0; ithChoice < village.girlsPreferences.length; ithChoice++) {
                    if (village.girlsPreferences[g][ithChoice] == bCurrent) { //whichever if statement returns true first == index of boy 1 ahead of boy 2/vice versa
                        marriage.wives[g] = bCurrent;
                        //System.out.println("NO SWAP " + g + " husband: " + marriage.wives[g]);
                        break;
                    }
                    if (village.girlsPreferences[g][ithChoice] == b) {
                        marriage.wives[g] = b;
                        //swap(marriage.wives,b,bCurrent);  // caused an infinite loop anywhere I tried to use swap in this function; could I please have feedback here on why?
                        boysEngaged[b] = true;
                        boysEngaged[bCurrent] = false;
                        //System.out.println("SWAP " + g + " husband: " + marriage.wives[g]);
                        break;
                    }
                }
                }
            }
            }
            //for (int i = 0; i < boysPreferences.length; i ++) {
            //System.out.println(marriage.husbandOfGirl(i) + " " + marriage.wifeOfBoy(i));
            //}
        return marriage;
    }
    
        //I did not use this function as I wasn't sure what to swap?
        //I attempted above, but it created an infinite loop and I'm not sure why
    static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    static void randomize(int[] a) {
        Random random = new Random();
        for (int i = 1; i < a.length; i += 1) {
            swap(a, i, random.nextInt(i));
        }
    }
    
}}
