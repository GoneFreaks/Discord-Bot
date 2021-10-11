# Discord-Bot

## Just a couple of guys having fun while creating the "successor" of groovy

Start the bot by typing ***java -jar Gruwie.jar*** into your console.

The bot has diffrent configurations, you can address each of them by modifying the ***config.properties File***.<br>
This file has to be in the **same directory** as the jar while starting the bot, otherwise the Bot won't boot.<br>

The config has three datatypes:	

				boolean (default)
				String
				Number
										
If you use an unmatching datatype for a property the Bot will use default-values instead.

In order to use the Bot, you have to set some properties first:

				1.token
				If this property has an invalid value, the Bot won't be able to boot
										
				2.owner_id
				This property has to be set, if you're intending to use the remote-system
				
The owner of Bot (the Account whose id is equals to the owner_id) can execute some Admin-Commands by typing them in a private-Chat with Gruwie.<br>
Keep in mind that you can theoratically change every single config-Option and reload it, but this could cause unexpected behaviour.<br>
Every non critical option is marked with an #uncritical-Tag.<br>