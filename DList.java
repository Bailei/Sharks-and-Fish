public class DList{
	public DListNode head;
	public DListNode tail;
	public long size;
	public int width;
	public int height;
	public int starveTime;

	public DList(){
		head = null;
		tail = null;
		size = 0;
	}

	public DList(int i, int j, int starveTime){
		width = i;
		height = j;
		this.starveTime = starveTime;
		head = null;
		tail = null;
		size = 0;
	}

	public void addTail(int runTypes, int runLengths){
		if(size != 0){
			DListNode newEnd = new DListNode();
			newEnd.runTypes = runTypes;
			newEnd.runLengths = runLengths;
			newEnd.prev = tail;
			tail.next = newEnd;
			tail = newEnd;
			size++;
		}else if(size == 0){
			DListNode newEnd = new DListNode();
			newEnd.runTypes = runTypes;
			newEnd.runLengths = runLengths;
			head = newEnd;
			tail = newEnd;
			size++;
		}
	}

	public void addTail(int runTypes, int runLengths, int hunger){
		if(size != 0){
			DListNode newEnd = new DListNode();
			newEnd.runTypes = runTypes;
			newEnd.runLengths = runLengths;
			newEnd.hunger = hunger;
			newEnd.prev = tail;
			tail.next = newEnd;
			tail = newEnd;
			size++;
		}else if(size == 0){
			DListNode newEnd = new DListNode();
			newEnd.runTypes = runTypes;
			newEnd.runLengths = runLengths;
			newEnd.hunger = hunger;
			head = newEnd;
			tail = newEnd;
			size++;
		}
	}
}