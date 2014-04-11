package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import semanticMarkup.know.lib.WordNetPOSKnowledgeBase;
import semanticMarkup.ling.learn.Configuration;
import semanticMarkup.ling.learn.Learner;
import semanticMarkup.ling.learn.dataholder.DataHolder;
import semanticMarkup.ling.learn.utility.LearnerUtility;
import semanticMarkup.ling.learn.utility.WordFormUtility;
import semanticMarkup.ling.transform.ITokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPSentencesTokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPTokenizer;

public class PhraseClauseAnnotatorTest {
	private PhraseClauseAnnotator tester;
	private Configuration configuration;
	private LearnerUtility myLearnerUtility;

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

		this.tester = new PhraseClauseAnnotator(myLearnerUtility);
	}
	
	@Test
	public void testPhraseClauseHelper() {
		
		String sentence = "mid and distal <B>progressively</B> smaller , <B>becoming</B> <B>sessile</B> , <B>narrower</B> , <N>bases</N> obtuse to acuminate , <M><B>cauline</B></M> <B>usually</B> 15 or fewer <B>.</B>";		
		assertEquals("phraseChauseHelper - empty return", new ArrayList<String>(), tester.phraseClauseHelper(sentence));
		
		sentence = "<M><B>cauline</B></M> <B>linear</B> or <B>oblong</B> , <B>crowded</B> or well separated , <B>usually</B> <B>not</B> surpassing <N>heads</N> <B>.</B>";
		List<String> target = new ArrayList<String>(2);
		target.add("");
		target.add("heads");
		assertEquals("phraseChauseHelper", target, tester.phraseClauseHelper(sentence));
		
		sentence = "distal <M><B>cauline</B></M> <B>sessile</B> , ?<N>decurrent</N> <B>.</B>";
		target.clear();
		target.add("");
		target.add("decurrent");
		assertEquals("phraseChauseHelper", target, tester.phraseClauseHelper(sentence));
	}

}
