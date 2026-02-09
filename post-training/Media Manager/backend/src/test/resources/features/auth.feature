Feature: User Authentication

  Scenario: Customer logs in successfully
    Given the user is on the login page
    When the user enters "customer1" and "password"
    And clicks the login button
    Then the user should be redirected to the dashboard
    And see "Customer" as their role

  Scenario: Employee logs in successfully
    Given the user is on the login page
    When the user enters "employee1" and "password"
    And clicks the login button
    Then the user should be redirected to the dashboard
    And see "Employee" as their role

  Scenario: Login fails with invalid credentials
    Given the user is on the login page
    When the user enters "wronguser" and "wrongpass"
    And clicks the login button
    Then an error message "Invalid credentials" should be displayed
