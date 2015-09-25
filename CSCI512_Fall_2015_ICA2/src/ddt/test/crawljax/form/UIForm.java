package ddt.test.crawljax.form;

import com.crawljax.core.configuration.Form;

// For update and insert form
public class UIForm extends Form {
	public UIForm() {
		super();
		// EmpRecord{Update, Insert}
		field("title").setValues("CEO", "");
		// EmpRecord{Update, Insert}
		field("emp_password").setValues("x", "");
		// EmpRecord{Update, Insert}
		field("emp_level").setValues("Admin", "None");
		// EmpRecord{Update, Insert}
		field("address").setValues("x", "");
		// EmpRecord{Update, Insert}
		field("work_phone").setValues("123456", "x", "");
		// EmpRecord{Update, Insert}
		field("home_phone").setValues("123456", "x", "");
		// EmpRecord{Update, Insert}
		field("cell_phone").setValues("123456", "x", "");
		// EmpRecord{Update, Insert}
		field("picture").setValues("johns.jpg", "bobk.jpg", "");
	}
}