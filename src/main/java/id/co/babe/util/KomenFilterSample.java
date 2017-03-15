package id.co.babe.util;

import java.util.ArrayList;
import java.util.List;


public class KomenFilterSample {

	public static void main(String[] args) {
		// ruleFilter();
		// logisticFilter();
		//trainModel();
		estimate();
		//checkRule();
	}
	
	public static void checkRule() {
		String data = "ÙÙ„Ø¹ï¿½ Ø© Ø§Ù„Ù„Ù‡ Ø¹Ù„Ù‰ Ø§Ù„ÙƒØ§ÙØ±ÙŠÙ†...";
		String output = RuleFilter.deAccent(data);
		System.out.println(output);
	}

	public static final String ROOT = "";//"/home/mainspring/tutorial/learn/text-classifier/data/";

	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset();
		data.updateData(DataReader.readSpamKomens(ROOT + "192_spams.txt"), 1);
		data.updateData(DataReader.readSpamKomens(ROOT + "15_3_spams.txt"), 1);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam_komen.txt"), 0.8);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam_output.txt.1"), 0.8);
		data.updateData(DataReader.readNormalKomens(ROOT + "pure_comments.txt.1"), 0.8);
		//data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam.txt.1"), 0.8);
		//data.updateData(DataReader.readSpamKomens(ROOT + "pure_spam_1.txt.1"), 0.8);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam_unique.txt.1"), 0.8);

		System.out.println("Train data: ");
		System.out.println(data.train_pos + " -- " + data.train_neg);

		System.out.println("\n\nTest data: ");
		System.out.println(data.test_pos + " -- " + data.test_neg);
		System.out.println("\n\n-------------------");

		return data;
	}



	
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

	public static void estimate() {
		KomenDataset data = buildData();


		// Test the model

		int false_pos = 0;
		int false_neg = 0;
		int true_pos = 0;
		int true_neg = 0;

		List<String> falseNegList = new ArrayList<>();
		List<String> falsePosList = new ArrayList<>();

		for (int i = 0; i < data.train.size(); i++) {
			Komen k = data.train.get(i);
			
			String res = ruleInference(k.content);
			
			if (k.label.equals(Komen.SPAM) && res.equals(Komen.NORMAL)) {
				falseNegList.add(RuleFilter.printRule(k.content));//k.content);//
			}

			if (k.label.equals(Komen.NORMAL) && res.equals(Komen.SPAM)) {
				falsePosList.add(RuleFilter.printRule(k.content));//k.content);//
			}

			if (k.label.equals(Komen.NORMAL)) {
				if (res.equals(Komen.NORMAL)) {
					true_neg++;
				} else { // label == NORMAL && res == SPAM
					false_pos++;
				}
			} else {
				if (res.equals(Komen.SPAM)) {
					true_pos++;
				} else { // label == SPAM && res == NORMAL
					false_neg++; 
				}
			}

		}

		System.out.println("\n\n");
		System.out.println("False_pos: " + false_pos + " -- Total_pos: " + (false_pos + true_pos));
		System.out.println("False_neg: " + false_neg + " -- Total_true: " + (false_neg + true_pos));

		TextfileIO.writeFile(ROOT + "false_negative.txt", falseNegList);
		TextfileIO.writeFile(ROOT + "false_positive.txt", falsePosList);

		double precision = true_pos * 1.0 / (true_pos + false_pos);
		double recall = true_pos * 1.0 / (false_neg + true_pos);
		System.out.println("Precision: " + precision + " -- Recall: " + recall);

		double f_score = 2 * precision * recall / (precision + recall);
		System.out.println("F-Score: " + f_score);
	}



}
