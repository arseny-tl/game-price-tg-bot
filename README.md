### GamePriceTelegramBot
Yet, another telegram bot.

You can use two commands:
1. /hello {name}
2. /findGame {gameName}

Response for first command is always 'hello {name}'.

Second one searches for prices in gameShark and returns 10 price offers with thumbnail. 

Source files contain instrumentation similar to  https://github.com/xabgesagtx/telegram-spring-boot-starter. I have tried to use it, but wanted to create my own instrumentation just to practice. 

### Prerequisites
1. JDK 17 and above

### How to use it
1. Create bot in telegram https://core.telegram.org/bots#3-how-do-i-create-a-bot
2. Run maven goal: mvnw clean package
3. In docker folder:
   1. Set up MY_BOT_TOKEN/MY_BOT_USERNAME in env file with values from step 1
   2. Run docker-compose up

### TODO
Application:
1. Logging
2. Concurrency
3. Reactive
4. Tests

Infrastructure:
1. Bot Metrics (actuator - prometheus)
2. Grafana dashboard (which route and how many times was called, mean/max/min time to process, etc.)
3. k8s descriptors (minikube at least)
