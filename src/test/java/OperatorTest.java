import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by hmenn on 3/24/16.
 */
public class OperatorTest {

	@Test
	public void controlOperatorTest() throws Exception {

		// operatorlerin dogru olup olmadigi fonksiyonun return degerine gore kontrol edilir
		Operator add = new Operator("+");
		assertEquals(true,add.controlOperator());


		Operator test = new Operator("test");
		assertEquals(false,test.controlOperator());


		Operator print = new Operator("print");
		assertEquals(true,print.controlOperator());

	}

	@Test
	public void is4operationOperatorTest() throws Exception {

		Operator sub = new Operator("-");
		assertEquals(true,sub.is4operationOperator());


		Operator mult = new Operator("*");
		assertEquals(true,mult.is4operationOperator());

		Operator printf = new Operator("print");
		assertEquals(false, printf.is4operationOperator());

	}


	@Test
	public void operatorASMStringTest() throws Exception {

		Operator div = new Operator("/");
		assertEquals("div",div.operatorASMString());


		Operator sub = new Operator("-");
		assertEquals("sub",sub.operatorASMString());


		Operator add = new Operator("+");
		assertEquals("add",add.operatorASMString());

	}
}