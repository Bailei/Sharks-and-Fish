 /* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object.  Descriptions of the methods you must implement appear below.
 *  They include constructors of the form
 *
 *      public RunLengthEncoding(int i, int j, int starveTime);
 *      public RunLengthEncoding(int i, int j, int starveTime,
 *                               int[] runTypes, int[] runLengths) {
 *      public RunLengthEncoding(Ocean ocean) {
 *
 *  that create a run-length encoding of an Ocean having width i and height j,
 *  in which sharks starve after starveTime timesteps.
 *
 *  The first constructor creates a run-length encoding of an Ocean in which
 *  every cell is empty.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 *
 *  See the README file accompanying this project for additional details.
 */

public class RunLengthEncoding {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */
  private int width;
  private int height;
  private int starveTime;
  private DList runs;
  private DListNode curr;
  private DListNode first;

  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with three parameters) is a constructor that creates
   *  a run-length encoding of an empty ocean having width i and height j,
   *  in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public RunLengthEncoding(int i, int j, int starveTime) {
    width = i;
    height = j;
    this.starveTime = starveTime;
    DList runs = new DList();
    first = runs.head;
    curr = first;
  }

  /**
   *  RunLengthEncoding() (with five parameters) is a constructor that creates
   *  a run-length encoding of an ocean having width i and height j, in which
   *  sharks starve after starveTime timesteps.  The runs of the run-length
   *  encoding are taken from two input arrays.  Run i has length runLengths[i]
   *  and species runTypes[i].
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   *  @param runTypes is an array that represents the species represented by
   *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
   *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
   *         sharks (which are equivalent to sharks that have just eaten).
   *  @param runLengths is an array that represents the length of each run.
   *         The sum of all elements of the runLengths array should be i * j.
   */

  public RunLengthEncoding(int i, int j, int starveTime,
                           int[] runTypes, int[] runLengths) {
    width = i;
    height = j;
    this.starveTime = starveTime;
    DList runs = new DList();
    for(int k = 0; k < runTypes.length; k++){
      runs.addTail(runTypes[k], runLengths[k]);
    }
    first = runs.head;
    curr = first;
  }

  /**
   *  restartRuns() and nextRun() are two methods that work together to return
   *  all the runs in the run-length encoding, one by one.  Each time
   *  nextRun() is invoked, it returns a different run (represented as an
   *  array of two ints), until every run has been returned.  The first time
   *  nextRun() is invoked, it returns the first run in the encoding, which
   *  contains cell (0, 0).  After every run has been returned, nextRun()
   *  returns null, which lets the calling program know that there are no more
   *  runs in the encoding.
   *
   *  The restartRuns() method resets the enumeration, so that nextRun() will
   *  once again enumerate all the runs as if nextRun() were being invoked for
   *  the first time.
   *
   *  (Note:  Don't worry about what might happen if nextRun() is interleaved
   *  with addFish() or addShark(); it won't happen.)
   */

  /**
   *  restartRuns() resets the enumeration as described above, so that
   *  nextRun() will enumerate all the runs from the beginning.
   */

  public void restartRuns() {
    curr = first;
  }

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  an array of two ints (constructed here), representing the type and the
   *  size of the run, in that order.
   *  @return the next run in the enumeration, represented by an array of
   *          two ints.  The int at index zero indicates the run type
   *          (Ocean.EMPTY, Ocean.SHARK, or Ocean.FISH).  The int at index one
   *          indicates the run length (which must be at least 1).
   */

  public int[] nextRun() {
    int[] run = new int[2];
    if(curr == null){
      return null;
    }else{
      run[0] = curr.runTypes;
      run[1] = curr.runLengths;
      curr = curr.next;
      return run;
    }
  }

  /**
   *  toOcean() converts a run-length encoding of an ocean into an Ocean
   *  object.  You will need to implement the three-parameter addShark method
   *  in the Ocean class for this method's use.
   *  @return the Ocean represented by a run-length encoding.
   */

  public Ocean toOcean() {
    Ocean toOcean = new Ocean(this.width, this.height, this.starveTime);
    DListNode curr2 = first;
    int i = 0, j = 0;
    while(curr2 != null){
      for(int n = 0; n < curr2.runLengths; n++){
        if(curr2.runTypes == Ocean.FISH){
          toOcean.addFish(j, i);
        }
        if(curr2.runTypes == Ocean.SHARK){
          toOcean.addShark(j, i, curr2.hunger);
        }
        j++;
        if(j % this.height == 0){
          i++;
        }        
      }
      curr2 = curr2.next;
    }
    return toOcean;
  }

  // public Ocean toOcean(){
  //   Ocean toOcean = new Ocean(this.width, this.height, this.starveTime);
  //   DListNode curr2 = first;
  //   int counter = 0;
  //   for(int j = 0; j < this.height; j++){
  //     for(int i = 0; i < this.width; i++){
  //       if(curr2 == null){
  //         return toOcean;
  //       }else{
  //         if(curr2.runTypes == Ocean.FISH){
  //           toOcean.addFish(i, j);
  //         }
  //         if(curr2.runTypes == Ocean.SHARK){
  //           toOcean.addShark(i, j, curr2.hunger);
  //         }
  //       }
  //       counter++;
  //       if(counter == curr2.runLengths){
  //         curr2 = curr2.next;
  //         counter = 0;
  //       }
  //     }
  //   }
  //   return toOcean;
  // }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of an input Ocean.  You will need to implement
   *  the sharkFeeding method in the Ocean class for this constructor's use.
   *  @param sea is the ocean to encode.
   */

  public RunLengthEncoding(Ocean sea) {
    DList runs = new DList(); 
    width = sea.width();
    height = sea.height();
    starveTime = sea.starveTime();
    int i = 0;
    int j = 0; 
    runs.addTail(sea.cellContents(i, j), 1, sea.sharkFeeding(i, j));
    first = runs.head;
    curr = first;
    for(j = 0; j < sea.height(); j++){
      for(i = 0; i < sea.width(); i++){
        if(!(i == 0 && j == 0)){
          if(sea.cellContents(i, j) == curr.runTypes && sea.sharkFeeding(i, j) == curr.hunger){
            curr.runLengths++;
          }else{
            runs.addTail(sea.cellContents(i, j), 1, sea.sharkFeeding(i, j));
            curr = curr.next;
          }
        }
      }
    }
    curr = first;
    check();
  }

  /**
   *  The following methods are required for Part IV.
   */

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.  The final run-length
   *  encoding should be compressed as much as currsible; there should not be
   *  two consecutive runs of sharks with the same degree of hunger.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  The final run-length encoding should be compressed as much as currsible;
   *  there should not be two consecutive runs of sharks with the same degree
   *  of hunger.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  private void check() {
    // int length = 0;
    // while(curr.next != null){
    //   if(curr.runTypes == curr.next.runTypes){
    //     if(curr.hunger == curr.next.hunger){  
    //       System.out.println("This RunLengthEncoding is illegal");
    //     }
    //   }
    //   if(curr.runLengths < 1){
    //     System.out.println("This RunLengthEncoding has some runs that is illegal");
    //   }
    //   length = length + curr.runLengths;
    //   curr = curr.next;
    // }
    // length = length + runs.tail.runLengths;
    // if(length != this.width * this.height){
    //   System.out.println("This RunLengthEncoding is illegal");
    // }
  }

}