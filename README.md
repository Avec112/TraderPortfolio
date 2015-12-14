# TraderPortfolio
[www.investtech.no](www.investtech.no) has a trader portfolio put up based on their analysis and their tools for technical analysis. This portfolio have done quite well
since the beginning 2002. Since then the portfolio is up almost 25000% (after commision) compared to OSEBX 405%

Investtech however does not provide a notification service for changes done to the portfolio. 

I have made an applications that triggers a method taking a "snapshot" of current portfolio status (changes) so you as an investor can buy and sell
the stocks in the portfolio more or less at the same time as Investtech.

Note! The Trader portfolio has a high risk so you have to deside if this is something for you. 

## Trader portfolio results period between 2012 and 2015 (14 dec)
Comparison | 2015| 2014 | 2013 | 2012
--- | --- | --- | --- | ---
portfolio	| 95.86	| 29.51|	55.27 |	37.76
portfolio after comission | 79.39	| 17.94	| 42.06	| 26.38
Oslo stock exchange (osebx)	| 3.26 |	4.95	| 23.59	| 15.36

## Requirement
* Maven 3
* Jdk 1.8
* www.investtech.no (Norwegian) [Trader account](http://www.investtech.no/main/market.php?CountryID=1&p=staticPage&fn=products) or higher
* Gmail account
* Two Twitter accounts (optional). Two since you cannot get at notification from self with direct messages 

## Features
* Follow Investechs Trader portfolio (only Norwegian stocks)
* Recieve current portfolio status (buys and sells) by mail (gmail) same day as Investech put up their recommandation
* Twitter notification when you should buy or sell a stock (feature is comming)
* If there are no changes in the portfolio you will recieve a mail within the hour of the stock exchange closing time saying there was no changes to the portfolio that day 



## Configuration
Before you start the application you must configure it. Go to `[traderportfolio]/src/main/resources` and open the file `application.properties` in an editor. Add your values to the following keys.
```
# Investech login user and password
investtech.username=
investtech.password=

# Gmail user and password
gmail.username=
gmail.password=

# Twitter (without the @)
twitter.recipientId=
```

## Start application
After you have configured the application it is time to run it. 
From the command line (first time) type `mvn clean install` and then `mvn exec:java` or just type `mvn clean install exec:java` in on go.
To stop the application hit `ctrl + c`. 

When the application starts you should see something like 
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.3.0.RELEASE)

19:29:28.597 [no.avec.Application.main()] INFO  no.avec.Application - Starting Application on ducati.local with PID 40688
19:29:28.598 [no.avec.Application.main()] INFO  no.avec.Application - The following profiles are active: prod
19:29:29.274 [no.avec.Application.main()] INFO  no.avec.Application - Started Application in 0.814 seconds (JVM running for 5.099)
```


