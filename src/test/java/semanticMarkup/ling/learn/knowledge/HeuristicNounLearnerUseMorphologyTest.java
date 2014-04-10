package semanticMarkup.ling.learn.knowledge;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

public class HeuristicNounLearnerUseMorphologyTest {

	private HeuristicNounLearnerUseMorphology tester;
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
		
		this.tester = new HeuristicNounLearnerUseMorphology(this.myLearnerUtility);
		
		Constant myConstant = new Constant();
		WordFormUtility myWordFormUtility = new WordFormUtility(wordNetPOSKnowledgeBase);
		this.myDataHolder = new DataHolder(configuration, myConstant, myWordFormUtility);
	}
	
	@Test
	public void testGetHeuristicsNounsHelper() {
		HashSet<String> words = new HashSet<String>();
		words.add("septa");
		words.add("word1");
		words.add("septum");
		assertEquals("getHeuristicsNouns - handleSpecialCase 1", "septa[p]",
				tester.getHeuristicsNounsHelper("septa[s]", words));
	}
	
	@Test
	public void testGetPresentAbsentNouns() {
		// Method getPresentAbsentNouns
		assertEquals("getPresentAbsentNouns - no present/absent", "",
				tester.getPresentAbsentNouns("only one pair of abcly presen"));
		assertEquals("getPresentAbsentNouns - and|or|to", "",
				tester.getPresentAbsentNouns("only one pair of and present"));
		assertEquals("getPresentAbsentNouns - STOP words", "",
				tester.getPresentAbsentNouns("only one pair of without absent"));
		assertEquals(
				"getPresentAbsentNoun - always|often|seldom|sometimes|[a-z]+lys",
				"",
				tester.getPresentAbsentNouns("only one pair of abcly present"));
		assertEquals("getPresentAbsentNouns - PENDINGS", "circuli[p]",
				tester.getPresentAbsentNouns("only one pair of circuli absent"));
		assertEquals("getPresentAbsentNouns - end with ss", "glass[s]",
				tester.getPresentAbsentNouns("only one pair of glass absent"));
		assertEquals(
				"getPresentAbsentNouns - end with none ss",
				"computers[p]",
				tester.getPresentAbsentNouns("only one pair of computers absent"));
		assertEquals("getPresentAbsentNouns - teeth", "teeth[p]",
				tester.getPresentAbsentNouns("only one pair of teeth present"));
		assertEquals("getPresentAbsentNouns - not SENDINGS", "serum[s]",
				tester.getPresentAbsentNouns("only one pair of serum absent"));
		assertEquals(
				"getPresentAbsentNouns - SENDINGS",
				"computer[s]",
				tester.getPresentAbsentNouns("only one pair of computer absent"));
	}
	
	@Test
	public void testIsDescriptor() {
		// Method filterOutDescriptors
		Set<String> rNouns = new HashSet<String>();
		Set<String> rDescriptors = new HashSet<String>();
		Set<String> results = new HashSet<String>();
		rNouns.add("noun1");
		rNouns.add("descriptor2");
		rNouns.add("noun2");
		rDescriptors.add("descriptor1");
		rDescriptors.add("descriptor2");
		rDescriptors.add("descriptor3");
		results.add("noun1");
		results.add("noun2");
		assertEquals("filterOutDescriptors", results,
				tester.filterOutDescriptors(rNouns, rDescriptors));
	}
	

	@Test
	public void testGetTaxonNameNouns() {

		// Nouns rule 0: Taxon name nouns
		Set<String> taxonNames = new HashSet<String>();
		// Method getTaxonNameNouns
		assertEquals("getTaxonNameNouns - not match", taxonNames,
				tester.getTaxonNameNouns("word word word"));
		assertEquals("getTaxonNameNouns - empty taxon name", taxonNames,
				tester.getTaxonNameNouns("< i >< / i >"));
		taxonNames.add("word1 word2	word3");
		taxonNames.add("word1");
		taxonNames.add("word2");
		taxonNames.add("word3");
		taxonNames.add("word4 word5");
		taxonNames.add("word4");
		taxonNames.add("word5");
		assertEquals(
				"getTaxonNameNouns - match",
				taxonNames,
				tester.getTaxonNameNouns("< i	>word1 word2	word3< /	i>, < i >word4 word5<	/i>"));
	}
	
	@Test
	public void testGetNounsMecklesCartilage() {
		// Nouns rule 0.5: Method getNounsMecklesCartilage
		Set<String> nouns = new HashSet<String>();
		assertEquals("getTaxonNameNouns - not match", nouns,
				tester.getNounsMecklesCartilage("word word word"));
		nouns.add("meckel#s");
		nouns.add("meckels");
		nouns.add("meckel");
		assertEquals("getTaxonNameNouns - match", nouns,
				tester.getNounsMecklesCartilage("word Meckel#s word"));
	}
	

	@Test
	public void testGetNounsRule1() {
		// Method getNounsRule1
		// Set<String> descriptorMap = new HashSet<String>();
		Set<String> nouns1 = new HashSet<String>();
		nouns1.add("term1");
		assertEquals(
				"getNounsRule1",
				nouns1,
				tester.getNounsRule1(this.myDataHolder,
						"Chang_2004.xml_ ffa60eb1-4320-4e69-b151-75a2615dca4b_29482156-8083-430c-91f4-e80209b50138.txt-0",
						"term1", new HashMap<String, Boolean>()));
	}

	@Test
	public void testGetNounsRule2() {
		// Method getNounsRule2
		Set<String> nouns2 = new HashSet<String>();
		assertEquals("getNounsRule2 - not match", nouns2,
				tester.getNounsRule2("word word 	word soe width nea"));
		nouns2.add("nouna");
		assertEquals("getNounsRule2 - match 1", nouns2,
				tester.getNounsRule2("word word 	word some nouna"));
		nouns2.add("nounb");
		assertEquals(
				"getNounsRule2 - match 2",
				nouns2,
				tester.getNounsRule2("word some nouna near word some width near word third nounb near end"));
		assertEquals(
				"getNounsRule2 - match 2",
				nouns2,
				tester.getNounsRule2("word some nouna near word some width near word third nounb near end nounc abction of end"));
	}

	@Test
	public void testGetNounsRule3Helper() {
		// Method getNounsRule3
		Set<String> nouns3 = new HashSet<String>();
		nouns3.add("II");
		nouns3.add("IX");
		assertEquals(
				"getNounsRule3",
				nouns3,
				tester.getNounsRule3Helper("posterior and dorsal to foramen for nerve II (i.e. a posterior oblique myodome IX)"));
		nouns3.remove("II");
		nouns3.remove("IX");
		nouns3.add("Meckelian");
		assertEquals(
				"getNounsRule3",
				nouns3,
				tester.getNounsRule3Helper("Pronounced dorsal process on Meckelian element"));
	}

	@Test
	public void testGetNounsRule4() {
		// Method getNounsRule4
		Set<String> nouns4 = new HashSet<String>();
		assertEquals("getNounsRule4 - not match", nouns4,
				tester.getNounsRule4("word word 	word noun one"));
		nouns4.add("nouna");
		assertEquals("getNounsRule4 - not match", nouns4,
				tester.getNounsRule4("word word 	word nouna 1"));
		nouns4.remove("nouna");
		nouns4.add("nounb");
		assertEquals(
				"getNounsRule4 - not match",
				nouns4,
				tester.getNounsRule4("word word 	word page 1 word above 2 word NoUnb 2 end"));
	}

	@Test
	public void testGetDescriptorsRule1() {
		// Method getDescriptorsRule1
		Set<String> descriptors1 = new HashSet<String>();
		descriptors1.add("absent");
		assertEquals("getDescriptorsRule1", descriptors1,
				tester.getDescriptorsRule1(
						"Brazeau_2009.xml_states200_state202.txt-0", "absent",
						new HashSet<String>()));
		descriptors1.remove("absent");
		descriptors1.add("present");
		Set<String> nouns = new HashSet<String>();
		nouns.add("present");
		assertEquals("getDescriptorsRule1", new HashSet<String>(),
				tester.getDescriptorsRule1(
						"Brazeau_2009.xml_states200_state203.txt-0", "present",
						nouns));
		assertEquals("getDescriptorsRule1", descriptors1,
				tester.getDescriptorsRule1(
						"Brazeau_2009.xml_states200_state203.txt-0", "present",
						new HashSet<String>()));
	}

	@Test
	public void testIsMatched() {
		// Method isMatched
		Map<String, Boolean> descriptorMap = new HashMap<String, Boolean>();
		descriptorMap.put("term1", false);
		assertEquals("isMatched", false, descriptorMap.get("term1"));
		assertEquals("isMatched", true, tester.isMatched(
				"begin word word was term1 word word end", "term1",
				descriptorMap));
		assertEquals("isMatched", true, descriptorMap.get("term1"));
	}
	
}
