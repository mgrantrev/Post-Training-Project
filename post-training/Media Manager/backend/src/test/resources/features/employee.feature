Feature: Employee Management

  Background:
    Given the employee is logged in as "employee1"

  Scenario: Employee views sales metrics
    Then the employee should see their sales metrics dashboard
    And see the total revenue and assisted customers count

  Scenario: Employee manages support tickets
    When the employee navigates to the manage tickets page
    Then they should see all customer support tickets
    When they select a ticket and respond with "I am looking into this"
    And close the ticket
    Then the ticket status should be "Closed"

  Scenario: Employee views assisted customers list
    Then the employee should see a list of customers they have assisted

  Scenario: Employee views sales details
    Then the employee should see a list of recent sales with track and billing information
