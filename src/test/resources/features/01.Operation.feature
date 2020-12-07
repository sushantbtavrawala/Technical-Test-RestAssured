Feature: Verify different fixtures using Rest-Assured

  Scenario: 1- Validate that 3 fixtures are returned within response
    Given I perform GET operation "fixtures"
    And I should see the "fixtureId" and fixtureId_count "3" with the return object
    And I validate fixture Id is not null

  Scenario: 2- Validate that new fixture is created with POST response
    Given I perform POST operation with test data file "src/test/resources/TestData/data.json"
    And I should see newly fixture ID "4" within the return object

  Scenario: 3- Validate that new fixture is created with GET response (Not working)
    Given I should see teams array object has a teamId of 'HOME'
