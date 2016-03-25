/**
 * Created by Mercan on 3/23/16.
 */

/**
 * Register classı , register bilgilerini set etmeye
 * dataları return etmeye ve eşitligi kontrolu saglar
 */
public class Register {
	private String data;
	private int registerID;

	/**
	 * Constructor
	 * @param registerID int
	 * @param data String
     */
	public Register(int registerID, String data){
		this.data = data;
		this.registerID = registerID;
	}

	/**
	 * Setter
	 * @param data String
     */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * Setter
	 * @param registerID int
     */
	public void setRegisterID(int registerID) {
		this.registerID = registerID;
	}

	/**
	 * Getter
	 * @return String
     */
	public String getData() {

		return data;
	}

	/**
	 * getter
	 * @return int
     */
	public int getRegisterID() {
		return registerID;
	}

	/**
	 * Object'in toString methodunun overrride'i
	 * @return String
     */
	@Override
	public String toString() {
		return "$t"+registerID;
	}

	/**
	 * Objectin equals methodunun override i
	 * @param o Object
	 * @return
     */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Register register = (Register) o;

		return data != null ? data.equals(register.data) : register.data == null;

	}
}
