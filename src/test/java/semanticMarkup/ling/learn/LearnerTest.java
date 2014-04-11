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
