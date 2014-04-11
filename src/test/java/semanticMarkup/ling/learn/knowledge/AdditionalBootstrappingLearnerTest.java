package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

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

public class AdditionalBootstrappingLearnerTest {
	private AdditionalBootstrappingLearner tester;
	private Configuration configuration;
	private LearnerUtility myLearnerUtility;

	private DataHolder dataholderFactory() {
		WordNetPOSKnowledgeBase wordNetPOSKnowledgeBase = null;
		try {
			wordNetPOSKnowledgeBase = new WordNetPOSKnowledgeBase(
					configuration.getWordNetDictDir(), false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(
				wordNetPOSKnowledgeBase);
		return new DataHolder(configuration, myConstant, myWordFormUtility);
	}

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

		this.tester = new AdditionalBootstrappingLearner(myLearnerUtility,
				configuration);
	}

	@Test
	public void testOneLeadMarkup() {
		DataHolder myDataHolder = this.dataholderFactory();
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(0, "src", "sent", "osent", "lead1 lead2",
						"status", "tag tag", "modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(1, "src", "sent", "osent",
						"midsagittal fontanel present", "status", null,
						"modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(2, "src", "sent", "osent",
						"midsagittal fontanel present", "status", "tag1",
						"modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(3, "src", "sent", "osent", "tagx",
						"status", null, "modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(4, "src", "sent", "osent", "tagx tagx",
						"status", null, "modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(5, "src", "sent", "osent",
						"midsagittal fontanel present", "status", "tagx",
						"modifer", "type"));
		myDataHolder.getSentenceHolder().add(
				new SentenceStructure(6, "src", "sent", "osent",
						"midsagittal fontanel", "status", "tag2", "modifer",
						"type"));

		tester.oneLeadWordMarkup(myDataHolder, myDataHolder.getCurrentTags());
		assertEquals("oneLeadMarkup", "tagx", myDataHolder.getSentence(3)
				.getTag());
	}
	
	@Test
    public void testWrapupMarkup() {		
		// case 1
		DataHolder myDataHolder1 = this.dataholderFactory();
		
		myDataHolder1.getSentenceHolder().add(new SentenceStructure(7, "src", "sent", "osent","sensory line not null","status","notnull","modifer","type"));
		myDataHolder1.getSentenceHolder().add(new SentenceStructure(192, "src", "sent", "osent","sensory line ignore","status","ignore","modifer","type"));
		myDataHolder1.getSentenceHolder().add(new SentenceStructure(193, "src", "sent", "osent","sensory line canal","status",null,"modifer","type"));
		myDataHolder1.getSentenceHolder().add(new SentenceStructure(267, "src", "sent", "osent","sensory line canals","status",null,"modifer","type"));
		myDataHolder1.getSentenceHolder().add(new SentenceStructure(269, "src", "sent", "osent","opening via tubular","status",null,"modifer","type"));
		
		myDataHolder1.add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"line", "s", "*", "1", "1", "", null}));
		myDataHolder1.add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"canals", "p", "*", "1", "1", "", null}));
		
		tester.wrapupMarkup(myDataHolder1);
		
//		assertEquals("wrapupmarkup - case 1 - tag sentence", "sensory line canal", myDataHolder1.getSentence(193).getTag());
//		assertEquals("wrapupmarkup - case 1 - tag sentence", "sensory line", myDataHolder1.getSentence(267).getTag());
		
		// case 2
		DataHolder myDataHolder2 = this.dataholderFactory();
		
		myDataHolder2.getSentenceHolder().add(new SentenceStructure(115, "src", "sent", "osent","midsagittal fontanel absent","status",null,"modifer","type"));
		myDataHolder2.getSentenceHolder().add(new SentenceStructure(116, "src", "sent", "osent","midsagittal fontanel present","status",null,"modifer","type"));
		
		myDataHolder2.add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"fontanel", "s", "*", "1", "1", "", null}));
		myDataHolder2.add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"absent", "b", "*", "1", "1", "", null}));
		myDataHolder2.add2Holder(DataHolder.WORDPOS, Arrays.asList(new String[] {"present", "b", "*", "1", "1", "", null}));
		
		tester.wrapupMarkup(myDataHolder2);
		
//		assertEquals("wrapupmarkup - case 2 - tag sentence", "midsagittal fontanel", myDataHolder2.getSentence(115).getTag());
//		assertEquals("wrapupmarkup - case 2 - tag sentence", "midsagittal fontanel", myDataHolder2.getSentence(116).getTag());
    }

}
