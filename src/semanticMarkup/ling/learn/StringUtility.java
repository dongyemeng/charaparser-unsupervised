package semanticMarkup.ling.learn;

public class StringUtility {

	public StringUtility() {
		// TODO Auto-generated constructor stub
	}

	public static String strip(String text) {				
		text=text.replaceAll("<(([^ >]|\n)*)>", " ");
		text=text.replaceAll("<\\?[^>]*\\?>", " "); //<? ... ?>
		text=text.replaceAll("&[^ ]{2,5};", " "); //remove &nbsp;
		text=text.replaceAll("\\s+", " ");
		
		return text;
	}
	
	/**
	 * 
	 * @param text
	 *            : string in which all punctuations to remove
	 * @param c
	 *            : a punctuatin to keep
	 * @return: string after puctuations are removed except the one in c
	 */

	public static String removePunctuation(String text, String c) {
		//System.out.println("Old: " + text);
		if (c == null) {
			text = text.replaceAll("[\\p{Punct}]", "");
		} else {
			text = text.replaceAll(c, "aaa");
			text = text.replaceAll("[\\p{Punct}]", "");
			text = text.replaceAll("aaa", c);
		}
		//System.out.println("New: " + text);

		return text;
	}
	
	public static String trimString (String text){
		String myText = text;
		myText = myText.replaceAll("^\\s+|\\s+$", "");
		return myText;
	}
	
	/**
	 * Helper of method updateTable: process word
	 * 
	 * @param w
	 * @return
	 */

	public static String processWord(String word) {
		//$word =~ s#<\S+?>##g; #remove tag from the word
		//$word =~ s#\s+$##;
		//$word =~ s#^\s*##;
		
		word = word.replaceAll("<\\S+?>", "");
		word = word.replaceAll("\\s+$", "");
		word = word.replaceAll("^\\s*", "");
		
		return word;
	}
	
	public static String removeAll(String word, String regex) {
		String newWord = word.replaceAll(regex, ""); 
		return newWord;
	}
	
	// if($t !~ /\b(?:$STOP)\b/ && $t =~/\w/ && $t !~ /\d/ && length $t > 1){
	public static boolean isWord(String token) {
		String regex = "\\b(" + Constant.STOP + ")\\b";
		if (token.matches(regex)) {
			return false;
		}

		if (!token.matches("\\w+")) {
			return false;
		}

		if (token.length() <= 1) {
			return false;
		}

		return true;
	}
	
	/**
	 * in perl, it escape [] {} and () for mysql regexp, not perl regrexp. May
	 * not be necessary in Java
	 * 
	 * @param singularPluralVariations
	 * @return
	 */
	public static String escape(String singularPluralVariations) {
		// TODO Auto-generated method stub
		return singularPluralVariations;
	}

	/**
	 * check if a word is a word in the wordList
	 * 
	 * @param word
	 *            the word to check
	 * 
	 * @param wordList
	 *            the words to match to
	 * @return a boolean variable. true mean word is a word in the list. false
	 *         means it is not
	 */
	public static boolean isMatchedWords(String word, String wordList){
		return word.matches("^.*\\b(?:"+wordList+")\\b.*$");
	}

	/**
	 * Given a list of words in one string in the form of
	 * "(word1|word2|...|wordn)", remove the word from the list if it is in the
	 * list.
	 * 
	 * @param word
	 *            the word to remove
	 * @param wordList
	 *            the list to remove the word from
	 * @return the list after remove the word
	 */
	public static String removeFromWordList(String word, String wordList) {
		String newWordList = wordList;
		
		newWordList = newWordList.replaceAll("\\b" + word + "\\b", "");
		newWordList = newWordList.replaceAll("^\\|", "");
		newWordList = newWordList.replaceAll("\\|\\|", "|");
		newWordList = newWordList.replaceAll("\\|$", "");
		
		return newWordList;
	}
}