Feature: Original test suite

Scenario: Open Default without login
	When load "/Default.jsp"

Scenario: Only logout
	When logout

Scenario: Login error
	When logout
	When login as "DDT"/"DDT"

Scenario: Login as admin
	When logout
	When login as "admin"/"admin"

Scenario: Test sort once by item with 0 order(s)
	When test sort once by item with 0 order(s)

Scenario: Buy 20 of item 1 as admin
	When buy 1 item 1 as "admin"/"admin"

Scenario: Search Internet in category x
	When search item "Internet" in category "x"

Scenario: Test sort twice by item with 2 order(s)
	When test sort twice by item with 2 order(s)

Scenario: Login as admin and open AdminMenu
	When logout
	When login as "admin"/"admin" with ret_page '/bookstore/AdminMenu.jsp'

Scenario: Login as admin and update MyInfo
	When logout
	When login as "admin"/"admin" with ret_page '/bookstore/MyInfo.jsp'
	When update admin note to "DDT"

Scenario: Login as guest
	When logout
	When login as "guest"/"guest"

Scenario: Login as admin and open MyInfo
	When logout
	When login as "admin"/"admin" with ret_page '/bookstore/MyInfo.jsp'