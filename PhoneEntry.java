
public class PhoneEntry {
	private String _name;
	private int _phonenum;
	
	public PhoneEntry(String aName, int aPhoneno) {
		_name = aName;
		_phonenum = aPhoneno;
	}
	
	public void setName(String newname) {
		_name = newname;
	}
	
	public void setNumber(int newnumber) {
		_phonenum = newnumber;
	}

	public String getName() {
		return _name;
	}

	public int getNumber() {
		return _phonenum;
	}


}
