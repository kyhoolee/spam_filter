package id.co.babe.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import id.co.babe.util.Util;

public class RuleFilter {
	

	public static final Set<String> blackWords = new HashSet<String>(Arrays.asList(
			"service", "sofa", "servis", "code", "kode", "facebook", "promo",
			
			"obat", "perangsang",  "kuat", "porn", "viagra", "jual", "vimax", "bbm",
			"herbal", 
			"thor", "hammer", "pingin", "hamer",
			"sex", "sedia", "penis", "seks", "adult", "onani", "syahwat", "bokep", "khusus",
			"vagina", "ejakulasi", "hubungan",
			"besar", "panjang", "keras",
			
			"0813", "0851", "0856", "0857", "0858","0821","0812", "0878", "0853", "0852",
			"0818", "0877","081", "0822", "0811", "0877",
			
			"fb.com", "p://", "www", "http://­"
			
			));
	
	public static String preProcess(String input) {
		String result = input.replace("\n", " ");
		return result;
	}
	
	public static String simply(String input) {
		String result = input.replaceAll("[\\-\\+\\.\\^\\:\\,]", "").toLowerCase();
		System.out.println(result);
		System.out.println(result);
		return result;
	}
	
	public static boolean checkSpam(String input) {
		if(
			(specialWordRule(input) > 3)  
				|| (singlecharacterRule(input) > 0.75)
					|| (specialCharacter(input) > 2)
					|| (singlecharacterRule(input) > 0.3)
					|| (specialWordRule(input) >= 1 && blackWord(input) > 1)
					|| (specialWordRule(input) > 3 && blackWord(input) >= 1)
					|| (blackWord(input) >= 4) 
					|| (contactWord(input) >= 1)
					|| (specialSpam(input))
			) {
			return true;
		}
		return false;
	}
	
	public static String ruleSpam(String input) {
		System.out.println("----------------");
		input = preProcess(input);
		System.out.println(checkSpam(input));
		if(checkSpam(input) || checkSpam(simply(input))) {
			System.out.println("-------check spam ------ spam");
			return Komen.SPAM;
		}
		
		
		
		return Komen.NORMAL;
	}
	

	public static String ruleNormal(String input) {
		input = preProcess(input);
		System.out.println(checkNormal(input));
		System.out.println(checkNormal(simply(input)));
		if(checkNormal(input) && checkNormal(simply(input))) {
			System.out.println("---------check normal --------- normal");
			return Komen.NORMAL;
		}
		return Komen.SPAM;
	}
	
	public static boolean checkNormal(String input) {
		if( 
				(specialWordRule(input) < 10 && blackWord(input) < 1 && contactWord(input) < 1 && !specialSpam(input) && specialCharacter(input) < 1 && singlecharacterRule(input) < 0.7)
				|| (blackWord(input) <= 1 && specialWordRule(input) <= 2 && singlecharacterRule(input) <= 1 && contactWord(input) < 1 && !specialSpam(input) && specialCharacter(input) < 1 && singlecharacterRule(input) < 0.7)
				) {
			return true;
		}
		return false;
	}
	
	public static String printRule(String input) {
		String sep = " -- ";
		String result = (
				"  " + specialWordRule(input) + sep 
				+ "  " + singlecharacterRule(input) + sep
				+ "  " + specialCharacter(input) + sep
				+ "  " + singlecharacterRule(input) + sep
				+ "  " + blackWord(input) + sep 
				+ "  " + contactWord(input) + sep
				+ "  " + specialSpam(input) + sep
				+ "  " + Util.stringList(pinWord(input)) + sep
				+ "  " + input
				);

		// System.out.println(result);
		return result;
	}
	
	public static List<String> pinWord(String input) {
		return Util.bbPin(input);
	}
	
	
	
	public static String removeSpace(String input) {
		String output = input.replaceAll("\\s+","");
		
		return output;
	}
	
	public static double blackWord(String input) {
		double result = 0; 
		
		for(String black : blackWords) {
			double tmp = Util.countSub(removeSpace(input).toLowerCase(), black);
//			if(tmp > 0) {
//				System.out.print(black + " ");
//			}
			result += tmp;
		}
		//System.out.println();
		
		//result += Util.bbPin(input).size();
		
		return result;
	}
	
	public static double contactWord(String input) {
		double result = 0;
		
		result += Util.bbPin(input).size();
		result += Util.phoneNumber(input).size();
		result += Util.email(input).size();
		//System.out.println("Contactword: " + result);
		return result;
	}
	

	
	public static double singlecharacterRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		if(words.length == 0)
			return 0;
		int count = 0;
				
		for(int i = 0 ; i < words.length ; i ++) {
			if(words[i].length() == 1) 
				count ++;
		}
		
		double check = 0;//(double) count * 1.0 / words.length;
		
		return check;
	}
	
	
	
	public static double specialCharacter(String input) {
		double result = 0; 
		//String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
		String alphaAndDigits = input.replaceAll("[ﾀﾣﾤ£$_ß∆¥《》ŠŞÅ¬ð🅰♏♏ⓐβⓓΣ]+",""); //"[^\\x00-\\x7F]","");//
		result = input.length() - alphaAndDigits.length();
		double size = input.split("\\s+").length;
		result += accentDistance(input) /2;//0;//result / size;
		
		return result;
	}
	
	
	public static double shortWord(String input) {
		double result = 0;
		double length = input.length();
		
		if(length == 0)
			return 1.0;
		
		String[] ws = input.split("\\s+");
		for(int i = 0 ; i < ws.length ; i++) {
			if(ws[i].length() <= 3)
				result ++;
		}
		result /= ws.length;
		
		return result;
	}
	
	
	public static double accentDistance(String input) {
		String de_accent = deAccent(input);
		double result = getHammingDistance(input, de_accent);
		return result;
	}

	public static String deAccent(String str) {
	    String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD); 
	    Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	    return pattern.matcher(nfdNormalizedString).replaceAll("");
	}
	
	public static int getHammingDistance(String sequence1, String sequence2) {
	    char[] s1 = sequence1.toCharArray();
	    char[] s2 = sequence2.toCharArray();

	    int shorter = Math.min(s1.length, s2.length);
	    int longest = Math.max(s1.length, s2.length);

	    int result = 0;
	    for (int i=0; i<shorter; i++) {
	        if (s1[i] != s2[i]) result++;
	    }

	    result += longest - shorter;

	    return result;
	}

	
	public static int specialWordRule(String input) {
		if(input  == null || input.length() == 0)
			return 0;
		
		String[] words = input.toLowerCase().split(" ");
		int count = 0;
				
		boolean nc = false;
		boolean ns = false;
		boolean cs = false;
		for(int i = 0 ; i < words.length ; i ++) {
			boolean n = false;//words[i].matches(".*\\d+.*");
			boolean c = words[i].matches(".*[a-zA-Z]+.*");
			
//			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//			Matcher m = p.matcher("I am a string");
//			boolean s = m.find();
			boolean s = words[i].matches(".*[�#$%^&*€∆]+.*");
			
			if((n&&c) || (n&&s) || (c&&s)) // 
				count ++;
//			if(n&&c&&s)
//				count += 10;
			
			if(n&&c) nc = true;
			if(n&&s) ns = true;
			if(c&&s) cs = true;
		}
		
		return count;
	}
	
	
	public static boolean specialSpam(String input) {
		boolean result = false;
		
		// Check: Id Line 
		if(input.toLowerCase().matches("(.*)(id)(\\s*)(line)(.*)")) {
			result = true;
		}
		
		// Check: space only
		if(input.trim().length() == 0) {
			result = true;
		}
		
		return result;
	}
	
	public static void regexMatch(String input) {
		if(input.matches("/\\s+[Oo]\\s*[Bb]\\s*[Aa]\\*[Tt]\\s+/"))
			System.out.println(input);
		
		if(input.matches("/\\s+0[0-9]{2,}\\s*[0-9]{3,}/"))
			System.out.println(input);
		
		
		if(input.matches("/\\s+([0-9A-F]{2}\\s*){3,}/"))
			System.out.println(input);
		
		if(input.matches("/[0-9][ABCDEFabcdef]\\s*[ABCDEFabcdef]{2,}/"))
			System.out.println(input);
	}
	
	public static void main(String[] args) {
		String input = " HAMMâ‚¬R OF THOR   C a p s u l  D o b e l  F u n g s i  O b a t  U n t u k  P e m b e s a r  P E N l S  D a n  K u a t  T a h a n  L a m a  P r o d u k  T e r b a i k  No.1 ðŸ’¯% H Ä“ r b a l  A m a n  T a n p a  E f e k  S a m p i n g.  M i n a t  H u b :  ðŸ’® Hp/WÃƒ : 08Z1 8788 1779 ðŸ’® ÃŸÃŸ : Ä549161Ä";
		System.out.println(removeSpace(input));
	}

}
