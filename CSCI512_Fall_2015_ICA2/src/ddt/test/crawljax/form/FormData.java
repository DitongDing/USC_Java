package ddt.test.crawljax.form;

import com.crawljax.core.configuration.Form;

// TODO: think about how to control form filling in different pages, for reducing time for search field in all pages.
public class FormData extends Form {
	public FormData() {
		super();
		// // Login.login
		// field("Login").setValues("admin");
		// // Login.login
		// field("Password").setValues("admin");
		// Default.Search, DepsRecord.Update, EmpsGrid.Search, EmpsRecord.Insert, EmpsRecord.Update
		field("name").setValues("Ali", "xyz", "DDT", "");
		// Default.Search, EmpsRecord.Insert, EmpsRecord.Update
		field("email").setValues("bobk@company.com", "");
		// Default.Search, EmpsRecord.Insert, EmpsRecord.Update
		field("dep_id").setValues("All", "Administration", "");
		// EmpsGrid.Search, EmpsRecord.Insert, EmpsRecord.Update
		field("emp_login").setValues("admin", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("title").setValues("CEO", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("password").setValues("x", "123456", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("emp_level").setValues("None", "Admin", "123456", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("address").setValues("x", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("work_phone").setValues("x", "123456", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("home_phone").setValues("x", "123456", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("cell_phone").setValues("x", "123456", "");
		// EmpsRecord.Insert, EmpsRecord.Update
		field("manmonth").setValues(true, false);
		// EmpsRecord.Insert, EmpsRecord.Update
		field("picture").setValues("xxx", "");
	}
}
