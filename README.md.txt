
NOTE-
database -> db
collection name -> accountData
New fieldcalled type has been added to help in classification between savings and current


Query to add two accounts-

db.accountData.insertMany([
{"Name":"Richy", "Balance":190.0, "Type":"Saving"},
{"Name":"Poory", "Balance":1000000.0, "Type":"Current"}])
