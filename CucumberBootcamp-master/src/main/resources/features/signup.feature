Feature:
  @signup
  Scenario: Successfull Signup
    When User makes signup request
    Then Signup message should be "User registered successfully!"
    And  Signup response status is 200
