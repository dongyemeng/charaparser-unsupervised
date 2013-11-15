//Find Modifier/Organ for the same Ox: M1 Ox, M2 Ox Example: inner phyllaries, middle phyllaries
//Find Mx/Oy where Ox != Oy Example: inner florets
// ==>inner/middle = type modifier
// Find TM C (character) patterns => TM = adjective nouns
// outer and middle => outer is adject noun
// outer and mid => mid is adject noun
//===> infer more boundary words/structure: outer [ligules], inner [fertile]

package semanticMarkup.ling.learn.knowledge;

import org.apache.commons.lang3.StringUtils;

import semanticMarkup.ling.learn.dataholder.DataHolder;
import semanticMarkup.ling.learn.dataholder.SentenceStructure;
import semanticMarkup.ling.learn.utility.LearnerUtility;

public class AdjectiveSubjectBootstrapping implements IModule {
	private LearnerUtility myLearnerUtility;

	public AdjectiveSubjectBootstrapping(LearnerUtility learnerUtility) {
		this.myLearnerUtility = learnerUtility;
	}

	@Override
	public void run(DataHolder dataholderHandler) {
		int flag = 0;
		int count = 0;
		
		do {
			this.myLearnerUtility.tagAllSentences(dataholderHandler, "singletag", "sentence");
			int res1 = this.adjectiveSubjects(dataholderHandler);
			flag += res1;
			
			int res2 = discoverNewModifiers(dataholderHandler);
			
		} while (flag > 0);
		
		for (SentenceStructure sentenceItem : dataholderHandler.getSentenceHolder()) {
			String tag = sentenceItem.getTag();
			if (StringUtils.equals(tag, "andor")) {
				sentenceItem.setTag(null);
			}
		}
		
		this.myLearnerUtility.tagAllSentences(dataholderHandler, "singletag", "sentence");
		this.adjectiveSubjects(dataholderHandler);
	}

	public int adjectiveSubjects(DataHolder dataholderHandler) {
		return 0;
		// TODO Auto-generated method stub
		
	}
	

	public int discoverNewModifiers(DataHolder dataholderHandler) {
		// TODO Auto-generated method stub
		return 0;
	}


}
