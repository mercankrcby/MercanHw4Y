/**
 * Created by Mercan on 3/24/16.
 */

/**
 * Converter classina ait kendi olusturdugumuz Exception classi
 */
public class ConverterException extends Exception {

	public ConverterException(String message){
		super(message);
	}

	@Override
	public String toString(){
		return "Converter ERROR : "+getMessage();
	}

}
