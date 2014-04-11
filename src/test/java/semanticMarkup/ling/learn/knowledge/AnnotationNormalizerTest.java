package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

public class AnnotationNormalizerTest {
	private AnnotationNormalizer tester;
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
		Map<String, Boolean> checkedModifiers = new HashMap<String, Boolean>();

		this.tester = new AnnotationNormalizer(configuration.getLearningMode(),
				checkedModifiers, myLearnerUtility);

		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(
				wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant,
				myWordFormUtility);
	}

	@Test
	public void testFinalizeCompoundModifier() {
		// case 1
		String modifier = "maxillary and [dentary] tooth_ bearing";
		String tag = "elements";
		String sentence = "maxillary and dentary <B>tooth_</B> bearing <N>elements</N>";

		assertEquals("finalizeCompoundModifier case 1", modifier,
				tester.finalizeCompoundModifier(myDataHolder, modifier, tag,
						sentence));

	}
	
	@Test
	public void testGetMCount(){
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
		
		assertEquals("getMCount", 5, tester.getMCount(myDataHolder, "marginal"));
	}
	
	@Test
	public void testNormalizeItem() {
		assertEquals("normalizeItem case 2", "general", tester.normalizeItem("general"));
		assertEquals("normalizeItem case 2", "fin", tester.normalizeItem("fins"));
		assertEquals("normalizeItem case 2", "squamosal and quadratojugal and bone",
				tester.normalizeItem("squamosal and quadratojugal and bones"));
	}

}
