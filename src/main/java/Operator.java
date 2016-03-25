/**
 * Created by Mercan on 3/23/16.
 */

/**
 * {+,-,*,/} operatorleri ve print icin yazilmis olan classdir
 */
public class Operator {
	//Data fields
	private String type;

	/**
	 * Constructor
	 * @param type
     */
	public Operator(String type){
		this.type = type;
	}

	/**
	 * Operatorun turunu belirtir
	 * @return boolean
     */
	public boolean controlOperator(){

		switch (this.type){
			case "+":
			case "-":
			case "print":
			case "*":
			case "/":
			case "=": return true;
			default: return false;
		}
	}

	/**
	 * Method 4 islem yapilan operatorlerden mi diye bakar
	 * @return
     */
	//
	public boolean is4operationOperator(){
		switch (this.type){
			case "+":
			case "-":
			case "*":
			case "/": return true;
			default: return false;
		}
	}

	/**
	 * Getter fonksiyonu
	 * @return String
     */
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return type;
	}

	/**
	 * Yapilan isleme bagli olarak ekrana basilacak ifade
	 * @return
     */
	public String operatorASMString(){
		switch (this.type){
			case "+": return "add";
			case "-": return "sub";
			case "print": return "print";
			case "*": return "mult";
			case "/": return "div";
			default: return "";
		}
	}
}
