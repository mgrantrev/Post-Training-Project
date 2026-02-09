Feature: Customer Media Management

  Background:
    Given the customer is logged in as "customer1"

  Scenario: Customer views their owned tracks
    Then the customer should see a list of their purchased tracks
    And each track should show the album title and artist name

  Scenario: Customer searches for a track
    When the customer searches for "Test"
    Then the track list should only show tracks matching "Test"

  Scenario: Customer views support tickets
    When the customer navigates to the support page
    Then they should see their existing support tickets
    And they can see responses to their tickets

  Scenario: Customer creates a support ticket
    When the customer navigates to the support page
    And creates a ticket with subject "Need help" and description "Something is wrong"
    Then they should see "Need help" in their ticket list
