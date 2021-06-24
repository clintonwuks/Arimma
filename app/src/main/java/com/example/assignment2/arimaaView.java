package com.example.assignment2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class arimaaView extends View {
    private Paint grey, white, black, blue, green;
    private Rect square;
    private int move_count, player1count, player2count;
    int sourcecol;
    int sourcerow;
    int dest_col;
    int dest_row;
    int sqrHeight;
    int sqrWidth;
    int left_high,right_high,top_high,bottom_high;
    private int width,height;
    private int player1_pieces , player2_pieces;
//    private Player player1;
//    private Player player2;
    private int players[][] ;
    private boolean notTurn[][] ;
    private String pieces[][] ;
    private boolean highlighted[][];
    private boolean player1turn, player2turn;
    private int colNum, rowNum;
    private Bitmap gold_rabbit,gold_elephant, gold_camel, gold_horse, gold_dog, gold_cat;
    private float touchx[];// x position of each touch
    private float touchy[];// y position of each touch
    private boolean touch;// do we have at least on touch
    private Bitmap silver_rabbit,silver_elephant, silver_camel, silver_horse, silver_dog, silver_cat;
    private Bitmap arrow_up;
    private Context c;
    private Button undo_button;
    private TextView player1, player2, playerturn;
    private Canvas canvas;
    private boolean checker, checker2;

    public arimaaView(Context c) {
        super(c);
        init();
    }

    // constructor that takes in a context and also a list of attributes
    // that were set through XML
    public arimaaView(Context c, AttributeSet as) {
        super(c, as);
        this.c= c;

        gold_rabbit = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_rabbit);
        gold_camel = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_camel);
        gold_elephant = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_elephant);
        gold_dog = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_dog);
        gold_cat = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_cat);
        gold_horse = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.gold_horse);

        silver_rabbit = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_rabbit);
        silver_camel = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_camel);
        silver_elephant = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_elephant);
        silver_dog = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_dog);
        silver_cat = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_cat);
        silver_horse = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.silver_horse);
        arrow_up = BitmapFactory.decodeResource(c.getResources(),
                R.mipmap.arrow_up);
        init();
    }

    // constructor that take in a context, attribute set and also a default
    // style in case the view is to be styled in a certian way
    public arimaaView(Context c, AttributeSet as, int default_style) {
        super(c, as, default_style);
        init();
    }

    // refactored init method as most of this code is shared by all the
    // constructors
    private void init() {
//       player1 = findViewById(R.id.player1);
//       player2 = findViewById(R.id.player2);
         //player1 = (TextView) ((Activity) getContext()).findViewById(R.id.player1);
        //player pieces for keeping track of scores
        player1_pieces=16;
        player2_pieces=16;
         move_count=0;
         //set player 1 to go first
        player1turn = true;
        player2turn = false;
         //initialise integers that will be used to move players
         sourcecol=0;
         sourcerow=0;
         dest_col=0;
         dest_row=0;
         // initialise the integers for highlighting squares
         left_high=0;
         right_high=0;
         top_high=0;
         bottom_high=0;
        //CREATE PAINT OBJECT
        grey = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        green = new Paint(Paint.ANTI_ALIAS_FLAG);
       // grey.setColor(0x808080);
        grey.setColor(0xFF7C7B7B);
        white.setColor(0xFFFFFFFF);
        black.setColor(0xFF000000);
        blue.setColor(0xFFBB86FC);
        green.setColor(0xFF018786);
         colNum=8; rowNum = 8;
        players = new int[colNum][rowNum];
        notTurn = new boolean[colNum][rowNum];
        pieces = new String[8][8];
        highlighted = new boolean[colNum][rowNum];

        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                highlighted[i][j]=false;
                pieces[i][j]="";
            }
        }

        for(int i=0; i<8; i++){
            players[i][0] =1;
            players[i][1] =1;

            players[i][6] =2;
            players[i][7] =2;

            pieces[i][0] = "goldrabbit";
            pieces[i][7] ="silverrabbit";


        }

        pieces[0][1] = "goldcat";
        pieces[1][1] = "golddog";
        pieces[2][1] = "goldhorse";
        pieces[3][1] = "goldcamel";
        pieces[4][1] = "goldelephant";
        pieces[5][1] = "goldhorse";
        pieces[6][1] = "golddog";
        pieces[7][1] = "goldcat";

        pieces[2][2] = "trapSquare";
        pieces[2][5] = "trapSquare";
        pieces[5][2] = "trapSquare";
        pieces[5][5] = "trapSquare";

        pieces[0][6] = "silvercat";
        pieces[1][6] = "silverdog";
        pieces[2][6] = "silverhorse";
        pieces[3][6] = "silvercamel";
        pieces[4][6] = "silverelephant";
        pieces[5][6] = "silverhorse";
        pieces[6][6] = "silverdog";
        pieces[7][6] = "silvercat";

        touchx = new float[2];
        touchy = new float[2];

        touch = false;
        addToNotTurn(2);

////        INITIALIZE THE SQUARES
//        square = new Rect(-100, -100, 100, 100);

        player1count=0;
        player2count=0;
        checker=true;
        checker2=true;

        Toast.makeText(c,
                "GOLD PLAYER 1 GOES FIRST",
                Toast.LENGTH_LONG)
                .show();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set board as per screen size
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxwidth = getMeasuredWidth();
        int maxheight = getMeasuredHeight();
            width = maxwidth;
            height = maxheight;
            setMeasuredDimension(width, height);

    }

    // public method that needs to be overridden to draw the contents of this
    // widget

    public void onDraw(Canvas canvas) {
        this.canvas = canvas;
    // call the superclass method
        super.onDraw(canvas);
//        player1.setText("Player 1\n"+player1_pieces);
//        player2.setText("Player 1\n"+player2_pieces);

        player1 = (TextView) ((Activity) getContext()).findViewById(R.id.player1);
        player2 = (TextView) ((Activity) getContext()).findViewById(R.id.player2);

        playerturn = (TextView) ((Activity) getContext()).findViewById(R.id.playerturn);

        sqrHeight = width / rowNum;
        sqrWidth = height / colNum;
        Log.d(TAG, "onDraw: "+sqrWidth);

        player1.setText("Player 1\n"+player1_pieces);
        player2.setText("Player 2\n"+player2_pieces);

        //playerturn.setText("PLAYER 1 TURN!!");



//        square = new Rect(i * sqrWidth, j *
////                                    sqrHeight, (i + 1) * sqrWidth, (j +
////                                    1) * sqrHeight);
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {

                    if (i % 2 == 0) {
                        if (j % 2 == 0) {
//                            square = new Rect(i * sqrWidth, j *
//                                    sqrHeight, (i + 1) * sqrWidth, (j +
//                                    1) * sqrHeight);

                            canvas.drawRect(i * sqrWidth, j * sqrHeight, (i + 1) * sqrWidth, (j + 1) * sqrHeight, white);

                        } else {
                            canvas.drawRect(i * sqrWidth, j * sqrHeight, (i + 1) * sqrWidth, (j + 1) * sqrHeight, grey);

                        }
                    } else {
                        if (j % 2 == 0) {

                            canvas.drawRect(i * sqrWidth, j *
                                    sqrHeight, (i + 1) * sqrWidth, (j +
                                    1) * sqrHeight, grey);


                        } else {

                            canvas.drawRect(i * sqrWidth, j *
                                    sqrHeight, (i + 1) * sqrWidth, (j +
                                    1) * sqrHeight, white);

                        }
                    }
                }
            }

        canvas.translate(0,0);
       // Drawable d = R.drawable.gold_rabbit;

     //   Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                square = new Rect(i * sqrWidth, j *
                                    sqrHeight, (i + 1) * sqrWidth, (j +
                                    1) * sqrHeight);

                if(highlighted[i][j]){
                    canvas.drawRect(i * sqrWidth, j *
                            sqrHeight, (i + 1) * sqrWidth, (j +
                            1) * sqrHeight, blue);
                }

                if(pieces[i][j].equals("trapSquare")){
                    canvas.drawRect(i * sqrWidth, j *
                            sqrHeight, (i + 1) * sqrWidth, (j +
                            1) * sqrHeight, green);
                }

                 else if (players[i][j] == 1 && pieces[i][j].equals("goldrabbit")) {

                    canvas.drawBitmap(gold_rabbit, new Rect(0,0,100,100), square, white);
                }


                else if (players[i][j] == 1 && pieces[i][j].equals("goldcat")) {

                    canvas.drawBitmap(gold_cat, new Rect(0,0,100,100), square, white);
                }
                else if (players[i][j] == 1 && pieces[i][j].equals("golddog")) {

                    canvas.drawBitmap(gold_dog, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 1 && pieces[i][j].equals("goldhorse")) {

                    canvas.drawBitmap(gold_horse, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 1 && pieces[i][j].equals("goldcamel")) {

                    canvas.drawBitmap(gold_dog, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 1 && pieces[i][j].equals("goldelephant")) {

                    canvas.drawBitmap(gold_elephant, new Rect(0,0,100,100), square, white);
                }
                else if(players[i][j] == 2 && pieces[i][j].equals("silverrabbit")){
                    canvas.drawBitmap(silver_rabbit, new Rect(0,0,100,100), square, black);
//                }
            } else if (players[i][j] == 2 && pieces[i][j].equals("silvercat")) {

                    canvas.drawBitmap(silver_cat, new Rect(0,0,100,100), square, white);
                }
                else if (players[i][j] == 2 && pieces[i][j].equals("silverdog")) {

                    canvas.drawBitmap(silver_dog, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 2 && pieces[i][j].equals("silverhorse")) {

                    canvas.drawBitmap(silver_horse, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 2 && pieces[i][j].equals("silvercamel")) {

                    canvas.drawBitmap(silver_dog, new Rect(0,0,100,100), square, white);
                } else if (players[i][j] == 2 && pieces[i][j].equals("silverelephant")) {

                    canvas.drawBitmap(silver_elephant, new Rect(0,0,100,100), square, white);
                }

//



//        }
    }}}
    // public method that needs to be overridden to handle the touches from a
    // user
    public boolean onTouchEvent(MotionEvent event) {
//        int sourcecol=0;
//        int sourcerow=0;
//        int dest_col=0;
//        int dest_row=0;

        // if we get to this point they we have not handled the touch
        // ask the system to handle it instead

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            int pointer_id = event.getPointerId(event.getActionIndex());
            touchx[pointer_id] = event.getX();
            touchy[pointer_id] = event.getY();

            // else
                if (move_count == 0) {
//            touchx[1] = event.getX();
//            touchy[1] = event.getY();
                    sourcecol = (int) (touchx[pointer_id] / sqrHeight);
                    sourcerow = (int) (touchy[pointer_id] / sqrWidth);
                    top_high = (sourcerow - 1) % 7;
                    bottom_high = (sourcerow + 1) % 8;
                    left_high = (sourcecol + 1);
                    right_high = (sourcecol - 1);


                    if (notTurn[sourcecol][sourcerow]) {
                        Toast.makeText(c,
                                "NOT YOUR TURN!!",
                                Toast.LENGTH_SHORT)
                                .show();
                        Log.d(TAG, "onTouchEvent: not your turn");

                        highlighted[sourcecol][sourcerow] = false;
                        return super.onTouchEvent(event);

                    } else if (!notTurn[sourcecol][sourcerow]) {
//                        addToNotTurn(players[sourcecol][sourcerow]);
                        //if ()
                        highlighted[sourcecol][sourcerow] = true;
                        //method to check if selected pawn at edge

                        //isBorder();
                        if (pieces[sourcecol][sourcerow].equals("")) {
                            Toast.makeText(c,
                                    "empty square",
                                    Toast.LENGTH_SHORT)
                                    .show();
                            Log.d(TAG, "onTouchEvent: empty square");

                            highlighted[sourcecol][sourcerow] = false;
                            addToTurn(players[sourcecol][sourcerow]);
                           // invalidate();
                            return super.onTouchEvent(event);
                         //   return true;
                        } else if (!pieces[sourcecol][sourcerow].equals("")) {

                            if (!isRabbit(sourcecol, sourcerow)) {
                                //  if(isBorder(sourcecol,sourcerow))
                                highlighted[sourcecol][bottom_high] = true;
                                highlighted[sourcecol][top_high] = true;
                                if (left_high < 8) {
                                    highlighted[left_high][sourcerow] = true;
                                } else {
                                    highlighted[left_high % 8][sourcerow] = false;
                                }
                                if (right_high < 0) {
                                    highlighted[right_high + 7][sourcerow] = false;
                                } else {
                                    highlighted[right_high % 8][sourcerow] = true;
                                }
                            } else {
                                if (isGoldrabbit(sourcecol, sourcerow)) {
                                    highlighted[sourcecol][bottom_high] = true;
                                    // highlighted[sourcecol][top_high] = false;
                                } else {
                                    highlighted[sourcecol][top_high] = true;
                                    //  highlighted[sourcecol][bottom_high] = false;
                                }
                                // highlighted[sourcecol][(sourcerow - 1) % 7] = true;
                                if (right_high < 0) {
                                    highlighted[right_high + 7][sourcerow] = false;
                                } else {
                                    highlighted[right_high % 8][sourcerow] = true;
                                }
                                if (left_high < 8) {
                                    highlighted[left_high][sourcerow] = true;
                                } else {
                                    highlighted[left_high % 8][sourcerow] = false;
                                }
                            }

//                    highlighted[left_high][sourcerow] = true;
//                    highlighted[right_high][sourcerow] = true;
//                }
                        }
                    }
                    invalidate();
                    return true;

            } else {
                dest_col = (int) (touchx[pointer_id] / sqrHeight);
                dest_row = (int) (touchy[pointer_id] / sqrWidth);
                //FUNCTION TO CHECK IF MOVE IS LEGAL OR FUNCTION TO CHECK IF SQUARE IS OCCUPIED BEFORE
                //SETTING DESTINATION
                movepiece(sourcecol, sourcerow, dest_col, dest_row);


                    Log.d("mytag", "moveevent: accepted move");

//                if (pieces[dest_col][dest_row]=="trapSquare"){
//                    pieces[sourcecol][sourcerow]="";
//                    players[sourcecol][sourcerow]=0;
//                    invalidate();
//                    return true;
//                }

            }
            touch = true;
            Log.d("my tap", "onTouchEvent: source x and y are " + sourcecol + " " + sourcerow + "destination x and y are " + dest_col + " " + dest_row);


            invalidate();
            return true;
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
// this indicates that the user has removed the last finger from the
// screen and has ended all touch events. here we just disable the
// last touch.
            int pointer_id = event.getPointerId(event.getActionIndex());
            touch = false;
//            sourcecol = (int)(touchx[pointer_id]/sqrHeight);
//            sourcerow = (int)(touchy[pointer_id]/sqrWidth);
//            dest_col = (int) (touchx[pointer_id+1] / sqrHeight);
//            dest_row = (int) (touchy[pointer_id+1] / sqrWidth);
            Log.d("my tap", "onTouchEvent: source x and y are " + sourcecol + " " + sourcerow + "destination x and y are " + dest_col + " " + dest_row);
            if (move_count >= 1) {
                move_count = 0;
//                dest_col = 0;
//                dest_row = 0;
            } else
                move_count++;
            invalidate();
            return true;
        }


        return super.onTouchEvent(event);

    }

    private void addToTurn(int player) {
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                if(players[i][j]==player) {
                    notTurn[i][j] = false;
                }
            }
        }
    }

    private boolean isGoldrabbit(int col, int row) {
        if(pieces[col][row].contains("goldrabbit")){
            Log.d(TAG, "isgoldRabbit: true" );
            return true;
        } else
            Log.d(TAG, "isRabbit: false" );
        return false;
    }


    private void isBorder(int col, int row) {
    }
    private int getRow(String pieceName){
        int row = 0;
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                if (pieces[i][j].equals(pieceName)){
                    row= j;
                    break;
                }
            }
        }
        return row;
    }
    private int getColumn(String pieceName){
        int col=0;
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                if (pieces[i][j].equals(pieceName)){
                    col=i;
                    break;
                }
            }
        }
        return col;
    }


    private boolean isRabbit(int sourcecolumn, int sourceroww) {
        if(pieces[sourcecolumn][sourceroww].contains("rabbit")){
            Log.d(TAG, "isRabbit: rabbit" );
            return true;
        } else
            Log.d(TAG, "isRabbit: non rabbit" );
            return false;
    }

    private void movepiece(int sourcecolumn, int sourceroww, int dest_column, int dest_roww) {
        boolean isTrap;
       // isSquareOccupied[dest_column][dest_roww];
        if (isSquareOccupied(dest_column,dest_roww)){
            Toast.makeText(c,
                    "Square Occupied",
                    Toast.LENGTH_SHORT)
                    .show();
            //pieces[sourcecolumn][sourceroww] = "";
            //players[sourcecolumn][sourceroww] = 0;
            resetHighlighted();
            return;
        } else
        if ((sourcecolumn==dest_column && sourceroww == dest_roww) || !highlighted[dest_column][dest_roww]) {
            Toast.makeText(c,
                    "Illegal Move",
                    Toast.LENGTH_SHORT)
                    .show();
            //pieces[sourcecolumn][sourceroww] = "";
            //players[sourcecolumn][sourceroww] = 0;
            resetHighlighted();
            return;
        } else

        if (pieces[dest_col][dest_row].equals("trapSquare")){

//            invalidate();
            if((players[sourcecolumn][sourceroww]) == 1){
                if ((players[sourcecolumn+1][2])!=1 || (players[sourcecolumn-1][2])!=1) {

                    if (player1count == 3) {
                        addToNotTurn(players[sourcecolumn][sourceroww]);
                        if (players[sourcecolumn][sourceroww] == 1) {
                            playerturn.setText(R.string.player2_turn);
                        } else {

                            playerturn.setText(R.string.player1_turn);
                        }
                        // player1count=0;
                        checker = false;
                        Log.d("mychecker", "p1checker: "+player1count);
                    }



                    if (checker){

                        player1count++;
                    }
                    else {
                        player1count = 0;
                        checker = true;
                    }
                    pieces[sourcecol][sourcerow] = "";
                    players[sourcecol][sourcerow] = 0;
                    player1_pieces--;
                    invalidate();
                } else
                    if ((players[sourcecolumn+1][2]) ==1 || (players[sourcecolumn-1][2]) ==1 ){
                        pieces[dest_column][dest_roww] = pieces[sourcecolumn][sourceroww];
                        players[dest_column][dest_roww] = players[sourcecolumn][sourceroww];

                        if (player1count == 3) {
                            addToNotTurn(players[sourcecolumn][sourceroww]);
                            if (players[sourcecolumn][sourceroww] == 1) {
                                playerturn.setText(R.string.player2_turn);
                            } else {

                                playerturn.setText(R.string.player1_turn);
                            }
                            // player1count=0;
                            checker = false;
                            Log.d("mychecker", "p1checker: "+player1count);
                        }

                        if (checker){

                            player1count++;
                        }
                        else {
                            player1count = 0;
                            checker = true;
                        }
                        pieces[sourcecolumn][sourceroww] = "";
                        players[sourcecolumn][sourceroww] = 0;

                        invalidate();
                    }
               // else

                    //player1.setText("Player 1\n"+player1_pieces);
                   // invalidate();
                   // player2.setText("Player 2\n"+player2_pieces);
                    Log.d(TAG, "movepiece: player 1"+ player1_pieces);

            } else
            if((players[sourcecolumn][sourceroww])==2){
                if ((players[sourcecolumn+1][5])!=2 || (players[sourcecolumn-1][5])!=2) {

                    if (player2count == 3) {
                        addToNotTurn(players[sourcecolumn][sourceroww]);
                        if (players[sourcecolumn][sourceroww] == 1) {
                            playerturn.setText(R.string.player2_turn);
                        } else {

                            playerturn.setText(R.string.player1_turn);
                        }
                        // player1count=0;
                        checker = false;
                        Log.d("mychecker", "p1checker: "+player1count);
                    }

                    if (checker2){

                        player2count++;
                    }
                    else {
                        player2count = 0;
                        checker2 = true;
                    }

                    pieces[sourcecol][sourcerow] = "";
                    players[sourcecol][sourcerow] = 0;
                    player2_pieces--;
                    invalidate();
                } else
                if ((players[sourcecolumn+1][5]) ==2 || (players[sourcecolumn-1][5]) ==2 ){
                    pieces[dest_column][dest_roww] = pieces[sourcecolumn][sourceroww];
                    players[dest_column][dest_roww] = players[sourcecolumn][sourceroww];

                    if (player2count == 3) {
                        addToNotTurn(players[sourcecolumn][sourceroww]);
                        if (players[sourcecolumn][sourceroww] == 1) {
                            playerturn.setText(R.string.player2_turn);
                        } else {

                            playerturn.setText(R.string.player1_turn);
                        }
                        // player1count=0;
                        checker = false;
                        Log.d("mychecker", "p1checker: "+player1count);
                    }

                    if (checker2){

                        player2count++;
                    }
                    else {
                        player2count = 0;
                        checker2 = true;
                    }

                    pieces[sourcecolumn][sourceroww] = "";
                    players[sourcecolumn][sourceroww] = 0;

                    invalidate();
                   // drawSquare();
                }
            }


            resetHighlighted();


            invalidate();
            return;
        }


        pieces[dest_column][dest_roww] = pieces[sourcecolumn][sourceroww];
        players[dest_column][dest_roww] = players[sourcecolumn][sourceroww];



        if (players[sourcecolumn][sourceroww]==1) {
            if (player1count == 3) {
                addToNotTurn(players[sourcecolumn][sourceroww]);
                if (players[sourcecolumn][sourceroww] == 1) {
                    playerturn.setText(R.string.player2_turn);
                } else {

                    playerturn.setText(R.string.player1_turn);
                }
               // player1count=0;
                checker = false;
                Log.d("mychecker", "p1checker: "+player1count);
            }
        } else {
            Log.d("mychecker", "p2checker: "+player2count);
            if (player2count == 3) {
                addToNotTurn(players[sourcecolumn][sourceroww]);
                if (players[sourcecolumn][sourceroww] == 2) {
                    playerturn.setText(R.string.player1_turn);
                } else {

                    playerturn.setText(R.string.player2_turn);
                }
               // player2count=0;
                checker2=false;


            }
        }

        if(players[sourcecolumn][sourceroww]==1){
            if (checker){

                player1count++;
            }
            else
                player1count=0;
                checker=true;
        } else
            {
                if (checker2){
                    player2count++;
                }
                else
                    player2count=0;
                checker2=true;
        }

//        pieces[sourcecolumn][sourceroww] = "";
//        players[sourcecolumn][sourceroww] = 0;
        //highlighted[sourcecol][sourcerow] = false;
        if ((pieces[sourcecolumn][sourceroww].equals("goldrabbit")) || pieces[sourcecolumn][sourceroww].equals("silverrabbit")){
            //LOGIC TO END GAME
            if (((pieces[sourcecolumn][sourceroww]).equals("goldrabbit")) && dest_roww==7){
                Toast.makeText(c,
                        "PLAYER 1 WINS!!!",
                        Toast.LENGTH_LONG)
                        .show();
                pieces[sourcecolumn][sourceroww] = "";
                players[sourcecolumn][sourceroww] = 0;
                resetHighlighted();
                init();
                return;
            }
            if (((pieces[sourcecolumn][sourceroww]).equals("silverrabbit")) && dest_roww==0){
                Toast.makeText(c,
                        "PLAYER 2 WINS!!!",
                        Toast.LENGTH_LONG)
                        .show();
                pieces[sourcecolumn][sourceroww] = "";
                players[sourcecolumn][sourceroww] = 0;
                resetHighlighted();
                init();
                return;

            }

        }
        pieces[sourcecolumn][sourceroww] = "";
        players[sourcecolumn][sourceroww] = 0;
        resetHighlighted();
//        addToNotTurn(players[dest_col][dest_row]);
////                            playerturn.setText("PLAYER "+ players[sourcecol][sourcerow]+" TURN");
//        if(players[dest_col][dest_row] == 1) {
//            playerturn.setText("PLAYER 2 TURN");
//        } else {
//
//            playerturn.setText("PLAYER 1 TURN");
//        }


       // Log.d(TAG, "movepiece: "+isTrap);
        if ((sourcecolumn==2 && sourceroww == 2) || (sourcecolumn==5 && sourceroww == 2) || (sourcecolumn==2 && sourceroww == 5) || (sourcecolumn==5 && sourceroww == 5)){
           // Log.d(TAG, "isTrap: "+isTrap);
            pieces[sourcecolumn][sourceroww]="trapSquare";
            invalidate();
        }
    }

    private void resetHighlighted() {
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                highlighted[i][j]=false;
            }
        }
    }

    private void addToNotTurn(int player) {
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                notTurn[i][j] = false;
            }
        }
        for (int i = 0; i < colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                if(players[i][j]==player) {
                    notTurn[i][j] = true;
                }
            }
        }
    }


    public void drawSquare(int i, int j){
        canvas.drawRect(i * sqrWidth, j *
                sqrHeight, (i + 1) * sqrWidth, (j +
                1) * sqrHeight, green);
    }

    public boolean isSquareOccupied(int i, int j){
        return (players[i][j] == 1) || (players[i][j] == 2);

    }


}
