package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

public class AdjectiveSubjectBootstrappingLearnerTest {
	private  AdjectiveSubjectBootstrappingLearner tester;
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
		
		this.tester = new  AdjectiveSubjectBootstrappingLearner(myLearnerUtility, configuration.getLearningMode(), configuration.getMaxTagLength());
		
		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant, myWordFormUtility);
	}
	
	@Test
	public void testAdjectiveSubjectsHelper(){
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
				tester.adjectiveSubjectsPart1(myDataHolder, typeModifiers));
	}
	
	@Test
	public void testAdjectiveSubjectsPart2Helper1(){
		Set<String> typeModifiers = new HashSet<String>();
		typeModifiers.addAll(Arrays.asList("open|paired|anterior|through".split("\\|")));
		assertEquals("AdjectiveSubjectsPart2Helper1", true, tester.adjectiveSubjectsPart2Helper1("restricted to <B>the</B> <M>anterior</M> <B>third</B> <B>of</B> <B>the</B> <N>jaw</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", false, tester.adjectiveSubjectsPart2Helper1("restricted to <B>the</B> <B>third</B> <B>of</B> <B>the</B> <N>jaw</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", true, tester.adjectiveSubjectsPart2Helper1("<B>series</B> <B>of</B> <M>paired</M> <B>median</B> <N>skull</N> roofng <N>bones</N> <B>that</B> meet <B>at</B> <B>the</B> <M>dorsal</M> midline <B>of</B> <B>the</B> <N>skull</N>", typeModifiers));
		assertEquals("AdjectiveSubjectsPart2Helper1", false, tester.adjectiveSubjectsPart2Helper1("<B>series</B> <B>of paired median</B> <N>skull</N> roofng <N>bones</N> <B>that</B> meet <B>at</B> <B>the</B> <M>dorsal</M> midline <B>of</B> <B>the</B> <N>skull</N>", typeModifiers));
	}
	
	@Test
	public void testAndOrTagCase1Helper() {	
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
		
		assertEquals("andOrTagCase1Helper", target, tester.andOrTagCase1Helper(pattern, wPattern, words, token));
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
		
		assertEquals("andOrTagCase1Helper", target, tester.andOrTagCase1Helper(pattern, wPattern, words, token));	
	}

}
