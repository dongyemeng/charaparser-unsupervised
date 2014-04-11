package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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

public class PatternBasedAnnotatorTest {

	private PatternBasedAnnotator tester;
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
		
		this.tester = new PatternBasedAnnotator();
		
		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant, myWordFormUtility);
	}
	
	@Test
	public void testRun() {
		this.myDataHolder.add2Holder(DataHolder.SENTENCE, 
				Arrays.asList(new String[] {"source1", "sentence1", "x=word word word", "lead1", "status1", "tag1", "modifier1", "type1"}));
		tester.run(myDataHolder);;
		
		List<SentenceStructure> targetSentenceHolder = new LinkedList<SentenceStructure>();
		targetSentenceHolder.add(new SentenceStructure(0, "source1", "sentence1", "x=word word word", "lead1", "status1", "chromosome", "", "type1"));
		
		assertEquals("markupByPattern", targetSentenceHolder, myDataHolder.getSentenceHolder());
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


}
