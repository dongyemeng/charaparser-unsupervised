package semanticMarkup.ling.learn;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import semanticMarkup.know.lib.WordNetPOSKnowledgeBase;
import semanticMarkup.ling.learn.auxiliary.GetNounsAfterPtnReturnValue;
import semanticMarkup.ling.learn.auxiliary.StringAndInt;
import semanticMarkup.ling.learn.dataholder.DataHolder;
import semanticMarkup.ling.learn.dataholder.SentenceStructure;
import semanticMarkup.ling.learn.dataholder.WordPOSKey;
import semanticMarkup.ling.learn.knowledge.Constant;
import semanticMarkup.ling.learn.utility.LearnerUtility;
import semanticMarkup.ling.transform.ITokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPSentencesTokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPTokenizer;

public class LearnerTest {

	private Learner tester;

	@Before
	public void initialize() {
		this.tester = learnerFactory();
	}

//	@Test
//	public void testLearn() {
//		Configuration myConfiguration = new Configuration();
//		Utility myUtility = new Utility(myConfiguration);
//		DataHolder results = new DataHolder(myConfiguration, myUtility);
//
//		Map<String, String> myHeuristicNounTable = results
//				.getHeuristicNounTable();
//		myHeuristicNounTable.put("word1", "type1");
//
//		List<Sentence> mySentenceTable = results.getSentenceHolder();
//		mySentenceTable.add(new Sentence(0, "source1", "sentence1",
//				"originalSentence", "lead1", "status1", "tag1", "modifier1",
//				"type1"));
//
//		// Learner tester = new Learner("plain","res/WordNet/WordNet-3.0/dict");
//
//		// assertEquals ("learner", results, tester.Learn(tms));
//
//		// results = tester.Learn(tms);
//
//		// assertEquals ("learner", results, tester.Learn(tms));
//	}





//	@Test
//	public void testPopulateUnknownWordsTable() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testDiscountPOS() {
		// case "all"
		// see doItCaseHandle case 2
	}
	
	@Test
	public void testResolveConfict() {
		// see doItCaseHandle case 2
	}

	@Test
	public void testChangePOS(){
		// see doItCaseHandle case 2
	}
	
	@Test
	public void testUpdatePOS(){
		// see doItCaseHandle case 2
	}
	











	


	
	@Test
	public void testMarkupByPattern() {
		Learner myTester = learnerFactory();

		myTester.getDataHolder().add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "sentence1", "x=word word word", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myTester.markupByPattern();
		
		List<SentenceStructure> targetSentenceHolder = new LinkedList<SentenceStructure>();
		targetSentenceHolder.add(new SentenceStructure(0, "source1", "sentence1", "x=word word word", "lead1", "status1", "chromosome", "", "type1"));
		
		assertEquals("markupByPattern", targetSentenceHolder, myTester.getDataHolder().getSentenceHolder());
	}
	
	@Test
	public void testMarkupByPatternHelper(){
		// case 1
		SentenceStructure mySentence1 = new SentenceStructure(0, "source1", "sentence1", "x=word word word", "lead1", "status1", "tag1", "modifier1", "type1");
		SentenceStructure target1 = new SentenceStructure(0, "source1", "sentence1", "x=word word word", "lead1", "status1", "chromosome", "", "type1");
		tester.markupByPatternHelper(mySentence1);
		assertEquals("markupByPatternHelper - case 1", target1,mySentence1);
		
		// case 2
		SentenceStructure mySentence2 = new SentenceStructure(1, "source2", "sentence2", "2n=abc...", "lead2", "status2", "tag2", "modifier2", null);
		SentenceStructure target2 = new SentenceStructure(1, "source2", "sentence2", "2n=abc...", "lead2", "status2", "chromosome", "", null);
		tester.markupByPatternHelper(mySentence2);
		assertEquals("markupByPatternHelper - case 2", target2,mySentence2);
		
		// case 3
		SentenceStructure mySentence3 = new SentenceStructure(2, "source", "sentence", "x word word", "lead", "status", "tag", "modifier", null);
		SentenceStructure target3 = new SentenceStructure(2, "source", "sentence", "x word word", "lead", "status", "chromosome", "", null);
		tester.markupByPatternHelper(mySentence3);
		assertEquals("markupByPatternHelper - case 3", target3, mySentence3);
		
		// case 4
		SentenceStructure mySentence4 = new SentenceStructure(3, "source", "sentence", "2n word word", "lead",null, "tag", "modifier", null);
		SentenceStructure target4 = new SentenceStructure(3, "source", "sentence", "2n word word", "lead", null, "chromosome", "", null);
		tester.markupByPatternHelper(mySentence4);
		assertEquals("markupByPatternHelper - case 4", target4, mySentence4);
		
		// case 5
		SentenceStructure mySentence5 = new SentenceStructure(4, "source", "sentence", "2 nword word", "lead", "status", "tag", "modifier", "");
		SentenceStructure target5 = new SentenceStructure(4, "source", "sentence", "2 nword word", "lead", "status", "chromosome", "", "");
		tester.markupByPatternHelper(mySentence5);
		assertEquals("markupByPatternHelper - case 5", target5, mySentence5);
		
		// case 6
		SentenceStructure mySentence6 = new SentenceStructure(5, "source", "sentence", "fl. word word", "lead", "status", null, null, "");
		SentenceStructure target6 = new SentenceStructure(5, "source", "sentence", "fl. word word", "lead", "status", "flowerTime", "", "");
		tester.markupByPatternHelper(mySentence6);
		assertEquals("markupByPatternHelper - case 6", target6, mySentence6);
		
		// case 7
		SentenceStructure mySentence7 = new SentenceStructure(6, "source", "sentence", "fr.word word", "lead", "status", null, "", "");
		SentenceStructure target7 = new SentenceStructure(6, "source", "sentence", "fr.word word", "lead", "status", "fruitTime", "", "");
		tester.markupByPatternHelper(mySentence7);
		assertEquals("markupByPatternHelper - case 7", target7, mySentence7);
	}

	@Test
	public void testMarkupIgnore() {
		Learner myTester = learnerFactory();

		myTester.getDataHolder().add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "sentence1", "IGNOREPTN", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myTester.markupIgnore();
		
		List<SentenceStructure> targetSentenceHolder = new LinkedList<SentenceStructure>();
		targetSentenceHolder.add(new SentenceStructure(0, "source1", "sentence1", "IGNOREPTN", "lead1", "status1", "ignore", "", "type1"));
		
		assertEquals("markupIgnore", targetSentenceHolder, myTester.getDataHolder().getSentenceHolder());

	}

	@Test
	public void testMarkupIgnoreHelper() {
		SentenceStructure mySentence1 = new SentenceStructure(0, "source", "sentence", "IGNOREPTN", "lead", "status", null, "", "");
		SentenceStructure target1 = new SentenceStructure(0, "source", "sentence", "IGNOREPTN", "lead", "status", "ignore", "", "");
		tester.markupIgnoreHelper(mySentence1);
		assertEquals("markupIgnoreHelper", target1, mySentence1);
		
		SentenceStructure mySentence2 = new SentenceStructure(1, "source", "sentence", " IGNOREPTN", "lead", "status", null, "", "");
		SentenceStructure target2 = new SentenceStructure(1, "source", "sentence", " IGNOREPTN", "lead", "status", "ignore", "", "");
		tester.markupIgnoreHelper(mySentence2);
		assertEquals("markupIgnoreHelper", target2, mySentence2);
	}

	


    @Test
    public void testHasHead(){
        assertEquals("hasHead - null", false, 
        		tester.hasHead(	null, 
        						Arrays.asList("passing through most".split(" "))));
        assertEquals("hasHead - not has", false, 
        		tester.hasHead(	Arrays.asList("passing through".split(" ")), 
        						Arrays.asList("passing throug most".split(" "))));
        assertEquals("hasHead - empty head", true, 
        		tester.hasHead(	new ArrayList<String>(), 
        						Arrays.asList("passing through most".split(" "))));
        assertEquals("hasHead - has", true, 
        		tester.hasHead(	Arrays.asList("passing through".split(" ")), 
        						Arrays.asList("passing through most".split(" "))));
        assertEquals("hasHead - head same as list", true, 
        		tester.hasHead(	Arrays.asList("passing through most".split(" ")), 
        						Arrays.asList("passing through most".split(" "))));
    }
    
   
    @Test
    public void testWrapupMarkup() {		
//		// case 1
//		Learner myTester1 = learnerFactory();
//		
//		myTester1.getDataHolder().getSentenceHolder().add(new SentenceStructure(7, "src", "sent", "osent","sensory line not null","status","notnull","modifer","type"));
//		myTester1.getDataHolder().getSentenceHolder().add(new SentenceStructure(192, "src", "sent", "osent","sensory line ignore","status","ignore","modifer","type"));
//		myTester1.getDataHolder().getSentenceHolder().add(new SentenceStructure(193, "src", "sent", "osent","sensory line canal","status",null,"modifer","type"));
//		myTester1.getDataHolder().getSentenceHolder().add(new SentenceStructure(267, "src", "sent", "osent","sensory line canals","status",null,"modifer","type"));
//		myTester1.getDataHolder().getSentenceHolder().add(new SentenceStructure(269, "src", "sent", "osent","opening via tubular","status",null,"modifer","type"));
//		
//		myTester1.getDataHolder().add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"line", "s", "*", "1", "1", "", null}));
//		myTester1.getDataHolder().add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"canals", "p", "*", "1", "1", "", null}));
//		
//		myTester1.wrapupMarkup();
//		
//		assertEquals("wrapupmarkup - case 1 - tag sentence", "sensory line canal", myTester1.getDataHolder().getSentence(193).getTag());
//		assertEquals("wrapupmarkup - case 1 - tag sentence", "sensory line", myTester1.getDataHolder().getSentence(267).getTag());
//		
//		// case 2
//		Learner myTester2 = learnerFactory();
//		
//		myTester2.getDataHolder().getSentenceHolder().add(new SentenceStructure(115, "src", "sent", "osent","midsagittal fontanel absent","status",null,"modifer","type"));
//		myTester2.getDataHolder().getSentenceHolder().add(new SentenceStructure(116, "src", "sent", "osent","midsagittal fontanel present","status",null,"modifer","type"));
//		
//		myTester2.getDataHolder().add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"fontanel", "s", "*", "1", "1", "", null}));
//		myTester2.getDataHolder().add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"absent", "b", "*", "1", "1", "", null}));
//		myTester2.getDataHolder().add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"present", "b", "*", "1", "1", "", null}));
//		
//		myTester2.wrapupMarkup();
//		
//		assertEquals("wrapupmarkup - case 2 - tag sentence", "midsagittal fontanel", myTester2.getDataHolder().getSentence(115).getTag());
//		assertEquals("wrapupmarkup - case 2 - tag sentence", "midsagittal fontanel", myTester2.getDataHolder().getSentence(116).getTag());
    }
    
    @Test
    public void testOneLeadMarkup(){
		Learner myTester = learnerFactory();
		
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(0, "src", "sent", "osent","lead1 lead2","status","tag tag","modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(1, "src", "sent", "osent","midsagittal fontanel present","status",null,"modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(2, "src", "sent", "osent","midsagittal fontanel present","status","tag1","modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(3, "src", "sent", "osent","tagx","status",null,"modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(4, "src", "sent", "osent","tagx tagx","status",null,"modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(5, "src", "sent", "osent","midsagittal fontanel present","status","tagx","modifer","type"));
		myTester.getDataHolder().getSentenceHolder().add(new SentenceStructure(6, "src", "sent", "osent","midsagittal fontanel","status","tag2","modifer","type"));
		
		myTester.oneLeadWordMarkup(myTester.getDataHolder().getCurrentTags());
		assertEquals("oneLeadMarkup", "tagx", myTester.getDataHolder().getSentence(3).getTag());		
    }
	
	@Test
	public void testUnknownWordBootstrapping(){
		
//		// 1. Preprocessing
//		Learner myTester1 = learnerFactory();
//		myTester1.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("word1 unknown".split(" ")));
//		Set<String> expected = new HashSet<String>();
////		expected.add("")
//		assertEquals("unknownWordBootstrappingGetUnknownWord", expected , myTester1.unknownWordBootstrappingGetUnknownWord("(ee)"));
		
		
		
		// 3. Postprocessing
		Learner myTester3 = learnerFactory();
		
		myTester3.getDataHolder().add2Holder(DataHolder.WORDPOS, 
				Arrays.asList(new String[] {"word1", "p", "role", "0", "0", "", ""}));
		myTester3.getDataHolder().add2Holder(DataHolder.WORDPOS, 
				Arrays.asList(new String[] {"word2", "b", "role", "0", "0", "", ""}));
		myTester3.getDataHolder().add2Holder(DataHolder.WORDPOS, 
				Arrays.asList(new String[] {"word3", "s", "role", "0", "0", "", ""}));
		
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("word1 word1".split(" ")));
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("word2 unknown".split(" ")));
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("_wORd3 unknown".split(" ")));
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("word?_4 unknown".split(" ")));
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("nor unknown".split(" ")));
		myTester3.getDataHolder().add2Holder(DataHolder.UNKNOWNWORD, Arrays.asList("word_6 unknown".split(" ")));

		
		myTester3.getDataHolder().getSentenceHolder().add(new SentenceStructure(0, "src", "word1 word_6 word2", "osent","lead","status","tag","modifer","type"));
		myTester3.getDataHolder().getSentenceHolder().add(new SentenceStructure(1, "src", "word_6 word2", "osent","lead","status","tag","modifer","type"));
		myTester3.getDataHolder().getSentenceHolder().add(new SentenceStructure(2, "src", "word1 word6 word2", "osent","lead","status","tag","modifer","type"));
		
		myTester3.unknownWordBootstrappingPostprocessing();
		assertEquals("unknownWordBootstrapping - Postprocessing", "word1 <B>word_6</B> word2", myTester3.getDataHolder().getSentence(0).getSentence());
		assertEquals("unknownWordBootstrapping - Postprocessing", "<B>word_6</B> word2", myTester3.getDataHolder().getSentence(1).getSentence());
		assertEquals("unknownWordBootstrapping - Postprocessing", "word1 word6 word2", myTester3.getDataHolder().getSentence(2).getSentence());
		
		myTester3.unknownWordBootstrappingPostprocessing();
		
	}
	
	@Test
	public void testDittoHelper() {
		String nPhrasePattern = "(?:<[A-Z]*[NO]+[A-Z]*>[^<]+?<\\/[A-Z]*[NO]+[A-Z]*>\\s*)+";
		String mPhrasePattern = "(?:<[A-Z]*M[A-Z]*>[^<]+?<\\/[A-Z]*M[A-Z]*>\\s*)+";
		
		Learner myTester = learnerFactory();
		assertEquals("ditto helper", 0, myTester.dittoHelper(myTester.getDataHolder(), 0, "prismatic calcified <N>cartilage</N>", nPhrasePattern, mPhrasePattern));
		
		assertEquals("ditto helper", 1, myTester.dittoHelper(
				myTester.getDataHolder(), 0, "<B>absent</B>", nPhrasePattern,
				mPhrasePattern));
		assertEquals("ditto helper", 21, 
				myTester.dittoHelper(myTester.getDataHolder(), 0, 
						"<B>in</B> tubes below visceral surface <B>of</B> <M>dermal</M> <N>bone</N>", 
						nPhrasePattern, mPhrasePattern));		
	}
	
	@Test
	public void testPhraseClauseHelper() {
		Learner myTester = learnerFactory();
		
		String sentence = "mid and distal <B>progressively</B> smaller , <B>becoming</B> <B>sessile</B> , <B>narrower</B> , <N>bases</N> obtuse to acuminate , <M><B>cauline</B></M> <B>usually</B> 15 or fewer <B>.</B>";		
		assertEquals("phraseChauseHelper - empty return", new ArrayList<String>(), myTester.phraseClauseHelper(sentence));
		
		sentence = "<M><B>cauline</B></M> <B>linear</B> or <B>oblong</B> , <B>crowded</B> or well separated , <B>usually</B> <B>not</B> surpassing <N>heads</N> <B>.</B>";
		List<String> target = new ArrayList<String>(2);
		target.add("");
		target.add("heads");
		assertEquals("phraseChauseHelper", target, myTester.phraseClauseHelper(sentence));
		
		sentence = "distal <M><B>cauline</B></M> <B>sessile</B> , ?<N>decurrent</N> <B>.</B>";
		target.clear();
		target.add("");
		target.add("decurrent");
		assertEquals("phraseChauseHelper", target, myTester.phraseClauseHelper(sentence));
	}
	
	@Test
	public void testPronounCharacterSubjectHelper() {
		Learner myTester = learnerFactory();
		List<String> target = new ArrayList<String>(2);
		String lead;
		String sentence;
		String modifier;
		String tag;
		
		// null
		lead = "prismatic calcified cartilage";
		sentence = "prismatic calcified <N>cartilage</N>";
		modifier = null;
		tag = null;
		assertEquals("pronounCharacterSubjectHelper null", null, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
		
		// case 1.1.1
		lead = "size of";
		sentence = "<B>size</B> <B>of</B> <N>lateral</N> <B>gular</B>";
		modifier = "";
		tag = "ditto";
		target.clear();
		target.add("");
		target.add("lateral");
		assertEquals("pronounCharacterSubjectHelper case 1.1.1", target, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
		
		// case 1.2.1.1
		lead = "body scale profile";
		sentence = "<M>body</M> <N>scale</N> <B>profile</B>";
		modifier = "body";
		tag = "scale";
		target.clear();
		target.add("body ");
		target.add("scale");
		assertEquals("pronounCharacterSubjectHelper case 1.2.1.1", target, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
		
		// case 1.2.1.1
		lead = "lyre_ shaped";
		sentence = "<N>lyre_</N> <B>shaped</B>";
		modifier = "";
		tag = "lyre_";
		target.clear();
		target.add("");
		target.add("ditto");
		assertEquals("pronounCharacterSubjectHelper case 1.2.1.2", target, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
				
		// case 1.2.2
		lead = "shape of";
		sentence = "<B>shape</B> <B>of</B> opercular <N>ossification</N>";
		modifier = "";
		tag = "ditto";
		target.clear();
		target.add("");
		target.add("ditto");
		assertEquals("pronounCharacterSubjectHelper case 1.2.2", target, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
	}
	
	@Test
	public void testPronounCharacterSubjectHelper4() {
		Learner myTester = learnerFactory();
		List<String> target = new ArrayList<String>(2);
		String lead;
		String sentence;
		String modifier;
		String tag;
		
		// null
		lead = "prismatic calcified cartilage";
		sentence = "prismatic calcified <N>cartilage</N>";
		modifier = null;
		tag = null;
		assertEquals("pronounCharacterSubjectHelper null", null, myTester.pronounCharacterSubjectHelper4(lead, sentence, modifier, tag));
	
//		
//				lead = "skull shape";
//				sentence = "<N>skull</N> <B>shape</B>";
//				modifier = "";
//				tag = "skull";
//				target.clear();
//				target.add("");
//				target.add("skull");
//				assertEquals("pronounCharacterSubjectHelper4", target, myTester.pronounCharacterSubjectHelper(lead, sentence, modifier, tag));
				
		
	}
	
	@Test
	public void testAndOrTagCase1Helper() {
		Learner myTester = learnerFactory();		
		String sPattern = Constant.SEGANDORPTN;
		String wPattern = Constant.ANDORPTN;
		Set<String> token = new HashSet<String>();
		token.addAll(Arrays.asList("and or nor".split(" ")));
		token.add("\\");
		token.add("and / or");
		
		// test case 1
		String pattern = "qqn&p";
		List<String> words = new ArrayList<String>();
		words.addAll(Arrays.asList("smaller undifferentiated <N>plates</N> or tesserae".split(" ")));
		
		List<List<String>> target = new ArrayList<List<String>>();
		List<String> mPatterns = new ArrayList<String>();
		mPatterns.add("qq");
		List<String> mSegments = new ArrayList<String>();
		mSegments.add("smaller undifferentiated");
		List<String> sPatterns = new ArrayList<String>();
		sPatterns.addAll(Arrays.asList("n p".split(" ")));
		List<String> sSegments = new ArrayList<String>();
		sSegments.addAll(Arrays.asList("<N>plates</N> tesserae".split(" ")));	
		
		List<String> tagAndModifier1 = new ArrayList<String>();
		tagAndModifier1.add("");
		tagAndModifier1.add("smaller undifferentiated plates or tesserae");
		List<String> tagAndModifier2 = new ArrayList<String>();
		
		List<String> update1 = new ArrayList<String>();
		List<String> update2 = new ArrayList<String>();
		update2.add("tesserae");
		
		target.add(mPatterns);
		target.add(mSegments);
		target.add(sPatterns);
		target.add(sSegments);
		
		target.add(tagAndModifier1);
		target.add(tagAndModifier2);
		
		target.add(update1);
		target.add(update2);
		
		assertEquals("andOrTagCase1Helper", target, myTester.andOrTagCase1Helper(pattern, wPattern, words, token));
//		List<List<String>> returned = myTester.andOrTagCase1Helper(pattern, wPattern, words, token);
//		System.out.println(returned);
		
		// test case 2
		pattern = "n&qqnbq";
		words.clear();
		words.addAll(Arrays.asList("<N>perforate</N> or fenestrate anterodorsal <N>portion</N> <B>of</B> palatoquadrate".split(" ")));
		mPatterns.clear();
		mSegments.clear();
		sPatterns.clear();
		sSegments.clear();
		
		mPatterns.add("qq");
		mSegments.add("fenestrate anterodorsal");
		sPatterns.addAll(Arrays.asList("n n".split(" ")));
		sSegments.addAll(Arrays.asList("<N>perforate</N> <N>portion</N>".split(" ")));
		
		tagAndModifier1.clear();
		tagAndModifier1.add("");
		tagAndModifier1.add("perforate or fenestrate anterodorsal portion");
		tagAndModifier2.clear();
		
		update1.clear();
		update2.clear();
		
		assertEquals("andOrTagCase1Helper", target, myTester.andOrTagCase1Helper(pattern, wPattern, words, token));	
	}
	
	@Test
	public void testFinalizeCompoundModifier() {
		Learner myTester = learnerFactory();	
		
		// case 1
		String modifier = "maxillary and [dentary] tooth_ bearing";
		String tag = "elements";
		String sentence = "maxillary and dentary <B>tooth_</B> bearing <N>elements</N>";
				
		assertEquals("finalizeCompoundModifier case 1", modifier,
				myTester.finalizeCompoundModifier(myTester.getDataHolder(), modifier, tag, sentence));
		
	}
	
	@Test
	public void testGetMCount(){
		Learner myTester = learnerFactory();
		DataHolder myDataHolder = myTester.getDataHolder();
		
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "<B>number</B> <B>of</B> <M><B>marginal</B></M> <N>bones</N> <B>alongside</B> postparietal", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "through <M><B>marginal</B></M> <N>bones</N> <B>alongside</B> postparietal", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "<M><B>marginal</B></M> <N>teeth</N> <B>on</B> dentary", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "<B>broad</B> <M><B>marginal</B></M> <N>tooth</N> <B>field</B>", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "<B>narrow</B> <M><B>marginal</B></M> <N>tooth</N> <N>row</N>", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "anterodorsal <B>peg_</B> like <N>process</N> <B>on</B> <N>scale</N>", "o1", "lead1", "status1", "tag1", "modifier1", "type1"}));
		
		assertEquals("getMCount", 5, myTester.getMCount(myDataHolder, "marginal"));
	}
	

	
	@Test
	public void testNormalizeItem() {
		Learner myTester = learnerFactory();
		DataHolder myDataHolder = myTester.getDataHolder();
		assertEquals("normalizeItem case 2", "general", myTester.normalizeItem("general"));
		assertEquals("normalizeItem case 2", "fin", myTester.normalizeItem("fins"));
		assertEquals("normalizeItem case 2", "squamosal and quadratojugal and bone",
				myTester.normalizeItem("squamosal and quadratojugal and bones"));
	}
	
	@Test
	public void testAdjectiveSubjectsHelper(){
		Learner myTester = learnerFactory();
		DataHolder myDataHolder = myTester.getDataHolder();

		Set<String> typeModifiers = new HashSet<String>();
		Set<String> target = new HashSet<String>();
		target.addAll(Arrays.asList("open anterior paired".split(" ")));
		
		myDataHolder.add2Holder(
				DataHolder.SENTENCE,
				Arrays.asList(new String[] { "src", "endolymphatic <N>ducts</N> <M><B>open</B></M> <B>in</B> <M>dermal</M> <N>skull</N> roof", "osent", "lead",
						"status", "", "structure3", "type" }));
		
		myDataHolder.add2Holder(
				DataHolder.SENTENCE,
				Arrays.asList(new String[] { "src", "restricted to <B>the</B> <M>anterior</M> <B>third</B> <B>of</B> <B>the</B> <N>jaw</N>", "osent", "lead",
						"status", "", "structure3", "type" }));
		
		myDataHolder.add2Holder(
				DataHolder.SENTENCE,
				Arrays.asList(new String[] { "src", "<B>series</B> <B>of</B> <M>paired</M> <B>median</B> <N>skull</N> roofng <N>bones</N> <B>that</B> meet <B>at</B> <B>the</B> <M>dorsal</M> midline <B>of</B> <B>the</B> <N>skull</N>", "osent", "lead",
						"status", "", "structure3", "type" }));
		myDataHolder.add2Holder(
				DataHolder.SENTENCE,
				Arrays.asList(new String[] { "src", "anterior dorsal fontanelle", "osent", "lead",
						"status", "", "structure3", "type" }));
		
		assertEquals("adjectiveSubjectsHelper", target,
				myTester.adjectiveSubjectsPart1(myDataHolder, typeModifiers));
	}
	
	@Test
	public void testAdjectiveSubjectsPart2Helper1(){
		Learner myTester = learnerFactory();
		DataHolder myDataHolder = myTester.getDataHolder();
		Set<String> typeModifiers = new HashSet<String>();
		typeModifiers.addAll(Arrays.asList("open|paired|anterior|through".split("\\|")));
		assertEquals("AdjectiveSubjectsPart2Helper1", true, myTester.adjectiveSubjectsPart2Helper1("restricted to <B>the</B> <M>anterior</M> <B>third</B> <B>of</B> <B>the</B> <N>jaw</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", false, myTester.adjectiveSubjectsPart2Helper1("restricted to <B>the</B> <B>third</B> <B>of</B> <B>the</B> <N>jaw</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", true, myTester.adjectiveSubjectsPart2Helper1("<B>series</B> <B>of</B> <M>paired</M> <B>median</B> <N>skull</N> roofng <N>bones</N> <B>that</B> meet <B>at</B> <B>the</B> <M>dorsal</M> midline <B>of</B> <B>the</B> <N>skull</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", false, myTester.adjectiveSubjectsPart2Helper1("<B>series</B> <B>of paired median</B> <N>skull</N> roofng <N>bones</N> <B>that</B> meet <B>at</B> <B>the</B> <M>dorsal</M> midline <B>of</B> <B>the</B> <N>skull</N>", typeModifiers));
	}
	
	
	private Learner learnerFactory() {
		Learner tester;

		Configuration myConfiguration = new Configuration();
		ITokenizer tokenizer = new OpenNLPTokenizer(
				myConfiguration.getOpenNLPTokenizerDir());
		ITokenizer sentenceDetector = new OpenNLPSentencesTokenizer(
				myConfiguration.getOpenNLPSentenceDetectorDir());
		WordNetPOSKnowledgeBase wordNetPOSKnowledgeBase = null;
		try {
			wordNetPOSKnowledgeBase = new WordNetPOSKnowledgeBase(myConfiguration.getWordNetDictDir(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		LearnerUtility myLearnerUtility = new LearnerUtility(sentenceDetector,
				tokenizer, wordNetPOSKnowledgeBase);
		tester = new Learner(myConfiguration, tokenizer, myLearnerUtility);

		return tester;
	}
}
