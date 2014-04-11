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

public class DittoAnnotatorTest {
	private DittoAnnotator tester;
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
		
		this.tester = new DittoAnnotator(myLearnerUtility);
		
		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant, myWordFormUtility);
	}
	
	@Test
	public void testDittoHelper() {
		String nPhrasePattern = "(?:<[A-Z]*[NO]+[A-Z]*>[^<]+?<\\/[A-Z]*[NO]+[A-Z]*>\\s*)+";
		String mPhrasePattern = "(?:<[A-Z]*M[A-Z]*>[^<]+?<\\/[A-Z]*M[A-Z]*>\\s*)+";
		

		assertEquals("ditto helper", 0, tester.dittoHelper(myDataHolder, 0, "prismatic calcified <N>cartilage</N>", nPhrasePattern, mPhrasePattern));
		
		assertEquals("ditto helper", 1, tester.dittoHelper(
				myDataHolder, 0, "<B>absent</B>", nPhrasePattern,
				mPhrasePattern));
		assertEquals("ditto helper", 21, 
				tester.dittoHelper(myDataHolder, 0, 
						"<B>in</B> tubes below visceral surface <B>of</B> <M>dermal</M> <N>bone</N>", 
						nPhrasePattern, mPhrasePattern));		
	}

}
