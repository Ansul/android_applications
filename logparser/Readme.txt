

Debugging Touch activity in the given issue logs.
Application parses the Touch activity occuring in the logs.
Outputs the parsed activity on the available device.

Application helps in efficient touch debugging by putting the touch activity(Press/Release Palm, Key Press & Touch Event)  occurring in the issue logs on the actual device.

Directions: 

Add kernel log or main log to be parsed at 
	/mnt/sdcard/kernel.log or /mnt/sdcard/main.log
abd push <Filename> /mnt/sdcard/<kernel.log OR main.log>

Press Parse/Reset Button -> Parses new log or Resets variables

Use Complete Parse Button-> All PRESS & RELEASE events

Start Button -> To see step by step activity using manual touch. 

Autorun Button -> To start the visualizing touch events on available device as occurred during the issue.

- Settings

Kernel Log Parser
 Manual ( Touch: LCD ) ratio
 Pressure : Outputs touch events only with Pressure value below 		  the required pressure.

Main Log Parser
Manual ( Touch: LCD ) ratio
Autorun rate: 1x , 2x , 3x autorun rate than the actual occurance.


To clear the screen during manual Step by Step Touch event viewing use hardware Back button and press Start Button again to continue the process

Long press Back button to exit


