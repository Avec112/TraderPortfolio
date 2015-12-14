# TraderPortfolio
www.nvesttech.no has a trader portfolio put up based on their analysis and their tools for technical analysis. This portfolio have done quite well
since the beginning 2002. Since then the portfolio is up almost 25000% (after commision) compared to OSEBX 405%

Investtech however does not provide a notification service for changes done to the portfolio. 

I have made an applications that triggers a method taking a "snapshot" of current portfolio status (changes) so you as an investor can buy and sell
the stocks in the portfolio more or less at the same time as Investtech.

Note! The Trader portfolio has a high risk so you have to deside if this is something for you. 

## Requirement
* Maven 3
* Jdk 1.8
* www.investtech.no (Norwegian) [Trader account](http://www.investtech.no/main/market.php?CountryID=1&p=staticPage&fn=products) or higher
* Gmail account
* Two Twitter accounts (optional). Two since you cannot get at notification from self with direct messages 

## Features
* Follow Investechs Trader portfolio (only Norwegian stocks)
* Recieve current portfolio status (buys and sells) by mail same day as Investech put up their recommandation
* 

## Trader portfolio results period between 2012 and 2015 (14 dec)
Comparison | 2015| 2014 | 2013 | 2012
--- | --- | --- | --- | ---
portfolio	| 95.86	| 29.51|	55.27 |	37.76
portfolio after comission | 79.39	| 17.94	| 42.06	| 26.38
Oslo stock exchange (osebx)	| 3.26 |	4.95	| 23.59	| 15.36

## Configuration

## Start application
> mvn clean install
> mvn exec:java

