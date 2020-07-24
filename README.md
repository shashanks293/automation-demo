# automation-demo
Automation to create jira tickets

Steps to run:
1. setup Scala: "brew install scala" in mac
2. Setup sbt: https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html
3. clone the repo
4. navigate to repo in terminal
5. enter command "sbt" and hit enter button
6. Once sbt console is open enter "compile" and hit enter to compile the code
7. once code is compiled successfully enter command "run" and hit enter button to run the service
8. hit the api "POST localhost:9000/api/v1/createJiraTicket" to execute the code.

