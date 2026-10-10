# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[![Sequence Diagram](2-sequence-diagram.svg)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAEYAdE8z2oEAK7YAxAAsAMwAHABMAJwgMH7I9gAWYDoI3oYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD03gZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVx0UBQwAA1jAAELADgwWEAR1SOUwbE43FgpXuN2eyNRGSxajAAFEAB4qbAEAoACgAlNdqpQ7vdiuZ6gEAAwC4YjZZzdTAezzepjalQLx1GB6NGY7FgJZXdAcXGPAl3So3UT1WFZHKUZkZU1gSgUtXsw0qfUybRKFTqepSsAAVQGzLeHxQ7Nk8ldalUTuMtQAYkhODAfZRg8AlZYYP7ZmIYXDEQnYJskGB4mmBgGYMAELDUamULTsmANEnQ+puQbqkb4wMkxyRI6ia3qKSFBW4RxLO0Eeg6QymYUSYSecg+TBBUERWNxapJdLlnKFfV03My8OqzB5PD0FCzJxMI3lGGW1Ueyh6mhvAgEN2qKInbe3apaiAlbWrmfqdtoQYunezb3JGCgcGiuZdg6hj3L+YYAUBKAKN4hbMsAOHxEmEEhlB4YwTotRwWi2GFkhba9vceJPIqlp1jaahvlgTF6n2j41C8EwlhmSwysCyz4YW7QQOeaAicslyflyvG8hg9RhEKa4HvMMCid8EnxFJMlyfs0KVoicAQBwhgAKxCjAUbgt4sL1BZb5ohAyhOWWaCWJs8SUCgaywmATloDAlAKjekF-g+yH1BF0Cft+vHcaSCVQDq+JKcSnKKulinzpUKlgPUtnCqMYphluCw7vK0D1MyVmqIBxAzjAEAAGbhXVUCshqV7alqpieD4-jQOwUoxFGcDUtIcAKDAAAyEBZIUxVOnO9TNG0XS9AY6j5GgIpaWsvz-BwVxzk6qWKiMJ0wGdOymdmZIqiglI4jdsW5fUKIcLaVJTigjKHWyBUPsV-IaRV65VVKNWyj19TKui71qv1Q1fTlT71AgK2xsyy2rQDOT2vRKGVGh7owJ6IFacRiikRGFH2bGCFgfIKbFlMGaYFmCIdjzh4cBAahoAA5MwNZ1u1sCxiA0CwuAD36H8OxRSRMW8XFgvvBmRii6oEtS7WOSyzAwzy4rKDK49AJJQx-b8fG0wEdASAAF4oBwQMgwU4PKYuqnLgKTgipVErwzKu71WmruFu7Xu7DAmrXlTZHY1+Kj1Ih2gOxTzqa+hgFwtaNHxHhBFERrjNa5UsHwTA5d0U+D43fUROxhkqicZlzHfQOt33fphnoJduUQ0HJUwOp5V3UJcxrCP0lj3zZnwJZNl2Q5pxOc+G9ue1nmwOwvn+bCQUoCF7zdZF6cD1n+-5chbe6mlPV9zxmfv5FV2ByU08yrh1hpHbciM9wwEamoFqfswqdVvtAPqKcBooI8F4XwfhPAoHQDEOIiQsE4KJj4LA61v6KgaNIaki1qTtGpN0Ho+1VCHWGMvGSAdKgPCyoqVh6BP5KSdu2PG9hiGExWsQkmYAyat1QtFdCNMr7l0rpJFeaAGZNjIvXFmGQLCoBoE3Kued+Y5njvERO3sa7qIfu2V87584P1JF6ExZifb0mBjOAOC4AFQzDqMEBm4o7gNjt4JxUBPbe0vENe+2tyb1GbnnF+Mii7UystwMuBElEGRUWopm5F6jSBQKkwwcT5B2JSm-RUVAIBIG1FjMh9RKnVI8UVKealoYjGegLCyVkYBlXso5ZyB8EDuWPt5M+AVL7XzCvlKJmd2zP3Jq-Lh8UP61Kdj-RKf9PFLiATDDc1Vo5I0gU1GBbV4HpSQanQa140GjT8LCNEfhsCxkRItGWABxDMGhSFrPIW8mh9D7AZhYQRUes4J6MXKfUHhhQsYCOzjAZAOQPlzEJnWZFKAJFSMfj+WR1NPSKOhdkuuRgtFX1CSgPR5dE55H9kYl2+lnEWJybM+FNiPwJLqfSt2oSk6+3cZs5pXiQ4+PGHsgJMdFTBIZTy8JlymVaxZfvYpwBSmU1xf+BFaLPlgyiZo+o2jyV6PRRoDllR25LS1XMbuvdVl8VJCMdFrycjj0HpPIVs9gGAvFDVBo4wvUoAAJLSBqk4MIQQAggj8gWFAuYAxfBBMkUA8JY3CUhCCf1AA5DMkILgWzQJ0Dp5lN49O3v0-erkhlHxAF5U+flxmo0mQgjKMy4VPw-qazhzFlmRVtZtJtTT4AtJLXPCO-iwESoasc0JsD2pdXORja8Q0bkYI4AAdgiAKFAAoYjUiCHAGaAA2eAmEYDopgEUKeG0fqNFaB0AFQKTGgpFJmjMLrbgQqWWWEFKjn0ZizXMQtr1UYfS4uU+xio-oSL5aDLF2VBVLkFCOvx+zaoQJRqqKkC6algcVfUEucgUDouZHATC6LMWqsLrXOR+L0mEvlfeXJMADW6KKdyz2NLCh0scdKsJ2oW18WsW+dlCzomDxziE3j0H-YCsHUKwUIrkPisOVKtjScIlp3VVY+FyqKPpzw6RjMzIX1zCJQxvVx7S6GHRaUj9XaLMEbIxxBAoGuHgZeMsf1QavjIJk5DGebSxieeDfUUN4bANdK3tuneKQBkVuGdWk+Pk60XwbaFft-GdbzOkbZgk3bEq9uvVl99Wzg47NFIp8dhyoHNWnacudPULkoKXSNDBlgCl402LgpACQwBtffBATrAApKpYUz1+ETSAeEF6AFXrEzer0u0ej+uBcomSIpsAIGAG1qAFk8ZQDWEFt9-DO25ehetzb23dvQAOxmINgG-rAbVHwwqdqIPkjRoDVxsCwa+aHYh4BYrKtodRI9zDyDMY4dbfUAAViNojw3YyOZA1i5Kaqkkapo7hOjuqSX6rJSx-RCceUcbXi9bjqnzEZZiTANlNmfniZ47yr7-LwUlenvJtcgO9gTrjoz2VKCqc40J4ReJIm0dUbxVfIjQX2R0qHJWUcp4JyFEF4-eo6KkzDA21tygV3YBoAgMwWnHa+3oqjEJqTYLXX-wQwKVcuy4ZA9juWBXqYzwXnBxp9HWn94a9F9I8X6j9zYC0NaIj2vLsQD2zduYQbTPQXM1RB6F2uSQBprzDt5qEdoEcz3Zzz23MwHaQOvzHrRjheLb06Le8XI+ErR5BLozkuBVSzfaZmnRNq-7ZnyF-aCtzaK3B2T2y2mjpQ4ExU1WTmHVnU2xrQ1mvoP8J4LbXWesr9TIgOEJ9sAbcIBx893zXtbUodQ2h9DjDsJO-aq4sKBPwpSVfFAP2xeUaDzAEA3A8D0YT7jwd2+jwEAjBtAYAOovAZhTx-JgD5A9ADAf8M4ocachM6dr9FRad+9nZjdWd4Ng4y9i86UIth0+ld5Ys694sa0ktz4W9go0t29vdO85l20RMct1kMoMDWCS8h0ysx8o5UNY4p9asZ8zkGssMUEgA)

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
