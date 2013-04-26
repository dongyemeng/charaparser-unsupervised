package semanticMarkup.ling.learn;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import semanticMarkup.core.Treatment;
import semanticMarkup.ling.learn.UnsupervisedClauseMarkup;
import semanticMarkup.ling.learn.FileLoader;

public class UnsupervisedClauseMarkupTest {
	UnsupervisedClauseMarkup tester;

	@Before
	public void initialize() {
		tester = new UnsupervisedClauseMarkup("plain",
				"res/WordNet/WordNet-3.0/dict");

	}

	@Test
	public void testGetAdjNouns() {
		DataHolder myDataHolder = tester.getDataHolder();
		List<Sentence> sentenceTable = myDataHolder.getSentenceTable();
		sentenceTable.add(new Sentence("source1", "word1 word2", "", "", "",
				"tag1", "modifier1", ""));
		sentenceTable.add(new Sentence("source2", "word2 word3", "", "", "",
				"[tag2", " modifier2[abc]", ""));
		sentenceTable.add(new Sentence("source3", "word3", "", "", "", "[tag3",
				"[abc]modifier2	", ""));
		sentenceTable.add(new Sentence("source4", "word1 word3 word4", "", "",
				"", "[tag4", "	mo[123]difier3", ""));

		List<String> resultGetAdjNouns = new ArrayList<String>();
		resultGetAdjNouns.add("modifier3");
		resultGetAdjNouns.add("modifier2");

		assertEquals("Method getAdjNouns", resultGetAdjNouns,
				tester.getAdjNouns());
	}

	@Test
	public void testGetAdjNounSent() {
		UnsupervisedClauseMarkup tester = new UnsupervisedClauseMarkup("plain",
				"res/WordNet/WordNet-3.0/dict");
		DataHolder myDataHolder = tester.getDataHolder();
		List<Sentence> sentenceTable = myDataHolder.getSentenceTable();
		sentenceTable.add(new Sentence("source1", "word1 word2", "", "", "",
				"tag1", "modifier1", ""));
		sentenceTable.add(new Sentence("source2", "word2 word3", "", "", "",
				"[tag2", " modifier2[abc]", ""));
		sentenceTable.add(new Sentence("source3", "word3", "", "", "", "[tag3",
				"[abc]modifier2	", ""));
		sentenceTable.add(new Sentence("source4", "word1 word3 word4", "", "",
				"", "[tag4", "	mo[123]difier3", ""));

		Map<String, String> resultGetAdjNounSent = new HashMap<String, String>();
		resultGetAdjNounSent.put("[tag2", "modifier2");
		resultGetAdjNounSent.put("[tag3", "modifier2");
		resultGetAdjNounSent.put("[tag4", "modifier3");

		assertEquals("Method getAdjNouns", resultGetAdjNounSent,
				tester.getAdjNounSent());
	}

	@Test
	public void testGetWordToSoures() {
		UnsupervisedClauseMarkup tester = new UnsupervisedClauseMarkup("plain",
				"res/WordNet/WordNet-3.0/dict");
		DataHolder myDataHolder = tester.getDataHolder();
		List<Sentence> sentenceTable = myDataHolder.getSentenceTable();
		sentenceTable.add(new Sentence("source1", "word1 word2", "", "", "",
				"tag1", "modifier1", ""));
		sentenceTable.add(new Sentence("source2", "word2 word3", "", "", "",
				"[tag2", " modifier2[abc]", ""));
		sentenceTable.add(new Sentence("source3", "word3", "", "", "", "[tag3",
				"[abc]modifier2	", ""));
		sentenceTable.add(new Sentence("source4", "word1 word3 word4", "", "",
				"", "[tag4", "	mo[123]difier3", ""));

		// getWordToSources
		Map<String, Set<String>> resultGetWordToSources = new HashMap<String, Set<String>>();
		resultGetWordToSources.put("word1", new HashSet<String>());
		resultGetWordToSources.get("word1").add("source1");
		resultGetWordToSources.get("word1").add("source4");

		resultGetWordToSources.put("word2", new HashSet<String>());
		resultGetWordToSources.get("word2").add("source1");
		resultGetWordToSources.get("word2").add("source2");

		resultGetWordToSources.put("word3", new HashSet<String>());
		resultGetWordToSources.get("word3").add("source2");
		resultGetWordToSources.get("word3").add("source3");
		resultGetWordToSources.get("word3").add("source4");

		resultGetWordToSources.put("word4", new HashSet<String>());
		resultGetWordToSources.get("word4").add("source4");

		assertEquals("Method getWordToSources", resultGetWordToSources,
				tester.getWordToSources());
	}

	@Test
	public void testGetHeuristicNouns() {
		UnsupervisedClauseMarkup tester = new UnsupervisedClauseMarkup("plain",
				"res/WordNet/WordNet-3.0/dict");
		DataHolder myDataHolder = tester.getDataHolder();
		Map<String, String> myHeuristicNouns = myDataHolder
				.getHeuristicNounTable();
		myHeuristicNouns.put("word1", "type1");
		myHeuristicNouns.put("word2", "type2");

		Map<String, String> resultGetHeuristicNouns = new HashMap<String, String>();
		resultGetHeuristicNouns.put("word2", "type2");
		resultGetHeuristicNouns.put("word1", "type1");

		assertEquals("Method getHeuristicNouns", resultGetHeuristicNouns,
				tester.getHeuristicNouns());
	}

}
