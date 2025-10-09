# ♕ BYU CS 240 Chess
This is Jonas' version control for the CS 240 Chess project

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[Chess Server Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVzofClRAf64Hlp8kIgmpOxfNCjzAeJVBIjACBCeKGKCcJBJEmApJvoYu40vuDJMlOylcje3l3kuwpihKboynKZbvEqmAqsGGputqur6gFRgQGoaAAOTMFaaJBbyIVWTZPZ9tunlWf6AByEBulGMZxoUoGYcgqYwOmACMBE5qoebzNBRYlq60yXtASAAF4oLsdFNsODpJr6zpdhu7pbh5grzfSVAmkgWjok0+h-DsGKRdoqlHeppIJeqhULgKi0dtZLrat4DgwCZAIVSB1T+gA6m99iHVsOwNSgsYKVprUlGAaZON1oxjL1-UFmMQ3QCNFHjVNM2Nuud37j9nb1BYqTfV5RX8mOE4oM+8Tnpe15bQ9lQPmuAaM+ty3trppZOeKGSqABmC80TNQvIRBnzCRqHfBRVH1jLtFQ49ok4XhBEBTRZFjPLiGK6RDb0Yx3h+P4XgoOgMRxIkFtW05vhYKJgqgfUDTSBG-ERu0EbdD0cmqApwx60hKs6RZ-oh+gIsR9pT02XZ9iO45QmOy5ahueTVK3lTMCMmAdM8gz8H62gc7BQtrPCjA4VPpz8gpXqxqXgrhQ3RqBPFUtxMwGV-YbY9rswLVhfaGDEPxi1qttdhMC4fhoyzQxzNizZo-yFnMg5-SMAojMEA0OvwDF5Rpfl5Ti5V-UGT7zQzcvtojf6lHzXc49vP1A7p6C8LouD79YErhT2TDPWGc8NaLzxibZi-gUTrn8NgcUGp+JohgAAcSVBoZ2VVSwNDQd7P29glTBxbqXMODxYSR1IaHP+3dnorXzhgnMGI0FMiYWodOJJN7M18gXeux8X7n3uveautcOYPwbjqJuL94qqg7ivf+Pc+7kxwfUEe-Dx5NRViAmGcMEbZn5CjQaxYMaSlGnqbG00jZzW3g9EqL0j7cNsbw9hqgT6tyEYTUK9RWHMBNAgPOmCn731PqHAeH5Y4+LYZgn+CBAKxzFnpZYRCcwFgaOMFJKAACS0gCydXCMEQIIJNjxF1CgN0nI9jfGSKANUFTIKLG+Jk6qSpGkXBgJ0IBADobtXnlmZJmC0kZKVDkvJBSinLBKWU+phkxjVIQLUmZA05kgmaa0uZ7TOlQM8KbAIHAADsbgnAoCcDECMwQ4BcQAGzwBpugisRRQEuwAW7VoHRCHEPMaE9AWY1lzC6RJOOH8YAjAGXMFpsyljmUoXHDy9RDxyBQOwjEcAabsM4ZnAe2cK473zkfdxZ9O6VyFPUMRR9gkyPbjAIli57ErWUeEuhNUIBH00ZDYBlQ1YdXhj1Qx+ZjHDTMVjKAk0rFL2pQoul9RHFYq3ji0cMAEXomRYImlLMSW3KPIYYAvcUCbECXMCl1Do6MooV+eoqKtXov-HEmOMLElgTBdk3J9R8mFJgACxMOjekQNBWMTJozXXjI9dspiZtLAoD7BAfVsQkAJDABGqN+qABSEBxT3MNf4GpIA1SPJhs8wFrzmQyR6JkkhJckJZmwAsiNUA4AQDslANYAbpCeowhEmFLx1g1soPWxtew-osCyT7HoAAhfiCg4AAGkmkjJdTAN1gQQ20Pji9AAVmmtAyLU3imtYSDO7k37YovgqvF-CCVIU8SFK+NdxR1wkTqqRz9jVtzkRK2xq8XoMrfqo4eLKNHRnBlojl8BQF6N5bmflhYTGlmZF8yxuNjaSroWvfhTj5UuKVEXFtV7iXLlvRKdhMon0GudWqz9K0iNc07JtZxecmRNB7XWht0AlkYhw+R7xBH1xTPRD0bNGp0okZbbIxK775UUe7BAZg36aOKPFgGKsoZwxoDZZPbp09dHcszKMZYyMoNoxg-UE0ZoawqahVA5Dq76W9n7j+8OnaYA7q3UqWJ8T7Xyb0m2+E3rZ59MgcbHZMCvDAHlLG+NIX5SIGDLAYA2Bq2EDyAUal2CmW4I9l7H2ftjDkOBSAbgeAMSkhXXCxVBWoAovKzyPQBgMWHrk8e+68LytFc4ze-LMWYqqGbZgtYjwQmtw0Ka4F0W8DVf0CgNzdqvwOpeN5npfnfVL0wEAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
