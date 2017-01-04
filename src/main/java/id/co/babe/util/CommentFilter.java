package id.co.babe.util;



public class CommentFilter {

	public static String ruleInference(String content) {
		String res = Komen.NORMAL;

		content = Util.filter(content);
		if (res == Komen.NORMAL) {
			res = RuleFilter.ruleSpam(content);
		} 
		
		if(res == Komen.SPAM) {
			res = RuleFilter.ruleNormal(content);
		}

		return res;
	}
}
