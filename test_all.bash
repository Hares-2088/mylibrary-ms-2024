#!/usr/bin/env bash
#
# Sample usage:
#   ./test_all.bash start stop
#   start and stop are optional
#
#   HOST=localhost PORT=7000 ./test-em-all.bash
#
# When not in Docker
#: ${HOST=localhost}
#: ${PORT=7000}

# When in Docker
: ${HOST=localhost}
: ${PORT=8080}

#array to hold all our test data ids
allTestMemberIds=()
allTestLoanId=()

function assertCurl() {

  local expectedHttpCode=$1
  local curlCmd="$2 -w \"%{http_code}\""
  local result=$(eval $curlCmd)
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP Code: $httpCode)"
    else
      echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
    fi
  else
      echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpCode, GOT: $httpCode, WILL ABORT!"
      echo  "- Failing command: $curlCmd"
      echo  "- Response Body: $RESPONSE"
      exit 1
  fi
}

function assertEqual() {

  local expected=$1
  local actual=$2

  if [ "$actual" = "$expected" ]
  then
    echo "Test OK (actual value: $actual)"
  else
    echo "Test FAILED, EXPECTED VALUE: $expected, ACTUAL VALUE: $actual, WILL ABORT"
    exit 1
  fi
}

#have all the microservices come up yet?
function testUrl() {
    url=$@
    if curl $url -ks -f -o /dev/null
    then
          echo "Ok"
          return 0
    else
          echo -n "not yet"
          return 1
    fi;
}

#prepare the test data that will be passed in the curl commands for posts and puts
function setupTestdata() {

##CREATE SOME Member TEST DATA - THIS WILL BE USED FOR THE POST REQUEST
#
body=\
'{
         "firstName": "Ayoub",
         "lastName": "Bessam",
         "email": "john.doe@example.com",
         "benefits": "Free shipping, Member discounts",
         "memberType": "STUDENT",
         "street": "123 Main St",
         "city": "Quebec",
         "province": "Quebec",
         "country": "Anycountry"
 }'
    recreateMember 1 "$body"
#
#Create some loan test data

body=\
  '{
   	"bookId": "b1",
    "returned": "false" 
   }'
    recreateLoan 1 "$body" "m1"

} #end of setupTestdata


#USING CUSTOMER TEST DATA - EXECUTE POST REQUEST
function recreateMember() {
    local testId=$1
    local aggregate=$2

    #create the member and record the generated memberId
    memberId=$(curl -X POST http://$HOST:$PORT/api/v1/members -H "Content-Type:
    application/json" --data "$aggregate" | jq '.memberId')
    allTestMemberIds[$testId]=$memberId
    echo "Added Member with memberId: ${allTestMemberIds[$testId]}"
}

#USING Loan TEST DATA - EXECUTE POST REQUEST
function recreateLoan() {
    local testId=$1
    local aggregate=$2
    local memberId=$3

    #create the loan and record the generated loanId
    loanId=$(curl -X POST http://$HOST:$PORT/api/v1/members/$memberId/loans -H "Content-Type:
    application/json" --data "$aggregate" | jq '.loanId')
    allTestLoanId[$testId]=$loanId
    echo "Added Loan with loanId: ${allTestLoanId[$testId]}"
}

#don't start testing until all the microservices are up and running
function waitForService() {
    url=$@
    echo -n "Wait for: $url... "
    n=0
    until testUrl $url
    do
        n=$((n + 1))
        if [[ $n == 100 ]]
        then
            echo " Give up"
            exit 1
        else
            sleep 6
            echo -n ", retry #$n "
        fi
    done
}

#start of test script
set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

if [[ $@ == *"start"* ]]
then
    echo "Restarting the test environment..."
    echo "$ docker-compose down"
    docker-compose down
    echo "$ docker-compose up -d"
    docker-compose up -d
fi

#try to delete an entity/aggregate that you've set up but that you don't need. This will confirm that things are working
waitForService curl -X DELETE http://$HOST:$PORT/api/v1/members/m1

setupTestdata

#EXECUTE EXPLICIT TESTS AND VALIDATE RESPONSE
#
##verify that a get all members works
echo -e "\nTest 1: Verify that a get all members works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/members -s"
assertEqual 10 $(echo $RESPONSE | jq ". | length")
#
#
## Verify that a normal get by id of earlier posted member works
echo -e "\nTest 2: Verify that a normal get by id of earlier posted member works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/members/${allTestMemberIds[1]} '${body}' -s"
assertEqual ${allTestMemberIds[1]} $(echo $RESPONSE | jq .memberId)
assertEqual "\"Adem\"" $(echo $RESPONSE | jq ".firstName")
#
#
## Verify that an update of an earlier posted member works - put at api-gateway has no response body
echo -e "\nTest 3: Verify that an update of an earlier posted member works"
body=\
'{
 	"bookId": "b1",
     "returned": "false"
 }'
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/members/${allTestMemberIds[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
#
#
## Verify that a delete of an earlier posted member works
echo -e "\nTest 4: Verify that a delete of an earlier posted member works"
assertCurl 204 "curl -X DELETE http://$HOST:$PORT/api/v1/members/${allTestMemberIds[1]} -s"
#
#
## Verify that a 404 (Not Found) status is returned for a non existing memberId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 5: Verify that a 404 (Not Found) error is returned for a get member request with a non existing memberId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/members/c3540a89-cb47-4c96-888e-ff96708db4d7 -s"
#
#
## Verify that a 422 (Unprocessable Entity) status is returned for an invalid memberId (c3540a89-cb47-4c96-888e-ff96708db4d)
echo -e "\nTest 6: Verify that a 422 (Unprocessable Entity) status is returned for a get member request with an invalid memberId"
assertCurl 422 "curl http://$HOST:$PORT/api/v1/members/c3540a89-cb47-4c96-888e-ff96708db4d -s"

#verify that a get all loans works
echo -e "\nTest 7: Verify that a get all loans works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/members/m1/loans -s"
assertEqual 2 $(echo $RESPONSE | jq ". | length")

#verify that a get loan by id loanid and memberid works
echo -e "\nTest 8: Verify that a get loan by id loanid and memberid works"
assertCurl 200 "curl http://$HOST:$PORT/api/v1/members/m1/loans/${allTestLoanId[1]} -s"
assertEqual ${allTestLoanId[1]} $(echo $RESPONSE | jq .loanId)


#Verify that an update of an earlier posted loan works - put at api-gateway has no response body
echo -e "\nTest 9: Verify that an update of an earlier posted loan works"

body=\
'{
 	"bookId": "b1",
  "returned": "true"
 }'
assertCurl 200 "curl -X PUT http://$HOST:$PORT/api/v1/members/cus01/loans/${allTestLoanId[1]} -H \"Content-Type: application/json\" -d '${body}' -s"
assertEqual ${allTestLoanId[1]} $(echo $RESPONSE | jq .loanId)
assertEqual 200.00 $(echo $RESPONSE | jq ".totalAmount")
assertEqual 110.00 $(echo $RESPONSE | jq ".totalDue")
#
#
#TODO: Add verification of vehicle status change to sold

# Verify that a delete of an earlier posted loan works
echo -e "\nTest 10: Verify that a delete of an earlier posted loan works"
assertCurl 204 "curl -X DELETE http://$HOST:$PORT/api/v1/members/m1/loans/${allTestLoanId[1]} -s"

# Verify that a 404 (Not Found) status is returned for a non existing loanId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 11: Verify that a 404 (Not Found) error is returned for a get loan request with a non existing loanId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/members/m1/loans/c3540a89-cb47-4c96-888e-ff96708db4d7 -s"

# Verify that a 422 (Unprocessable Entity) status is returned for an invalid loanId (c3540a89-cb47-4c96-888e-ff96708db4d)
echo -e "\nTest 12: Verify that a 422 (Unprocessable Entity) status is returned for a get loan request with an invalid loanId"
assertCurl 422 "curl http://$HOST:$PORT/api/v1/members/m1/loans/c3540a89-cb47-4c96-888e-ff96708db4d -s"

# Verify that a 404 (Not Found) status is returned for a non existing memberId (c3540a89-cb47-4c96-888e-ff96708db4d7)
echo -e "\nTest 13: Verify that a 404 (Not Found) error is returned for a get loan request with a non existing memberId"
assertCurl 404 "curl http://$HOST:$PORT/api/v1/members/c3540a89-cb47-4c96-888e-ff96708db4d7/loans -s"


if [[ $@ == *"stop"* ]]
then
    echo "We are done, stopping the test environment..."
    echo "$ docker-compose down"
    docker-compose down
fi