package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import semanticMarkup.know.lib.WordNetPOSKnowledgeBase;
import semanticMarkup.ling.learn.Configuration;
import semanticMarkup.ling.learn.Learner;
import semanticMarkup.ling.learn.dataholder.DataHolder;
import semanticMarkup.ling.learn.dataholder.SentenceStructure;
import semanticMarkup.ling.learn.utility.LearnerUtility;
import semanticMarkup.ling.learn.utility.WordFormUtility;
import semanticMarkup.ling.transform.ITokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPSentencesTokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPTokenizer;

public class IgnorePatternAnnotatorTest {
	private IgnorePatternAnnotator tester;
	private Configuration configuration;
	private LearnerUtility myLearnerUtility;
	private DataHolder myDataHolder;

	@Before
	public void initialize() {
		this.configuration = new Configuration();
		ITokenizer sentenceDetector = new OpenNLPSentencesTokenizer(
				configuration.getOpenNLPSentenceDetectorDir());
		ITokenizer tokenizer = new OpenNLPTokenizer(
				configuration.getOpenNLPTokenizerDir());

		WordNetPOSKnowledgeBase wordNetPOSKnowledgeBase = null;
		try {
			wordNetPOSKnowledgeBase = new WordNetPOSKnowledgeBase(
					configuration.getWordNetDictDir(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.myLearnerUtility = new LearnerUtility(sentenceDetector, tokenizer,
				wordNetPOSKnowledgeBase);

		this.tester = new IgnorePatternAnnotator();

		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(
				wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant,
				myWordFormUtility);
	}

	@Test
	public void testMarkupIgnore() {
		myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "sentence1", "IGNOREPTN", "lead1", "status1", "tag1", "modifier1", "type1"}));
		tester.markupIgnore(myDataHolder);
		
		List<SentenceStructure> targetSentenceHolder = new LinkedList<SentenceStructure>();
		targetSentenceHolder.add(new SentenceStructure(0, "source1", "sentence1", "IGNOREPTN", "lead1", "status1", "ignore", "", "type1"));
		
		assertEquals("markupIgnore", targetSentenceHolder, myDataHolder.getSentenceHolder());
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

}
