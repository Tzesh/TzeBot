# Discord Bot & Substructure
![GitHub issues](https://img.shields.io/github/issues/Tzesh/TzeBot) ![GitHub pull requests](https://img.shields.io/github/issues-pr/Tzesh/TzeBot) ![GitHub top language](https://img.shields.io/github/languages/top/Tzesh/TzeBot) ![GitHub repo size](https://img.shields.io/github/repo-size/Tzesh/TzeBot) ![GitHub All Releases](https://img.shields.io/github/downloads/Tzesh/TzeBot/total) ![GitHub last commit](https://img.shields.io/github/last-commit/Tzesh/TzeBot) ![GitHub Release Date](https://img.shields.io/github/release-date/Tzesh/TzeBot)

## What is TzeBot?
TzeBot, is a discord bot project that I've started about 6th month of 2020. The reasons that I started to develop TzeBot were:
1. There was not a such discord bot substructure that runs properly at all. Some of them were broken completely, some of them were just worked terribly.
2. There was not a such discord bot substructure that whose has working GUI or good manual that describes you how can you host your own bot on it.

Then I just wonder why was I not developing a really good one to publish it for free to use. All of the story of TzeBot just came out like this.

## How can I use it?
### Use TzeBot as a substructure of your bot

Just download the latest version of TzeBot <a href="https://github.com/Tzesh/TzeBot/releases">**in this link**</a>. And extract TzeBot-x.x.rar to a directory then open TzeBot-x.x.jar. If you want to use TzeBot as a substructure of your bot, you'll need 3 things that have to be filled:

![GUI](https://user-images.githubusercontent.com/55401127/195959210-35b938f3-2851-410d-bc76-f0dd588ecf8e.png)

1. Discord ID of Owner: Just enable developer mode in Discord and then just message an arbitrary channel and then right click on yourself then "Copy ID".
2. Discord Bot Token: You propably have it already, if you don't just search in google "how to create discord bot in discord developer portal?".
3. Youtube API Key: Just follow the instructions <a href="https://developers.google.com/youtube/v3/getting-started">in this page</a>.

Now you are ready to go. Just hit start button to make your bot online. And then create a invite link to invite your bot into your channel. You can change every single message by editing localizer.json located once you started the bot. Also you can add new features/commands or improve existing ones by forking the repository.

When it came your server first time, it's prefix will be "**.**" and it's language will be "**English**". You can change it's language and prefix if you want to, before that shall we just look at the commands of TzeBot? Type "**.help**":
![Help Command](https://i.imgur.com/WlFcuRG.png)

As you can see, all of the commands are categorized, if you want to look at the all moderation commands and their explanations just type "**.help moderation**":


![Moderation Commands](https://i.imgur.com/3g4MRSA.png)

If you want to look at music commands of TzeBot, just type "**.help music**":


![Music Commands](https://i.imgur.com/mO0KUxA.png)

If you aim to use TzeBot as a music bot that you might have been noticed "**.channel**" command. You can use TzeBot as a normal music bot by writing each command "**.play**", "**.skip**", "**.volume**"... Or you can just setup your music channel to feel the difference and convenience of TzeBot. Type "**.channel create**":

![Music Commands](https://i.imgur.com/aAQo1Xc.png)

Then all you have to do is just go to the created music channel and type "**.channel set**":

![Music Commands](https://i.imgur.com/gpGL7Zw.png)

After you wrote "**.channel set**" into the music channel you will have been noticed that your message is deleted and 2 messages added into music channel. First one is banner of TzeBot, the other one is the reaction-control panel or at least the name that I put into it. It allows you to control music player with emotes. And all the functions of the emotes is above of themselves. You can just type name of the song or URL of the song that you want to play into channel.

#### Features of TzeBot substructure
* Automatic saving-loading: Don't worry about losing 
of your servers. All of the database is saving per 15 minutes and also on close and loading every start.
* Version control: If you are encountering some problems when using TzeBot substructure, just give us a feedback and then wait for a new version. In every start TzeBot will check if there is an update or not.
* Sharding: This is a feature that allows you to use your bot too many of servers. 1 shard unofficially equals about 1500 server load.
* MongoDB: TzeBot can use MongoDB as a database. It's really easy to setup, you can just change variables in .env file, and then you are ready to go.

#### Requirements
- Java Version (18.0+)

TzeBot averagely uses 125-225 MB RAM and %0.1 CPU while working, it's really resource friendly. If you have any questions you are always free to ask me if you encounter any errors or something that might has to be feedbacked please open an issue in issue section and ask in there...
