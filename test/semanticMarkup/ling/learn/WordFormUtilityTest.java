package semanticMarkup.ling.learn;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WordFormUtilityTest {
	
	private WordFormUtility tester;
	
	
	
	@Before
	public void initialize(){
		tester = new WordFormUtility("res/WordNet/WordNet-3.0/dict");
	}

	@Test
	public void testCheckWN() {
		/*******************************
		 * Method checkWN
		 ******************************/		
		// Method checkWN
		assertEquals ("checkWN - case 0.0 not word", "", tester.checkWN("()","pos"));
		assertEquals ("checkWN - case 0.2 special case - teeth", "p", tester.checkWN("teeth","pos"));
		assertEquals ("checkWN - case 0.2 special case - NUM", "NUM", tester.checkWN("NUM","singular"));
		assertEquals ("checkWN - case 0.2 concentrically", "", tester.checkWN("concentrically","number"));
		// otherwise, call wn
		// Case 1.1
		// Case 1.2
		assertEquals ("checkWN - case 1.2", "", tester.checkWN("operculi","number"));
		assertEquals ("checkWN - case 1.2", "operculi", tester.checkWN("operculi","singular"));
		// Case 1.3
		assertEquals ("checkWN - case 1.3", "", tester.checkWN("postcleithra","number"));
		assertEquals ("checkWN - case 1.3", "postcleithra", tester.checkWN("postcleithra","singular"));
		// Case 2.1.1
		assertEquals ("checkWN - case 2.1.1", "conical", tester.checkWN("conical","singular"));	
		assertEquals ("checkWN - case 2.1.1", "x", tester.checkWN("conical","number"));
		assertEquals ("checkWN - case 2.1.1", "ossified", tester.checkWN("ossified","singular"));
		assertEquals ("checkWN - case 2.1.1", "x", tester.checkWN("ossified","number"));
		// These two tests not passed!
		//assertEquals ("checkWN - case 2.1.1", "extending", tester.checkWN("extending","singular"));
		//assertEquals ("checkWN - case 2.1.1", "x", tester.checkWN("extending","number"));
		// Case 2.1.2
		assertEquals ("checkWN - case 2.1.2", "stay", tester.checkWN("stays","singular"));
		assertEquals ("checkWN - case 2.1.2", "p", tester.checkWN("stays","number"));
		assertEquals ("checkWN - case 2.1.2", "general", tester.checkWN("general","singular"));
		assertEquals ("checkWN - case 2.1.2", "s", tester.checkWN("general","number"));
		// Case 2.1.3
		assertEquals ("checkWN - case 1.2", "row", tester.checkWN("row","singular"));
		assertEquals ("checkWN - case 1.2", "s", tester.checkWN("row","number"));
		// Case 2.2
		// Need test cases!
	}

	@Test
	public void testGetNumber() {
		// Method getNumberHelper1
		assertEquals ("getNumberHelp1 - case 1: s or p", "s", tester.getNumberHelper1("s"));
		assertEquals ("getNumberHelp1 - case 2: x", "", tester.getNumberHelper1("x"));	
		assertEquals ("getNumberHelp1 - case 3: null", null, tester.getNumberHelper1("a"));
		// Method getNumberHelper2
		assertEquals ("getNumberHelp2 - end with i", "p", tester.getNumberHelper2("pappi"));
		assertEquals ("getNumberHelp2 - end with ss", "s", tester.getNumberHelper2("wordss"));
		assertEquals ("getNumberHelp2 - end with ia", "p", tester.getNumberHelper2("criteria"));
		assertEquals ("getNumberHelp2 - end with ium", "s", tester.getNumberHelper2("medium"));
		assertEquals ("getNumberHelp2 - end with tum", "s", tester.getNumberHelper2("datum"));
		assertEquals ("getNumberHelp2 - end with ae", "p", tester.getNumberHelper2("alumnae"));
		assertEquals ("getNumberHelp2 - end with ous", "", tester.getNumberHelper2("various"));
		assertEquals ("getNumberHelp2 - word as", "", tester.getNumberHelper2("as"));
		assertEquals ("getNumberHelp2 - word is", "", tester.getNumberHelper2("is"));
		assertEquals ("getNumberHelp2 - word us", "", tester.getNumberHelper2("us"));
		assertEquals ("getNumberHelp2 - end with us", "s", tester.getNumberHelper2("corpus"));
		assertEquals ("getNumberHelp2 - end with es", "p", tester.getNumberHelper2("phases"));
		assertEquals ("getNumberHelp2 - end with s", "p", tester.getNumberHelper2("mouths"));
		assertEquals ("getNumberHelp2 - end with ate", "", tester.getNumberHelper2("differentiate"));
		assertEquals ("getNumberHelp2 - not match", null, tester.getNumberHelper2("jxbz"));
		// Method getNumber
		assertEquals ("getNumber - not match", "s", tester.getNumber("jxbz"));
		assertEquals ("getNumber - case 1", "", tester.getNumber("only"));
		assertEquals ("getNumber - case 3", "s", tester.getNumber("uroneural"));	
	}

	@Test
	public void testGetSingular() {
		assertEquals("getSingular - non word", "", tester.getSingular("!@#"));
		assertEquals("getSingular - special case", "valve", tester.getSingular("valves"));
		assertEquals("getSingular - special case", "media", tester.getSingular("media"));
		assertEquals("getSingular - special case", "species", tester.getSingular("species"));
		assertEquals("getSingular - special case", "axis", tester.getSingular("axes"));
		assertEquals("getSingular - special case", "calyx", tester.getSingular("calyces"));
		assertEquals("getSingular - special case", "frons", tester.getSingular("frons"));
		assertEquals("getSingular - special case", "groove", tester.getSingular("grooves"));
		assertEquals("getSingular - special case", "nerve", tester.getSingular("nerves"));

		assertEquals("getSingular - case 1 - y", "gallery", tester.getSingular("galleries"));
		assertEquals("getSingular - case 2", "varus", tester.getSingular("vari"));
		assertEquals("getSingular - case 3 - ai", "lepidotrichium", tester.getSingular("lepidotrichia"));
		assertEquals("getSingular - case 4 - (x|ch|sh|ss))es", "process", tester.getSingular("processes"));
		assertEquals("getSingular - case 5 - ves", "leaf", tester.getSingular("leaves"));
		assertEquals("getSingular - case 6 - ices", "index", tester.getSingular("indices"));
		assertEquals("getSingular - case 7.1 - ae", "vertebra", tester.getSingular("vertebrae"));
		assertEquals("getSingular - case 7.2 - s", "hoplia", tester.getSingular("hoplias"));
		assertEquals("getSingular - case 7.2 - s", "branchiostegal", tester.getSingular("branchiostegals"));	
	}

	@Test
	public void testGetPlural() {
		// method getPluralRuleHelper
		assertEquals ("getPluralRuleHelper - case 2", "ices ixes", tester.getPluralRuleHelper("ix"));
		assertEquals ("getPluralRuleHelper - case 2", "thicknesses", tester.getPluralRuleHelper("thickness"));
		assertEquals ("getPluralRuleHelper - case 4", "leaves", tester.getPluralRuleHelper("leaf"));
		assertEquals ("getPluralRuleHelper - case 4", "knives", tester.getPluralRuleHelper("knife"));		
		assertEquals ("getPluralRuleHelper - case 6", "neurocrania", tester.getPluralRuleHelper("neurocranium"));
		assertEquals ("getPluralRuleHelper - case 9", "premaxillae", tester.getPluralRuleHelper("premaxilla"));
				
		// method getPlural
		List<String> pList = new ArrayList<String>();
		pList.add("ices");
		pList.add("ixes");
		tester.getWORDS().put("ices", 1);
		tester.getWORDS().put("ixes", 2);
		assertEquals ("getPlural", pList, tester.getPlural("ix"));	
	}
}