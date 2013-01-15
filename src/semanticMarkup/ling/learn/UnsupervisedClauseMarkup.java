package semanticMarkup.ling.learn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

import semanticMarkup.core.Treatment;
import semanticMarkup.knowledge.lib.WordNetAPI;

public class UnsupervisedClauseMarkup implements ITerminologyLearner {

	// directory of /descriptions folder
	private String desDir = "";
	// directory of /characters folder
	private String chrDir = "";
	// database name
	private String dataBase = "";
	// learning mode
	private String learningMode = "";
	// prefix for all tables generated by this program
	private String prefix = "";
	// default general tag
	private String defaultGeneralTag = "general";
	// knowledge base
	private String knlgBase = "phenoscape";
	// tag length
	private int tagLength = 150;

	// Database parameters
	private String host = "localhost";
	private String user = "biocreative";
	private String password = "biocreative";

	// DNGYE_TODO
	// my $dbh = DBI->connect("DBI:mysql:host=$host", $user, $password)
	// or die DBI->errstr."\n";

	private String CHECKEDWORDS = ":"; // leading three words of sentences
	private int NUM_LEAD_WORDS = 3; // $N leading words
	private int SENTID = 0;
	private int DECISIONID = 0;
	private String PROPERNOUN = "propernouns"; // EOL

	private Hashtable<String, String> WNNUMBER = new Hashtable(); // word->(p|s)
	private Hashtable<String, String> WNSINGULAR = new Hashtable();// word->singular
	private Hashtable<String, String> WNPOS = new Hashtable(); // word->POSs
	private Hashtable<String, String> WNPOSRECORDS = new Hashtable();
	private String NEWDESCRIPTION = ""; // record the index of sentences that
										// ends a description
	private Hashtable<String, String> WORDS = new Hashtable();
	private Hashtable<String, String> PLURALS = new Hashtable();

	private String NUMBER = "zero|one|ones|first|two|second|three|third|thirds|four|fourth|fourths|quarter|five|fifth|fifths|six|sixth|sixths|seven|seventh|sevenths|eight|eighths|eighth|nine|ninths|ninth|tenths|tenth";
	// the following two patterns are used in mySQL rlike
	private String PREFIX = "ab|ad|bi|deca|de|dis|di|dodeca|endo|end|e|hemi|hetero|hexa|homo|infra|inter|ir|macro|mega|meso|micro|mid|mono|multi|ob|octo|over|penta|poly|postero|post|ptero|pseudo|quadri|quinque|semi|sub|sur|syn|tetra|tri|uni|un|xero|[a-z0-9]+_";
	private String SUFFIX = "er|est|fid|form|ish|less|like|ly|merous|most|shaped"; // 3_nerved,
																					// )_nerved,
																					// dealt
																					// with
																					// in
																					// subroutine
	private String FORBIDDEN = "to|and|or|nor"; // words in this list can not be
												// treated as boundaries
												// "to|a|b" etc.
	private String PRONOUN = "all|each|every|some|few|individual|both|other";
	private String CHARACTER = "lengths|length|lengthed|width|widths|widthed|heights|height|character|characters|distribution|distributions|outline|outlines|profile|profiles|feature|features|form|forms|mechanism|mechanisms|nature|natures|shape|shapes|shaped|size|sizes|sized";// remove
																																																																					// growth,
																																																																					// for
																																																																					// growth
																																																																					// line.
																																																																					// check
																																																																					// 207,
																																																																					// 3971
	private String PREPOSITION = "above|across|after|along|around|as|at|before|below|beneath|between|beyond|by|during|for|from|in|into|near|of|off|on|onto|out|outside|over|than|throught|throughout|toward|towards|up|upward|with|without";
	private String TAGS = "";
	private String PLENDINGS = "[^aeiou]ies|i|ia|(x|ch|sh)es|ves|ices|ae|s";
	private String CLUSTERSTRING = "group|groups|clusters|cluster|arrays|array|series|fascicles|fascicle|pairs|pair|rows|number|numbers|\\d+";
	private String SUBSTRUCTURESTRING = "part|parts|area|areas|portion|portions";
	private String mptn = "((?:[mbq][,&]*)*(?:m|b|q(?=[pon])))";// grouped #may
																// contain q but
																// not the last
																// m, unless it
																// is followed
																// by a p
	private String nptn = "((?:[nop][,&]*)*[nop])"; // grouped #must present, no
													// q allowed

	// perl->java : $bptn =
	// "([,;:\\.]*\$|,*[bm]|(?<=[pon]),*q)"; -> String bptn = "([,;:\\\\.]*\\$|,*[bm]|(?<=[pon]),*q)";
	private String bptn = "([,;:\\\\.]*\\$|,*[bm]|(?<=[pon]),*q)"; // grouped
																	// #when
																	// following
																	// a p, a b
																	// could be
																	// a q

	private String SEGANDORPTN = "(?:" + mptn + nptn + ")"; // ((?:[mq],?)*&?(?:m|q(?=p))?)((?:[np],?)*&?[np])
	private String ANDORPTN = "^(?:" + SEGANDORPTN + "[,&]+)*" + SEGANDORPTN
			+ bptn;

	private String IGNOREPTN = "(IGNOREPTN)"; // disabled
	// private String stop =
	// "state|page|fig|"+"a|about|above|across|after|along|also|although|amp|an|and|are|as|at|be|because|become|becomes|becoming|been|before|behind|being|beneath|between|beyond|but|by|ca|can|could|did|do|does|doing|done|during|for|from|had|has|have|hence|here|how|if|in|into|inside|inward|is|it|its|least|may|might|more|most|near|no|not|of|off|on|onto|or|out|outside|outward|over|should|so|than|that|the|then|there|these|this|those|throughout|to|toward|towards|under|up|upward|via|was|were|what|when|where|whereas|which|why|with|within|without|would";

	private String STOP = "state|page|fig|"
			+ "a|about|above|across|after|along|also|although|amp|an|and|are|as|at|be|because|become|becomes|becoming|been|before|behind|being|beneath|between|beyond|but|by|ca|can|could|did|do|does|doing|done|during|for|from|had|has|have|hence|here|how|if|in|into|inside|inward|is|it|its|least|may|might|more|most|near|no|not|of|off|on|onto|or|out|outside|outward|over|should|so|than|that|the|then|there|these|this|those|throughout|to|toward|towards|under|up|upward|via|was|were|what|when|where|whereas|which|why|with|within|without|would";

	// List to store all unknown words
	// List<String> unknownWordList = new ArrayList<String>();
	// Set<String> unknownWordSet = new TreeSet<String>();

	// Table sentence
	// List<String> sentence = new ArrayList<String>();
	// List<String> originalSent = new ArrayList<String>();
	// List<String> tag = new ArrayList<String>();
	// List<String> modifier = new ArrayList<String>();

	// Table sentence
	List<Sentence> sentenceTable = new ArrayList<Sentence>();

	// Table unknownwords
	Map<String, String> unknownWordTable = new HashMap<String, String>();

	// Table wordpos
	Map<WordPOSKey, WordPOSValue> wordPOSTable = new HashMap<WordPOSKey, WordPOSValue>();

	// DNGYE_TODO

	public UnsupervisedClauseMarkup(String dir, String db, String lm, String p) {
		System.out.println("Initialized:\n");
		this.desDir = dir.concat("/");
		this.chrDir = desDir.replaceAll("descriptions.*", "characters/");
		this.dataBase = db;
		this.learningMode = lm;
		this.prefix = p;
		System.out.println(String.format("Read directory: %s", this.desDir));
		System.out.println(String
				.format("Character directory: %s", this.chrDir));
		System.out.println(String.format("%s", this.dataBase));
		System.out.println(String.format("%s", this.learningMode));
		System.out.println(String.format("%s", this.prefix));
	}

	// replace '.', '?', ';', ':', '!' within brackets by some special markers,
	// to avoid split within brackets during sentence segmentation
	public String hideMarksInBrackets(String text) {

		if (text == null || text == "") {
			return text;
		}

		String hide = "";
		int lRound = 0;
		int lSquare = 0;
		int lCurly = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			switch (c) {
			case '(':
				lRound++;
				hide = hide + c;
				break;
			case ')':
				lRound--;
				hide = hide + c;
				break;
			case '[':
				lSquare++;
				hide = hide + c;
				break;
			case ']':
				lSquare--;
				hide = hide + c;
				break;
			case '{':
				lCurly++;
				hide = hide + c;
				break;
			case '}':
				lCurly--;
				hide = hide + c;
				break;
			default:
				if (lRound + lSquare + lCurly > 0) {
					if (c == '.') {
						hide = hide + "[DOT] ";
					} else if (c == '?') {
						hide = hide + "[QST] ";
					} else if (c == ';') {
						hide = hide + "[SQL] ";
					} else if (c == ':') {
						hide = hide + "[QLN] ";
					} else if (c == '!') {
						hide = hide + "[EXM] ";
					} else {
						hide = hide + c;
					}
				} else {
					hide = hide + c;
				}
			}
		}
		return hide;

	}

	public String restoreMarksInBrackets(String text) {

		if (text == null || text == "") {
			return text;
		}

		// restore "." from "[DOT]"
		// s#\[\s*DOT\s*\]#.#g;
		text = text.replaceAll("\\[\\s*DOT\\s*\\]", ".");
		// restore "?" from "[QST]"
		// s#\[\s*QST\s*\]#?#g;
		text = text.replaceAll("\\[\\s*QST\\s*\\]", "?");
		// restore ";" from "[SQL]"
		// s#\[\s*SQL\s*\]#;#g;
		text = text.replaceAll("\\[\\s*SQL\\s*\\]", ";");
		// restore ":" from "[QLN]"
		// s#\[\s*QLN\s*\]#:#g;
		text = text.replaceAll("\\[\\s*QLN\\s*\\]", ":");
		// restore "." from "[DOT]"
		// s#\[\s*EXM\s*\]#!#g;
		text = text.replaceAll("\\[\\s*EXM\\s*\\]", "!");

		return text;
	}

	public String handleText(String t) {

		if (t == null || t == "") {
			return t;
		}

		String text = t;

		//
		text = text.replaceAll("[\"']", "");

		// plano - to
		text = text.replaceAll("\\s*-\\s*to\\s+", " to ");

		//
		text = text.replaceAll("[-_]+shaped", "-shaped");

		// unhide <i>
		text = text.replaceAll("&lt;i&gt;", "<i>");

		// unhide </i>, these will be used by characterHeuristics to
		// collect taxon names
		text = text.replaceAll("&lt;/i&gt;", "</i>");

		// remove 2a. (key marks)
		text = text.replaceAll("^\\s*\\d+[a-z].\\s*", "");

		// this is not used any more, see perl code - Dongye
		// store text at this point in original
		// String original = text;

		// remove HTML entities
		text = text.replaceAll("&[;#\\w\\d]+;", " ");

		//
		text = text.replaceAll(" & ", " and ");

		// replace '.', '?', ';', ':', '!' within brackets by some
		// special markers, to avoid split within brackets during
		// sentence segmentation
		// System.out.println("Before Hide: "+text);
		text = hideMarksInBrackets(text);
		// System.out.println("After Hide: "+text+"\n");

		text = text.replaceAll("_", "-"); // _ to -
		text = text.replaceAll("", ""); //

		//
		// Matcher matcher1 = Pattern.compile("\\s+([:;\\.])").matcher(text);
		// if (matcher1.lookingAt()) {
		// text = text.replaceAll("\\s+([:;\\.])", matcher1.group(1));
		// }

		// absent ; => absent;
		while (true) {
			Matcher matcher1 = Pattern.compile("(^.*?)\\s+([:;\\.].*$)")
					.matcher(text);
			if (matcher1.lookingAt()) {
				text = matcher1.group(1) + matcher1.group(2);
			} else {
				break;
			}
		}

		// absent;blade => absent; blade
		while (true) {
			Matcher matcher2 = Pattern.compile("(^.*?\\w)([:;\\.])(\\w.*$)")
					.matcher(text);
			if (matcher2.lookingAt()) {
				// text = text.replaceAll("^.*\\w[:;\\.]\\w.*",
				// matcher2.group(1)
				// + matcher2.group(2) + " " + matcher2.group(3));
				text = matcher2.group(1) + matcher2.group(2) + " "
						+ matcher2.group(3);
			} else {
				break;
			}
		}

		// 1 . 5 => 1.5
		while (true) {
			Matcher matcher3 = Pattern.compile("(^.*?\\d\\s*\\.)\\s+(\\d.*$)")
					.matcher(text);
			if (matcher3.lookingAt()) {
				text = matcher3.group(1) + matcher3.group(2);
			} else {
				break;
			}
		}

		// ###NOT necessary at all, done before in "absent ; => absent;"###
		// diam . =>diam.
		// Matcher matcher4 =
		// Pattern.compile("(\\sdiam)\\s+(\\.)").matcher(text);
		// if (matcher4.lookingAt()) {
		// text = text.replaceAll("\\sdiam\\s+\\.", matcher4.group(1)
		// + matcher4.group(2));
		// }

		// ca . =>ca.
		// Matcher matcher5 = Pattern.compile("(\\sca)\\s+(\\.)").matcher(text);
		// if (matcher5.lookingAt()) {
		// text = text.replaceAll("\\sca\\s+\\.",
		// matcher5.group(1) + matcher5.group(2));
		// }

		//
		while (true) {
			Matcher matcher6 = Pattern.compile(
					"(^.*\\d\\s+(cm|mm|dm|m)\\s*)\\.(\\s+[^A-Z].*$)").matcher(
					text);
			if (matcher6.lookingAt()) {
				text = matcher6.group(1) + "[DOT]" + matcher6.group(3);
			} else {
				break;
			}
		}

		return text;
	}

	// add space before and after all occurance of the regex in the string str
	public String addSpace(String str, String regex) {

		if (str == null || str == "" || regex == null || regex == "") {
			return str;
		}

		Matcher matcher = Pattern.compile("(^.*)(" + regex + ")(.*$)").matcher(
				str);
		if (matcher.lookingAt()) {
			str = addSpace(matcher.group(1), regex) + " " + matcher.group(2)
					+ " " + addSpace(matcher.group(3), regex);
			return str;
		} else {
			return str;
		}

	}

	public String handleSentence(String s) {

		if (s == null || s == "") {
			return s;
		}

		String sentence = s;

		// remove (.a.)
		sentence = sentence.replaceAll("\\([^()]*?[a-zA-Z][^()]*?\\)", " ");

		// remove [.a.]
		sentence = sentence.replaceAll("\\[[^\\]\\[]*?[a-zA-Z][^\\]\\[]*?\\]",
				" ");

		// remove {.a.}
		sentence = sentence.replaceAll("\\{[^{}]*?[a-zA-Z][^{}]*?\\}", " ");

		// to fix basi- and hypobranchial
		// s#\s*[-]+\s*([a-z])#_ $1#g;
		while (true) {
			Matcher matcher = Pattern.compile("(^.*?)\\s*[-]+\\s*([a-z].*$)")
					.matcher(sentence);
			if (matcher.lookingAt()) {
				sentence = matcher.group(1) + "_ " + matcher.group(2);
			} else {
				break;
			}
		}

		// add space around nonword char
		// s#(\W)# $1 #g;
		/*
		 * while (true) { Matcher matcher8
		 * =Pattern.compile("(^.*)(\\S\\W\\S)(.*$)").matcher( sentence); if
		 * (matcher8.lookingAt()) { //sentence = sentence.replaceAll("\\W", " "+
		 * // matcher8.group(1) // + " "); sentence = matcher8.group(1) + " "+
		 * matcher8.group(2) + " " + matcher8.group(3); } else { break; } } /
		 */
		sentence = this.addSpace(sentence, "\\W");

		// String [] substrings=
		// Pattern.compile("(\\W)").split(sentence);//matcher(sentence);
		// matcher8.replaceAll(" " + matcher8.group(1) + " ");
		// sentence="";
		// for (int i=0;i<substrings.length;i++) {
		// sentence=sentence+" "+substrings[i]+" ";
		// }

		// multiple spaces => 1 space
		// s#\s+# #g;
		sentence = sentence.replaceAll("\\s+", " ");

		// trim
		// s#^\s*##;
		sentence = sentence.replaceAll("^\\s*", "");

		// trim
		// s#\s*$##;
		sentence = sentence.replaceAll("\\s*$", "");

		// all to lower case
		sentence = sentence.toLowerCase();

		return sentence;

	}

	public boolean populatesents() {
		boolean debug = false;

		System.out.println("Reading sentences:\n");
		FileLoader fileLoader = new FileLoader(this.desDir);
		if (!fileLoader.load())
			return false;

		List<String> fileNameList = fileLoader.getFileNameList();
		List<Integer> typeList = fileLoader.getTypeList();
		List<String> textList = fileLoader.getTextList();

		String text;
		for (int i = 0; i < fileLoader.getCount(); i++) {
			text = textList.get(i);
			if (text != null) {
				// process this text
				text = this.handleText(text);
				// use Apache OpenNLP to do sentence segmentation
				String sentences[] = {};
				sentences = this.segmentSentence(text);
				
				HashMap<Integer, String> leadMap = new HashMap<Integer,String>(); 
				
				if (debug)
					System.out.println("Text: " + text);

				List<String> sentcopy = new LinkedList<String>();
				List<Integer> validindex = new LinkedList<Integer>();
				int index = 0;
				// for each sentence, do some operations
				for (int j = 0; j < sentences.length; j++) {
					
					if (debug)
						System.out.println("Sentence " + j + ": "
								+ sentences[j]);

					// TODO: Dongye
					// Do something for this sentence:
					// may have fewer than $N words
					// if(!/\w+/){next;}
					if (debug)
						System.out.println(sentences[j]);
					if (!sentences[j].matches("^.*\\w+.*$")) {
						continue;
					}
					validindex.add(j);
					// restore ".", "?", ";", ":", "."
					sentences[j] = this.restoreMarksInBrackets(sentences[j]);
					// push(@sentcopy, $_);
					sentcopy.add(sentences[j]);

					// remove bracketed text from sentence (keep those in
					// originalsent); this step will not be able to remove 
					// nested brackets, such as (petioles (2-)4-8 cm).
					// nested brackets will be removed after threedsent 
					// step in POSTagger4StanfordParser.java
					sentences[j] = this.handleSentence(sentences[j]);

					// getallwords($_);

					// first tokenize this sentence
					InputStream modelIn;
					try {
						modelIn = new FileInputStream(
								"/Users/nescent/Phenoscape/charaparser-unsupervised/res/en-token.bin");
						TokenizerModel model = new TokenizerModel(modelIn);
						Tokenizer tokenizer = new TokenizerME(model);
						String tokens[] = tokenizer.tokenize(sentences[j]);
						for (int i1 = 0; i1 < tokens.length; i1++) {
							this.unknownWordTable.put(tokens[i1], "unknown");
						}
						String lead="";
						int minL = tokens.length>this.NUM_LEAD_WORDS? this.NUM_LEAD_WORDS:tokens.length;
						for (int i2=0; i2<minL;i2++) {
							lead=lead+tokens[i2]+" ";							
						}
						lead=lead.replaceAll("\\s$", "");
						//lead=lead.substring(lead.length()-1);
						leadMap.put(j, lead);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvalidFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					index++;
				}

				for (int j = 0; j < validindex.size(); j++) {
					String line = sentences[validindex.get(j)];
					String oline = sentcopy.get(validindex.get(j));

					// handle line first
					// remove all ' to avoid escape problems
					// $line =~ s#'# #g;
					line.replaceAll("\'", " ");

					// then handle oline
					Matcher matcher = Pattern.compile(
							"(\\d)\\s*\\[\\s*DOT\\s*\\]\\s*(\\d)").matcher(
							oline);
					if (matcher.lookingAt()) {
						oline = oline.replaceAll(
								"(\\d)\\s*\\[\\s*DOT\\s*\\]\\s*(\\d)",
								matcher.group(1) + matcher.group(2));
					}

					// restore ".", "?", ";", ":", "."
					oline = this.restoreMarksInBrackets(oline);
					oline = oline.replaceAll("\'", " ");
					String lead = leadMap.get(j);

					if (oline.length() >= 2000) { // EOL
						oline = line;
					}
					
					
					this.sentenceTable.add(new Sentence(line, oline, lead,
							null, null, null, null));

					this.SENTID++;
				}
			}

		}
		// copy all unknown words from unknownWordSet into unknownWordList, set
		// the tags in the unknownWordTagList be 0 (0 - unknown)
		// Iterator<String> unknownWordIterator = unknownWordSet.iterator();
		// ensure unknownWordList and unknownWordTagList have enough capacity to
		// hold the words and tags
		// ((ArrayList) this.unknownWordList)
		// .ensureCapacity(unknownWordSet.size());
		// ((HashMap) this.unknownWordTable).ensureCapacity(unknownWordSet
		// .size());
		// while (unknownWordIterator.hasNext()) {
		// String unknownWord=unknownWordIterator.next();
		// unknownWordList.add(unknownWord);
		// unknownWordTable.put(unknownWord, "unknown");
		// }
		System.out.println("Total sentences = " + SENTID);
		return true;
	}
	
	String[] segmentSentence(String text) {
		String sentences[] = {};
		
		SentenceModel model;
		// need to be replaced by a relative path
		InputStream modelIn;
		try {
			modelIn = new FileInputStream(
					"/Users/nescent/Phenoscape/charaparser-unsupervised/res/en-sent.bin");				
			// "../../../../../../res/en-sent.bin");
			try {
				model = new SentenceModel(modelIn);		
				SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);		
				sentences = sentenceDetector.sentDetect(text);
			} catch (InvalidFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sentences;
	}

	public void addHeuristicsNouns() {
		;
	}
	
	public Set<String> getHeuristicsNouns(Set<String> words) {
		String N_ENDINGS = "\\w\\w(?:ist|sure)\\b";
		String V_ENDINGS = "(?:ing)\\b";
		String S_ENDINGS = "(?:on|is|ex|ix|um|us|a)\\b";
		String P_ENDINGS = "(?:ia|es|ices|i|ae)\\b";
		
		//HashSet<String> words = new HashSet<String>();
		HashSet<String> nouns = new HashSet<String>();
		
		// get all words

		// loop over words
		Iterator<String> it = words.iterator();
		while(it.hasNext()) {
			String word = it.next();
			// if word has N endings
				// put word into nouns
			// if word has S endings: w+S ending
				// if (root + N endings exist) && (root + V ending does not exist) 
					// put w[s] into nouns
					// put w[p] into nouns
			// if word has P endings: w+N ending
				// if (root + S endings exist) && (root + V ending does not exist) 
					// put w[p] into nouns
					// put w[s] into nouns
		}
		
		return nouns;
	}

	// ---------------addHeuristicsNouns Help Function----
	// #solve the problem: septa and septum are both s
	// septum - Singular
	// septa -Plural
	// septa[s] => septa[p]
	public String addHeuristicsNounsHelper(String oldNoun, Set<String> words) {
		String newNoun = oldNoun;

		if (oldNoun.matches("^.*a\\[s\\]$")) {
			String noun = oldNoun.replaceAll("\\[s\\]", "");
			System.out.println(noun);
			if (words.contains(noun)) {
				newNoun = noun + "[p]";
			}
		}

		return newNoun;
	}


	public void addStopWords() {
		// my @stops = split(/\|/,$stop);
		// String []temp=this.STOP.split("|");
		ArrayList<String> stops = new ArrayList();
		stops.addAll(Arrays.asList(this.STOP.split("\\|")));
		// new ArrayList();
		// for (int i=0;i<temp.length;i++) {
		// stops.add(temp[i]);
		// }
		// push(@stops, "NUM", "(", "[", "{", ")", "]", "}");
		stops.addAll(Arrays.asList(new String[] { "NUM", "(", "[", "{", ")",
				"]", "}", "\\\\d+" }));
		// String []a= {stops,"NUM", "(", "[", "{", ")", "]", "}"};

		// push(@stops, "\\\\d+");
		// stops.addAll(Arrays.asList(new String[]{}));

		// print "stop list:\n@stops\n" if $debug;
		// print STDOUT "stop list:\n@stops\n";;

		System.out.println(stops);
		System.out.println(this.FORBIDDEN);

		for (int i = 0; i < stops.size(); i++) {
			String word = stops.get(i);
			// String reg="\\b("+this.FORBIDDEN+")\\b";
			// boolean f = word.matches(reg);
			if (word.matches("\\b(" + this.FORBIDDEN + ")\\b")) {
				continue;
			}
			// update(word, "b", "*", "wordpos", 0);
			this.wordPOSTable.put(new WordPOSKey(word, "b"), new WordPOSValue(
					"*", 0, 0, null, null));
			System.out.println("Update " + word);
		}
	}

	public void addCharacters() {
		ArrayList<String> chars = new ArrayList();
		chars.addAll(Arrays.asList(this.CHARACTER.split("\\|")));

		System.out.println(chars);
		System.out.println(this.CHARACTER);

		for (int i = 0; i < chars.size(); i++) {
			String word = chars.get(i);
			// String reg="\\b("+this.FORBIDDEN+")\\b";
			// boolean f = word.matches(reg);
			if (word.matches("\\b(" + this.FORBIDDEN + ")\\b")) {
				continue;
			}
			// update(word, "b", "*", "wordpos", 0);
			this.wordPOSTable.put(new WordPOSKey(word, "b"), new WordPOSValue(
					"", 0, 0, null, null));
			System.out.println("Update " + word);
		}
	}

	public void addNumbers() {
		ArrayList<String> nums = new ArrayList();
		nums.addAll(Arrays.asList(this.NUMBER.split("\\|")));

		System.out.println(nums);
		System.out.println(this.NUMBER);

		for (int i = 0; i < nums.size(); i++) {
			String word = nums.get(i);
			// String reg="\\b("+this.FORBIDDEN+")\\b";
			// boolean f = word.matches(reg);
			if (word.matches("\\b(" + this.FORBIDDEN + ")\\b")) {
				continue;
			}
			// update(word, "b", "*", "wordpos", 0);
			this.wordPOSTable.put(new WordPOSKey(word, "b"), new WordPOSValue(
					"*", 0, 0, null, null));
			System.out.println("Update " + word);
		}
		this.wordPOSTable.put(new WordPOSKey("NUM", "b"), new WordPOSValue("*",
				0, 0, null, null));
	}

	public void addClusterstrings() {
		ArrayList<String> cltstrs = new ArrayList();
		cltstrs.addAll(Arrays.asList(this.CLUSTERSTRING.split("\\|")));

		System.out.println(cltstrs);
		System.out.println(this.CLUSTERSTRING);

		for (int i = 0; i < cltstrs.size(); i++) {
			String word = cltstrs.get(i);
			// String reg="\\b("+this.FORBIDDEN+")\\b";
			// boolean f = word.matches(reg);
			if (word.matches("\\b(" + this.FORBIDDEN + ")\\b")) {
				continue;
			}
			// update(word, "b", "*", "wordpos", 0);
			this.wordPOSTable.put(new WordPOSKey(word, "b"), new WordPOSValue(
					"*", 1, 1, null, null));
			System.out.println("Update " + word);
		}
	}

	public void addProperNouns() {
		ArrayList<String> ppnouns = new ArrayList();
		ppnouns.addAll(Arrays.asList(this.PROPERNOUN.split("\\|")));

		System.out.println(ppnouns);
		System.out.println(this.PROPERNOUN);

		for (int i = 0; i < ppnouns.size(); i++) {
			String word = ppnouns.get(i);
			// String reg="\\b("+this.FORBIDDEN+")\\b";
			// boolean f = word.matches(reg);
			if (word.matches("\\b(" + this.FORBIDDEN + ")\\b")) {
				continue;
			}
			// update(word, "b", "*", "wordpos", 0);
			this.wordPOSTable.put(new WordPOSKey(word, "z"), new WordPOSValue(
					"*", 0, 0, null, null));
			System.out.println("Update " + word);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// suffix: -fid(adj), -form (adj), -ish(adj), -less(adj), -like (adj)),
	// -merous(adj), -most(adj), -shaped(adj), -ous(adj)
	// -ly (adv), -er (advj), -est (advj),
	// foreach unknownword in unknownwords table
	// seperate root and suffix
	// if root is a word in WN or unknownwords table
	// make the unknowword a "b" boundary

	// suffix is defined in global variable SUFFIX
	public void posBySuffix() {
		String p1 = "^[a-z_]+(" + this.SUFFIX + ")$";
		String p2 = "^[._.][a-z]+"; // , _nerved
		Iterator<Map.Entry<String, String>> iterator = this.unknownWordTable
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, String> unknownWordEntry = iterator.next();
			String unknownWord = unknownWordEntry.getKey();
			String unknownWordTag = unknownWordEntry.getValue();
			// String unknownWord = "anteriorly";
			// String unknownWordTag = "unknown";
			// the tag of this word is unknown
			if (unknownWordTag.equals("unknown")) {
				if (unknownWord.matches(p1)) {
					Matcher matcher = Pattern.compile(
							"(.*?)(" + this.SUFFIX + ")$").matcher(unknownWord);
					if ((unknownWord.matches("^[a-zA-Z0-9_-]+$"))
							&& matcher.matches()) {
						if (this.containSuffix(unknownWord, matcher.group(1),
								matcher.group(2))) {
							this.wordPOSTable.put(new WordPOSKey(unknownWord,
									"b"), new WordPOSValue("*", 0, 0, null,
									null));
							System.out
									.println("posBySuffix set $unknownword a boundary word\n");
						}
					}
				}

				if (unknownWord.matches(p2)) {
					// unknownWordTable.put(unknownWord, "b");
					this.wordPOSTable.put(new WordPOSKey(unknownWord, "b"),
							new WordPOSValue("*", 0, 0, null, null));
					System.out
							.println("posbysuffix set $unknownword a boundary word\n");
				}
			}
		}
	}

	// return false or true depending on if the word contains the suffix as the
	// suffix
	public boolean containSuffix(String word, String base, String suffix) {
		boolean flag = false; // return value
		boolean wordInWN = false; // if this word is in WordNet
		boolean baseInWN = false;
		WordNetAPI myWN;

		// check base
		// this if statement is added by Dongye
		if (base.length() == 0) {
			return true;
		}

		try {
			myWN = new WordNetAPI("/Users/nescent/Phenoscape/WordNet-3.0/dict",
					false);

			// $base =~ s#_##g; #cup_shaped
			// $wnoutputword = `wn $word -over`;
			// if ($wnoutputword !~/\w/){#word not in WN
			// $wordinwn = 0;
			// }else{ #found $word in WN:
			// $wnoutputword =~ s#\n# #g;
			// $wordinwn = 1;
			// }

			base.replaceAll("_", ""); // cup_shaped

			if (myWN.contains(word)) {
				wordInWN = true; // word is in WordNet
			} else {
				// $wnoutputword =~ s#\n# #g;

				wordInWN = false;
			}

			if (myWN.contains(base)) {
				baseInWN = true;
			} else {
				// $wnoutputbase =~ s#\n# #g;
				baseInWN = false;
			}

			// if WN pos is adv, return 1: e.g. ly, or if $base is in
			// unknownwords
			// table
			if (suffix.equals("ly")) {
				if (wordInWN) {
					// if($wnoutputword =~/Overview of adv $word/){
					if (myWN.isAdverb(word)) {
						return true;
					}
				}
				// if the word is in unknown word set, return true
				if (this.unknownWordTable.containsKey(base)) {
					return true;
				}
			}

			// if WN recognize superlative, comparative adjs, return 1: e.g. er,
			// est
			else if (suffix.equals("er") || suffix.equals("est")) {
				if (wordInWN) {
					// if($wnoutputword =~/Overview of adj (\w+)/){#$word =
					// softer,
					// $1 = soft vs. $word=$1=neuter
					// $word = softer, $1 = soft vs. $word=$1=neuter
					if (myWN.isAdjective(word) || myWN.isAdverb(word)) {
						return true;
					}
					// return 1 if $word=~/^$1\w+/;
				}
			}

			/*
			 * else{#if $base is in WN or unknownwords table, or if $word has
			 * sole pos adj in WN, return 1: e.g. scalelike if($baseinwn){return
			 * 1;} if($wnoutputword =~/Overview of adj/ && $wnoutputword
			 * !~/Overview of .*? Overview of/){ return 1;; } $sth =
			 * $dbh->prepare("select word from "
			 * .$prefix."_unknownwords where word = '$base'"); $sth->execute()
			 * or print STDOUT "$sth->errstr\n"; return 1 if $sth->rows > 0; }
			 */

			// if $base is in WN or unknownwords table, or if $word has sole pos
			// adj
			// in WN, return 1: e.g. scalelike
			else {
				if (myWN.isSoleAdjective(word)) {
					return true;
				}
				if (baseInWN) {
					return true;
				}
				if (this.unknownWordTable.containsKey(base)) {
					return true;
				}
			}

			return flag;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void markupbypattern() {
		System.out.println("markupbypattern start");
		// int cap=this.sentence.size();
		int cap = this.sentenceTable.size();
		// ((ArrayList)this.tag).ensureCapacity(cap);
		// ((ArrayList)this.modifier).ensureCapacity(cap);
		// for (int i=0;i<this.originalSent.size();i++) {
		for (int i = 0; i < cap; i++) {
			// case 1
			// if (this.originalSent.get(i).matches("^x=.*")) {
			if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^x=.*")) {
				// tag.set(i, "chromosome");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("chromosome");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 2
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^2n=.*")) {
				// tag.set(i, "chromosome");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("chromosome");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 3
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^x .*")) {
				// tag.set(i, "chromosome");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("chromosome");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 4
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^2n .*")) {
				// tag.set(i, "chromosome");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("chromosome");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 5
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^2 n.*")) {
				// tag.set(i, "chromosome");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("chromosome");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 6
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^fl.*")) {
				// tag.set(i, "flowerTime");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("flowerTime");
				this.sentenceTable.get(i).setModifier("");
			}
			// case 7
			else if (this.sentenceTable.get(i).getOriginalSentence()
					.matches("^fr.*")) {
				// tag.set(i, "flowerTime");
				// modifier.set(i, "");
				this.sentenceTable.get(i).setTag("flowerTime");
				this.sentenceTable.get(i).setModifier("");
			}
		}
		System.out.println("markupbypattern end");
	}

	// private String IGNOREPTN ="(IGNOREPTN)"; //disabled
	public void markupIgnore() {
		// $sth =
		// $dbh->prepare("update ".$prefix."_sentence set tag = 'ignore', modifier='' where originalsent rlike '(^| )$IGNOREPTN ' ");
		for (int i = 0; i < this.sentenceTable.size(); i++) {
			String thisSent = this.sentenceTable.get(i).getOriginalSentence();
			String p = "(^| )" + this.IGNOREPTN;
			if (thisSent.matches("(^|^ )" + this.IGNOREPTN + ".?")) {
				sentenceTable.get(i).setTag("ignore");
				sentenceTable.get(i).setModifier("");
			}
		}
	}

	public void learn(List<Treatment> treatments) {
		// TODO: Implement the unsupervised algorithm here!
		System.out.println("Method: learn\n");
	}

	public Map<Treatment, List<String>> getSentences() {
		System.out.println("Method: getSentences\n");
		return null;
	}

	public Map<Treatment, List<String>> getSentencesForOrganStateMarker() {
		System.out.println("Method: getSentencesForOrganStateMarker\n");
		return null;
	}

	public List<String> getAdjNouns() {
		System.out.println("Method: getAdjNouns\n");
		return null;
	}

	public Map<String, String> getAdjNounSent() {
		System.out.println("Method: getAdjNounsSent\n");
		return null;
	}

	public Set<String> getBracketTags() {
		System.out.println("Method: getAdjNounsSent\n");
		return null;
	}

	public Set<String> getWordRoleTags() {
		System.out.println("Method: getSentenceTags\n");
		return null;
	}

	public Map<String, Set<String>> getWordToSources() {
		System.out.println("Method: getBracketTags\n");
		return null;
	}

	public Map<String, Set<String>> getRoleToWords() {
		System.out.println("Method: getRoleToWords\n");
		return null;

	}

	public Map<String, Set<String>> getWordsToRoles() {
		System.out.println("Method: getWordsToRoles\n");
		return null;
	}

	public Map<String, String> getHeuristicNouns() {
		System.out.println("Method: getHeuristicNouns\n");
		return null;
	}

	public Map<Treatment, List<String>> getSentenceTags() {
		System.out.println("Method: getTermCategories\n");
		return null;
	}

	public Map<String, Set<String>> getTermCategories() {
		System.out.println("Method: getTermCategories\n");
		return null;
	}

	public boolean testWN(String word) {
		try {
			WordNetAPI myWN = new WordNetAPI(
					"/Users/nescent/Phenoscape/WordNet-3.0/dict", false);

			if (myWN.isNoun(word)) {
				System.out.println(word + " is a noun");
			}
			if (myWN.isVerb(word)) {
				System.out.println(word + " is a verb");
			}
			if (myWN.isAdjective(word)) {
				System.out.println(word + " is a adj");
			}
			if (myWN.isAdverb(word)) {
				System.out.println(word + " is a adv");
			}

			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	

	// ---------------TEST Helper function----------------
	public void printWordPOSTable() {
		Iterator<Map.Entry<WordPOSKey, WordPOSValue>> entries = this.wordPOSTable
				.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<WordPOSKey, WordPOSValue> entry = entries.next();
			System.out.println(entry.getKey().getWord() + ", "
					+ entry.getKey().getPOS() + ", "
					+ entry.getValue().getRole() + ", "
					+ entry.getValue().getCertainTye() + ", "
					+ entry.getValue().getCertainTyl() + ", "
					+ entry.getValue().getSavedFlag() + ", "
					+ entry.getValue().getSavedID());
		}
	}
}