import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Tetris extends JFrame implements KeyListener{
    //Number of rows in the game 26, number of columns 12
    private static final int game_x = 26;
    private static final int game_y = 12;
    //Text field arrays
    JTextArea[][] text;
    //Two-dimensional arrays
    int[][] data;
    //Tabs showing the status of the game
    JLabel label1;
    //Tabs showing game scores
    JLabel label;
    //Used to determine if the game is over
    boolean isrunning;
    //An array to store all the squares
    int[] allRect;
    //Variable for storing the current square
    int rect;
    //Resting time of threads
    int time = 1000;
    //Indicates square coordinates
    int x, y;
    //This variable is used to calculate the score
    int score = 0;
    //Define a flag variable to determine if the game is paused
    boolean game_pause = false;
    //Define a variable to record the number of times the pause button has been pressed
    int pause_times = 0;

    public void initWindow() {
        //Set window size
        this.setSize(600,850);
        //Set whether the window is visible or not
        this.setVisible(true);
        //Set window centering
        this.setLocationRelativeTo(null);
        //Setting up the release form
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set window size unchangeable
        this.setResizable(false);
        //Set title
        this.setTitle("Tetris");
    }

    //Initialising the game interface
    public void initGamePanel() {
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x,game_y,1,1));
        //Initialising the panel
        for (int i = 0 ; i < text.length ; i++) {
            for (int j = 0 ; j < text[i].length ;j++) {
                //Set the number of rows in the text field
                text[i][j] = new JTextArea(game_x,game_y);
                //Set the background colour of the text field
                text[i][j].setBackground(Color.WHITE);
                //Adding keyboard listener events
                text[i][j].addKeyListener(this);
                //Initializing game boundaries
                if (j == 0 || j == text[i].length-1 || i == text.length-1) {
                    text[i][j].setBackground(Color.MAGENTA);
                    data[i][j] = 1;
                }
                //Set text area not editable
                text[i][j].setEditable(false);
                //Text area added to the main panel
                game_main.add(text[i][j]);
            }
        }
        //Add to window
        this.setLayout(new BorderLayout());
        this.add(game_main,BorderLayout.CENTER);
    }

    //Instructions panel for initialising the game
    public void initExplainPanel() {
        //Create the left description panel of the game
        JPanel explain_left = new JPanel();
        //Create the right description panel of the game
        JPanel explain_right = new JPanel();

        explain_left.setLayout(new GridLayout(4,1));
        explain_right.setLayout(new GridLayout(2,1));
        //Initialising the left description panel

        //In the left description panel, add the description text
        explain_left.add(new JLabel("Space:deformation"));
        explain_left.add(new JLabel("Left: Shift left"));
        explain_left.add(new JLabel("Right: shift right"));
        explain_left.add(new JLabel("Down: Whereabouts"));
        //Set the content of the label in red font
        label1.setForeground(Color.RED);
        //Add game status tab, game score tab, to the right description panel
        explain_right.add(label);
        explain_right.add(label1);
        //Add the left description panel to the left side of the window
        this.add(explain_left,BorderLayout.WEST);
        //Add the right description panel to the right side of the window
        this.add(explain_right,BorderLayout.EAST);
    }

    public Tetris() {
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        //Initialize the label indicating the game state
        label1 = new JLabel("Game Status:In Play");
        //Initialize the label indicating the game score
        label = new JLabel("Scores:0");
        initGamePanel();
        initExplainPanel();
        initWindow();
        //Initialisation of the start of play logo
        isrunning = true;
        //Initialise the array of squares
        allRect = new int[]{0x00cc,0x8888,0x000f,0x888f,0xf888,0xf111,0x111f,0x0eee,0xffff,0x0008,0x0888,0x000e,0x0088,0x000c,0x08c8,0x00e4
                ,0x04c4,0x004e,0x08c4,0x006c,0x04c8,0x00c6};
    }

    public static void main(String[] args) {
        Tetris tertris = new Tetris();
        tertris.game_begin();
    }

    //How to start the game
    public void game_begin() {
        while (true){
            //Determine if the game is over
            if (!isrunning) {
                break;
            }

            //Play the game
            game_run();
        }
        //Show "Game Over" in the tab position
        label1.setText("Game Status:Game Over");
    }

    //Method for randomly generating the shape of a falling cube
    public void ranRect() {
        Random random = new Random();

        rect = allRect[random.nextInt(22)];
    }

    //How to run the game
    public void game_run() {
        ranRect();
        //Position of the cube drop
        x = 0;
        y = 5;

        for (int i = 0;i < game_x;i++) {
            try {
                Thread.sleep(time);

                if (game_pause) {
                    i--;
                } else {
                    //Determining whether a cube is dropable
                    if (!canFall(x,y)) {
                        //Set data to 1, indicating that a square is occupied
                        changData(x,y);
                        //Loop through 4 levels to see if any rows can be eliminated
                        for (int j = x;j < x + 4;j++) {
                            int sum = 0;

                            for (int k = 1;k <= (game_y-2);k++) {
                                if (data[j][k] == 1) {
                                    sum++;
                                }
                            }

                            //Determine if there is a line that can be eliminated
                            if (sum == (game_y-2)) {
                                //Eliminate the line j
                                removeRow(j);
                            }
                        }
                        //Determining if a game has failed
                        for (int j = 1;j <= (game_y-2);j++) {
                            if (data[3][j] == 1) {
                                isrunning = false;
                                break;
                            }
                        }
                        break;
                    } else {
                        //Layer + 1
                        x++;
                        //Square down one row
                        fall(x,y);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //A method for determining whether a cube can continue to fall
    public boolean canFall(int m,int n) {
        //Define a variable
        int temp = 0x8000;
        //Traversing 4 * 4 squares
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    //Determine if there is a square in the next row at that position
                    if (data[m+1][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
        //Can be dropped
        return true;
    }

    //Method for changing the value of the area corresponding to a non-droppable square
    public void changData(int m,int n) {
        //Define a variable
        int temp = 0x8000;
        //Traversing the entire 4 * 4 square
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    data[m][n] = 1;
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //Remove all squares in a row to make the above squares fall
    public void removeRow(int row) {
        int temp = 100;
        for (int i = row;i >= 1;i--) {
            for (int j = 1;j <= (game_y-2);j++) {
                //Conducting coverage
                data[i][j] = data[i-1][j];
            }
        }
        //Refreshing the play area
        reflesh(row);

        //Cube acceleration
        if (time > temp) {
            time -= temp;
        }

        score += temp;

        //Show changed scores
        label.setText("Scores:" + score);
    }

    //How to refresh the game screen after removing a line
    public void reflesh(int row) {
        //Traversing a play area of more than one row
        for (int i = row;i >= 1;i--) {
            for (int j = 1;j <= (game_y-2);j++) {
                if (data[i][j] == 1) {
                    text[i][j].setBackground(Color.BLUE);
                }else {
                    text[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    //The way the cube drops down one level
    public void fall(int m,int n) {
        if (m > 0) {
            //Clear the previous layer of squares
            clear(m-1,n);
        }
        //Redrawing the squares
        draw(m,n);
    }

    //How to clear a coloured area on the previous level after a cube has fallen
    public void clear(int m,int n) {
        //Defining variables
        int temp = 0x8000;
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.WHITE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    //Method of redrawing a backward square
    public void draw(int m,int n) {
        //Defining variables
        int temp = 0x8000;
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((temp & rect) != 0) {
                    text[m][n].setBackground(Color.BLUE);
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Control game pause
        if (e.getKeyChar() == 'p') {
            //Determine if the game is over
            if (!isrunning) {
                return;
            }

            pause_times++;

            //Judgement press once to pause the game
            if (pause_times == 1) {
                game_pause = true;
                label1.setText("Game Status:Paused");
            }
            //Judgement pressed twice, play continues
            if (pause_times == 2) {
                game_pause = false;
                pause_times = 0;
                label1.setText("Game Status:In Play");
            }
        }

        //Control cubes for deformation
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            //Determine if the game is over
            if (!isrunning) {
                return;
            }

            //Determine if the game is paused
            if (game_pause) {
                return;
            }

            //Define the variable that stores the index of the current square
            int old;
            for (old = 0;old < allRect.length;old++) {
                //Determine if it is the current square
                if (rect == allRect[old]) {
                    break;
                }
            }

            //Define variables to store deformed squares
            int next;

            //Judging by the squares
            if (old == 0 || old == 7 || old == 8 || old == 9) {
                return;
            }

            //Clear the current square
            clear(x,y);

            if (old == 1 || old == 2) {
                next = allRect[old == 1 ? 2 : 1];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old >= 3 && old <= 6) {
                next = allRect[old + 1 > 6 ? 3 : old + 1];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old == 10 || old == 11) {
                next = allRect[old == 10 ? 11 : 10];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old == 12 || old == 13) {
                next = allRect[old == 12 ? 13 : 12];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old >= 14 && old <= 17) {
                next = allRect[old + 1 > 17 ? 14 : old + 1];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old == 18 || old == 19) {
                next = allRect[old == 18 ? 19 : 18];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            if (old == 20 || old == 21) {
                next = allRect[old == 20 ? 21 : 20];

                if (canTurn(next,x,y)) {
                    rect = next;
                }
            }

            //Redrawing the deformed square
            draw(x,y);
        }
    }

    //A method for determining whether a square is deformable at this point
    public boolean canTurn(int a,int m,int n) {
        //Creating variables
        int temp = 0x8000;
        //Traversing the entire square
        for (int i = 0;i < 4;i++) {
            for (int j = 0;j < 4;j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n -4;
        }
        //Can be deformed
        return true;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Square for left shift
        if (e.getKeyCode() == 37) {
            //Determine if the game is over
            if (!isrunning) {
                return;
            }

            //Determine if the game is paused
            if (game_pause) {
                return;
            }

            //Does the cube hit the left wall
            if (y <= 1) {
                return;
            }

            //Define a variable
            int temp = 0x8000;

            for (int i = x;i < x + 4;i++) {
                for (int j = y;j < y + 4;j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j-1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }

            //First clear the current square
            clear(x,y);

            y--;

            draw(x,y);
        }

        //Square for right shift
        if (e.getKeyCode() == 39) {
            //Determine if the game is over
            if (!isrunning) {
                return;
            }

            //Determine if the game is paused
            if (game_pause) {
                return;
            }

            //Defining variables
            int temp = 0x8000;
            int m = x;
            int n = y;

            //Store the rightmost coordinate value
            int num = 1;

            for (int i = 0;i < 4;i++) {
                for (int j = 0;j < 4;j++) {
                    if ((temp & rect) != 0) {
                        if (n > num) {
                            num = n;
                        }
                    }
                    n++;
                    temp >>= 1;
                }
                m++;
                n = n - 4;
            }

            //Determine if you have hit the right wall
            if (num >= (game_y-2)) {
                return;
            }

            //Whether the block touches another block on its way to the right
            temp = 0x8000;
            for (int i = x;i < x + 4;i++) {
                for (int j = y;j < y + 4;j++) {
                    if ((temp & rect) != 0) {
                        if (data[i][j+1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }

            //Clear the current square
            clear(x,y);

            y++;

            draw(x,y);
        }

        //Cube for drop
        if (e.getKeyCode() == 40) {
            //Determine if the game is over
            if (!isrunning) {
                return;
            }

            //Determine if the game is paused
            if (game_pause) {
                return;
            }

            //Determining whether a cube is dropable
            if (!canFall(x,y)) {
                return;
            }

            clear(x,y);

            //Change the coordinates of the square
            x++;

            draw(x,y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
