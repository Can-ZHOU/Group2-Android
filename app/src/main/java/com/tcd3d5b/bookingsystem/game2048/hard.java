package com.tcd3d5b.bookingsystem.game2048;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tcd3d5b.bookingsystem.R;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class hard extends AppCompatActivity {

    private Random rnd;
    private int[][] field, saved;
    private Action[][] action;
    private boolean goon;
    private long score, maxScore, saveScore;
    private final String maxScoreFileName = "maxScoreHard.dat";
    private int fieldSize = 5;

    private Timer mTimer;
    private MyTimerTask mMyTimerTask;

    //appAnimationComment

    private Animation alphaTransition;
    private Animation scaleTransition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard);

        ImageButton resBtn  = findViewById(R.id.res);
        resBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });

        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, 1000, 1000);


        alphaTransition = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alphaTransition.reset();

        scaleTransition = AnimationUtils.loadAnimation(this, R.anim.scaler);
        scaleTransition.reset();

        findViewById(R.id.screen)
                .setOnTouchListener(
                        new SwipeListener(this){
                            public void onSwipeBottom(){
                                if(canMoveBottom()){
                                    saveState();
                                    moveBottom();
                                    proceedMoveResult();
                                }else{
                                    Toast.makeText(
                                            hard.this,
                                            getString(R.string.no_move),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            public void onSwipeTop(){
                                if(canMoveTop()){
                                    saveState();
                                    moveTop();
                                    proceedMoveResult();
                                }else{
                                    Toast.makeText(
                                            hard.this,
                                            getString(R.string.no_move),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            public void onSwipeLeft(){
                                if(canMoveLeft()){
                                    saveState();
                                    moveLeft();
                                    proceedMoveResult();
                                }else{
                                    Toast.makeText(
                                            hard.this,
                                            getString(R.string.no_move),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }

                            public void onSwipeRight(){
                                if(canMoveRight()){
                                    saveState();
                                    moveRight();
                                    proceedMoveResult();
                                }else{
                                    Toast.makeText(
                                            hard.this,
                                            getString(R.string.no_move),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                );

        findViewById(R.id.btnBack)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restoreState();
                    }
                });


        rnd = new Random();
        field = new int[fieldSize][fieldSize];
        saved = new int[fieldSize][fieldSize];
        action = new Action[fieldSize][fieldSize];
        restart();
    }

    private  void saveMaxScore() throws IOException{
        FileOutputStream newScoreFile = openFileOutput(maxScoreFileName, Context.MODE_PRIVATE);
        DataOutputStream writer = new DataOutputStream(newScoreFile);
        writer.writeLong(maxScore);
        writer.close();
        newScoreFile.close();
    }

    private  void proceedMoveResult(){
        if(! goon){
            for(int i = 0 ; i< fieldSize; ++i)
                for(int j = 0 ; j< fieldSize; ++j)
                    if(field[i][j]== 2048)
                        showWinMessage();
        }
        boolean res = addVal();
        showField();

        if(!res){
            epicFail();
        }
    }

    private boolean canMoveBottom(){
        int i , j , k;
        for( j=0; j < fieldSize; ++j){
            for(i = fieldSize - 1 ; i> 0 ; --i){
                if(field[i][j]== 0){
                    k = i-1;
                    while(k >= 0 && field[k][j] == 0){
                        --k;

                    }
                    if(k >= 0){
                        return true;
                    }
                }
            }

            for(i = fieldSize - 1; i> 0; --i){
                if(field[i][j]!=0 && field[i][j]==field[i-1][j]){
                    return  true;
                }
            }

        }
        return false;
    }

    private void moveBottom() {
        int i, j, k;
        for (j = 0; j < fieldSize; ++j) {
            for (i = fieldSize - 1; i > 0; --i) {
                if (field[i][j] == 0) {
                    k = i - 1;
                    while( k >= 0 && field[k][j]==0)
                        --k;
                    if(k >= 0){
                        field[i][j] = field[k][j];
                        field[k][j] = 0;
                    }
                }
            }

            for(i = fieldSize - 1; i> 0; --i){
                if(field[i][j] != 0 && field[i][j] == field[i-1][j]){
                    field[i][j] *= 2;
                    action[i][j] = Action.Collapse;
                    score += field[i][j];
                    for(k = i-1; k > 0; --k){
                        field[k][j] = field[k-1][j];
                    }
                    field[0][j] = 0;
                }
            }
        }
    }

    private  boolean canMoveTop(){
        int i, j, k;
        for(j =0; j < fieldSize; ++j){
            for(i=0; i < fieldSize - 1; ++i){
                if(field[i][j] == 0){
                    k = i + 1;
                    while( k < fieldSize && field[k][j] == 0 ){
                        ++k;
                    }
                    if(k < fieldSize){
                        return true;
                    }
                }
            }
            for(i=0; i < fieldSize - 1; ++i){
                if(field[i][j]!=0 && field[i][j]==field[i+1][j]){
                    return  true;
                }
            }

        }

        return false;
    }

    private void moveTop(){
        int i, j, k;
        for(j = 0; j < fieldSize; ++j){
            for(i=0; i < fieldSize - 1; ++i){
                if(field[i][j] == 0){
                    k = i + 1;
                    while( k < fieldSize && field[k][j] == 0 ){
                        ++k;
                    }
                    if(k < fieldSize){
                        field[i][j] = field[k][j];
                        field[k][j] = 0;
                    }
                }
            }

            for(i=0; i < fieldSize - 1; ++i){
                if(field[i][j] != 0 && field[i][j] == field[i+1][j]){
                    field[i][j] *= 2;
                    action[i][j] = Action.Collapse;
                    score += field[i][j];
                    for(k = i + 1; k < fieldSize - 1; ++k){
                        field[k][j] = field[k + 1][j];
                    }
                    field[fieldSize - 1][j] = 0;
                }
            }
        }

    }

    private boolean canMoveLeft(){
        int i , j , k;

        for(i = 0; i< fieldSize; ++i){
            for(j = 0 ; j < fieldSize - 1; ++j){
                if(field[i][j] == 0){
                    k = j + 1;
                    while(k < fieldSize && field[i][k]== 0)
                        ++k;
                    if(k < fieldSize){
                        return true;
                    }
                }
            }

            for( j = 0 ; j < fieldSize - 1 ;++j){
                if(field[i][j]!=0 && field[i][j] == field[i][j+1]){
                    return true;
                }
            }
        }
        return false;

    }

    private void moveLeft(){
        int i , j , k;

        for(i = 0; i< fieldSize; ++i){
            for(j = 0 ; j < fieldSize - 1; ++j){
                if(field[i][j]==0){
                    k = j + 1;
                    while(k < fieldSize && field[i][k]== 0)
                        ++k;
                    if(k < fieldSize){
                        field[i][j] = field[i][k];
                        field[i][k] = 0;
                    }
                }
            }

            for( j = 0 ; j < fieldSize - 1 ;++j){
                if(field[i][j] != 0 && field[i][j] == field[i][j + 1]){
                    field[i][j] *= 2;
                    action[i][j] = Action.Collapse;
                    score += field[i][j];
                    for(k = j + 1; k < fieldSize - 1; ++k){
                        field[i][k] = field[i][k +1];
                    }
                    field[i][fieldSize - 1] = 0;
                }
            }
        }

    }

    private boolean canMoveRight(){
        int i, k , j;
        for(i = 0; i< fieldSize; ++i){
            for(j = fieldSize - 1 ; j >0; --j){
                if(field[i][j]==0){
                    k = j - 1;
                    while(k >=0 && field[i][k]== 0)
                        --k;
                    if(k >=0){
                        return true;
                    }
                }
            }

            for( j = fieldSize - 1 ; j > 0 ;--j){
                if(field[i][j]!=0 && field[i][j] == field[i][j - 1]){
                    return true;
                }
            }
        }
        return false;
    }

    private void moveRight(){
        int i, k , j;
        for(i = 0; i< fieldSize; ++i){
            for(j = fieldSize - 1 ; j > 0; --j){
                if(field[i][j]==0){
                    k = j - 1;
                    while(k >= 0 && field[i][k]== 0)
                        --k;
                    if(k >= 0){
                        field[i][j] = field[i][k];
                        field[i][k] = 0;
                    }
                }
            }

            for( j = fieldSize - 1 ; j > 0 ;--j){
                if(field[i][j] != 0 && field[i][j] == field[i][j - 1]){
                    field[i][j] *= 2;
                    action[i][j] = Action.Collapse;
                    score += field[i][j];
                    for(k = j - 1; k > 0; --k){
                        field[i][k] = field[i][k -1];
                    }
                    field[i][0] = 0;
                }
            }
        }
    }

    private void epicFail(){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle(getString(R.string.fail))
                .setMessage(getString(R.string.play_again))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.once_more, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setCancelable(false)
                .show();
    }

    private  void showWinMessage(){
        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setTitle(getString(R.string.victory))
                .setMessage(getString(R.string.play_again))
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.once_more, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        restart();
                    }
                })
                .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0);
                    }
                })
                .setNeutralButton(R.string.goon, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goon = true;
                    }
                })
                .setCancelable(false)
                .show();

    }

    private void restart(){
        goon = false;
        score = 0;

        try{
            FileInputStream maxScoreFile;
            maxScoreFile = openFileInput(maxScoreFileName);
            DataInputStream reader = new DataInputStream(maxScoreFile);
            maxScore = reader.readLong();
            reader.close();
            maxScoreFile.close();
        }
        catch (Exception ex){
            try{
                saveMaxScore();
            }catch (Exception ex2){
                maxScore = -1;
            }
        }

        clear();
        addVal();
        saveState();
        showField();
    }

    private TextView getCellByCoords(int x, int y){
        int cellId =
                getResources()
                        .getIdentifier(
                                "cell" + x + y,
                                "id",
                                getPackageName()
                        );
        return findViewById(cellId);
    }

    private void clear(){
        int i,j;
        for( i = 0; i< fieldSize; ++i){
            for( j = 0; j< fieldSize; ++j){
                field[i][j] = 0;
                action[i][j] = Action.None;
            }
        }
        mMyTimerTask.setN(0);
    }

    private void saveState(){
        for(int i = 0 ; i < fieldSize ; ++i)
            for(int j = 0; j < fieldSize; ++j)
                saved[i][j] = field[i][j];
        saveScore = score;
    }

    private void restoreState(){
        score = saveScore;
        for(int i = 0 ; i < fieldSize ; ++i)
            for(int j = 0; j < fieldSize; ++j)
                field[i][j] = saved[i][j]  ;
        showField();

    }

    private void showField(){
        ((TextView)
                findViewById(R.id.scoreNow)
        ).setText(score + "");

        if(score > maxScore){
            maxScore = score;
            try{
                saveMaxScore();
            }catch (IOException ex){

            }

        }

        ((TextView)
                findViewById(R.id.maxScore)
        ).setText(maxScore + "");

        for(int i = 0; i< fieldSize; ++i){
            for(int j = 0; j < fieldSize; ++j){
                TextView cell = getCellByCoords(i,j);
                int bgColor, fgColor;

                switch (field[i][j]){
                    case 0:
                        bgColor = 0xFFCCC0B4;
                        fgColor = 0xFFCCC0B4;
                        break;
                    case 2:
                        bgColor = 0xFFEDE3D9;
                        fgColor = 0xFF736A61;
                        break;
                    case 4:
                        bgColor = 0xFFECE0C8;
                        fgColor = 0xFF7A7067;
                        break;
                    case 8:
                        bgColor = 0xFFF2B179;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 16:
                        bgColor = 0xFFEB8E55;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 32:
                        bgColor = 0xFFF57C5F;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 64:
                        bgColor = 0xFFE85A34;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 128:
                        bgColor = 0xFFF3D96B;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 256:
                        bgColor = 0xFFE4C22D;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 512:
                        bgColor = 0xFFE4C02A;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 1024:
                        bgColor = 0xFFE4C22D;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case 2048:
                        bgColor = 0xFFE4C32D;
                        fgColor = 0xFFFFFFFF;
                        break;
                    case  4096:
                        bgColor = 0xFFB784AB;
                        fgColor = 0xFFFFFFFF;
                        break;
                    default:
                        bgColor = 0xFFAA60A6;
                        fgColor = 0xFFFFFFFF;
                }

                cell.setBackgroundColor(bgColor);
                cell.setTextColor(fgColor);
                cell.setText(field[i][j]+"");

                switch (action[i][j]){
                    case Appear:
                        cell.startAnimation(alphaTransition);
                        action[i][j] = Action.None;
                        break;
                    case Collapse:
                        cell.startAnimation(scaleTransition);
                        action[i][j] = Action.None;
                        break;

                }

            }
        }
    }

    private  boolean addVal(){
        ArrayList<Coord> free = new ArrayList<>();
        for(int i = 0; i< fieldSize; ++i){
            for(int j = 0 ; j< fieldSize; ++j){
                if(field[i][j] == 0){
                    free.add(new Coord(i , j));
                }
            }
        }
        if(free.isEmpty()) return false;

        int r = rnd.nextInt(free.size());

        field
                [ free.get( r ).x ]
                [ free.get( r ).y ] =
                (rnd.nextInt(10 ) > 0)
                        ? 2
                        : 4 ;

        action
                [free.get( r ).x]
                [ free.get( r ).y ] =
                Action.Appear;

        free.remove(r);
        if(free.isEmpty()){
            //No free cells but may me move
            for(int i= 0; i< fieldSize; ++i){
                for(int j = 0; j< fieldSize; ++j){
                    if(i > 0 && field[i][j]== field[i-1][j]) return true;
                    if(i < fieldSize - 1 && field[i][j]== field[i+1][j]) return true;
                    if(j > 0 && field[i][j]== field[i][j -1]) return true;
                    if(j< fieldSize - 1  && field[i][j]== field[i][j + 1]) return true;

                }
            }
            return  false;
        }
        return  true;
    }

    class Coord{
        public  int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class MyTimerTask extends TimerTask {
        private int n;
        @Override
        public void run() {

            ++n;
            final TextView t = findViewById(R.id.gTimer);


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    t.setText(""+ n / 60 + "-" + ( n % 60 ));
                }
            });
        }

        public void setN(int n) {
            this.n = n;
        }
    }

    enum Action{
        None,
        Appear,
        Collapse
    }

}
