package id.co.babe.util;

import java.util.ArrayList;
import java.util.List;


public class KomenFilterSample {

	public static void main(String[] args) {
		// ruleFilter();
		// logisticFilter();
		//trainModel();
		//estimate();
		checkRule();
		//filter();
	}
	
	public static void filter() {
		String data = "A-g-e-n  R-e-s-m-i Hub.O81,220,179,777 OBAT BIG LONG AL4T VIT4 L  ANABOLIC RX24 ASLiii DAN OBAT KUAT, TAHAN L4 M4"; 
		String s = data.replace("\n", " ");
		System.out.println(s);
		String r = s.replaceAll("[-+\\.\\^:,]", "");
		System.out.println(r);
	}
	
	public static void checkRule() {
		String data = "J.U.A.L: O.B.A.T: H.E.R.B.A.L: V.I.A.G.R.A  U.S.A: V.I.M.A.X  C.A.N.A.D.A: H.A.M.M.E.R  O.F  T.H.O.R: P.E.M.E.S.A.N.A.N: T.L.P.N  W.A  0.8  2.3  1.3  3.8  6.6  6.7: B.B.M  D.9  9.B  E.D  9.6: B.B.M  D.8  F.0  E.0  5.5";
				
				
				
				
				//"A-g-e-n  R-e-s-m-i Hub.O81,220,179,777 OBAT BIG LONG AL4T VIT4 L  ANABOLIC RX24 ASLiii DAN OBAT KUAT, TAHAN L4 M4"; 
				//"A-g-e-n R-e-s-m-i Hub.O81,220,179,777 OBAT BIG LONG AL4T VIT4 L ANABOLIC RX24 ASLiii DAN OBAT KUAT, TAHAN L4 M4"; 
				//"2 βonμ$ 1 frree ongkir V-I-A-G-R-A / V-I-M-A-X / H-A-M-M-E-R . P-A-N-J-A-N-G / B-E-S-A-R / K-E-R-A-S / K-U-A-T /  T-A-H-A-N / L-A-M-A / CE GAH: I-M-P-O-T-E-N / L-M-H,S-Y-A-H-W-A-T/M-A-N-I E-N-C-E-R /E-J-A-K-U-L-A-S-I,D-I-N-I  T. 08-22-27-19-44-69 Bm. 2-6-C-5-3-8-A-3 A-W-A-S / P-A-L-S-U /FATAL";
				//"P R O M O. HARGA. MURAH\n\nPEM'BESAR. P E N I S. TERBAIK\n\n-HAM*ER OF T-H-O-R\n\n-OB*@T PR@NGSANG\n-BONEKA FULL B O D Y \n-@L@T B@-NTU S E C K DLL\n\nP I N : D 9 A 0 4 5 7 5\nTLP/W A : 082-225-852-349"; 
				//"P R O M O. HARGA. MURAH PEM'BESAR. P E N I S. TERBAIK -HAM*ER OF THOR -OB*@T PR@NGSANG -BONEKA FULL B O D Y -@L@T B@-NTU S E C K DLL P I N : D 9 A 0 4 5 7 5 TLP/W A : 082-225-852-349"; 
				//"P R O M O. HARGA. MURAH		PEM'BESAR. P E N I S. TERBAIK		-HAM*ER OF THOR		-OB*@T PR@NGSANG	-BONEKA FULL B O D Y 	-@L@T B@-NTU S E C K DLL		P I N : D 9 A 0 4 5 7 5	TLP/W A : 082-225-852-349";
				//"Mbak Inul, terlepas dukungan anda ke penista agama itu hak anda. Anda yg memilih anda jg yg akan diminta pertanggung jawaban.. yg kami gak terima, kenapa hrus mendukung sang penista dgn menjelekkan para Ulama.. apakah anda lupa dunia hanya sementara, tobat mbak, munpung masih diberi nyawa..."; 
				//"Wùiiihhh...mañtappp, ini yg bikiñ hidùp lebih hidup...segerá mkñ disana, yg comeñt negatif pd munáfik tuh...pdhl suka jg.😜😛";
				//"Program anies yg gagal adalah utk guru2. kenapa sy blg gagal ? krn guru2 dites kemampuannya menjadi guru yg profesional tesnya melalui online, banyak guru2 yg tdk lulus sehingga mrk harus meninggalkan siswa2nya ikut tes lagi bukan hanya 1, 2 atau 3 hari bahkan ada yg lebih. sementara ada lulus cara";
				//"JAKARTA SKRG GAK TERLALU BUTUH PEMIMPIN YG SANTUN, TETAPI TDK BISA BEKERJA.!! KALAU ANIS BISA BEKERJA TENTU TIDAK DI PECAT JADI MENTERI OLEH JOKOWI.! APALAGI DIA TEAM SUKSES NYA JOKOWI SAAT ITU.!! ITU BERARTI KEMUNGKINAN BESAR SI ANIS INI MEMANG BETUL2 TIDAK BISA BEKERJA DGN BAIK.!! INGAT KAWAN.!!"; 
				//"Taksi online ada karna taksi konvensional tak bisa memuaskan keinginan konsumen yaitu murah capat dan nyaman kalau taksi konvensional bisa bersaing dgn 3 hal tadi dijamin taksi online gak laku sepanjang 3 hal itu tak dilakukan jangan harap konsumen akan memakai taksi konvensioala jaman udah berubah";
		String r = CommentFilter.ruleInference(data);
		System.out.println(r);
		System.out.println(RuleFilter.printRule(data));
		System.out.println(RuleFilter.singlecharacterRule(data));
	}

	public static final String ROOT = "";//"/home/mainspring/tutorial/learn/text-classifier/data/";

	private static KomenDataset buildData() {
		KomenDataset data = new KomenDataset();
		data.updateData(DataReader.readNormalKomens(ROOT + "7.4.normal.txt"), 0.1);
		data.updateData(DataReader.readSpamKomens(ROOT + "spam.11.5.txt"), 1);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam.293.txt"), 1);
		//data.updateData(DataReader.readSpamKomens(ROOT + "15_3_spams.txt"), 1);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam_komen.txt"), 0.8);
		//data.updateData(DataReader.readSpamKomens(ROOT + "spam_output.txt.1"), 0.8);
		//data.updateData(DataReader.readNormalKomens(ROOT + "normal.293.txt"), 0.2);
		//data.updateData(DataReader.readNormalKomens(ROOT + "pure_comments.txt.1"), 1.0);
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
				falseNegList.add(k.content);//RuleFilter.printRule(k.content));//
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
