import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * Created by hmenn on 3/24/16.
 */
public class ConverterTest {

	private Converter converter;

	@Before
	public void setUp() throws Exception {

		converter = new Converter("input.txt");


	}

	@After
	public void tearDown() throws Exception {

	}

	// Dosyadan okuma kontrolunu yapmak icin
	@Test
	public void readExpressionsFromFileTest() throws Exception {

		boolean expected = true;
		boolean actual = converter.readExpressionsFromFile();

		// dosyadan okuyamadigi zaman kendi icinde exception firlatip exit ile cikacak ve test olmamis olacak
		// exit gelmedigi surece sorun yok
		assertEquals(expected,actual);

	}



	@Test
	public void convertPostfixTest() throws Exception {

		boolean expected = true;
		boolean actual = converter.convertPostfix();

		// okudugumuz seyleri simdede postfixe cevirmeye calisalim
		// true gelirse sorun yok
		assertEquals(expected,actual);

	}

	@Test
	public void printPostfixToFileTest() throws Exception {

	}

	@Test
	public void printAssemblyToFileTest() throws Exception {

	}

	@Test
	public void convertAssemblyTest() throws Exception {

	}
}