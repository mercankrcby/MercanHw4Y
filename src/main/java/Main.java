/**
 * Created by Mercan on 3/23/16.
 */
public class Main {
	public static void main(String... args){

		try {
			Converter converter = new Converter("input.txt");
			converter.readExpressionsFromFile();
			converter.convertPostfix();
			converter.printPostfixToFile("postfix.txt");
			converter.convertAssembly();
			converter.printAssemblyToFile("output.asm");
			System.out.println("Convertion Completed :)");

		}catch (Exception e){
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}

	}
}
