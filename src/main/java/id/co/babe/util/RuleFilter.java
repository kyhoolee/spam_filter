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
			
			"obat", "perangsang",  "kuat",
			"sex", "sedia", "penis", "seks", "adult", "onani", "syahwat", "bokep", "khusus",
			"vagina", "ejakulasi", "hubungan",
			"besar", "panjang", "keras",
			
			"0813", "0851", "0856", "0857", "0858","0821","0812", "0878", "0853", "0852",
			"0818", "0877","081", "0822", "0811", "0877",
			
			"fb.com", "line", "p://", "www", "http://­"
			
			));
	
	
	
	public static String ruleSpam(String input) {

		if(
			(specialWordRule(input) > 3)  
			|| (singlecharacterRule(input) > 0.75)
				|| (specialCharacter(input) > 2)
				|| (singlecharacterRule(input) > 0.3)
				|| (specialWordRule(input) >= 1 && blackWord(input) > 1)
				|| (specialWordRule(input) > 3 && blackWord(input) >= 1)
				|| (blackWord(input) >= 3) 
				|| (contactWord(input) >= 1)
				|| (specialSpam(input))
				) {
			return Komen.SPAM;
		}
		
		return Komen.NORMAL;
	}
	

	public static String ruleNormal(String input) {
		//0.017857142857142856 -- 42.0 -- 12 -- 0.0 -- 0.0 -- 
		//0.027522935779816515 -- 19.0 -- 0 -- 0.10526315789473684 -- 0.0 
		if( 
				(specialWordRule(input) < 10 && blackWord(input) < 1 && contactWord(input) < 1 && !specialSpam(input) && specialCharacter(input) < 1 && singlecharacterRule(input) < 0.7)
				|| (blackWord(input) <= 1 && specialWordRule(input) <= 2 && singlecharacterRule(input) <= 1 && contactWord(input) < 1 && !specialSpam(input) && specialCharacter(input) < 1 && singlecharacterRule(input) < 0.7)
				) {
			return Komen.NORMAL;
		}
		
		return Komen.SPAM;
	}
	
	public static String printRule(String input) {
		String sep = " -- ";
		String result = (
				specialWordRule(input) + sep 
				+ singlecharacterRule(input)
				+ specialCharacter(input) + sep
				+ singlecharacterRule(input) + sep
				+ blackWord(input) + sep 
				+ contactWord(input) + sep
				+ specialSpam(input) + sep
				+ Util.stringList(pinWord(input)) + sep
				+ input
				);

		// System.out.println(result);
		return result;
	}
	
	public static List<String> pinWord(String input) {
		return Util.bbPin(input);
	}
	
	public static double blackWord(String input) {
		double result = 0; 
		
		for(String black : blackWords) {
			result += Util.countSub(input.toLowerCase(), black);
		}
		
		//result += Util.bbPin(input).size();
		
		return result;
	}
	
	public static double contactWord(String input) {
		double result = 0;
		
		result += Util.bbPin(input).size();
		result += Util.phoneNumber(input).size();
		result += Util.email(input).size();
		
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
		
		double check =(double) count * 1.0 / words.length;
		
		return check;
	}
	
	
	
	public static double specialCharacter(String input) {
		double result = 0; 
		//String resultString = subjectString.replaceAll("[^\\x00-\\x7F]", "");
		String alphaAndDigits = input.replaceAll("[�ﾀﾣﾤ£$_ß∆¥《》ŠŞÅ¬ð🅰♏♏ⓐβⓓΣ]+",""); //"[^\\x00-\\x7F]","");//
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
			boolean n = words[i].matches(".*\\d+.*");
			boolean c = words[i].matches(".*[a-zA-Z]+.*");
			
//			Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//			Matcher m = p.matcher("I am a string");
//			boolean s = m.find();
			boolean s = words[i].matches(".*[!@#$%^&*€∆]+.*");
			
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
		String input = "V.1.A.G.R.A USA O.B.A.T K.U.A.T PATEN\n\n          ASLI ORIGINAL IMPORT\n\nO.B.A.T K.U.A.T NO 1\n\nMGATASI DIABET- KENCANG.T.LAMA\nTlp:0.81.2.28.4.49.4.99/pin:D18A5778 ※ VI@GRA USA,KLG USA,CIALIS\n※ PEMUTIH WAJAH/BADAN \n※ ALAT BANTU P/W DLL.."; 
				//"JUαL  : V1∆GR∆ Pfizer USA 100mg \n\n                LASER   HOLOGRAM   \n                                                                                             LOGO KUDA BOLA API ¤RIGIN∆L \n\n  0.8.1.2.2.5.1.7.7.7.5.1    /  5e 69 44 67";
		//		"JUαL  : \" V1∆GR∆ Pfizer USA \" 100mg \n              LOGO \" BOLA API KUDA\"  ¤RIGIN∆L \n\n                 \n  0 8 1 2 2 5 1 7 7 7  5 1       /     5e 6 9 44 67";
		
		System.out.println(printRule(input));
	}

}
