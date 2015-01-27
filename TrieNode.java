import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TrieNode{
	static final String WORD_MARKER = "#"; //used to mark the end of a word
	private boolean isWord; //marks the end of a word
	private String key; //the string associated with the node
	private Map<String,TrieNode> children; //contains this node's children with their associated string
	private int freq; //number of times the key occurs in the corpus

	public TrieNode(String toInsert){
		key = toInsert;
		isWord = false;
		children = new HashMap<String,TrieNode>();
		freq = 0;
	}
	
    //getters and setters for TrieNode fields
	public String getKey(){
		return key;
	}

	public boolean isWord(){
		return isWord;
	}
	
	public void setIsWord(boolean word){
		this.isWord = word;
	}
	
	public int getFreq(){
		return freq;
	}

	public void setFreq(int s){
		freq = s;
	}
	
	public void incFreq(){
		freq ++;
	}

	public Collection<TrieNode> getChildren(){
		return children.values();
	}
	
	public int getNumChildren(){
		return children.values().size();
	}

	public void insert(String s){
		String [] toInsert = s.split(WORD_MARKER);
		insertRec(toInsert, 0);
	}

	public void insertRec(String[] s, int index){
		freq++;
		isWord = true;
		if(index == s.length) {
			isWord = true;
			return;
		}
		TrieNode child = children.get(s[index]);
		if(child == null){
			children.put(s[index],new TrieNode(s[index]));
			children.get(s[index]).insertRec(s, index + 1);
		} else {
			child.insertRec(s, index + 1);
		}
	}

	public TrieNode get(String s){
		TrieNode current = this;
		String [] toFind = s.split(WORD_MARKER);
		for(int i = 0; i < toFind.length; i++){
			TrieNode child = current.children.get(toFind[i]);
			if(child == null) return null;
			current = child;
		}
		return current;
	}

	public void getCounts(Map<Integer,Integer> map){
		if(!key.equals("")){
			if(map.containsKey(getFreq())){
				Integer val = map.get(getFreq());
				map.put(getFreq(), val + 1);
			} else {
				map.put(getFreq(), 1);
			}
		}
		for(TrieNode n: children.values()){
			n.getCounts(map);
		}
	}

	public void traverse(){
		System.out.println(key);
		if(children == null) return;
		for(TrieNode n: children.values()){
			n.traverse();
		}
	}

	public boolean contains(String query){
		return !(get(query) == null);
	}

	public void print(){
		if(isWord){
			System.out.println("Key " + key);
			System.out.println("Freq " + freq);
		}
		if(children!=null){
			Iterator<String> iterator = children.keySet().iterator();
			while(iterator.hasNext()) {
				TrieNode child = children.get(iterator.next());
				child.print();
			}
		} 
	}

	public String randomWalk(int r){
		int curr = 0;
		if(children == null) return null;
		Iterator<String> it = children.keySet().iterator();
		while(it.hasNext()){
			TrieNode t= children.get(it.next());
			curr = curr + t.getFreq();
			if(curr >= r){
				return t.getKey();
			}
		}
		return null;
	}

	public void print(Writer out, String gen) throws IOException{
		if(children!=null){
			Iterator<String> iterator = children.keySet().iterator();
			while(iterator.hasNext()) {//Node eachChild:children_){
				TrieNode child = children.get(iterator.next());
				child.print( out, gen + key + " ");
			}
		} 
		if(isWord) out.write(gen + key + " " + Long.toString(freq)+"\n");
	}
}