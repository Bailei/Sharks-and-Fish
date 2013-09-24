public class DListNode{
	public int runTypes;
	public int runLengths;
	public int hunger;
	public DListNode prev;
	public DListNode next;

	public DListNode(){
		prev = null;
		next = null;
		runTypes = 0;
		runLengths = 0;
		hunger = 0;
	}

	public DListNode(int runTypes, int runLengths){
		prev = null;
		next = null;
		this.runTypes = runTypes;
		this.runLengths = runLengths;
	};

	public DListNode(int runTypes, int runLengths, int hunger){
		prev = null;
		next = null;
		this.runTypes = runTypes;
		this.runLengths = runLengths;
		this.hunger = hunger;
	};


}