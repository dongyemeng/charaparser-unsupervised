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
import semanticMarkup.ling.learn.utility.LearnerUtility;
import semanticMarkup.ling.transform.ITokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPSentencesTokenizer;
import semanticMarkup.ling.transform.lib.OpenNLPTokenizer;

public class PronounCharactersAnnotatorTest {
	private PronounCharactersAnnotator tester;
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

		this.tester = new PronounCharactersAnnotator(myLearnerUtility);
	}

	@Test
	public void testPronounCharacterSubjectHelper() {
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
		assertEquals("pronounCharacterSubjectHelper null", null,
				tester.pronounCharacterSubjectHelper(lead, sentence, modifier,
						tag));

		// case 1.1.1
		lead = "size of";
		sentence = "<B>size</B> <B>of</B> <N>lateral</N> <B>gular</B>";
		modifier = "";
		tag = "ditto";
		target.clear();
		target.add("");
		target.add("lateral");
		assertEquals("pronounCharacterSubjectHelper case 1.1.1", target,
				tester.pronounCharacterSubjectHelper(lead, sentence, modifier,
						tag));

		// case 1.2.1.1
		lead = "body scale profile";
		sentence = "<M>body</M> <N>scale</N> <B>profile</B>";
		modifier = "body";
		tag = "scale";
		target.clear();
		target.add("body ");
		target.add("scale");
		assertEquals("pronounCharacterSubjectHelper case 1.2.1.1", target,
				tester.pronounCharacterSubjectHelper(lead, sentence, modifier,
						tag));

		// case 1.2.1.1
		lead = "lyre_ shaped";
		sentence = "<N>lyre_</N> <B>shaped</B>";
		modifier = "";
		tag = "lyre_";
		target.clear();
		target.add("");
		target.add("ditto");
		assertEquals("pronounCharacterSubjectHelper case 1.2.1.2", target,
				tester.pronounCharacterSubjectHelper(lead, sentence, modifier,
						tag));

		// case 1.2.2
		lead = "shape of";
		sentence = "<B>shape</B> <B>of</B> opercular <N>ossification</N>";
		modifier = "";
		tag = "ditto";
		target.clear();
		target.add("");
		target.add("ditto");
		assertEquals("pronounCharacterSubjectHelper case 1.2.2", target,
				tester.pronounCharacterSubjectHelper(lead, sentence, modifier,
						tag));
	}

	@Test
	public void testPronounCharacterSubjectHelper4() {
		String lead;
		String sentence;
		String modifier;
		String tag;

		// null
		lead = "prismatic calcified cartilage";
		sentence = "prismatic calcified <N>cartilage</N>";
		modifier = null;
		tag = null;
		assertEquals("pronounCharacterSubjectHelper null", null,
				tester.pronounCharacterSubjectHelper4(lead, sentence, modifier,
						tag));
	}

}
