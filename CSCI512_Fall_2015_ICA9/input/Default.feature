Feature: Seek weekly delete faults seeded in bookstore

Scenario: Open default page with 0 weekly featured book(s)
	When login as "admin"/"admin"
	When delete default weekly featured book
	When load "/Default.jsp"
	When do the same on test app
	Then the files should be identical

Scenario: Open default page with 1 weekly featured book(s)
	When login as "admin"/"admin"
	When load "/Default.jsp"
	When do the same on test app
	Then the files should be identical