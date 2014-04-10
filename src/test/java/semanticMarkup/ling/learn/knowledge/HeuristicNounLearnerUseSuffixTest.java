package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import semanticMarkup.know.lib.WordNetPOSKnowledgeBase;
import semanticMarkup.ling.learn.Configuration;
import semanticMarkup.ling.learn.dataholder.DataHolder;
import semanticMarkup.ling.learn.utility.LearnerUtility;
import semanticMarkup.ling.learn.utility.WordFormUtility;
import semanticMarkup.ling.transform.ITokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPSentencesTokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPTokenizer;

public class HeuristicNounLearnerUseSuffixTest {
	private HeuristicNounLearnerUseSuffix tester;
	private Configuration configuration;
	private LearnerUtility myLearnerUtility;
	private DataHolder myDataHolder;
	
	@Before
	public void initialize() {
		this.configuration = new Configuration();
		ITokenizer sentenceDetector = new OpenNLPSentencesTokenizer(
				configuration.getOpenNLPSentenceDetectorDir());
		ITokenizer tokenizer = new OpenNLPTokenizer(configuration.getOpenNLPTokenizerDir());
		
		WordNetPOSKnowledgeBase wordNetPOSKnowledgeBase = null;
		try {
			wordNetPOSKnowledgeBase = new WordNetPOSKnowledgeBase(configuration.getWordNetDictDir(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		this.myLearnerUtility = new LearnerUtility(sentenceDetector, tokenizer, wordNetPOSKnowledgeBase);
		
		this.tester = new HeuristicNounLearnerUseSuffix(this.myLearnerUtility);
		
		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant, myWordFormUtility);
	}

	
	@Test
	public void testPosBySuffix() {
		// Pattern 1: ^[a-z_]+(er|est|fid|form|ish|less|like|ly|merous|most|shaped)$
		// Pattern 2: ^[._.][a-z]+
		
		tester.posBySuffix(myDataHolder);
	}
	
	@Test
	public void testPosBySuffixCase1Helper(){
		assertEquals("posBySuffix Case1 - match", true, tester.posBySuffixCase1Helper(this.myDataHolder, "approximately"));
		assertEquals("posBySuffix Case1 - not match", false, tester.posBySuffixCase1Helper(this.myDataHolder, "bigger"));
		assertEquals("posBySuffix Case1 - match", true, tester.posBySuffixCase1Helper(this.myDataHolder, "bifid"));
		assertEquals("posBySuffix Case1 - not match", false, tester.posBySuffixCase1Helper(this.myDataHolder, "per"));
	}
	
	@Test
	public void testPosBySuffixCase2Helper(){
		assertEquals("posBySuffix Case2 - match", true, tester.posBySuffixCase2Helper(this.myDataHolder, "_nerved"));
		assertEquals("posBySuffix Case2 - not match", false, tester.posBySuffixCase2Helper(this.myDataHolder, "nerved"));
	}

	@Test
	public void testContainSuffix() {
		// test method containSuffix
		assertEquals("containSuffix less", true,
				tester.containSuffix(this.myDataHolder, "less", "", "less"));
		assertEquals("containSuffix ly", true,
				tester.containSuffix(this.myDataHolder, "slightly", "slight", "ly"));
		assertEquals("containSuffix er", false,
				tester.containSuffix(this.myDataHolder, "fewer", "few", "er"));
		assertEquals("containSuffix est", true,
				tester.containSuffix(this.myDataHolder, "fastest", "fast", "est"));
		assertEquals("containSuffix base is in WN", true,
				tester.containSuffix(this.myDataHolder, "platform", "plat", "form"));
		assertEquals("containSuffix sole adj", true,
				tester.containSuffix(this.myDataHolder, "scalelike", "scale", "like"));
		
		// case 3.1.2 and case 3.3.3 not tested
		assertEquals("containSuffix 111", false,
				tester.containSuffix(this.myDataHolder, "anterolaterally", "anterolateral", "ly")); // 111
		assertEquals("containSuffix 121", false,
				tester.containSuffix(this.myDataHolder, "mesially", "mesial", "ly")); // 121
		assertEquals("containSuffix 122", false,
				tester.containSuffix(this.myDataHolder, "per", "p", "er")); // 122
		assertEquals("containSuffix 212", false,
				tester.containSuffix(this.myDataHolder, "border", "bord", "er")); // 212
		assertEquals("containSuffix 212", false,
				tester.containSuffix(this.myDataHolder, "bigger", "bigg", "er")); // 212
		assertEquals("containSuffix 221", true,
				tester.containSuffix(this.myDataHolder, "anteriorly", "anterior", "ly")); // 221
		assertEquals("containSuffix 222", false,
				tester.containSuffix(this.myDataHolder, "corner", "corn", "er")); // 222
		assertEquals("containSuffix 222", true,
				tester.containSuffix(this.myDataHolder, "lower", "low", "er")); // 222
		assertEquals("containSuffix 223", true,
				tester.containSuffix(this.myDataHolder, "bifid", "bi", "fid")); // 223

	}

	
}
