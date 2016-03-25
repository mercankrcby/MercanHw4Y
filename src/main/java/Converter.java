import java.io.*;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Created by Mercan on 3/23/16.
 */

/**
 * Class Converter : Dosya girdisinden aldigini olusturulan methodlar sayesinde
 * Assembly koduna cevirir
 */
public class Converter {

	//Kullanilan data fieldlar -Veri Yapilari-
	private static final int MAX_REGISTER = 8;
	private final String fileName; // okuma islemi yapilacak dosya
	private ArrayList<String> lines; // dosyamiz buraya satir satir yazilacak
	private ArrayList<String> postfixExpression; // postfix ifademiz satir satir kaydedilecek
	private ArrayList<String> assemblyExpression; // assembly ifademiz satir satir kaydedilecek
	private Stack<Register> systemRegisters; // kullanılabilecek registerler
	private ArrayList<Register> usedRegisters;

	/**
	 * Constructor
	 * Mainden aldigi degeri tutarak data fielda kaydedilir
	 * Ayni zamanda Veri yapilarimizinda genel yapisi cikartilir
	 * Registerlara ilk deger atamasi yapilir
	 * @param fileName String
     */
	public Converter(String fileName) {
		this.fileName = fileName;
		lines = new ArrayList<>();
		systemRegisters = new Stack<>();
		postfixExpression = new ArrayList<>();
		assemblyExpression = new ArrayList<>();
		usedRegisters = new ArrayList<>();

		for (int i = MAX_REGISTER; i >= 0; i--) {
			Register newReg = new Register(i, ""); // kullanilacak 9tane register ile doldur
			// stack oldugu icin tersten doldururuz ki stackten cekilince en kucuk numarali ola elimize gelsin
			systemRegisters.push(newReg);
		}
	}

	/**
	 * FileReader & BufferedReader yapisi kullanilarak dosyadan okuma islemi yapilmistir
	 * Dosya okumasi saglandiginda true return eder
	 * try-catch bloklariyla IOException firlatilmistir
	 * @return Boolean
     */
	public boolean readExpressionsFromFile() {
		String tempLine;

		try {
			File expressionFile = new File(fileName);
			FileReader fileReader = new FileReader(expressionFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((tempLine = bufferedReader.readLine()) != null) {
				lines.add(tempLine);
			}

			bufferedReader.close();
			fileReader.close();
		} catch (IOException e) {
			System.err.println(" IO ERROR : " + e.getMessage());
			System.err.println("Program Aborted!");
			System.exit(1);
		}
		return true;
	}

	/**
	 * infixten postfix e donusturmek icin InfixToPostfix classini kullandik
	 * Eklenen ifade ayni zamanda bir stringle de tutulmus olur
	 * En son olarak field a eklenir
	 * @return boolean
     */
	public boolean convertPostfix() {

		InfixToPostfix itp = new InfixToPostfix();
		String postfix;

		for (int i = 0; i < lines.size(); ++i) {
			postfix = itp.convert(lines.get(i));
			postfix += "\n";
			postfixExpression.add(postfix);
		}
		return true;
	}

	/**
	 * Postfix ifademizi kontrol amacli dosyaya yaziyoruz
	 * Delegation ile code-reuse yapmis olduk
	 * Kullanici icerde neler oldugunu bilmeden sadece dosya ismi ile yazdirabilir
	 * @param fileName
	 * @return boolean
     */
	public boolean printPostfixToFile(String fileName) {
		return printListToFile(postfixExpression, fileName);
	}

	/**
	 * Aldigi parametre olan arraylist postfix olan ifadeyi tutmaktaydi
	 * Girilen ikinci parametreye bagli olarak dosyaya yazma islemi yapilir
	 * Bu method normalde private olarak yapilmisti fakat test etmek amaci ile
	 * package visibility olarak yapildi
	 * @param Expression Data field dosyaya yazmak maksadiyla parametre olarak tutuldu
	 * @param fileName String deger alan parametre yazilacak dosyanin adini icermekte
     * @return boolean
     */


	boolean printListToFile(ArrayList<String> Expression, String fileName) {

		try {
			File outputFile = new File(fileName);

			if (!outputFile.exists())
				outputFile.createNewFile();

			FileWriter fileWriter = new FileWriter(outputFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (int i = 0; i < Expression.size(); ++i) {
				bufferedWriter.write(Expression.get(i));
			}

			bufferedWriter.close();
			fileWriter.close();

		} catch (IOException e) {
			System.err.println("IO ERROR : " + e.getMessage());
			System.err.println("PROGRAM ABORTED!!!");
			System.exit(1);
		}

		return true;

	}

	/**
	 * Assembly dosyasina yazma islemi yapilmistir
	 * @param fileName
	 * @return
     */
	public boolean printAssemblyToFile(String fileName) {
		return printListToFile(assemblyExpression, fileName);
	}

	/**
	 * Assembly e cevirme islemlerini yapacak
	 * -oncelikle ifade parcalara ayrilir
	 * -daha sonrasinda operator kontrolu yapilir
	 * -print durumunda stackden cekerek String e atilir
	 * -hesaplama fonksiyonu cagirilir
	 * -Diger durumda ise registerlardan cekilerek
	 * -assemblyExpression a eklenir
	 * @throws Exception
     */
	public void convertAssembly() throws Exception {

		Stack<String> operands;
		ArrayList<String> allOperands = new ArrayList<>();
		StringTokenizer token;
		String tempOperand;


		for (int i = 0; i < postfixExpression.size(); ++i) {
			operands = new Stack<>();
			token = new StringTokenizer(postfixExpression.get(i));
			while (token.hasMoreTokens()) {
				tempOperand = token.nextToken();
				Operator operator = new Operator(tempOperand);
				if (operator.controlOperator()) {
					// print degilse normal operator gibi 2 parametreli sekilde islem yapacak
					if (!operator.getType().equals("print")) {
						String right = operands.pop();
						String left = operands.pop();
						doCalculation(operands, left, right, operator);
					} else {
						String string = token.nextToken();
						int index = usedRegisters.indexOf(new Register(0, string));
						Register rightOfPrint = usedRegisters.get(index);
						assemblyExpression.add(createASM4Register("print", rightOfPrint, new Register(0, "c"), new Register(0, "c")));
					}
				} else { // operator degilse operandir
					operands.push(tempOperand);
				}
			}
		}


	}

	/**
	 * Methodda teker teker tum durumlar degerlendirilerek hesaplama islemi yapilmistir
	 * @param operands Stack<String>
	 * @param leftOperand String
	 * @param rightOperand String
	 * @param operator Operator
     * @throws Exception
     */
	private void doCalculation(Stack<String> operands, String leftOperand,
							   String rightOperand, Operator operator) throws Exception {
		//System.out.println("Left : " + leftOperand + " - Rigth : " + rightOperand + " - Operator : " + operator);

		Register rightOperandReg;
		Register leftOperandReg;

		try {
			// + - / * ise hepsi icin ayni islemleri yapacak
			// hesapla registerleri ayarla diziye at
			if (operator.is4operationOperator()) {
				String result = "_" + leftOperand + operator + rightOperand;

				if (usedRegisters.contains(new Register(0, leftOperand))) {
					int indexOfRegister = usedRegisters.indexOf(new Register(0, leftOperand));
					leftOperandReg = usedRegisters.get(indexOfRegister);
				} else {
					// demekki kodun ortasinda initialize edilmmis bir operand cikti
					// sayi ise yeni regster tahsis et sayi icin
					if (!Character.isDigit(leftOperand.charAt(0)))
						throw new ConverterException("Invalid Operand : " + leftOperand + "!!");

					leftOperandReg = systemRegisters.pop();
					leftOperandReg.setData(leftOperand);
					usedRegisters.add(leftOperandReg);
					// registeri tam sayi icin initialize et
					assemblyExpression.add("li  " + leftOperandReg + "," + leftOperand + "\n");
				}


				if (usedRegisters.contains(new Register(0, rightOperand))) {
					int indexOfRegister = usedRegisters.indexOf(new Register(0, rightOperand));
					rightOperandReg = usedRegisters.get(indexOfRegister);
				} else {
					// demekki kodun ortasinda initialize edilmmis bir operand cikti
					// sayi ise yeni regster tahsis et sayi icin
					if (!Character.isDigit(rightOperand.charAt(0)))
						throw new Exception("Daha once kullanilmamis sag operand !!" + rightOperand);

					rightOperandReg = systemRegisters.pop();
					rightOperandReg.setData(rightOperand);
					usedRegisters.add(rightOperandReg);
					// registeri tam sayi icin initialize et
					assemblyExpression.add("li  " + rightOperandReg + "," + rightOperand + "\n");
				}


				// Burada islemi kaydetmek icin 4 durum var
				// x + 3 ise 3unn registerine kaydet
				// 3 + x ise 4unn registerine kaydet
				// x + y ise yeni registere kaydet
				// 3 + 4 ise 3un registerine kaydet ve 4u fazla yer kaplamasin diye sil

				if (Character.isDigit(leftOperand.charAt(0)) && Character.isDigit(rightOperand.charAt(0))) {
					leftOperandReg.setData(result);
					assemblyExpression.add(createASM4Register(operator.operatorASMString(), leftOperandReg, leftOperandReg, rightOperandReg));
					int indexOfRightRegister = usedRegisters.indexOf(rightOperandReg);
					systemRegisters.push(usedRegisters.remove(indexOfRightRegister));
				} else if (Character.isDigit(leftOperand.charAt(0)) && !Character.isDigit(rightOperand.charAt(0))) {
					assemblyExpression.add(createASM4Register(operator.operatorASMString(), leftOperandReg, leftOperandReg, rightOperandReg));
					if (rightOperand.charAt(0) == '_')
						systemRegisters.push(usedRegisters.remove(usedRegisters.indexOf(rightOperandReg)));
					leftOperandReg.setData(result);
				} else if (!Character.isDigit(leftOperand.charAt(0)) && Character.isDigit(rightOperand.charAt(0))) {
					assemblyExpression.add(createASM4Register(operator.operatorASMString(), rightOperandReg, leftOperandReg, rightOperandReg));
					if (leftOperand.charAt(0) == '_')
						systemRegisters.push(usedRegisters.remove(usedRegisters.indexOf(leftOperandReg)));
					rightOperandReg.setData(result);
				} else if (!Character.isDigit(leftOperand.charAt(0)) && !Character.isDigit(rightOperand.charAt(0))) {

					if (rightOperand.charAt(0) == '_')
						systemRegisters.push(usedRegisters.remove(usedRegisters.indexOf(rightOperandReg)));
					if (leftOperand.charAt(0) == '_')
						systemRegisters.push(usedRegisters.remove(usedRegisters.indexOf(leftOperandReg)));

					Register newRegister = systemRegisters.pop();
					newRegister.setData(result);
					usedRegisters.add(newRegister);
					assemblyExpression.add(createASM4Register(operator.operatorASMString(), newRegister, leftOperandReg, rightOperandReg));
				}
				operands.push(result);
			} else if (operator.getType().equals("=")) {
				Register register;
				// eger atamanin sagi sayi ise direk li edilecek
				Character firstChar = rightOperand.charAt(0);
				if (Character.isDigit(firstChar)) {
					register = systemRegisters.pop();
					register.setData(leftOperand);
					usedRegisters.add(register);
					assemblyExpression.add("li  " + register + "," + rightOperand + "\n");
				} else {
					// eger degiskene kayit yapilacaksa sol operandı varsa belirle yoksa olustur
					if (usedRegisters.contains(new Register(0, leftOperand))) {
						register = usedRegisters.get(usedRegisters.indexOf(new Register(0, leftOperand)));
					} else { // degisken yoksa yeni degisken olustur
						register = systemRegisters.pop();
						register.setData(leftOperand);
						usedRegisters.add(register);
					}
					if (usedRegisters.contains(new Register(0, rightOperand))) {
						int indexOfRegister = usedRegisters.indexOf(new Register(0, rightOperand));
						rightOperandReg = usedRegisters.get(indexOfRegister);
						assemblyExpression.add("move  " + register + "," + rightOperandReg + "\n");
						// eger _ile basliosa yani gecici olarak olusturulan birsey ise onu ortadan kaldir
						if (rightOperand.charAt(0) == '_')
							systemRegisters.push(usedRegisters.remove(indexOfRegister));

					} else throw new ConverterException("Invalid operand : " + rightOperand);
				}
			}
		} catch (EmptyStackException e) {
			throw new ConverterException("NO AVAILABLE SYSTEM REGISTER TO USE!!");
		}
	}

	/**
	 * Registerlara islem yapildiktan sonra assemblyde nasil gosterileceklerini String olarak return edecek
	 * @param operation String
	 * @param resultRegister Register
	 * @param leftRegister Register
	 * @param rightRegister Register
     * @return String
     */
	private String createASM4Register(String operation, Register resultRegister, Register leftRegister, Register rightRegister) {
		if (operation.equals("add")) {
			return "add  " + resultRegister + "," + leftRegister + "," + rightRegister + "," + "\n";
		} else if (operation.equals("sub")) {
			return "sub  " + resultRegister + "," + leftRegister + "," + rightRegister + "," + "\n";
		} else if (operation.equals("mult")) {
			return "mult  " + leftRegister + "," + rightRegister + "," + "\n" + "mflo  " + resultRegister + "\n";
		} else if (operation.equals("div")) {
			return "div  " + leftRegister + "," + rightRegister + "," + "\n" + "mflo  " + resultRegister + "\n";
		} else if (operation.equals("print")) {
			return "move  $a0" + "," + resultRegister + "\n" + "li  $v0,1\n" + "syscall\n";
		}
		return "";
	}

}
