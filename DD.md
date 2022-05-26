# Design Document

## Functional Details and Project Structure

### Class

Class variables include but are not limited to:
|Variable	|Function	|Type|
|---|---|---|
|game_x	|Abscissa of location|	int|
|game_y	|Ordinate of location	|int|
|x	|Square length|	int|
|y	|Square width|	int|
|score	|Players' scores|	int|
|rect	|Square code|	int|
|time	|Game time|	int|
|isrunning	|Determining whether a game is played or not|	boolean|
|allRect	|Collection of all square codes|	int[ ]|
|data	|Coordinates|	int[ ]|

Class Methods include but are not limited to:
|Name	|Function|	Return|
|---|---|---|
|initWindows()|	Game Borders	|void|
|initGamePanel()	|Game Background	|void|
|initExplainPanel()|	Game description|	void|
|game_start()|	Game Status|	boolean|
|ranRect()|	Randomly generated drop squares	|random|
|changeData()	|Variation in the speed of the fall of a square|	int|
|canfall()|	Determining whether a square is dropable|	boolean|
|removeRow()	|Remove more than one row of squares|	int|
|reflesh()	|Refreshing the screen after removing a square|	int|
|clear()	|Clear the squares|	int|
|draw()	|Draw the squares|	int|
|keyPressed(KeyEvent e)|	Control the movement of the squares|	void|
|keyReleased(KeyEvent e)|	Control the deformation of the squares|	void|

### Execution plan
|Shape|	Code|
|---|---|
|0  0  0  0<br>0  0  0  0<br>1  1  0  0<br>1  1  0  0	|0x00cc|
|1  0  0  0<br>1  0  0  0<br>1  0  0  0<br>1  0  0  0	|0x8888|
|0  0  0  0<br>0  0  0  0<br>0  0  0  0<br>1  1  1  1	|0x000f|
|1  0  0  0<br>1  0  0  0<br>1  0  0  0<br>1  1  1  1	|0x888f|
|1  1  1  1<br>1  0  0  0<br>1  0  0  0<br>1  0  0  0	|0xf888|
|1  1  1  1<br>0  0  0  1<br>0  0  0  1<br>0  0  0  1	|0xf111|
|0  0  0  1<br>0  0  0  1<br>0  0  0  1<br>1  1  1  1	|0x111f|
|0  0  0  0<br>1  1  1  0<br>1  1  1  0<br>1  1  1  0	|0x0eee|
|1  1  1  1<br>1  1  1  1<br>1  1  1  1<br>1  1  1  1	|0xffff|
|0  0  0  0<br>0  0  0  0<br>0  0  0  0<br>1  0  0  0	|0x0008|