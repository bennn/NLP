import java.util.Map;
import java.util.TreeMap;

/**
 * Unigrams --- Implements unigrams language model
 * @author Sarah Lee
 */
public class Unigrams implements INgrams{
	private static final int SENTENCE_MAX = 150;
	private Trie t;
	private Map <Integer,Integer> turingMap;
	
	/**
	 * Constructor for the Unigram
	 * @param a Trie containing Unigram data
	 * @return a pointer to new Unigram object
	 */
	public Unigrams(Trie t){
		this.t = t;
		turingMap = new TreeMap<Integer, Integer>();
	}

	//Getters and Setters
	public Trie getTrie(){
		return t;
	}

	public void setTrie(Trie t){
		this.t = t;
	}
	
	public Map<Integer,Integer> getTuringMap(){
		return this.turingMap;
	}

	public void generateTuringMap(){
		t.getTuringCounts(turingMap);
	}

	public void printTuringMap(){
		for(Integer i: turingMap.keySet()){
			System.out.println("Key: " + i +","+ "Value: " + turingMap.get(i));
		}
	}

	public double addOneSmoothedProbability(String s){
		int count = t.stringFreq(s);
		System.out.println("the count is " + count);
		System.out.println("t size is " + t.getTotalFreq());
		if (count > 0) {
			return (count + 1) / (t.getTotalFreq() + (t.getVocabularySize()));
		}
		return 0.0;
	}

	public double unsmoothedProbability(String s){
		int count = t.stringFreq(s);
		System.out.println("the coutn is " + count);
		System.out.println("t size is " + t.getTotalFreq());
		if (count > 0) {
			return count / t.getTotalFreq();
		}
		return 0.0;
	}

	public String generateSentence(){
		StringBuilder sentence = new StringBuilder();
		String s1 = "";
		String s2 = "";
		
		/*keep appending words until either run into
		a sentence end punctuation or sentence is of
		reasonable length*/
		while(!s2.contains(".") && sentence.length() <= SENTENCE_MAX){
			s2 = t.generateWord(s1);
			sentence.append(s2 + " ");
			s1 = "";
		}
		//capitalizes the first character of the sentence
		String ret = Character.toString(sentence.toString().charAt(0)).toUpperCase() + 
				sentence.toString().substring(1);
		//appends punctuation if none at end of sentence
		if(!ret.contains(".")){
			ret = ret + ".";
		}
		return ret;
	}
	
	public void print(String filename){
		t.print(filename);
	}

}
