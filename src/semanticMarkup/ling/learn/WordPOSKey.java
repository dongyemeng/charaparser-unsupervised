package semanticMarkup.ling.learn;

public class WordPOSKey {
	
	private String word;
	private String pos;

	public WordPOSKey(String w, String p) {
		this.word=w;
		this.pos=p;
	}
	
	public String getWord() {
		return this.word;
	}
	
	//public void setWord(String s) {
	//	this.word=s;
	//}
	
	public String getPOS() {
		return this.pos;
	}
	
	//public void setPOS(String p) {
	//	this.pos=p;
	//}
	
	//public boolean equals(WordPOSKey wpk) {
	//	return ((this.word.equals(wpk.getWord())) 
	//			&& (this.pos.equals(wpk.getPOS())));
	//}
	
	public boolean equals(Object obj){
		if (obj==this){
			return true;
		}
		
		if (obj==null||obj.getClass()!=this.getClass()){
			return false;
		}
		
		WordPOSKey myWordPOSKey = (WordPOSKey) obj;
		
		boolean case1 = (this.word == null)?
				myWordPOSKey.getWord()==null : this.word.equals(myWordPOSKey.getWord());
		boolean case2 = (this.pos == null)?
				myWordPOSKey.getPOS()==null : this.pos.equals(myWordPOSKey.getPOS());
		
		return (case1 && case2);
		
	}

	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + (this.word == null ? 0 : this.word.hashCode());
		hash = hash * 31 + (this.pos == null ? 0 : this.pos.hashCode());
		return hash;
	}
	
	@Override
	public String toString() {
		return String.format("Key: [Word: %s, POS: %s]", this.word, this.pos);
	}
	
}
