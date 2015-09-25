package ddt.test.crawljax.form;

import com.crawljax.core.configuration.Form;

import ddt.utils.ComUtils;
import ddt.utils.ComUtils.RepeatUnit;

// For Update, Insert and Search form
public class UISForm extends Form {
	public UISForm() {
		super();
		// DepRecord{Update, Insert}, EmpRecord{Update, Insert}, EmpsGrid{Search}, Default{Search}
		// 16+1 for DepRecord.Insert
		field("name").setValues(
				ComUtils.getRepeatedInputs(new RepeatUnit("x", 8), new RepeatUnit("Ali", 8), new RepeatUnit("", 1)));
		// EmpRecord{Update, Insert}, EmpsGrid{Search}
		// 13+1 for EmpRecord.Insert
		field("emp_login").setValues(ComUtils.combine(ComUtils.getDifferentInputs(13), new String[] { "" }));
		// EmpRecord{Update, Insert}, Default{Search}
		field("dep_id").setValues("Software", "All");
		// EmpRecord{Update, Insert}, Default{Search}
		field("email").setValues("bobk@company.com", "");
		// EmpRecord{Update, Insert}, EmpsGrid{Search}
		field("manmonth").setValues(true, false);
	}
}
