

Debugging Touch activity in the given issue logs.
Application parses the Touch activity occuring in the logs.
Outputs the parsed activity on the available device.

Application helps in efficient touch debugging by putting the touch activity(Press/Release Palm, Key Press & Touch Event)  occurring in the issue logs on the actual device.


- Directions:

Add kernel log or main log to be parsed at 
	/mnt/sdcard/kernel.log or /mnt/sdcard/main.log
abd push <Filename> /mnt/sdcard/<kernel.log OR main.log>

Press Parse/Reset Button -> Parses new log or Resets variables

Use Complete Parse Button-> All PRESS & RELEASE events

Start Button -> To see step by step activity using manual touch. 

Autorun Button -> To start the visualizing touch events on available device as occurred during the issue.


- Color Code for different fingers:
	Finger<0>: BLACK
	Finger<1>: BLUE
	Finger<2>: GREEN
	Finger<3>: MAGENTA
	Finger<4>: RED
	Finger<5>: CYAN
	Finger<6>: LTGRAY
	Finger<7>: ORANGE
	Finger<8>: VIOLET
	Finger<9>: YELLOW


- Settings

Kernel Log Parser
 Manual ( Touch: LCD ) ratio
 Pressure : Outputs touch events only with Pressure value below 		  the required pressure.

Main Log Parser
Manual ( Touch: LCD ) ratio
Autorun rate: 1x , 2x , 3x autorun rate than the actual occurance.


To clear the screen during manual Step by Step Touch event viewing use hardware Back button and press Start Button again to continue the process

Long press Back button to exit


- For Eg.

Kernel.log
[	64.145152 / 01-01 19:00:49.889] [Touch] 1 finger pressed : <0> x[170] y[473] z[134]

[	64.209957 / 01-01 19:00:49.959] [Touch] touch_release[ ] : <0> x[170] y[473]


Main .log
01-01 00:04:42.589  1715  1736 D InputDevice: [InputReader][Touch] down[0] id:4  x:84  y:902

01-01 00:04:44.319  1715  1736 D InputDevice: [InputReader][Touch] move[0] id:4  x:92  y:874

01-01 00:04:44.329  1715  1736 D InputDevice: [InputReader][Touch] up[0] id:4  x:92  y:874

Containing the above sample Touch logs will be parsed & other logs are ignored.